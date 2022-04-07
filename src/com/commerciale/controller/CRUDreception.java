package com.commerciale.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.LineProduct;
import com.commerciale.model.Product;
import com.commerciale.model.Reception;
import com.commerciale.model.ReceptionProduct;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateReception;
import com.commerciale.view.CreateSupplier;
import com.commerciale.view.MainMenu;
import com.commerciale.view.ProductList;
import com.commerciale.view.ReceptionList;
import com.commerciale.view.SupplierList;
import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

public class CRUDreception {
	private Reception reception;
	private CreateReception createReception;
	private CRUDsupplier crudSupplier;
	private Currency currency;
	private String addOrUpdate ="";
	public static String ChangeFromReceptionList = "" ;
	private int selected = -1;
	public static int selectedReception;
	private String path = ""; 
	private double change;
	private double saleTva = 0;
	private String transpot = "";
	private HashMap<String,String> deletedProduct = new HashMap<String,String>();
	public CRUDreception(Reception reception, CreateReception createReception, Currency currency, CRUDsupplier crudSuppier) {
		this.reception = reception;
		this.createReception = createReception;
		this.currency = currency;
		this.crudSupplier = crudSupplier;
		this.createReception.addingProductListener(new addProductListener());
		this.createReception.cancelingListener(new cancelReceptionListener());
		this.createReception.SavingListener(new saveReceptionListener());
		this.createReception.deleteBtnListener(new deletingProductListener());
		this.createReception.addCurrencyValueListener(new addExchangeValue());
		this.createReception.addSupplier(new addingSupplierListener());
		this.createReception.productGridListener(new gridNavigListener());
		this.createReception.allNeddedWhenOpen(new behaveWhenOpen());
		this.createReception.fraitRdbtnSwitch(new TransportRadioButtons());
		this.createReception.calculateAfterTransportation(new calculationAfterTransp());
	}
	
	public void setSaleTva (double stva) {
		saleTva = stva;
	}
	public double getSaleTva() {
		return saleTva;
	}
	public void setAddOrUpdate (String addOrUpdate) {
		addOrUpdate = addOrUpdate;
	}
	public String getAddOrUpdate() {
		return addOrUpdate;
	}
/*********************************************** CRUD btn Class *******************************************/	
/*************** Reception View behavior on opening *************/
class behaveWhenOpen extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		if ((!(createReception.getTitle().equalsIgnoreCase("Modification d'un bon de reception")))&&(!(createReception.getTitle().equalsIgnoreCase("Reception d'une commande faturée")))) {
			
				getCurrencyValue();
		}
	}
}	
	
	
/***********************end of behavior ************************/
class TransportRadioButtons implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equalsIgnoreCase("DA")) {
    	transpot = "DA";
      }
      if (e.getActionCommand().equalsIgnoreCase("DEVISE")) {
    	transpot = "DEVISE";
      }
      createReception.getTxtTransportation().setEditable(true);
    }
}	

class calculationAfterTransp implements ActionListener{
     double totalDa=0, totalDevise = 0, tva = 0, transAmount = 0, TauxDeChange = 0, retirement = 0,
    		 precompte = 0, TTC = 0, tht = 0, sumTransDev = 0, transDevise = 0;
	@Override
	public void actionPerformed(ActionEvent e) {
		if ((CreateReception.productGrid.getRowCount()>0)&&(!createReception.getTransportation().isEmpty())&&(!transpot.isEmpty())) {
			try {
			TauxDeChange = Tools.decimalToDouble(createReception.getChange());
			for (int i = 0 ; i < CreateReception.productGrid.getRowCount() ; i++) {
				totalDevise  = totalDevise + Tools.decimalToDouble(CreateReception.productGridModel.getValueAt(i, 6).toString());
			}
			createReception.setTotalDevise(Tools.printDecimal(totalDevise));
			totalDa = totalDevise * TauxDeChange;
			createReception.setTotalDa(Tools.printDecimal(totalDa));
		
			//totalDa = Tools.decimalToDouble(createReception.getTotalDa());
			//totalDevise = Tools.decimalToDouble(createReception.getTotalDevise());
			
			transAmount = Tools.decimalToDouble(createReception.getTransportation());
			
			precompte = Tools.decimalToDouble(createReception.getPrecompteDouane());
			
			if (transpot.equalsIgnoreCase("DEVISE")) {  transDevise = transAmount;
														transAmount = transAmount * TauxDeChange;}
			else {transDevise = transAmount/TauxDeChange;}
			retirement = (totalDa+transAmount) * 1.19 * 0.04;
			tht = totalDa + transAmount + precompte;
			tva = tht* 0.19;
			TTC = tht + tva;
			sumTransDev = transDevise+totalDevise;
			createReception.setSumTransDev(Tools.printDecimal(sumTransDev));
			createReception.setRetirement(Tools.printDecimal(retirement));
			createReception.setTVA(Tools.printDecimal(tva));
			createReception.setTHT(Tools.printDecimal(tht));
			createReception.setTranspAmount(Tools.printDecimal(transAmount));
			createReception.setMontantFinal(Tools.printDecimal(TTC));
		}catch (Throwable ex) {JOptionPane.showMessageDialog(null, ex.getMessage());}
		
			
		}
		else {
			JOptionPane.showMessageDialog(null, "Verifiez que : 1- la liste des articles n'est pas vide \n " +
					   "2- Vous avez choisi DA ou Devise \n"  +
					   "3- Vous avez saisi les frais de transport ?!!");
		}	
	}
	
}

	/************ADD supplier  ****************************/
class addingSupplierListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		Supplier supplier = new Supplier();
		SupplierList SL = new SupplierList();
		CRUDsupplierDialog CSD = new CRUDsupplierDialog(supplier, SL );
		CSD.Select("from Supplier");
		SL.setVisible(true);
		
	}
	
}
/*****************ADD product **************************/
class addProductListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((createReception.getPrecompteDouane().isEmpty()) || (createReception.getPrecompteDouane().equals("0,00"))) {
			JOptionPane.showMessageDialog(null, "Veuillez SVP remplir le champ 'precompte douane' d'abord");
		}
		else {
		Product product = new Product();
		ProductList PL = new ProductList();
		Currency currency = new Currency();
		CRUDproductDialog CPD = new CRUDproductDialog(product, PL, currency);
		CPD.getCurrencyValue();
		CPD.Select("from Product");
		PL.setModal(true);
		PL.setVisible(true);
		}
	}
}
/***********CANCEL ***************************/
class cancelReceptionListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		Object[] options = { "Oui", "Non" };
		int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment Annuler ?", "Confirmation",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
		if (answer==0) {clearEverything();
						clearNumberOfProduct();
						createReception.txtPrecompteDouane.setText("0,00");
						createReception.setTransportation("0,00");
						createReception.setTranspAmount("");
							addOrUpdate = "";
							Reception reception = new Reception();
							ReceptionList receptionList = ReceptionList.getInstance();
						//	CreateReception createReception = CreateReception.getInstance();
							CRUDreceptionList crudReceptionList = new CRUDreceptionList(reception, receptionList);
							if (!receptionList.isVisible()) {
																MainMenu.desktopPane.add(receptionList);
																receptionList.show();}
						
						createReception.dispose();
		}
	}
}
/************SAVE ***************************/
class saveReceptionListener extends CustomActionListeners{
      private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			for (Component comp : createReception.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
						if (((JTextField) comp).getText().isEmpty()){ empty = true;}
				}
			}
			if (empty) {JOptionPane.showMessageDialog(null, "Veuillez SVP remplir tout les champs !!"); empty=false;}
			else if (createReception.getProductGridModel().getRowCount()>0) {
						if (createReception.getTitle().equalsIgnoreCase("Modification d'un bon de reception")) {	
															Update(selectedReception);
															addOrUpdate = "";
															
						}
						else {Insert();}
										clearEverything();
										clearNumberOfProduct();
										createReception.txtPrecompteDouane.setText("0,00");
										createReception.setTransportation("0,00");
										createReception.setTranspAmount("");
										Reception reception = new Reception();
										ReceptionList receptionList = ReceptionList.getInstance();
									//	CreateReception createReception = CreateReception.getInstance();
										CRUDreceptionList crudReceptionList = new CRUDreceptionList(reception, receptionList);
										if (!receptionList.isVisible()) {MainMenu.desktopPane.add(receptionList);
																			receptionList.show();
																			createReception.dispose();
										}
						}
			else { JOptionPane.showMessageDialog(null, "La liste des articles est vide !!");}
		} catch (Throwable e1) {JOptionPane.showMessageDialog(null,e1.getMessage());}
		
	}
	
}
/****************UPDATE **********************/
class updateReceptionListener extends CustomActionListeners{
	private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createReception.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le modifier !! "); empty = false;}
		else {
		for (Component comp : createReception.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setEditable(false);
				comp.setBackground(Color.WHITE);
			}
		} 
		btnSwitcher(true,false);
		addOrUpdate = "UPDATE";
	}
	}
}
/*************DELETE ***************************/
class deletingProductListener extends CustomActionListeners{
	@Override
	public void actionPerformed(ActionEvent e) {
		if (createReception.getProductGrid().getRowCount() <= 0) {JOptionPane.showMessageDialog(null, "La liste des articles est vide !! ");}
		else if (selected >=0) {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {
							createReception.getProductGridModel().removeRow(selected);
							selected = -1;
							createReception.setSumProduct(createReception.getProductGrid().getRowCount());
							try {gridCalcul();} 
							catch (NumberFormatException e1) {JOptionPane.showMessageDialog(null, e1.getMessage());}
							catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());}
							}
		}	else  {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le supprimer !! ");}
	}
}
/*************SEARCH ***************************/
class gridNavigListener extends CustomActionListeners{
	private String id ;
	private int SR;

	@Override
	public void mouseClicked(MouseEvent e) {
		selected = createReception.getProductGrid().getSelectedRow();
	}	
}

class addExchangeValue extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		String res= JOptionPane.showInputDialog(null, "Ajouter un taux de change ", "Nouveaux taux de change", JOptionPane.INFORMATION_MESSAGE);
		if (res != null) {
		try { double d =  Double.parseDouble(res);
			  Session session = hibernateUtil.getSessionFactory().openSession();
				try {
					session.beginTransaction();
					currency.setMoney(d);
					session.save(currency);
					session.getTransaction().commit();
				}catch (HibernateException ex) {
					session.getTransaction().rollback();
					JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un taux de change : "+ex);
				}
				finally {
					session.close();
				}
			  
		}catch(NumberFormatException ex) {
			  JOptionPane.showMessageDialog(null, "Enter a valid number ");
			  }
		}
		getCurrencyValue();
		Select("from Reception");
	}
	
}

 /***********************************CRUD **********************************************************************************************/
/*******Insert 
 * @throws Throwable */
public void Insert() throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Supplier> supl = session.createQuery("from Supplier where id = '"+Integer.parseInt(createReception.getSupId())+"'").list();
		Reception reception = new Reception();
		reception.setRef(createReception.getReference());
		reception.setSupplier(supl.get(0));
		reception.setTodayDate(new Date()); 
		reception.setRecDate((createReception.getRecDate()));
		reception.setTotalDevise(Tools.decimalToDouble(createReception.getTotalDevise()));
		reception.setTotalDinar(Tools.decimalToDouble(createReception.getTotalDa()));
		reception.setPrecompteDouane(Tools.decimalToDouble(createReception.getPrecompteDouane()));
		reception.setChange(Tools.decimalToDouble(createReception.getChange()));
		reception.setTva(Tools.decimalToDouble(createReception.getTVA()));
		reception.setTotal(Tools.decimalToDouble(createReception.getMontantFinal()));
		reception.setTht(Tools.decimalToDouble(createReception.getTHT()));
		reception.setRetirement(Tools.decimalToDouble(createReception.getRetirement()));
		reception.setTransportation(Tools.decimalToDouble(createReception.getTranspAmount()));
		for (int i = 0; i<createReception.getProductGrid().getRowCount(); i++) {
			List<Product> prdlst = session.createQuery("from Product where ref = '"+(createReception.getProductGridModel().getValueAt(i, 1).toString())+"'").list();
			ReceptionProduct receptionProduct = new ReceptionProduct();
			receptionProduct.setQuantity(Tools.decimalToInteger(createReception.getProductGridModel().getValueAt(i, 3).toString()));
			receptionProduct.setUnitPriceDevise(Tools.decimalToDouble(createReception.getProductGridModel().getValueAt(i, 4).toString()));
			receptionProduct.setUnitPriceDinar(Tools.decimalToDouble(createReception.getProductGridModel().getValueAt(i, 5).toString()));
			receptionProduct.setReception(reception);
			receptionProduct.setProduct(prdlst.get(0));
			updateProductQuantity(session,createReception.getProductGridModel().getValueAt(i, 1).toString(),Tools.decimalToInteger(createReception.getProductGridModel().getValueAt(i, 3).toString()));
			reception.getReceptionProduct().add(receptionProduct);
			prdlst.removeAll(prdlst);
		}
		session.save(reception);
		session.getTransaction().commit();
		JOptionPane.showMessageDialog(null, "Enregistrement acompli avec succés ");
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un article : "+ex.getMessage());
	}
	finally {
		session.close();
	}
	
}
/*******Select*/
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
//	createReception.getReceptionGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Reception> ar =  session.createQuery(query).list();
		//fillGrid(ar);
		session.getTransaction().commit();
	//	createReception.setSumReception(createReception.getReceptionGridModel().getRowCount());
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
}



/********update 
 * @throws Throwable */
public void Update(int selectedReception) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Reception reception = session.get(Reception.class, selectedReception);
		for (ReceptionProduct Rp : reception.getReceptionProduct()) {
			session.delete(Rp);
		}
		reception.getReceptionProduct().clear();
		Supplier sp = reception.getSupplier();
		reception.setRef(createReception.getReference());
		reception.setSupplier(sp);
		reception.setTodayDate(Tools.stringToDate(createReception.getTodayDate())); 
		reception.setRecDate(createReception.getRecDate());
		reception.setTotalDevise(Tools.decimalToDouble(createReception.getTotalDevise()));
		reception.setTotalDinar(Tools.decimalToDouble(createReception.getTotalDa()));
		reception.setPrecompteDouane(Tools.decimalToDouble(createReception.getPrecompteDouane()));
		reception.setChange(Tools.decimalToDouble(createReception.getChange()));
		reception.setTva(Tools.decimalToDouble(createReception.getTVA()));
		reception.setTotal(Tools.decimalToDouble(createReception.getMontantFinal()));
		reception.setTht(Tools.decimalToDouble(createReception.getTHT()));
		reception.setRetirement(Tools.decimalToDouble(createReception.getRetirement()));
		reception.setTransportation(Tools.decimalToDouble(createReception.getTranspAmount()));
		for (int i = 0; i<createReception.getProductGrid().getRowCount(); i++) {
			List<Product> prdlst = session.createQuery("from Product where ref = '"+(createReception.getProductGridModel().getValueAt(i, 1).toString())+"'").list();
			ReceptionProduct receptionProduct = new ReceptionProduct();
			receptionProduct.setQuantity(Tools.decimalToInteger(createReception.getProductGridModel().getValueAt(i, 3).toString()));
			receptionProduct.setUnitPriceDevise(Tools.decimalToDouble(createReception.getProductGridModel().getValueAt(i, 4).toString()));
			receptionProduct.setUnitPriceDinar(Tools.decimalToDouble(createReception.getProductGridModel().getValueAt(i, 5).toString()));
			receptionProduct.setReception(reception);
			receptionProduct.setProduct(prdlst.get(0));
			updateProductQuantity(session,createReception.getProductGridModel().getValueAt(i, 1).toString(),Tools.decimalToInteger(createReception.getProductGridModel().getValueAt(i, 3).toString()));
			reception.getReceptionProduct().add(receptionProduct);
			prdlst.remove(prdlst.get(0));
			
		}
		/***************************   update Product Qte ********************/
		updateProductQuantityDeleted(session, deletedProduct);
		deletedProduct.clear();
		/*****************************   end  ********************************/
		session.update(reception);
		session.getTransaction().commit();
		JOptionPane.showMessageDialog(null, "Modification acompli avec succés ");
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de modification d'un article : "+ex.getMessage());
	}
	finally {
		session.close();
	}
			
}

public void btnSwitcher (boolean cs, boolean adu) {
	createReception.getCancelbtn().setEnabled(cs);
	createReception.getSavebtn().setEnabled(cs);
	createReception.getAddbtn().setEnabled(adu);
	createReception.getDeletebtn().setEnabled(adu);
}

public void getCurrencyValue() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List <Currency> cu =  session.createQuery("from Currency").list();
		if (cu.size()<=0) {JOptionPane.showMessageDialog(null, "Le taux de change n'est pas initializer !!");}
		else {
		change = cu.get(cu.size()-1).getMoney();
		String money =Tools.printDecimal(change);
		createReception.setChange(money);}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
	
}

public void clearEverything() {
	for (Component comp : createReception.getContainer().getComponents()) {
		if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);}
	}
	createReception.getProductGridModel().setRowCount(0);
	CreateReception.txtPrecompteDouane.setText("0,00");
}

public void clearNumberOfProduct() {
	createReception.lblSumProduct.setText(String.valueOf(CreateReception.productGrid.getRowCount()));
}

public void updateProductQuantity(Session session, String ref , int qte) {
	int existingQte =0;
	
		List<Product> pr = session.createQuery("from Product where ref = '"+ ref+"'").list();
		existingQte = pr.get(0).getQuantity();
		pr.get(0).setQuantity(qte+existingQte); 
		session.update(pr.get(0));
	
}

public void updateProductQuantityDeleted(Session session, HashMap<String,String> hashMap) {
	int existingQte =0;
	Iterator<Map.Entry<String,String>> itr = deletedProduct.entrySet().iterator();
	while(itr.hasNext()) {
        Map.Entry<String,String> entry = itr.next();
        List<Product> ProdList = session.createQuery("from Product where ref = '"+entry.getKey()+"'").list();
        existingQte = ProdList.get(0).getQuantity();
		ProdList.get(0).setQuantity(existingQte - Integer.parseInt(entry.getValue()));
		session.update(ProdList.get(0));
	}
}
	


public void gridCalcul() throws NumberFormatException, Throwable {
	double montantDevise = 0;
	double montantDa = 0;
	double TVA = 0;
	double total = 0;
	if (CreateReception.productGrid.getRowCount()>0) {
	for (int i = 0 ; i < CreateReception.productGrid.getRowCount() ; i++) {
		montantDevise  = montantDevise + Tools.decimalToDouble(CreateReception.productGridModel.getValueAt(i, 6).toString());
	}
	CreateReception.txtMontant.setText(Tools.printDecimal(montantDevise));
	montantDa = Tools.CurrencyConverter(montantDevise, change);
	CreateReception.txtMontantDa.setText(Tools.printDecimal(montantDa));
	//TVA = Tools.calculTva(montantDa);
	//CreateReception.txtTVA.setText(Tools.printDecimal(TVA));
	//total = montantDa + TVA + Tools.decimalToDouble(CreateReception.txtPrecompteDouane.getText());
	//CreateReception.txtMontantFinal.setText(Tools.printDecimal(total));
	}
	else { CreateReception.txtMontant.setText(null);
		   CreateReception.txtMontantDa.setText(null);
		   CreateReception.txtTVA.setText(null);
		   CreateReception.txtMontantFinal.setText(null);
	}
}


public void fillDeletedProducts() {
	for (int prdGridRow =0; prdGridRow<createReception.getProductGrid().getRowCount(); prdGridRow++ ) {
		if (!(deletedProduct.containsKey(createReception.getProductGridModel().getValueAt(prdGridRow, 1).toString()))) {
	   deletedProduct.put(createReception.getProductGridModel().getValueAt(prdGridRow, 1).toString(), createReception.getProductGridModel().getValueAt(prdGridRow, 3).toString());  
		}
	}	
}


}











