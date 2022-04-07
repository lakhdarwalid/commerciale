package com.commerciale.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.controller.CRUDinvoiceListProforma.TansferToXLS;
import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.Invoice;
import com.commerciale.model.InvoiceProforma;
import com.commerciale.model.LineProduct;
import com.commerciale.model.LineProductProforma;
import com.commerciale.model.Product;
import com.commerciale.model.Reception;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.report.PrintInvoice;
import com.commerciale.report.ReportFrame;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateClient;
import com.commerciale.view.CreateInvoice;
import com.commerciale.view.CreateReception;
import com.commerciale.view.CreateSupplier;
import com.commerciale.view.InvoiceList;
import com.commerciale.view.MainMenu;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class CRUDinvoiceList {
	private Invoice invoice;
	private InvoiceList invoiceList;
	private CreateInvoice createInvoice;
	private String addOrUpdate;
	private int selected = -1;
	private int SR;
	private int selectedInvoice = -1;
	private String path = "";
	public CRUDinvoiceList(Invoice invoice, InvoiceList invoiceList) {
		this.invoice = invoice;
		this.invoiceList = invoiceList;
		this.invoiceList.addingInvoiceListener(new addInvoiceListener());
		this.invoiceList.updatingListener(new updateInvoiceListener());
		this.invoiceList.searchListener(new searchingInvoiceListener());
		this.invoiceList.findAllInvBtnListener(new searchingbtnInvoiceListener());
		this.invoiceList.deleteBtnListener(new deletingInvoiceListener());
		this.invoiceList.invoiceGridListener(new gridNavigListener());
		this.invoiceList.allNeddedWhenOpen(new behaveWhenOpen());
		this.invoiceList.printingbtnListener(new printingListener());
		this.invoiceList.printingNoImagebtnListener(new printingNoImgListener());
		this.invoiceList.printingNoPricebtnListener(new printingNoPriceListener());
		this.invoiceList.searhByDateListener(new SearchByDateListener());
		this.invoiceList.receptionerBtnListener(new ReceptionListener());
		this.invoiceList.TransferToExcel(new TansferToXLS());
	}
/*********************************************** CRUD btn Class *******************************************/	

	
	/**********PRINTING *************************/
class printingListener extends CustomActionListeners{

	@Override
	public void mouseClicked(MouseEvent e) {
      JasperPrint jasperPrint;
	  if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un element par cliquer sur la liste des factures SVP !!");}
	  else { 
		  Object[] options = { "Facture", "Bon de livraison", "Annuler"};
			int answer = JOptionPane.showOptionDialog(null, "Veuillez Choisir une option svp : ", "Confirmation de l'impression",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {
				PrintInvoice printInvoice = new PrintInvoice();
				
				try {
					
					jasperPrint = printInvoice.print("from Invoice where invoice_id= '"+selected+"'", "C:\\CommercialRessources\\InvoiceReport.jrxml");// "C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\InvoiceReport.jrxml");
					
					ReportFrame reportFrame = new ReportFrame();
					reportFrame.getContentPane().add(new JRViewer(jasperPrint));
				    reportFrame.show();
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());
				}
			}if (answer ==1) {
				PrintInvoice printInvoice = new PrintInvoice();
				try {
					jasperPrint = printInvoice.print("from Invoice where invoice_id= '"+selected+"'", "C:\\CommercialRessources\\DeliveryReceipt.jrxml");// "C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\InvoiceReport.jrxml");
					ReportFrame reportFrame = new ReportFrame();
					reportFrame.getContentPane().add(new JRViewer(jasperPrint));
				    reportFrame.show();
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());
				}
				
			}
	  }
	}
} 

class printingNoImgListener extends CustomActionListeners{

	@Override
	public void mouseClicked(MouseEvent e) {
      JasperPrint jasperPrint;
	  if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un element par cliquer sur la liste des factures SVP !!");}
	  else {
		  Object[] options = { "Facture TTC", "Facture THT", "Annuler"};
			int answer = JOptionPane.showOptionDialog(null, "Veuillez Choisir une option svp : ", "Confirmation de l'impression",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			
				PrintInvoice printInvoice = new PrintInvoice();
				try {
					if (answer==0) {
						jasperPrint = printInvoice.print("from Invoice where invoice_id= '"+selected+"'","C:\\CommercialRessources\\InvoiceReportNoImage.jrxml"); // "C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\InvoiceReportNoImage.jrxml");
					}else {
						jasperPrint = printInvoice.print("from Invoice where invoice_id= '"+selected+"'","C:\\CommercialRessources\\InvoiceReportNoImageHT.jrxml"); // "C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\InvoiceReportNoImage.jrxml");
					}
					ReportFrame reportFrame = new ReportFrame();
					reportFrame.getContentPane().add(new JRViewer(jasperPrint));
				    reportFrame.show();
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());
				}
			}
	  }
} 

class printingNoPriceListener extends CustomActionListeners{

	@Override
	public void mouseClicked(MouseEvent e) {
      JasperPrint jasperPrint;
	  if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un element par cliquer sur la liste des factures SVP !!");}
	  else {
		PrintInvoice printInvoice = new PrintInvoice();
		try {
			jasperPrint = printInvoice.print("from Invoice where invoice_id= '"+selected+"'", "C:\\CommercialRessources\\InvoiceReportNoPrice.jrxml");//"C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\InvoiceReportNoPrice.jrxml");
			ReportFrame reportFrame = new ReportFrame();
			reportFrame.getContentPane().add(new JRViewer(jasperPrint));
		    reportFrame.show();
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());
		}
	  }
	}
} 


/******************* ON WINDOW OPEN BEHAVIOR ***************/
 class behaveWhenOpen extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		try {
			invoiceList.changeBackgroundColor(new Color(240,248,255));
			Select("from Invoice");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		
	}
 }
/**********************BEHAVIOR ENDS ***********************/
	
/************ADD ****************************/
class addInvoiceListener extends CustomActionListeners{
   
	@Override
	public void actionPerformed(ActionEvent e) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String year = sdf.format(new Date());
		CreateInvoice createInvoice = CreateInvoice.getInstance();
		Currency currency = new Currency();
		Client client = new Client();
		CreateClient createClient = CreateClient.getInstance();
		CRUDclient crudClient = new CRUDclient(client, createClient,createInvoice);
		CRUDinvoice crudInvoice = new CRUDinvoice( invoice, createInvoice, currency, crudClient);
		if (!(getInvoiceNumber()==null)) {
				if (!createInvoice.isVisible()) {
					
										   createInvoice.setReference(getInvoiceNumber());
										   createInvoice.setRefYear("/"+year);
										   createInvoice.setTodayDate(new Date());
										   createInvoice.setTitle("Nouvelle Facture");
										  MainMenu.desktopPane.add(createInvoice);
										   createInvoice.show();
										   invoiceList.dispose();
										   invoiceList.setIL(null);
				}	
		CRUDinvoice.addOrUpdate = "ADD";  
		} else {JOptionPane.showMessageDialog(null, "Veuillez verifier la date de systeme avant de continuer svp !!"); }
	}
}


/****************UPDATE **********************/
class updateInvoiceListener extends CustomActionListeners{
	private double totalDevise, totalDa;  
	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des factures pour selectionner l'element que vous voulez le modifier !! ");}
		else {
		    createInvoice = CreateInvoice.getInstance();
			Currency currency = new Currency();
			Client client = new Client();
			CreateClient createClient = CreateClient.getInstance();
			CRUDclient crudClient = new CRUDclient(client, createClient,createInvoice);
			CRUDinvoice crudInvoice = new CRUDinvoice( invoice, createInvoice, 
					 currency, crudClient);
			if (!createInvoice.isVisible()) {
				createInvoice.setReference(invoiceList.getInvoiceGridModel().getValueAt(SR, 0).toString());
				createInvoice.setCustomerId(invoiceList.getInvoiceGridModel().getValueAt(SR, 9).toString());
				createInvoice.setSurface(invoiceList.getInvoiceGridModel().getValueAt(SR, 10).toString());
				createInvoice.setMetalColor(invoiceList.getInvoiceGridModel().getValueAt(SR, 11).toString());
				createInvoice.setSeatColor(invoiceList.getInvoiceGridModel().getValueAt(SR, 12).toString());
				createInvoice.setProject(invoiceList.getInvoiceGridModel().getValueAt(SR, 24).toString());
				createInvoice.setCustomerName(invoiceList.getInvoiceGridModel().getValueAt(SR, 1).toString());
				try {
					createInvoice.setTodayDate(Tools.stringToDate(invoiceList.getInvoiceGridModel().getValueAt(SR, 2).toString()));
				} catch (ParseException e3) {JOptionPane.showMessageDialog(null, e3.getMessage());}
				createInvoice.setTotalDevise(invoiceList.getInvoiceGridModel().getValueAt(SR, 3).toString());
			//	createInvoice.setTotalDa(invoiceList.getInvoiceGridModel().getValueAt(SR, 4).toString());
				createInvoice.setChange(invoiceList.getInvoiceGridModel().getValueAt(SR, 5).toString());
				createInvoice.setPrecompteDouane(invoiceList.getInvoiceGridModel().getValueAt(SR, 4).toString());
				createInvoice.setTVA(invoiceList.getInvoiceGridModel().getValueAt(SR, 7).toString());
				createInvoice.setMontantFinal(invoiceList.getInvoiceGridModel().getValueAt(SR, 8).toString());	
				createInvoice.setTotalHt(invoiceList.getInvoiceGridModel().getValueAt(SR, 15).toString());
				createInvoice.setMajoration(invoiceList.getInvoiceGridModel().getValueAt(SR, 13).toString());
				createInvoice.setMajResult(invoiceList.getInvoiceGridModel().getValueAt(SR, 14).toString());
				createInvoice.setRetirement(invoiceList.getInvoiceGridModel().getValueAt(SR, 16).toString());
				createInvoice.setDiffTva(invoiceList.getInvoiceGridModel().getValueAt(SR, 17).toString());
				createInvoice.setTransportation(invoiceList.getInvoiceGridModel().getValueAt(SR, 18).toString());
				createInvoice.setTranspAmount(invoiceList.getInvoiceGridModel().getValueAt(SR, 18).toString());
				createInvoice.setRefYear("/"+invoiceList.getInvoiceGridModel().getValueAt(SR, 20).toString());
				createInvoice.setDomic(invoiceList.getInvoiceGridModel().getValueAt(SR, 21).toString());
				createInvoice.setMagTrans(invoiceList.getInvoiceGridModel().getValueAt(SR, 22).toString());
				createInvoice.setBancTax(invoiceList.getInvoiceGridModel().getValueAt(SR, 23).toString());
				createInvoice.getProductGridModel().setRowCount(0);
					Object[] row = new Object[8];
					
					for (int i=0; i< invoiceList.getproductGrid().getRowCount(); i++) {
						
						try {
							row[0] = SelectProductImg(invoiceList.getProductGridModel().getValueAt(i, 1).toString());
						} catch (Throwable e1) {JOptionPane.showMessageDialog(null,"Image introuvable !! \n"+ e1.getMessage()); row[0] ="";
							}
						row[1] = invoiceList.getProductGridModel().getValueAt(i, 1).toString();
						row[2] = invoiceList.getProductGridModel().getValueAt(i, 2).toString();
						row[3] = invoiceList.getProductGridModel().getValueAt(i, 3).toString();
						row[4] = invoiceList.getProductGridModel().getValueAt(i, 4).toString();
						row[5] = invoiceList.getProductGridModel().getValueAt(i, 5).toString();
						try {
							totalDevise = Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 3).toString()) *
									       Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 4).toString());
						} catch (Throwable e2) {JOptionPane.showMessageDialog(null, e2.getMessage());
						}
						row[6] =Tools.printDecimal(totalDevise); 
						try {totalDa =Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 5).toString())* Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 3).toString());
						} catch (ParseException e1) {e1.printStackTrace();
						} catch (Throwable e1) {e1.printStackTrace();}
						row[7] = Tools.printDecimal(totalDa); 
						createInvoice.getProductGridModel().addRow(row);
					}
	
				MainMenu.desktopPane.add(createInvoice);
				CreateInvoice.lblSumProduct.setText(String.valueOf(createInvoice.getProductGrid().getRowCount()));
				CRUDinvoice.addOrUpdate = "UPDATE";
				crudInvoice.fillDeletedProducts();
				CRUDinvoice.selectedInvoice =Integer.parseInt(invoiceList.getInvoiceGridModel().getValueAt(SR, 19).toString());
				createInvoice.setTitle("Modification d'une facture");
				createInvoice.getSavebtn().setEnabled(true);
				createInvoice.show();
			    invoiceList.dispose();
			}}
		}
}
/*************DELETE ***************************/
class deletingInvoiceListener extends CustomActionListeners{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(selected<0) {
			JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des factures pour "
					+ "selectionner l'element que vous voulez le supprimer !! ");}
		else {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {Delete(selected); selected = -1;}
			try {
				Select("from Invoice");
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
		}	
	}
}
/*************SEARCH ***************************/
/**************SEARCHING BY DATE **********************/
class SearchByDateListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			String startDate = Tools.dateToStringFlipped(invoiceList.getStartDate());
			String endDate = Tools.dateToStringFlipped(invoiceList.getEndDate());
			Select("from Invoice where todayDate BETWEEN '"+startDate+"' AND '"+endDate+ "'");
		
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"Selectionez une periode SVP!! \n" +e1.getMessage());
		}
		
	}
	
}	
/***************SEARCHING ENDS ************************/
	
class searchingInvoiceListener extends CustomActionListeners{
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			SelectClient("from Client where name LIKE '%"+invoiceList.getTxtSearch()+"%'");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"hna "+ e1.getMessage());
		}
	}
}



class searchingbtnInvoiceListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		invoiceList.setTxtSearch(null);
		try {
			Select("from Invoice");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
}

class gridNavigListener extends CustomActionListeners{
	private String id ;
	private String status;
	@Override
	public void mouseClicked(MouseEvent e) {
		SR= invoiceList.getInvoiceGrid().getSelectedRow();
		id = invoiceList.getInvoiceGridModel().getValueAt(SR, 19).toString();
		selectedInvoice = Integer.parseInt(id);
		invoiceList.setTaxRetirement(invoiceList.getInvoiceGridModel().getValueAt(SR, 16).toString());
		invoiceList.setSuperficie(invoiceList.getInvoiceGridModel().getValueAt(SR, 10).toString());
		invoiceList.setTvaImpo(invoiceList.getInvoiceGridModel().getValueAt(SR, 17).toString());
		invoiceList.setTransport(invoiceList.getInvoiceGridModel().getValueAt(SR, 18).toString());
		invoiceList.setTaxDomic(invoiceList.getInvoiceGridModel().getValueAt(SR, 21).toString());
		invoiceList.setMagTrans(invoiceList.getInvoiceGridModel().getValueAt(SR, 22).toString());
		invoiceList.setBancTax(invoiceList.getInvoiceGridModel().getValueAt(SR, 23).toString());
		if (!(invoiceList.getInvoiceGridModel().getValueAt(SR, 24).toString().isEmpty())) {
		invoiceList.setProject(invoiceList.getInvoiceGridModel().getValueAt(SR, 24).toString());}
		selected = Integer.parseInt(id);
		try {
			SelectProduct("from Invoice where invoice_id="+id);
			
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
}


 /***********************************CRUD **********************************************************************************************/

/*******Select
 * @throws Throwable */
public void Select (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Invoice> ar =  session.createQuery(query).list();
		fillInvoiceGrid(ar);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

public void SelectClient (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery(query).list();
		if (cl.size()>0) {
		fillInvoiceGrid(cl.get(0).getInvoices());}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

public void SelectProduct (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	invoiceList.getProductGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Invoice> ar =  session.createQuery(query).list();
		fillProductGrid(ar.get(0).getLineProduct());
		session.getTransaction().commit();
		invoiceList.setSumProduct(invoiceList.getProductGridModel().getRowCount());
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

/*********Delete */
public void Delete(int selected) {
	double ttc = 0, cl_debt = 0 , ndebt = 0;
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Invoice ar = session.get(Invoice.class, selected);
		ttc = ar.getTotal();
		cl_debt=ar.getClient().getDebt();
		ndebt = cl_debt - ttc;
		ar.getClient().setDebt(ndebt);
		session.update(ar.getClient());
		session.remove(ar);
		session.getTransaction().commit();
	}
	catch(HibernateException e) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, e.getMessage());
	}
	finally {
		session.close();
	}
			
}

/************************************End of CRUD 
 * @throws Throwable **************************************************************************************/

public void fillInvoiceGrid (List<Invoice>  li) throws Throwable {
	Object[] row = new Object[25];
	if (invoiceList.getInvoiceGridModel().getRowCount()>0) {invoiceList.getInvoiceGridModel().setRowCount(0);}
	if (li.size()>0) {
		Collections.reverse(li);
	for (Invoice tempInvoice : li) {
		row[0] = tempInvoice.getRef();
		row[1] = tempInvoice.getClient().getName();
		row[2] = Tools.dateToString(tempInvoice.getTodayDate());
		row[3] = Tools.printDecimal(tempInvoice.getTotalDevise());
		row[4] = Tools.printDecimal(tempInvoice.getPrecompteDouane());
		row[5] = Tools.printDecimal(tempInvoice.getChange());
		row[6] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[7] = Tools.printDecimal(tempInvoice.getTva());
		row[8] = Tools.printDecimal(tempInvoice.getTotal());
		row[9] = tempInvoice.getClient().getId();
		row[10] = tempInvoice.getSurface();
		row[11] = tempInvoice.getMetalColor();
		row[12] = tempInvoice.getSeatColor();
		row[13] = tempInvoice.getProfitPercent();
		row[14] = Tools.printDecimal(tempInvoice.getProfit());
		row[15] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[16] = Tools.printDecimal(tempInvoice.getRetirement());
		row[17] = Tools.printDecimal(tempInvoice.getTvaImp());
		row[18] = Tools.printDecimal(tempInvoice.getTransortation());
		row[19] = tempInvoice.getId();
		row[20] = tempInvoice.getInvYear();
		row[21] = Tools.printDecimal(tempInvoice.getTaxDomic());
		row[22] = Tools.printDecimal(tempInvoice.getMagTrans());
		row[23] = Tools.printDecimal(tempInvoice.getBancTax());
		row[24] = tempInvoice.getProject();
		invoiceList.getInvoiceGridModel().addRow(row);
	}
	invoiceList.setSumInvoices(String.valueOf(invoiceList.getInvoiceGrid().getRowCount()));
	}
}

public void fillInvoiceGrid (Set<Invoice>  li) throws Throwable {
	Object[] row = new Object[25];
	if (invoiceList.getInvoiceGridModel().getRowCount()>0) {invoiceList.getInvoiceGridModel().setRowCount(0);}
	if (li.size()>0) { 
	List<Invoice> tempList = new ArrayList<Invoice>();
	tempList.addAll(li);
	Collections.reverse(tempList);
	for (Invoice tempInvoice : tempList) {
		row[0] = tempInvoice.getRef();
		row[1] = tempInvoice.getClient().getName();
		row[2] = Tools.dateToString(tempInvoice.getTodayDate());
		row[3] = Tools.printDecimal(tempInvoice.getTotalDevise());
		row[4] = Tools.printDecimal(tempInvoice.getPrecompteDouane());
		row[5] = Tools.printDecimal(tempInvoice.getChange());
		row[6] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[7] = Tools.printDecimal(tempInvoice.getTva());
		row[8] = Tools.printDecimal(tempInvoice.getTotal());
		row[9] = tempInvoice.getClient().getId();
		row[10] = tempInvoice.getSurface();
		row[11] = tempInvoice.getMetalColor();
		row[12] = tempInvoice.getSeatColor();
		row[13] = tempInvoice.getProfitPercent();
		row[14] = Tools.printDecimal(tempInvoice.getProfit());
		row[15] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[16] = Tools.printDecimal(tempInvoice.getRetirement());
		row[17] = Tools.printDecimal(tempInvoice.getTvaImp());
		row[18] = Tools.printDecimal(tempInvoice.getTransortation());
		row[19] = tempInvoice.getId();
		row[20] = tempInvoice.getInvYear();
		row[21] = Tools.printDecimal(tempInvoice.getTaxDomic());
		row[22] = Tools.printDecimal(tempInvoice.getMagTrans());
		row[23] = Tools.printDecimal(tempInvoice.getBancTax());
		row[24] = tempInvoice.getProject();
		invoiceList.getInvoiceGridModel().addRow(row);
	}
	invoiceList.setSumInvoices(String.valueOf(invoiceList.getInvoiceGrid().getRowCount()));
	}
}

public void fillProductGrid(List<LineProduct> prd) throws Throwable {
	Object[] row1 = new Object[6];
		for (LineProduct tempProduct : prd) { 
			if(tempProduct.getProduct().getImg()==null) { row1[0] = null;}
			else {
			try {
				row1[0] = Tools.ImageModifier(tempProduct.getProduct().getImg());
			} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
											+ e.getMessage());}}
			row1[1] = tempProduct.getProduct().getRef();
			row1[2] = tempProduct.getProduct().getDesg();
			row1[3] = tempProduct.getQuantity();
			row1[4] = Tools.printDecimal(tempProduct.getUnitPriceDevise());
			row1[5] = Tools.printDecimal(tempProduct.getUnitPriceDinar());
			invoiceList.getProductGridModel().addRow(row1);
		}
}

public void btnSwitcher (boolean cs, boolean adu) {
	invoiceList.getUpdatebtn().setEnabled(adu);
	invoiceList.getAddbtn().setEnabled(adu);
	invoiceList.getDeletebtn().setEnabled(adu);
	invoiceList.getInvoiceGrid().setEnabled(adu);
	
}

public ImageIcon SelectProductImg (String ref)throws Throwable  {
	ImageIcon img = null;
	Session session = hibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Product> ar =  session.createQuery("from Product where ref = '"+ref+"'").list();
		if (!(ar.get(0).getImg() == null)) {
			try {
				img = Tools.ImageModifier(ar.get(0).getImg());
			} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
											+ e.getMessage());}}
			else {img = null;}
		session.getTransaction().commit();
		session.close();
		return img;
	}

/*************** reception *****************/
class ReceptionListener implements ActionListener{
	private double totalDevise, totalDa; 
	@Override
	public void actionPerformed(ActionEvent e) {
		if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des factures pour selectionner l'element que vous voulez le modifier !! ");}
		else {
			Reception reception = new Reception();
		    CreateReception createReception = CreateReception.getInstance();
			Currency currency = new Currency();
			Supplier supplier = new Supplier();
			CreateSupplier createSupplier = CreateSupplier.getInstance();
			CRUDsupplier crudSupplier = new CRUDsupplier(supplier, createSupplier,createReception);
			CRUDreception crudReception = new CRUDreception( reception, createReception, 
					 currency, crudSupplier);
			if (!createReception.isVisible()) {
				createReception.setReference(invoiceList.getInvoiceGridModel().getValueAt(SR, 1).toString());
				createReception.setTodayDate(invoiceList.getInvoiceGridModel().getValueAt(SR, 2).toString());
				createReception.setTotalDevise(invoiceList.getInvoiceGridModel().getValueAt(SR, 3).toString());
			//	createReception.setTotalDa(invoiceList.getInvoiceGridModel().getValueAt(SR, 4).toString());
				createReception.setChange(invoiceList.getInvoiceGridModel().getValueAt(SR, 5).toString());
				createReception.setPrecompteDouane(invoiceList.getInvoiceGridModel().getValueAt(SR, 4).toString());
				createReception.setTVA(invoiceList.getInvoiceGridModel().getValueAt(SR, 7).toString());
				createReception.setMontantFinal(invoiceList.getInvoiceGridModel().getValueAt(SR, 8).toString());
				createReception.setTransportation(invoiceList.getInvoiceGridModel().getValueAt(SR, 18).toString());
				createReception.setTranspAmount(invoiceList.getInvoiceGridModel().getValueAt(SR, 18).toString());
				try {
					crudReception.setSaleTva(Tools.decimalToDouble(invoiceList.getInvoiceGridModel().getValueAt(SR, 17).toString()));
				//	createReception.setTVADiff(invoiceList.getInvoiceGridModel().getValueAt(SR, 8).toString());
				} catch (Throwable e3) {JOptionPane.showMessageDialog(null, e3.getMessage());}
				createReception.getProductGridModel().setRowCount(0);
					Object[] row = new Object[8];
					for (int i=0; i< invoiceList.getproductGrid().getRowCount(); i++) {
						try {
							row[0] = SelectProductImg(invoiceList.getProductGridModel().getValueAt(i, 1).toString());
						} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						row[1] = invoiceList.getProductGridModel().getValueAt(i, 1).toString();
						row[2] = invoiceList.getProductGridModel().getValueAt(i, 2).toString();
						row[3] = invoiceList.getProductGridModel().getValueAt(i, 3).toString();
						row[4] = invoiceList.getProductGridModel().getValueAt(i, 4).toString();
						row[5] = invoiceList.getProductGridModel().getValueAt(i, 5).toString();
						try {
							totalDevise = Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 3).toString()) *
									       Tools.decimalToDouble(invoiceList.getProductGridModel().getValueAt(i, 4).toString());
						} catch (Throwable e2) {JOptionPane.showMessageDialog(null, e2.getMessage());
						}
						row[6] =Tools.printDecimal(totalDevise); 
						try {totalDa = totalDevise * Tools.decimalToDouble(invoiceList.getInvoiceGridModel().getValueAt(SR, 5).toString());
						} catch (ParseException e1) {e1.printStackTrace();
						} catch (Throwable e1) {e1.printStackTrace();}
						row[7] = Tools.printDecimal(totalDa); 
						createReception.getProductGridModel().addRow(row);
					}
				CreateReception.lblSumProduct.setText(String.valueOf(createReception.getProductGrid().getRowCount()));
				CRUDinvoice.selectedInvoice =Integer.parseInt(invoiceList.getInvoiceGridModel().getValueAt(SR, 0).toString());
				createReception.setTitle("Reception d'une commande faturée");
				MainMenu.desktopPane.add(createReception);
				invoiceList.dispose();
				invoiceList.setIL(null);
				createReception.show();
    
			}}
	}
	
}
/***************end of reception ***********/

public String getInvoiceNumber() {
	String lastInvoiceNumber = null, invoiceNumber = null, tempInvoiceNumber = null;
	int curYear = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
	String year = sdf.format(new Date());
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Invoice> inv = session.createQuery("from Invoice").list();
		if (inv.size() <= 0){invoiceNumber = "01";}
		else if (inv.get(inv.size() -1).getInvYear()< Integer.parseInt(year)) {
				invoiceNumber = "01"; 
		}else if (inv.get(inv.size() -1).getInvYear()> Integer.parseInt(year)) {
				JOptionPane.showMessageDialog(null, "Je pense que votre date du systeme n'est pas ajours !!!");
		}else {
			lastInvoiceNumber = inv.get(inv.size() - 1).getRef();
			invoiceNumber = "0"+String.valueOf(Integer.parseInt(lastInvoiceNumber)+1);
		}
		session.getTransaction().commit();
	} catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex);
	} finally {
		session.close();
	}
	return invoiceNumber;

}


///////////////******************************************EXCEL **********************************
public List<LineProduct> getLPP (int selInv) throws Throwable {
	List<LineProduct> LP= null;
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Invoice Inv =  session.get(Invoice.class, selInv);
		LP = Inv.getLineProduct();
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
	
	return LP;
}


private String getCellValue(int x, int y) {
	return invoiceList.getProductGridModel().getValueAt(x, y).toString();
	
}

//**********************export to excel ****************************//
public void writeToExcell(int selInvP) throws Throwable {
	List<LineProduct> LP = getLPP(selectedInvoice);   
	XSSFWorkbook wb = new XSSFWorkbook(); //Excell workbook
	XSSFSheet ws = wb.createSheet();
	XSSFRow rowInv;
    Invoice inv = LP.get(0).getInvoice();
//***************	row number 1
    rowInv = ws.createRow(0);
	Cell cellInv = rowInv.createCell(0);
	cellInv.setCellValue("Fatcure N° ");
	Cell ref = rowInv.createCell(1);
	ref.setCellValue(inv.getRef()+"/"+inv.getInvYear());
	Cell dateinv = rowInv.createCell(2);
	dateinv.setCellValue("Date:");
	Cell dateinvo = rowInv.createCell(3);
	dateinvo.setCellValue(Tools.dateToString(inv.getTodayDate()));
//**************** row number 2
	rowInv = ws.createRow(1);
	Cell cellclname = rowInv.createCell(0);
	cellclname.setCellValue("Raison sociale :");
	Cell cellclname1 = rowInv.createCell(1);
	cellclname1.setCellValue(inv.getClient().getName());
	rowInv = ws.createRow(2);
	//****************************begin  product list * ******
	
     TreeMap<String, Object[]> data = new TreeMap<String, Object[]>();
     data.put("0", new Object[] {invoiceList.getProductGridModel().getColumnName(1),
    		 invoiceList.getProductGridModel().getColumnName(2), invoiceList.getProductGridModel().getColumnName(3),
    		 invoiceList.getProductGridModel().getColumnName(4)});
     
   int i =1;
    for (LineProduct lp: LP) {
    	 data.put(String.valueOf(i), new Object[] {lp.getProduct().getRef(), lp.getProduct().getDesg(), lp.getQuantity(), lp.getUnitPriceDevise()});
         i++;
    }
 
    Set<String> ids = data.keySet();
    XSSFRow row;
     int rowId = 3;
     
     for (String Key : ids) {
    	 row = ws.createRow(rowId++);
    	 Object[] values = data.get(Key);
    	 int cellId = 0;
    	 for (Object o : values) {
    		 
    		 Cell cell = row.createCell(cellId++);
    		 cell.setCellValue(o.toString());
    		 
    	 }
    	 
     }
     
//***************** invoice THT, TVA, TTC ***********************
    rowInv = ws.createRow(rowId+1);
 	Cell cellempty = rowInv.createCell(0);
 	cellempty.setCellValue("      ");   
    
     rowInv = ws.createRow(rowId+2);
     Cell cellTHT = rowInv.createCell(2);
  	cellTHT.setCellValue(" T.H.T : ");  
  	Cell cellTHTAm = rowInv.createCell(3);
  	cellTHTAm.setCellValue(inv.getTotalHt());  
  	
  	rowInv = ws.createRow(rowId+3);
    Cell cellTVA = rowInv.createCell(2);
 	cellTVA.setCellValue(" T.V.A : ");  
 	Cell cellTVAam = rowInv.createCell(3);
 	cellTVAam.setCellValue(inv.getTva());  
 	
 	rowInv = ws.createRow(rowId+4);
    Cell cellTTC = rowInv.createCell(2);
 	cellTTC.setCellValue(" T.T.C : ");  
 	Cell cellTTCam = rowInv.createCell(3);
 	cellTTCam.setCellValue(inv.getTotal());  
 	
     File fileToSave = null;
     try {
    	 
    	 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	 JFrame parentFrame = new JFrame();
    	 
    	 JFileChooser fileChooser = new JFileChooser();
    	 fileChooser.setDialogTitle("Emplacement et nom de fichier");
    	 UIManager.put("FileChooser.cancelButtonText", "Annuler");
    	 UIManager.put("FileChooser.saveButtonText", "Enregistrer");
    	 fileChooser.updateUI();
    	  
    	 int userSelection = fileChooser.showSaveDialog(parentFrame);
    	  
    	 if (userSelection == JFileChooser.APPROVE_OPTION) {
    	      fileToSave = fileChooser.getSelectedFile();
    	 } else UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    	 if (!(fileToSave==null)) {
    	 FileOutputStream fos = new FileOutputStream(new File(fileToSave.getAbsolutePath()+".xlsx"));
    	 wb.write(fos);
    	 fos.close();
    	 JOptionPane.showMessageDialog(null, "Fichier Excel a été crée sur le chemain : \n"+fileToSave.getAbsolutePath()+".xlsx");}
     }catch(FileNotFoundException ex) {
    	 
     } catch (IOException e) {
		
		e.printStackTrace();
	}
     UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
}	

class TansferToXLS implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (selectedInvoice>0) {
		try {
			writeToExcell(selectedInvoice);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		}else JOptionPane.showMessageDialog(null, "Selectionnez une facture svp !!");
		
	}
	
}

}
