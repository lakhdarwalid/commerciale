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
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.Product;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateInvoice;
import com.commerciale.view.CreateReception;
import com.commerciale.view.ProductList;

public class CRUDproductDialogCustomer {
	private Product product;
	private ProductList productList;
	private Currency currency;
	private String selected;
	private String path = "";
	private double change;
	public CRUDproductDialogCustomer(Product product, ProductList productList, Currency currency) {
		this.product = product;
		this.productList = productList;
		this.currency = currency;
		this.productList.searchListener(new searchingProductListener());
		this.productList.searchBtnListener(new searchingbtnProductListener());
		this.productList.productGridListener(new gridNavigListener());
		this.productList.addchange(new addExchangeValue());
	}	
	
	

/*************SEARCH ***************************/
class searchingProductListener extends CustomActionListeners{
    @Override
	public void keyReleased(KeyEvent e) {
		Select("from Product where ref LIKE '%"+productList.getTxtSearch()+"%'");
		
	}
}

class searchingbtnProductListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		productList.setTxtSearch(null);
		
		Select("from Product");
	}
	
}
class gridNavigListener extends CustomActionListeners{
	private String id ;
	private int SR;
	private int qte;

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean err;
		boolean prdExist = false ;
		SR= productList.getProductGrid().getSelectedRow();
		id = productList.getProductGridModel().getValueAt(SR, 1).toString();
		selected = id;
		for (int i=0; i<CreateInvoice.productGrid.getRowCount(); i++) {
			if (CreateInvoice.productGridModel.getValueAt(i, 1).toString().equals(id)) {prdExist = true;}
		}
		if(!prdExist) {
		if (productList.getProductGrid().isEnabled()) {
			do {
				String qt = JOptionPane.showInputDialog("Quantité"); 
			try {
			qte = Integer.parseInt(qt);
			err = false;
			}catch(Exception ex) {JOptionPane.showMessageDialog(null, qt + " n'est pas un nombre !!");
								 err = true;					}
			}while(err);
			fillInvoiceProductGrid(id, qte);
			CreateInvoice.lblSumProduct.setText(String.valueOf(CreateInvoice.productGrid.getRowCount()));
			try {
				gridCalcul();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Probleme de calculation !!!"+ e1.getMessage());
				e1.printStackTrace();
			} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			productList.dispose();}	
	}else {JOptionPane.showMessageDialog(null, "Cet Article est deja selectionné !!");}
		}
}

class addExchangeValue implements ActionListener{

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
		Select("from Product");
	}
	
}

 /***********************************CRUD **********************************************************************************************/
/*******Select*/
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	productList.getProductGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Product> ar =  session.createQuery(query).list();
		fillGrid(ar);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
}

/************************************End of CRUD **************************************************************************************/

public void fillInvoiceProductGrid(String row, int Qte) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Object[] row1 = new Object[8];
		List<Product> prd =  session.createQuery("from Product where ref = '"+row+"'").list();
		for (Product tempProduct : prd) {
			if(tempProduct.getImg()==null) { row1[0] = null;}
			else {
			try {
				row1[0] = Tools.ImageModifier(tempProduct.getImg());
			} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
											+ e.getMessage());}}
		row1[1] = tempProduct.getRef();
		row1[2] = tempProduct.getDesg(); 
		row1[3] = Qte; 
		row1[4] = Tools.printDecimal((tempProduct.getUnitPrice())); 
		row1[5] = Tools.printDecimal(Tools.CurrencyConverter(tempProduct.getUnitPrice(), change)); 
		row1[6] = Tools.printDecimal(Qte*tempProduct.getUnitPrice()); 
		row1[7] = Tools.printDecimal(Qte*(Tools.CurrencyConverter(tempProduct.getUnitPrice(), change)));
        CreateInvoice.productGridModel.addRow(row1);
		session.getTransaction().commit();
		}
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
	
}
public void fillGrid (List<Product> li) {
	Object[] row = new Object[6];
	for (Product tempProduct : li) {
		if(tempProduct.getImg()==null) { row[0] = null;}
		else {
		try {
			row[0] = Tools.ImageModifier(tempProduct.getImg());
		} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
										+ e.getMessage());}}
		row[1] = tempProduct.getRef();
		row[2] = tempProduct.getDesg();
		row[3] = tempProduct.getQuantity();
		row[4] = Tools.printDecimal(tempProduct.getUnitPrice());
		row[5] = Tools.printDecimal(Tools.CurrencyConverter(tempProduct.getUnitPrice(), change));
		productList.getProductGridModel().addRow(row);
		}
}


public void getCurrencyValue() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List <Currency> cu =  session.createQuery("from Currency").list();
		change = cu.get(cu.size()-1).getMoney();
		String money =Tools.printDecimal(change);
		productList.setChange(money);
		CreateInvoice.boxChange.setText(money);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
	
}
 
 
public void gridCalcul() throws NumberFormatException, Throwable {
	double montantDevise = 0;
	double montantDa = 0;
	double TVA = 0;
	double total = 0;
	double totalTTC = 0;
	double retirement = 0;
	for (int i = 0 ; i < CreateInvoice.productGrid.getRowCount() ; i++) {
		montantDevise  = montantDevise + Tools.decimalToDouble(CreateInvoice.productGridModel.getValueAt(i, 6).toString());
	}
	CreateInvoice.txtMontant.setText(Tools.printDecimal(montantDevise));
//	montantDa = Tools.CurrencyConverter(montantDevise, change);
//	CreateInvoice.txtMontantDa.setText(Tools.printDecimal(montantDa));
	/*TVA = Tools.calculTva(montantDa);
	CreateInvoice.txtTVA.setText(Tools.printDecimal(TVA));
	retirement = (montantDa+TVA )*0.04;
	CreateInvoice.txtRetirement.setText(Tools.printDecimal(retirement));*/
	
}
}
