package com.commerciale.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.InvoiceProforma;
import com.commerciale.model.LineProduct;
import com.commerciale.model.LineProductProforma;
import com.commerciale.model.Product;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.ClientList;
import com.commerciale.view.CreateClient;
import com.commerciale.view.CreateInvoice;
import com.commerciale.view.CreateReception;
import com.commerciale.view.InvoiceList;
import com.commerciale.view.MainMenu;
import com.commerciale.view.ProductList;

public class CRUDinvoiceProforma {
	private InvoiceProforma invoiceProforma;
	private CreateInvoice createInvoice;
	private CRUDclient crudClient;
	private Currency currency;
	public static String addOrUpdate = ""; 
	private double previousAmount;
	private int selected = -1;
	public static int selectedInvoice;
	private String path = "";
	private String transpot = "";
	private double change;
	private HashMap<String, String> deletedProduct = new HashMap<String, String>();

	public CRUDinvoiceProforma(InvoiceProforma invoiceProforma, CreateInvoice createInvoice, Currency currency, CRUDclient crudClient) {
		this.invoiceProforma = invoiceProforma;
		this.createInvoice = createInvoice;
		this.currency = currency;
		this.crudClient = crudClient;
		this.createInvoice.addingProductListener(new addProductListener());
		this.createInvoice.cancelingListener(new cancelInvoiceListener());
		this.createInvoice.SavingListener(new saveInvoiceListener());
		this.createInvoice.deleteBtnListener(new deletingProductListener());
		this.createInvoice.addCurrencyValueListener(new addExchangeValue());
		this.createInvoice.addCustomer(new addingCustomerListener());
		this.createInvoice.productGridListener(new gridNavigListener());
		this.createInvoice.allNeddedWhenOpen(new behaveWhenOpen());
		this.createInvoice.majoringListener(new MajorationListener());
		this.createInvoice.fraitRdbtnSwitch(new TransportRadioButtons());
		this.createInvoice.calculateAfterTransportation(new calculationAfterTransp());
		this.createInvoice.importingExcelInvoice(new XLSImport());
		this.createInvoice.popUpUpdateQte(new popUpUpdatingQte());
	//	this.createInvoice.expoVersExcelInvoice(new XLSExport());
	}

	/***********************************************/
	class TransportRadioButtons implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
	      if (e.getActionCommand().equalsIgnoreCase("DA")) {
	    	transpot = "DA";
	      }
	      if (e.getActionCommand().equalsIgnoreCase("DEVISE")) {
	    	transpot = "DEVISE";
	      }
	      createInvoice.getTxtTransportation().setEditable(true);
	    }
	}	
	
	
	class calculationAfterTransp implements ActionListener{
	     double transAmount = 0, TauxDeChange = 0 , montantDevise , trans_dev =0, tot = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
		
			TauxDeChange = Tools.decimalToDouble(createInvoice.getChange());
			transAmount = Tools.decimalToDouble(createInvoice.getTransportation());
			montantDevise = Tools.decimalToDouble(createInvoice.getTotalDevise());
			}catch (Throwable ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
			if ((createInvoice.getProductGrid().getRowCount()== 0)||(createInvoice.getTransportation().isEmpty())||(transpot.isEmpty())) {
				JOptionPane.showMessageDialog(null, "Verifiez que : 1- la liste des articles n'est pas vide \n " +
																   "2- Vous avez choisi DA ou Devise \n"  +
																   "3- Vous avez saisi les frais de transport ?!!");
			}
			else {
				if (transpot.equalsIgnoreCase("DEVISE")) {  trans_dev =transAmount;
															transAmount = transAmount * TauxDeChange;}
				else {trans_dev = transAmount/ TauxDeChange;}
				tot = montantDevise + trans_dev;
				createInvoice.setTranspAmount(Tools.printDecimal(transAmount));
				createInvoice.setTotalDeviseTransp(Tools.printDecimal(tot));
			}	
		}
		
	}
	/*************** InvoiceProforma View behavior on opening *************/
	class behaveWhenOpen extends CustomActionListeners {

		@Override
		public void internalFrameOpened(InternalFrameEvent e) {
			createInvoice.changeBackgroundColor(new Color(250,240,230));
			createInvoice.setFactureNlbl("Facture Proforma N° :");
	    	if (!(createInvoice.getTitle().equalsIgnoreCase("Modification d'une facture proforma"))) {
	    	
			getCurrencyValue();}
		}
	}

	/*********************** end of behavior ************************/
	/******************** Majoration ********************************/
	class MajorationListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		double totalht = 0, TVA = 0, diffTva = 0, totalTTC = 0, montantDa = 0, retirement = 0,
				precompte = 0, majoration = 0, majResult = 0, transAmount = 0, taxDomicile = 0,
				coefRep = 0, productExpences = 0, productTotal = 0, montantDevise = 0, tauxChange = 0,
				totalExpences = 0 , THT = 0, qte = 0, prodUnitPrice = 0 , magTrans = 0, taxBanc = 0,
				fobTransp = 0;
		boolean fob = false;
		if ((createInvoice.getPrecompteDouane().isEmpty())||(createInvoice.getMagTrans().isEmpty())) {
			JOptionPane.showMessageDialog(null, "Veuillez remplisser les champs des charges (precompte douane, transitaire et magazinage )  svp!!");
		}else {
		try {
			for (int i=0; i<createInvoice.getProductGrid().getRowCount(); i++) {
				montantDevise = montantDevise + Tools.decimalToDouble(CreateInvoice.productGridModel.getValueAt(i, 6).toString());
			}
			tauxChange = Tools.decimalToDouble(createInvoice.getChange());
			//JOptionPane.showMessageDialog(null, "Montant devise = "+montantDevise+"  , taux de change = "+tauxChange);
			montantDa = montantDevise * tauxChange;
		//	JOptionPane.showMessageDialog(null, "montant da = "+montantDa );
			fob = createInvoice.getFob();
			
			if (fob) {
				transAmount = 0;
				fobTransp = Tools.decimalToDouble(createInvoice.getTranspAmount());
				
			}
			else {
				transAmount = Tools.decimalToDouble(createInvoice.getTranspAmount());
				fobTransp = 0;
			}
			
			taxBanc = (montantDa + transAmount)* 0.01;
			createInvoice.setBancTax(Tools.printDecimal(taxBanc));
			taxDomicile = (montantDa + transAmount) * 0.005;
			createInvoice.setDomic(Tools.printDecimal(taxDomicile));
			retirement = ((montantDa+transAmount )*1.19) * 0.04;
			createInvoice.setRetirement(Tools.printDecimal(retirement));
			precompte = Tools.decimalToDouble(createInvoice.getPrecompteDouane()); 
			magTrans =  Tools.decimalToDouble(createInvoice.getMagTrans()); 
			majoration = Tools.decimalToDouble(createInvoice.getMajoration());
			totalExpences = precompte + transAmount + retirement + taxDomicile  + fobTransp;
			majResult = (montantDa + totalExpences) * majoration /100;
			for (int i=0; i<createInvoice.getProductGrid().getRowCount(); i++) {
				  productTotal = Tools.decimalToDouble(CreateInvoice.productGridModel.getValueAt(i, 6).toString())*tauxChange; 
				  coefRep=productTotal/montantDa;
				  productExpences = totalExpences*coefRep;
				  productTotal = (productTotal + productExpences)*(1+majoration /100);
				  productTotal = productTotal + ((magTrans+taxBanc) * coefRep);
				  qte = Tools.decimalToDouble(CreateInvoice.productGridModel.getValueAt(i, 3).toString());
				  prodUnitPrice = productTotal/qte;
				  CreateInvoice.productGridModel.setValueAt(Tools.printDecimal(prodUnitPrice), i, 5);
				  CreateInvoice.productGridModel.setValueAt(Tools.printDecimal(productTotal), i, 7);
				  THT = THT + productTotal;
				}
			
			createInvoice.setMajResult(Tools.printDecimal(majResult));
		//	totalht = montantDa+ transAmount + precompte + majResult + retirement + taxDomicile;
			createInvoice.setTotalHt(Tools.printDecimal(THT));
			TVA = THT * 0.19;
			CreateInvoice.txtTVA.setText(Tools.printDecimal(TVA));
			totalTTC = THT + TVA  ;
			createInvoice.setMontantFinal(Tools.printDecimal(totalTTC));
			diffTva = TVA-((montantDa+transAmount)*0.19);
			createInvoice.setDiffTva(Tools.printDecimal(diffTva));
		}catch (Throwable ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	}
	}

	/******************* end of majoration **************************/
	/************ ADD customer ****************************/
	class addingCustomerListener extends CustomActionListeners {

		@Override
		public void actionPerformed(ActionEvent e) {
			Client client = new Client();
			ClientList CL = ClientList.getInstance();
			CRUDclientDialog CCD = new CRUDclientDialog(client, CL);
			CCD.Select("from Client");
			CL.setModal(true);
			CL.setVisible(true);
           
		}

	}

	
	/***************** ADD product **************************/
	class addProductListener extends CustomActionListeners {

		@Override
		public void actionPerformed(ActionEvent e) {
		
				Product product = new Product();
				ProductList PL = new ProductList();
				Currency currency = new Currency();
				CRUDproductDialogCustomer CPDC = new CRUDproductDialogCustomer(product, PL, currency);
				CPDC.getCurrencyValue();
				CPDC.Select("from Product");
				PL.setModal(true);
				PL.setVisible(true);
		}
	}

	/*********** CANCEL ***************************/
	class cancelInvoiceListener extends CustomActionListeners {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object[] options = { "Oui", "Non" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment Annuler ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if (answer == 0) {
				clearEverything();
				clearNumberOfProduct();
				createInvoice.txtPrecompteDouane.setText("0");
				
					addOrUpdate = "";
				//	InvoiceProforma invoiceProforma = new InvoiceProforma();
					InvoiceList invoiceList = InvoiceList.getInstance();
					CreateInvoice createInvoice = CreateInvoice.getInstance();
					CRUDinvoiceListProforma crudInvoiceListProforma = new CRUDinvoiceListProforma(invoiceProforma, invoiceList);
				    if (!invoiceList.isVisible()) {	MainMenu.desktopPane.add(invoiceList);
													invoiceList.show();
					}
				    createInvoice.dispose();
				    createInvoice.setCI(null);
				    CreateClient.setCrClient(null);
				    
				}
			
			}
		
	}

	/************ SAVE ***************************/
	class saveInvoiceListener extends CustomActionListeners {
		private boolean empty = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
			
				if (createInvoice.getProductGrid().getRowCount() ==0) {JOptionPane.showMessageDialog(null, "La liste des articles est vide !!");
				} else {
					
						if (createInvoice.getTitle().equalsIgnoreCase("Modification d'une facture proforma")) {Update(selectedInvoice);}
						else {Insert();	}
						addOrUpdate = "";
						InvoiceProforma invoiceProforma = new InvoiceProforma();
						InvoiceList invoiceList = InvoiceList.getInstance();
					//	CreateInvoice createInvoice = CreateInvoice.getInstance();
						CRUDinvoiceListProforma crudInvoiceListProforma = new CRUDinvoiceListProforma(invoiceProforma, invoiceList);
						if (!invoiceList.isVisible()) {
						MainMenu.desktopPane.add(invoiceList);
						invoiceList.getFinalBtn().setEnabled(true);
						invoiceList.getReceptionBtn().setEnabled(false);
						invoiceList.setTitle("Liste des Factures Proforma");
						invoiceList.show();
						createInvoice.dispose();
						clearEverything();
						clearNumberOfProduct();
						} 
					
				}
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}

		}

	}

	/**************** UPDATE **********************/
	class updateInvoiceListener extends CustomActionListeners {
		private boolean empty = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createInvoice.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					if (((JTextField) comp).getText().isEmpty()) {
						empty = true;
					}
				}
			}
			if (empty) {
				JOptionPane.showMessageDialog(null,
						"Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le modifier !! ");
				empty = false;
			} else {
				for (Component comp : createInvoice.getContainer().getComponents()) {
					if (comp instanceof JTextField) {
						((JTextField) comp).setEditable(false);
						comp.setBackground(Color.WHITE);
					}
				}
				btnSwitcher(true, false);
				addOrUpdate = "UPDATE";

			}
		}
	}

	/************* DELETE ***************************/
	class deletingProductListener extends CustomActionListeners {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (createInvoice.getProductGrid().getRowCount() <= 0) {
				JOptionPane.showMessageDialog(null, "La liste des articles est vide !! ");
			} else if (selected >= 0) {
				Object[] options = { "confirmer", "Annuler" };
				int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (answer == 0) {
					createInvoice.getProductGridModel().removeRow(selected);
					selected = -1;
					createInvoice.setSumProduct(createInvoice.getProductGrid().getRowCount());
					try {
						gridCalcul(change);
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					} catch (Throwable e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le supprimer !! ");
			}

		}
	}

	/************* SEARCH ***************************/
	class gridNavigListener extends CustomActionListeners {
		private String id;
		private int SR;
		@Override
		public void mouseClicked(MouseEvent e) {
			selected = createInvoice.getProductGrid().getSelectedRow();
		/*	if (e.getButton()==MouseEvent.BUTTON3) {
				if (selected >= 0) {
					Object[] options = { "confirmer", "Annuler" };
					int answer = JOptionPane.showOptionDialog(null, "Voulez vous modifier la quantité pour : "+ 
					      createInvoice.getProductGridModel().getValueAt(selected, 1)+"  ?", "Confirmation",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
					if (answer == 0) {
						 String qt = JOptionPane.showInputDialog("Quantité"); 
						try {
					    	int qte = Integer.parseInt(qt);
					    	double unitPrice =Double.parseDouble(createInvoice.getProductGridModel().getValueAt(selected, 4).toString());
					    	createInvoice.getProductGridModel().setValueAt(qt, selected, 3);
					    	createInvoice.getProductGridModel().setValueAt(Tools.printDecimal(unitPrice*qte), selected, 6);
						}catch(Exception ex) {JOptionPane.showMessageDialog(null, qt + " n'est pas un nombre !!");
											 					}
					}
				}
			}*/
		}
	}
	
	
	class popUpUpdatingQte implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (selected>-1) {
				String qt = JOptionPane.showInputDialog("Quantité pour  "+createInvoice.getProductGridModel().getValueAt(selected, 1).toString()); 
				try {
					int qte = Integer.parseInt(qt);
			    	double unitPrice =Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(selected, 4).toString());
			    	createInvoice.getProductGridModel().setValueAt(qt, selected, 3);
			    	createInvoice.getProductGridModel().setValueAt(Tools.printDecimal(unitPrice*qte), selected, 6);
				}catch(Throwable ex) {JOptionPane.showMessageDialog(null, qt + " n'est pas un nombre !!");}
			}
		}
	}

	class addExchangeValue extends CustomActionListeners {

		@Override
		public void actionPerformed(ActionEvent e) throws NumberFormatException {
			String res = JOptionPane.showInputDialog(null, "Ajouter un taux de change ", "Nouveaux taux de change",
					JOptionPane.INFORMATION_MESSAGE);
			if (res != null) {
				try {
					double d = Double.parseDouble(res);
					Session session = hibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						currency.setMoney(d);
						session.save(currency);
						session.getTransaction().commit();
					} catch (HibernateException ex) {
						session.getTransaction().rollback();
						JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un taux de change : " + ex);
					} finally {
						session.close();
					}
					GridCalculAfterChange(d);
					gridCalcul(d);
				} catch (Throwable ex) {
					JOptionPane.showMessageDialog(null, "Enter a valid number ");
				}
			}
			getCurrencyValue();

			// Select("from InvoiceProforma");
		}

	}

	/***********************************
	 * CRUD
	 **********************************************************************************************/
	/*******
	 * Insert
	 * 
	 * @throws Throwable
	 */
	public void Insert() throws Throwable {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<Client> cl = session
					.createQuery("from Client where id = '" + Integer.parseInt(createInvoice.getCustomerId()) + "'")
					.list();
			InvoiceProforma invoiceProforma = new InvoiceProforma();
			invoiceProforma.setRef(createInvoice.getReference());
			invoiceProforma.setInvYear(Integer.parseInt(createInvoice.getRefYear()));
			invoiceProforma.setClient(cl.get(0));
			invoiceProforma.setTodayDate(new Date());
			if (createInvoice.getSurface().isEmpty()) {invoiceProforma.setSurface(0);}else {
			invoiceProforma.setSurface(Tools.decimalToInteger(createInvoice.getSurface()));}
			invoiceProforma.setMetalColor(createInvoice.getMetalColor());
			invoiceProforma.setSeatColor(createInvoice.getSeatColor());
			invoiceProforma.setProject(createInvoice.getProject());
			invoiceProforma.setTotalDevise(Tools.decimalToDouble(createInvoice.getTotalDevise()));
		//	invoiceProforma.setTotalDinar(Tools.decimalToDouble(createInvoice.getTotalDa()));
			if (createInvoice.getPrecompteDouane().isEmpty()) {invoiceProforma.setPrecompteDouane(0);}else {
			invoiceProforma.setPrecompteDouane(Tools.decimalToDouble(createInvoice.getPrecompteDouane()));}
			invoiceProforma.setChange(Tools.decimalToDouble(createInvoice.getChange()));
			invoiceProforma.setTva(Tools.decimalToDouble(createInvoice.getTVA()));
			invoiceProforma.setTotal(Tools.decimalToDouble(createInvoice.getMontantFinal()));
			if (createInvoice.getMajoration().isEmpty()) {invoiceProforma.setProfitPercent(0);}else {
			invoiceProforma.setProfitPercent(Tools.decimalToInteger(createInvoice.getMajoration()));}
			if (createInvoice.getMajResult().isEmpty()) {invoiceProforma.setProfit(0);}else {
			invoiceProforma.setProfit(Tools.decimalToDouble(createInvoice.getMajResult()));}
			invoiceProforma.setRetirement(Tools.decimalToDouble(createInvoice.getRetirement()));
			invoiceProforma.setTotalHt(Tools.decimalToDouble(createInvoice.getTotalHt()));
			invoiceProforma.setTvaImp(Tools.decimalToDouble(createInvoice.getDiffTva()));
			if (createInvoice.getTranspAmount().isEmpty()) {invoiceProforma.setTransortation(0);}else {
			invoiceProforma.setTransortation(Tools.decimalToDouble(createInvoice.getTranspAmount()));}
			invoiceProforma.setTaxDomic(Tools.decimalToDouble(createInvoice.getDomic()));
			if (createInvoice.getMagTrans().isEmpty()) {invoiceProforma.setMagTrans(0);}else {
				invoiceProforma.setMagTrans(Tools.decimalToDouble(createInvoice.getMagTrans()));}
			if (createInvoice.getBancTax().isEmpty()) {invoiceProforma.setBancTax(0);}else {
				invoiceProforma.setBancTax(Tools.decimalToDouble(createInvoice.getBancTax()));}
			for (int i = 0; i < createInvoice.getProductGrid().getRowCount(); i++) {
				List<Product> prdlst = session.createQuery("from Product where ref = '"
						+ (createInvoice.getProductGridModel().getValueAt(i, 1).toString()) + "'").list();
				LineProductProforma lineProductProforma = new LineProductProforma();
				lineProductProforma.setQuantity(
						Tools.decimalToInteger(createInvoice.getProductGridModel().getValueAt(i, 3).toString()));
				lineProductProforma.setUnitPriceDevise(
						Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 4).toString()));
				lineProductProforma.setUnitPriceDinar(
						Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 5).toString()));
				lineProductProforma.setInvoiceProforma(invoiceProforma);
				lineProductProforma.setProduct(prdlst.get(0));
				invoiceProforma.getLineProductProforma().add(lineProductProforma);
				prdlst.removeAll(prdlst);
			}
			double debit = cl.get(0).getDebt();
			cl.get(0).setDebt(debit + Tools.decimalToDouble(createInvoice.getMontantFinal()));
			session.update(cl.get(0));
			session.save(invoiceProforma);
			session.getTransaction().commit();
			JOptionPane.showMessageDialog(null, "Enregistrement acompli avec succés ");
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un article : " + ex.getMessage());
		} finally {
			session.close();
		}

	}

	/******* Select */
	public List<InvoiceProforma> Select(String invID) {
		List<InvoiceProforma> inv=null;
		Session session = hibernateUtil.getSessionFactory().openSession();

		try {
			session.beginTransaction();
			inv = session.createQuery("from InvoiceProforma where ref = '"+invID+"'").list();
			
			session.getTransaction().commit();
			
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex);
		} finally {
			session.close();
		}
		return inv;
	}

	/********
	 * update
	 * 
	 * @throws Throwable
	 */
	public void Update(int selectedInvoice) throws Throwable {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			InvoiceProforma invoiceProforma = session.get(InvoiceProforma.class, selectedInvoice);     
			previousAmount = invoiceProforma.getTotal();
			for (LineProductProforma Lpp : invoiceProforma.getLineProductProforma()) {
				session.delete(Lpp);
			}
			invoiceProforma.getLineProductProforma().clear();
			List<Client> cl = session.createQuery("from Client where id = '" + Integer.parseInt(createInvoice.getCustomerId()) +
					"'").list();
			invoiceProforma.setRef(createInvoice.getReference());
			invoiceProforma.setClient(cl.get(0));
			invoiceProforma.setTodayDate(createInvoice.getTodayDate());
			invoiceProforma.setSurface(Tools.decimalToInteger(createInvoice.getSurface()));
			invoiceProforma.setMetalColor(createInvoice.getMetalColor());
			invoiceProforma.setSeatColor(createInvoice.getSeatColor());
			invoiceProforma.setProject(createInvoice.getProject());
			invoiceProforma.setTotalDevise(Tools.decimalToDouble(createInvoice.getTotalDevise()));
		//	invoiceProforma.setTotalDinar(Tools.decimalToDouble(createInvoice.getTotalDa()));
			invoiceProforma.setPrecompteDouane(Tools.decimalToDouble(createInvoice.getPrecompteDouane()));
			invoiceProforma.setChange(Tools.decimalToDouble(createInvoice.getChange()));
			invoiceProforma.setTva(Tools.decimalToDouble(createInvoice.getTVA()));
			invoiceProforma.setTotal(Tools.decimalToDouble(createInvoice.getMontantFinal()));
			invoiceProforma.setProfitPercent(Tools.decimalToInteger(createInvoice.getMajoration()));
			invoiceProforma.setProfit(Tools.decimalToDouble(createInvoice.getMajResult()));
			invoiceProforma.setRetirement(Tools.decimalToDouble(createInvoice.getRetirement()));
			invoiceProforma.setTotalHt(Tools.decimalToDouble(createInvoice.getTotalHt()));
			invoiceProforma.setTvaImp(Tools.decimalToDouble(createInvoice.getDiffTva()));
			invoiceProforma.setTransortation(Tools.decimalToDouble(createInvoice.getTranspAmount()));
			invoiceProforma.setTaxDomic(Tools.decimalToDouble(createInvoice.getDomic()));
			invoiceProforma.setMagTrans(Tools.decimalToDouble(createInvoice.getMagTrans()));
			invoiceProforma.setBancTax(Tools.decimalToDouble(createInvoice.getBancTax()));
			for (int i = 0; i < createInvoice.getProductGrid().getRowCount(); i++) {
				List<Product> prdlst = session.createQuery("from Product where ref = '"
						+ (createInvoice.getProductGridModel().getValueAt(i, 1).toString()) + "'").list();
				LineProductProforma lineProductProforma = new LineProductProforma();
				lineProductProforma.setQuantity(
						Tools.decimalToInteger(createInvoice.getProductGridModel().getValueAt(i, 3).toString()));
				lineProductProforma.setUnitPriceDevise(
						Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 4).toString()));
				lineProductProforma.setUnitPriceDinar(
						Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 5).toString()));
				lineProductProforma.setInvoiceProforma(invoiceProforma);
				lineProductProforma.setProduct(prdlst.get(0));
				invoiceProforma.getLineProductProforma().add(lineProductProforma);
				prdlst.remove(prdlst.get(0));
			}
			
			deletedProduct.clear();
			double debit = cl.get(0).getDebt();
			cl.get(0).setDebt(debit + Tools.decimalToDouble(createInvoice.getMontantFinal()) - previousAmount);
			session.update(cl.get(0));
			previousAmount = 0;
			/***************************** end ********************************/
			session.update(invoiceProforma);
			session.getTransaction().commit();
			JOptionPane.showMessageDialog(null, "Modification acompli avec succés ");
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de modification d'un article : " + ex.getMessage());
		} finally {
			session.close();
		}

	}

	public void btnSwitcher(boolean cs, boolean adu) {
		createInvoice.getCancelbtn().setEnabled(cs);
		createInvoice.getSavebtn().setEnabled(cs);
		createInvoice.getAddbtn().setEnabled(adu);
		createInvoice.getDeletebtn().setEnabled(adu);
	}

	public void getCurrencyValue() {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<Currency> cu = session.createQuery("from Currency").list();
			if (cu.size() <= 0) {
				JOptionPane.showMessageDialog(null, "Le taux de change n'est pas initializer !!");
			} else {
				change = cu.get(cu.size() - 1).getMoney();
				String money = Tools.printDecimal(change);
				createInvoice.setChange(money);
			}
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex);
		} finally {
			session.close();
		}

	}

	public void clearEverything() {
		for (Component comp : createInvoice.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
			}
		}
		createInvoice.getProductGridModel().setRowCount(0);
		CreateInvoice.txtPrecompteDouane.setText("0,00");
		createInvoice.setSurface("0");
	}

	public void clearNumberOfProduct() {
		createInvoice.lblSumProduct.setText(String.valueOf(CreateInvoice.productGrid.getRowCount()));
	}

	public void updateProductQuantity(Session session, String ref, int qte) {
		int existingQte = 0;
		List<Product> pr = session.createQuery("from Product where ref = '" + ref + "'").list();
		existingQte = pr.get(0).getQuantity();
		pr.get(0).setQuantity(existingQte - qte);
		session.update(pr.get(0));

	}


	public void gridCalcul(double exch) throws NumberFormatException, Throwable {
		double montantDevise = 0;
	//	double montantDa = 0;
		double TVA = 0;
		double total = 0;
		double retirement = 0;
		if (CreateInvoice.productGrid.getRowCount() > 0) {
			for (int i = 0; i < CreateInvoice.productGrid.getRowCount(); i++) {
				montantDevise = montantDevise
						+ Tools.decimalToDouble(CreateInvoice.productGridModel.getValueAt(i, 6).toString());
			}
			CreateInvoice.txtMontant.setText(Tools.printDecimal(montantDevise));
		//	montantDa = Tools.CurrencyConverter(montantDevise, exch);
		//	CreateInvoice.txtMontantDa.setText(Tools.printDecimal(montantDa));
			//retirement = (montantDa *1.19) * 0.04;
		//	createInvoice.setRetirement(Tools.printDecimal(retirement));
		} else {
			CreateInvoice.txtMontant.setText(null);
		//	CreateInvoice.txtMontantDa.setText(null);
			CreateInvoice.txtTVA.setText(null);
			CreateInvoice.txtMontantFinal.setText(null);
		}
	}

	public void fillDeletedProducts() {
		for (int prdGridRow = 0; prdGridRow < createInvoice.getProductGrid().getRowCount(); prdGridRow++) {
			if (!(deletedProduct
					.containsKey(createInvoice.getProductGridModel().getValueAt(prdGridRow, 1).toString()))) {
				deletedProduct.put(createInvoice.getProductGridModel().getValueAt(prdGridRow, 1).toString(),
						createInvoice.getProductGridModel().getValueAt(prdGridRow, 3).toString());
			}
		}
	}

	public void GridCalculAfterChange(double change) throws ParseException, Throwable {
		String prixDa, totalDa;
		for (int i = 0; i < createInvoice.getProductGrid().getRowCount(); i++) {
			double prixDA = Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 4).toString())
					* change;
			prixDa = Tools.printDecimal(prixDA);
			createInvoice.getProductGridModel().setValueAt(prixDa, i, 5);
			double totalDA = Tools.decimalToDouble(createInvoice.getProductGridModel().getValueAt(i, 6).toString())
					* change;
			totalDa = Tools.printDecimal(totalDA);

			createInvoice.getProductGridModel().setValueAt(totalDa, i, 7);
		}
	}

	
	/***************************************Import excel file **********************************************************/
	class XLSImport implements ActionListener{
		int numberOfProduct = 0;
		int numberOfProductExisted = 0;
		String notFoundProd = "";
		double tauxDeChange;
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("XLS", "XLSX");
			fileChooser.setFileFilter(extensionFilter);
			fileChooser.showDialog(null, "Selectionnez votre Fichier Excel ");
			String myFilePath = fileChooser.getSelectedFile().getPath().toString();
			try {
				tauxDeChange = Tools.decimalToDouble(createInvoice.getChange());
				FileInputStream fis = new FileInputStream(new File(myFilePath));
				XSSFWorkbook book = new XSSFWorkbook(fis);
				Object[] row  = new Object[8];
				int rowcount = book.getSheetAt(0).getLastRowNum();
				for (int i=0 ; i<rowcount+1 ; i++) {
					Session session = hibernateUtil.getSessionFactory().openSession();
						try {
							session.beginTransaction();
							List<Product> prdls = session.createQuery("from Product where ref = '"+
									  book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString()+"'").list();
							if (prdls.size()==1) {
							if(prdls.get(0).getImg()==null) { row[0] = null;}
							else {	row[0] = Tools.ImageModifier(prdls.get(0).getImg());}
							row[1] = prdls.get(0).getRef();
							row[2] = prdls.get(0).getDesg();
		/*QTE*/				int qte = Tools.decimalToInteger(book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("C")).toString());
							row[3] = qte;
							row[4] = Tools.printDecimal(prdls.get(0).getUnitPrice());
		     				row[5] = Tools.printDecimal(prdls.get(0).getUnitPrice()*tauxDeChange);
		
							double totalDev = prdls.get(0).getUnitPrice() * qte; 
							row[6] = Tools.printDecimal(totalDev);
							row[7] = Tools.printDecimal(totalDev * tauxDeChange);
							createInvoice.getProductGridModel().addRow(row);
							}
							else {notFoundProd = notFoundProd+" "+book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString();}
							
							 prdls.clear();
							session.getTransaction().commit();
						} catch (HibernateException ex) {
							session.getTransaction().rollback();
							JOptionPane.showMessageDialog(null, "Erreur d'insertion d'une article: " + ex.getMessage());
						} finally {
							session.close();
						}
					
					}
				gridCalcul(tauxDeChange);
				createInvoice.setSumProduct(createInvoice.getProductGrid().getRowCount());
				book.close();
				fis.close();
				JOptionPane.showMessageDialog(null, "Opération terminée ... : \n" +
													"Liste des artiles introuvables : "+notFoundProd);
			} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
								
				}
		}
	}
	/*****************************************************end of xls import *************************************************/	
class XLSExport implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "exp to excel ");
			writeToExcell();
			
		}
	
}
private String getCellValue(int x, int y) {
	return createInvoice.getProductGridModel().getValueAt(x, y).toString();
	
}
	
public void writeToExcell() {
   
	XSSFWorkbook wb = new XSSFWorkbook(); //Excell workbook
	XSSFSheet ws = wb.createSheet();
     TreeMap<String, Object[]> data = new TreeMap<String, Object[]>();
     data.put("0", new Object[] {createInvoice.getProductGridModel().getColumnName(1),
    		 createInvoice.getProductGridModel().getColumnName(2), createInvoice.getProductGridModel().getColumnName(3),
    		 createInvoice.getProductGridModel().getColumnName(4)});
     data.put("1", new Object[] {getCellValue(0, 0), getCellValue(0, 1), getCellValue(0, 2), getCellValue(0, 3), getCellValue(0, 4) });
     data.put("2", new Object[] {getCellValue(1, 0), getCellValue(1, 1), getCellValue(1, 2), getCellValue(1, 3), getCellValue(1, 4) });
     data.put("3", new Object[] {getCellValue(2, 0) ,getCellValue(2, 1), getCellValue(2, 2), getCellValue(2, 3), getCellValue(2, 4) });
     data.put("4", new Object[] {getCellValue(3, 0), getCellValue(3, 1), getCellValue(3, 2), getCellValue(3, 3), getCellValue(3, 4) });
     
     Set<String> ids = data.keySet();
    XSSFRow row;
     int rowId = 0;
     
     for (String Key : ids) {
    	 row = ws.createRow(rowId++);
    	 Object[] values = data.get(Key);
    	 int cellId = 0;
    	 for (Object o : values) {
    		 
    		 Cell cell = row.createCell(cellId++);
    		 cell.setCellValue(o.toString());
    		 
    	 }
    	 
     }
     try {
    	 FileOutputStream fos = new FileOutputStream(new File("C:/CommercialRessources/Facture.xlsx"));
    	 wb.write(fos);
    	 fos.close();
     }catch(FileNotFoundException ex) {
    	 
     } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}	
	
	
}
