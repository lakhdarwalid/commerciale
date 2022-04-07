package com.commerciale.controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.commerciale.model.Client;
import com.commerciale.model.Deposit;
import com.commerciale.model.Invoice;
import com.commerciale.model.InvoiceProforma;
import com.commerciale.model.hibernateUtil;
import com.commerciale.report.PrintDepoReceipt;
import com.commerciale.report.PrintInvoiceProforma;
import com.commerciale.report.ReportFrame;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.ThumbnailFileChooser;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateDeposit;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class CRUDdeposit {
	private Deposit deposit;
	private CreateDeposit createDeposit;
	private Invoice invoice;
	private Client client;
	private int selected = -1;
	private int selectedInvoice = -1;
	private int selectedDeposit = -1;
	private String customerName;
	private int customer_id = 0;
	private int invoice_id = 0;
	private int deposit_id = 0;
	private String CheqImage =null;
	private String addOrUpdate = "";
	
	public CRUDdeposit(Deposit deposit, CreateDeposit createDeposit, Invoice invoice, Client client) {
		this.deposit = deposit;
		this.createDeposit = createDeposit;
		this.invoice = invoice;
		this.client = client;
		this.createDeposit.allNeddedWhenOpen(new BehaviorOnOpening());
		this.createDeposit.customerGridListener(new CustomerGridNavigation());
		this.createDeposit.addingDepoListener(new DepositListener());
		this.createDeposit.hidePaymentPanel(new HidingPaymentPanel());
		this.createDeposit.savingDepo(new SavingDeposit());
		this.createDeposit.invoiceGridListener(new InvoiceGridNavigation());
		this.createDeposit.searchListener(new SearchingByTyping());
		this.createDeposit.searchBtnListener(new SearchingByButton());
		this.createDeposit.deleteBtnListener(new DeletingListener());
		this.createDeposit.depositGridListener(new DepositGridNavigation());
		this.createDeposit.addingCheq(new AddCheqImage());
		this.createDeposit.updatingDeposit(new UpdateDeposit());
		this.createDeposit.printingbtnListener(new printingDepo());
	}
	
class BehaviorOnOpening extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		selectCustomer("from Client");
	}
	
}

class printingDepo implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		JasperPrint jasperPrint;
		  if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un client et une factures SVP !!");}
		  else {
			PrintDepoReceipt printInvoice = new PrintDepoReceipt();
			try {
				jasperPrint = printInvoice.print("from Deposit where id= '"+deposit_id+"'","C:\\CommercialRessources\\depoReceiptReport.jrxml"); 
				ReportFrame reportFrame = new ReportFrame();
				reportFrame.getContentPane().add(new JRViewer(jasperPrint));
			    reportFrame.show();
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());
			}
		  }
		
	}
	
}

/*********************Updating deposit **************/
class UpdateDeposit implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((selected<0) || (selectedInvoice<0) || (selectedDeposit<0)) {JOptionPane.showMessageDialog(null, "Selectionnez un client ,une facture et un versement svp !!");}
		else {
		createDeposit.getNewDepoContainer().setVisible(true);
		createDeposit.getDepoAmount().setEditable(true);
		createDeposit.getDepoAmount().setText(createDeposit.getDepositGridModel().getValueAt(selectedDeposit, 1).toString());
		try {
			createDeposit.getDepoDate().setDate(Tools.stringToDate(createDeposit.getDepositGridModel().getValueAt(selectedDeposit, 0).toString()));
		} catch (ParseException e1) {e1.printStackTrace();}
		createDeposit.getCustomerGrid().setEnabled(false);
		createDeposit.getInvoiceGrid().setEnabled(false);
		addOrUpdate = "update";
		}
		
	}
	
}

/******************  add cheq image *****************/
class AddCheqImage implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e1) {}
		JFileChooser TFC = new ThumbnailFileChooser();
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Images", "JPEG", "JPG", "PNG");
		TFC.setFileFilter(extensionFilter); 
		TFC.showDialog(null, "Selectionnez votre Cheque");//<-----------
		try {
			CheqImage = TFC.getSelectedFile().getPath().toString();//<----------
			createDeposit.getCheqImageLabel().setText(null);
			Image imageLogo = ImageIO.read(new File(CheqImage)).getScaledInstance(600, 200, Image.SCALE_SMOOTH);
			createDeposit.getCheqImageLabel().setIcon(new ImageIcon(imageLogo));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Vous avez pas choisix un Cheque");
		}
		
		try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}catch (Throwable e2) {}
	}
}
		

class DeletingListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(selectedDeposit<0) {
			JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des versements pour "
					+ "selectionner l'element que vous voulez le supprimer !! ");}
		else {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {Object[] options1 = { "Oui", "Non" };
							int answer1 = JOptionPane.showOptionDialog(null, "Voulez vous appliquer cet operation sur le compte de client ?", "Confirmation",
									JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options1, options1[0]);
				
							if (answer1==0) {deleteDepositAffectCustomer(customer_id, deposit_id);
											 selectedDeposit =-1;
											 selectCustomer("from Client");
											 selectInvoice(customer_id);
											 selectDepositThroughCustomer(customer_id);}
							else {deleteDeposit(deposit_id); selectedDeposit =-1;
								  selectDepositThroughCustomer(customer_id);
								  }
			} 
		
		}	
	}
} 

class SearchingByTyping extends CustomActionListeners{
	@Override
	public void keyReleased(KeyEvent e) {
		selectCustomer("from Client where name LIKE '%"+createDeposit.getTxtSearch()+"%'");
		
	}	
}

class SearchingByButton implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		selectCustomer("from Client");
		
	}
	
}

class CustomerGridNavigation extends CustomActionListeners{
		
		
	@Override
	public void mouseClicked(MouseEvent e) {
		selectedInvoice = -1;
		selected = createDeposit.getCustomerGrid().getSelectedRow();
		customer_id  = Integer.parseInt(createDeposit.getcustomerGridModel().getValueAt(selected, 4).toString());
		selectInvoice(customer_id);
		selectDepositThroughCustomer(customer_id);
		createDeposit.setCustomerNameForDepo(createDeposit.getcustomerGridModel().getValueAt(selected, 0).toString());
		createDeposit.getCheqImageLabel().setIcon(null);
	}
	
}

class InvoiceGridNavigation extends CustomActionListeners{
String ref, title;
@Override
public void mouseClicked(MouseEvent e) {
	selectedInvoice = createDeposit.getInvoiceGrid().getSelectedRow();
	ref  = createDeposit.getcustomerGridModel().getValueAt(selected, 0).toString();
	invoice_id = Integer.parseInt(createDeposit.getInvoiceGridModel().getValueAt(selectedInvoice, 3).toString());
    title = createDeposit.getCustomerNameForDepo();
	createDeposit.setCustomerNameForDepo(title + " pour la facture : "+createDeposit.getInvoiceGridModel().getValueAt(selectedInvoice, 0).toString());
	selectDepositThroughInvoice(invoice_id);
	createDeposit.getCheqImageLabel().setIcon(null);
}

}

class DepositGridNavigation extends CustomActionListeners{
@Override
public void mouseClicked(MouseEvent e) {
	selectedDeposit = createDeposit.getDepositGrid().getSelectedRow();
	deposit_id = Integer.parseInt(createDeposit.getDepositGridModel().getValueAt(selectedDeposit, 5).toString());
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
        Deposit deposit = session.get(Deposit.class, deposit_id);
		
		if (!(deposit.getCheq()==null)) {
			File f = new File(deposit.getCheq());
		if(f.exists() && !f.isDirectory()) { 
		Image imageLogo = ImageIO.read(new File(createDeposit.getDepositGridModel().getValueAt(selectedDeposit, 4).toString())).getScaledInstance(600, 200, Image.SCALE_SMOOTH);
		createDeposit.getCheqImageLabel().setIcon(new ImageIcon(imageLogo));}}
		else createDeposit.getCheqImageLabel().setIcon(null);
		session.getTransaction().commit();
	} catch (HibernateException e1) {
		    JOptionPane.showMessageDialog(null, e1.getMessage());
		     session.getTransaction().rollback();
	} catch (IOException e1) {
		JOptionPane.showMessageDialog(null, e1.getMessage());
		e1.printStackTrace();
	}finally {
		session.close();
	}
	
}
	
}

class DepositListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((selected<0) || (selectedInvoice<0)) {JOptionPane.showMessageDialog(null, "Selectionnez un client et une facture svp !!");}
		else {
		createDeposit.getNewDepoContainer().setVisible(true);
		createDeposit.getDepoAmount().setEditable(true);
		createDeposit.getDepoAmount().setText("0.00");
		createDeposit.getDepoDate().setDate(new Date());
		createDeposit.getCustomerGrid().setEnabled(false);
		createDeposit.getInvoiceGrid().setEnabled(false);
		createDeposit.setCheqNum(null);
		addOrUpdate = "add";
		}
	}
}

class SavingDeposit implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
		if (Tools.decimalToDouble(createDeposit.getDepoAmount().getText()) == 0) {JOptionPane.showMessageDialog(null, "Le montant est Zero !!!");}
		else {
			if (addOrUpdate.equalsIgnoreCase("add")) {
			insertDeposit(customer_id, invoice_id);}
			else if (addOrUpdate.equalsIgnoreCase("update")) {updateDeposit(deposit_id);}
			
		}
	} catch (Throwable e1) {
		JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un versement \n"+e1.getMessage());
	}
	  selectCustomer("from Client");
	  selectDepositThroughCustomer(customer_id);
	  createDeposit.getDepoAmount().setText("0.00");
	  createDeposit.getDepoDate().setDate(new Date());
	  createDeposit.getNewDepoContainer().setVisible(false);
	  selectDepositThroughInvoice(invoice_id);	
	  createDeposit.getCustomerGrid().setEnabled(true);
	  createDeposit.getInvoiceGrid().setEnabled(true);
	  createDeposit.setCheqNum(null);
	  createDeposit.getCheqImageLabel().setIcon(null);
	  addOrUpdate = "";
	}
	
}

class HidingPaymentPanel implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		createDeposit.getNewDepoContainer().setVisible(false);
		createDeposit.getDepoAmount().setEditable(false);
		createDeposit.getDepoDate().setDate(new Date());	
		createDeposit.getCustomerGrid().setEnabled(true);
		createDeposit.getInvoiceGrid().setEnabled(true);
		createDeposit.getCheqImageLabel().setIcon(null);
		addOrUpdate="";
	}
	
}

public void selectCustomer(String query) {
	double debt = 0, depo = 0, finalDebt = 0;
	Session session = hibernateUtil.getSessionFactory().openSession();
	createDeposit.getcustomerGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery(query).list();
		Object[] row = new Object[5];
		for (Client tempClient : cl) {
			debt = tempClient.getDebt();
			depo = tempClient.getDepo();
			finalDebt = debt- depo;
			
			row[0] = tempClient.getName();
			if (debt==0) {row[1] = "0,00";}
			else  {row[1] = Tools.printDecimal(debt);}
			if (depo==0) {row[2] = "0,00";}
			else  {row[2] = Tools.printDecimal(depo);}
			if (finalDebt==0) {row[3] = "0,00";}
			else  {row[3] = Tools.printDecimal(finalDebt);}
			row[4] = tempClient.getId();
			createDeposit.getcustomerGridModel().addRow(row);
		}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

public void selectInvoice(int id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	createDeposit.getInvoiceGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		Client cl =  session.get(Client.class, id);
		Object[] row = new Object[4];
		for (InvoiceProforma tempInvoice : cl.getInvoicesPro()) {

			row[0] = tempInvoice.getRef();
			row[1] = Tools.dateToString(tempInvoice.getTodayDate());
			row[2] = Tools.printDecimal(tempInvoice.getTotal());
			row[3] = tempInvoice.getId();
			createDeposit.getInvoiceGridModel().addRow(row);
			
		}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
} 

public void selectDepositThroughInvoice(int inv_id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	createDeposit.getDepositGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		InvoiceProforma inv = session.get(InvoiceProforma.class, inv_id);
		Object[] row = new Object[6];
		for (Deposit tempDepo : inv.getDeposits()) {
			row[0] = Tools.dateToString(tempDepo.getDepositDate());
			row[1] = Tools.printDecimal(tempDepo.getAmount());
			row[2] = Tools.printDecimal(tempDepo.getDebtInv());
			row[3] = tempDepo.getCheqNum();
			row[4] = tempDepo.getCheq();
			row[5] = tempDepo.getId();
			createDeposit.getDepositGridModel().addRow(row);
		}
		createDeposit.setNbreDepo(String.valueOf(createDeposit.getDepositGrid().getRowCount()));
		getTotalInDepoGrid();
		session.getTransaction().commit();	
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de collection de la liste des versements \n"+ ex.getMessage());
	}
	finally {
		session.close();
	}
	
}

public void selectDepositThroughCustomer(int cust_id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	createDeposit.getDepositGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		Client cl = session.get(Client.class, cust_id);
		Object[] row = new Object[6];
		for (Deposit tempDepo : cl.getDeposits()) {
			row[0] = Tools.dateToString(tempDepo.getDepositDate());
			row[1] = Tools.printDecimal(tempDepo.getAmount());
			row[2] = Tools.printDecimal(tempDepo.getDebtCus());
			row[3] = tempDepo.getCheqNum();
			row[4] = tempDepo.getCheq();
			row[5] = tempDepo.getId();
			createDeposit.getDepositGridModel().addRow(row);
		}
		createDeposit.setNbreDepo(String.valueOf(createDeposit.getDepositGrid().getRowCount()));
		getTotalInDepoGrid();
		session.getTransaction().commit();	
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de collection de la liste des versements \n"+ ex.getMessage());
	}
	finally {
		session.close();
	}
	
}

public void insertDeposit(int cust_id, int inv_id) throws Throwable {
	double sumDepo = 0 , debtInv = 0 , debtCus = 0, depoCus = 0;
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Client cl = session.get(Client.class, cust_id);
		debtCus = cl.getDebt() - cl.getDepo();
		depoCus = cl.getDepo();
		InvoiceProforma inv = session.get(InvoiceProforma.class, inv_id);

		for (Deposit depo : inv.getDeposits()) {
			sumDepo = sumDepo + depo.getAmount();
		}
		debtInv = inv.getTotal() - sumDepo;
		Deposit deposit = new Deposit();
		deposit.setClient(cl);
		deposit.setInvoiceProforma(inv);
		deposit.setDepositDate(createDeposit.getDepoDate().getDate());
		deposit.setCheqNum(createDeposit.getCheqNum());
		deposit.setAmount(Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setDebtInv(debtInv - Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setDebtCus(debtCus - Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setCheq(CheqImage);
		cl.setDepo(depoCus + Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		session.update(cl);
		session.save(deposit);		
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
	session.getTransaction().rollback();
	JOptionPane.showMessageDialog(null, "Erreur d'insertion :"+ex.getMessage());
	}
	finally {
	session.close();
	}
}

public void deleteDeposit(int depo_id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Deposit deposit = session.get(Deposit.class, depo_id);
		session.delete(deposit);
		session.getTransaction().commit();
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de supression d'un versement \n"+ex.getMessage());
	}
	finally {
		session.close();
	}
	
}

public void deleteDepositAffectCustomer(int cust_id, int depo_id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Client client = session.get(Client.class, cust_id);
		Deposit deposit = session.get(Deposit.class, depo_id);
		double exDebit = client.getDebt();
		double cusDepo = client.getDepo();
		double depo = deposit.getAmount();
		double newDebit = exDebit+depo ; 
		client.setDepo(cusDepo - depo);
		session.update(client);
		session.delete(deposit);
		session.getTransaction().commit();
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de supression d'un versement \n"+ex.getMessage());
	}
	finally {
		session.close();
	}
	
}

public void updateDeposit(int dep_id) throws Throwable {
	double oldDepo = 0 , debtInv = 0 , depoInv = 0, debtCus = 0, depoCus = 0;
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Deposit deposit = session.get(Deposit.class, dep_id);
		oldDepo = deposit.getAmount();
		Client cl = deposit.getClient();
		depoCus = cl.getDepo()-oldDepo;
		InvoiceProforma inv = deposit.getInvoiceProforma();
		for (Deposit dep : inv.getDeposits()) {
			depoInv = depoInv + dep.getAmount();
		}
		debtInv = inv.getTotal() - depoInv;
		debtInv = debtInv + oldDepo;
		deposit.setClient(cl);
		deposit.setInvoiceProforma(inv);
		deposit.setDepositDate(createDeposit.getDepoDate().getDate());
		deposit.setAmount(Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setDebtInv(debtInv - Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setDebtCus(debtCus - Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		deposit.setCheq(CheqImage);
		cl.setDepo(depoCus + Tools.decimalToDouble(createDeposit.getDepoAmount().getText()));
		session.update(cl);
		session.update(deposit);		
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
	session.getTransaction().rollback();
	JOptionPane.showMessageDialog(null, "Erreur d'insertion :"+ex.getMessage());
	}
	finally {
	session.close();
	}
}

public void getTotalInDepoGrid() {
	double  totdepo = 0;
	for (int i=0; i< createDeposit.getDepositGridModel().getRowCount(); i++) {
		try {
			totdepo = totdepo + Tools.decimalToDouble(createDeposit.getDepositGridModel().getValueAt(i, 1).toString());
		} catch (ParseException e) {e.printStackTrace();
		} catch (Throwable e) {e.printStackTrace();}
		
	}
	createDeposit.setTotalDepo(Tools.printDecimal(totdepo));
}

}
