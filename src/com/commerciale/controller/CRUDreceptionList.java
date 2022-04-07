package com.commerciale.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.util.SystemOutLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.controller.CRUDreception.behaveWhenOpen;
import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.Product;
import com.commerciale.model.Reception;
import com.commerciale.model.ReceptionProduct;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.report.PrintProduct;
import com.commerciale.report.PrintReception;
import com.commerciale.report.ReportFrame;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateReception;
import com.commerciale.view.CreateSupplier;
import com.commerciale.view.MainMenu;
import com.commerciale.view.ReceptionList;
import com.sun.xml.bind.v2.runtime.property.Property;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

public class CRUDreceptionList {
	private Reception reception;
	private ReceptionList receptionList;
	private CreateReception createReception;
	private String addOrUpdate;
	private int selected = -1;
	private int SR;
	private String path = "";
	public CRUDreceptionList(Reception reception, ReceptionList receptionList) {
		this.reception = reception;
		this.receptionList = receptionList;
		this.receptionList.addingReceptionListener(new addReceptionListener());
		this.receptionList.updatingListener(new updateReceptionListener());
		this.receptionList.searchListener(new searchingReceptionListener());
		this.receptionList.searchBtnListener(new searchingbtnReceptionListener());
		this.receptionList.deleteBtnListener(new deletingReceptionListener());
		this.receptionList.receptionGridListener(new gridNavigListener());
		this.receptionList.allNeddedWhenOpen(new behaveWhenOpen());
		this.receptionList.printingbtnListener(new printingListener());
		this.receptionList.searhByDateListener(new SearchByDateListener());
	}
/*********************************************** CRUD btn Class *******************************************/	

	
	/**********PRINTING *************************/
class printingListener extends CustomActionListeners{

	@Override
	public void mouseClicked(MouseEvent e) {
      JasperPrint jasperPrint;
	  if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un element par cliquer sur la liste des bons SVP !!");}
	  else {
		PrintReception printReception = new PrintReception();
		try {
			jasperPrint = printReception.print("from Reception where reception_id= '"+selected+"'", "C:\\CommercialRessources\\ReceptionReport.jrxml");  // "C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\ReceptionReport.jrxml");
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
			Select("from Reception");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
 }
/**********************BEHAVIOR ENDS ***********************/
	
/************ADD ****************************/
class addReceptionListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		CreateReception createReception = CreateReception.getInstance();
		Currency currency = new Currency();
		Supplier supplier = new Supplier();
		CreateSupplier createSupplier = CreateSupplier.getInstance();
		CRUDsupplier crudSupplier = new CRUDsupplier(supplier, createSupplier,createReception);
		CRUDreception crudReception = new CRUDreception( reception, createReception, 
				 currency, crudSupplier);
		if (!createReception.isVisible()) {
											createReception.setTitle("Nouveau Bon de Reception");
											MainMenu.desktopPane.add(createReception);
											createReception.show();
		}	
		addOrUpdate = "ADD";  
		}
	
}
/***********CANCEL ***************************/
class cancelReceptionListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : receptionList.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
				((JTextField) comp).setEditable(false);
				comp.setBackground(Color.WHITE);
			}
			if (comp instanceof JComboBox) {comp.setEnabled(false);}
		} 
		btnSwitcher(false, true);
		addOrUpdate="";
	}
	
}
/****************UPDATE **********************/
class updateReceptionListener extends CustomActionListeners{
	private double totalDevise, totalDa;  
	@Override
	public void actionPerformed(ActionEvent e) {
		//createReception = null;
		if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des Bons pour selectionner l'element que vous voulez le modifier !! ");}
		else {
		    createReception = CreateReception.getInstance();
			Currency currency = new Currency();
			Supplier supplier = new Supplier();
			CreateSupplier createSupplier = CreateSupplier.getInstance();
			CRUDsupplier crudSupplier = new CRUDsupplier(supplier, createSupplier,createReception);
			CRUDreception crudReception = new CRUDreception( reception, createReception, 
					 currency, crudSupplier);
			if (!createReception.isVisible()) {
					createReception.setReference(receptionList.getReceptionGridModel().getValueAt(SR, 1).toString());
					createReception.setSupId(receptionList.getReceptionGridModel().getValueAt(SR, 12).toString());
					createReception.setSupName(receptionList.getReceptionGridModel().getValueAt(SR, 2).toString());
					createReception.setTodayDate(receptionList.getReceptionGridModel().getValueAt(SR, 3).toString());
					try {createReception.setRecDate(Tools.stringToDate(receptionList.getReceptionGridModel().getValueAt(SR, 4).toString()));
					} catch (ParseException e1) {JOptionPane.showMessageDialog(null, e1.getMessage());}
					createReception.setTotalDevise(receptionList.getReceptionGridModel().getValueAt(SR, 5).toString());
					createReception.setTotalDa(receptionList.getReceptionGridModel().getValueAt(SR, 6).toString());
					createReception.setChange(receptionList.getReceptionGridModel().getValueAt(SR, 7).toString());
					createReception.setPrecompteDouane(receptionList.getReceptionGridModel().getValueAt(SR, 8).toString());
					createReception.setTHT(receptionList.getReceptionGridModel().getValueAt(SR, 9).toString());
					createReception.setTVA(receptionList.getReceptionGridModel().getValueAt(SR, 10).toString());
					createReception.setMontantFinal(receptionList.getReceptionGridModel().getValueAt(SR, 11).toString());
					createReception.setRetirement(receptionList.getReceptionGridModel().getValueAt(SR, 13).toString());
					createReception.setTranspAmount(receptionList.getReceptionGridModel().getValueAt(SR, 14).toString());
					createReception.getProductGridModel().setRowCount(0);
					Object[] row = new Object[8];
					for (int i=0; i< receptionList.getproductGrid().getRowCount(); i++) {
						try {
							row[0] = new ImageIcon(SelectProductImg(receptionList.getProductGridModel().getValueAt(i, 1).toString()));
						} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						row[1] = receptionList.getProductGridModel().getValueAt(i, 1).toString();
						row[2] = receptionList.getProductGridModel().getValueAt(i, 2).toString();
						row[3] = receptionList.getProductGridModel().getValueAt(i, 3).toString();
						row[4] = receptionList.getProductGridModel().getValueAt(i, 4).toString();
						row[5] = receptionList.getProductGridModel().getValueAt(i, 5).toString();
						try {
							totalDevise = Tools.decimalToDouble(receptionList.getProductGridModel().getValueAt(i, 3).toString()) *
									       Tools.decimalToDouble(receptionList.getProductGridModel().getValueAt(i, 4).toString());
						} catch (Throwable e2) {JOptionPane.showMessageDialog(null, e2.getMessage());
						}
						row[6] =Tools.printDecimal(totalDevise); 
						try {totalDa = totalDevise * Tools.decimalToDouble(receptionList.getReceptionGridModel().getValueAt(SR, 7).toString());
						} catch (ParseException e1) {e1.printStackTrace();
						} catch (Throwable e1) {e1.printStackTrace();}
						row[7] = Tools.printDecimal(totalDa); 
						createReception.getProductGridModel().addRow(row);
					}
					createReception.setTitle("Modification d'un bon de reception");
					crudReception.setAddOrUpdate("UPDATE");
					CRUDreception.selectedReception =Integer.parseInt(receptionList.getReceptionGridModel().getValueAt(SR, 0).toString());
				    crudReception.fillDeletedProducts();
				MainMenu.desktopPane.add(createReception);
				createReception.show();
				
				receptionList.dispose();
			}}
		}
}
/*************DELETE ***************************/
class deletingReceptionListener extends CustomActionListeners{
	@Override
	public void actionPerformed(ActionEvent e) {
		if(selected<0) {
			JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des Bons pour "
					+ "selectionner l'element que vous voulez le supprimer !! ");}
		else {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {Delete(selected); selected = -1;}
			try {
				Select("from Reception");
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
			String startDate = Tools.dateToStringFlipped(receptionList.getStartDate());
			String endDate = Tools.dateToStringFlipped(receptionList.getEndDate());
			Select("from Reception where todayDate BETWEEN '"+startDate+"' AND '"+endDate+ "'");
		
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"Selectionez une periode SVP!! \n" +e1.getMessage());
		}
		
	}
	
}	
/***************SEARCHING ENDS ************************/
	
class searchingReceptionListener extends CustomActionListeners{
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			SelectSupplier("from Supplier where name LIKE '%"+receptionList.getTxtSearch()+"%'");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
}

class searchingbtnReceptionListener extends CustomActionListeners{

	@Override
	public void actionPerformed(ActionEvent e) {
		receptionList.setTxtSearch(null);
		try {
			Select("from Reception");
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
	
}
class gridNavigListener extends CustomActionListeners{
	private String id ;

	@Override
	public void mouseClicked(MouseEvent e) {
		SR= receptionList.getReceptionGrid().getSelectedRow();
		id = receptionList.getReceptionGridModel().getValueAt(SR, 0).toString();
		selected = Integer.parseInt(id);
		receptionList.setRetirement(receptionList.getReceptionGridModel().getValueAt(SR, 13).toString());
		receptionList.setTransport(receptionList.getReceptionGridModel().getValueAt(SR, 14).toString());
		try {
			SelectProduct("from Reception where reception_id="+id);
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
		List<Reception> ar =  session.createQuery(query).list();
		fillReceptionGrid(ar);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

public void SelectSupplier (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery(query).list();
		if (sup.size()>0) {
		fillReceptionGrid(sup.get(0).getReceptions());}
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
	receptionList.getProductGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Reception> ar =  session.createQuery(query).list();
		fillProductGrid(ar.get(0).getReceptionProduct());
		session.getTransaction().commit();
		receptionList.setSumProduct(receptionList.getProductGridModel().getRowCount());
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
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Reception ar = session.get(Reception.class, selected);
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

public void fillReceptionGrid (Set<Reception> li) throws Throwable {
	Object[] row = new Object[15];
	if (receptionList.getReceptionGridModel().getRowCount()>0) {receptionList.getReceptionGridModel().setRowCount(0);}
	for (Reception tempReception : li) {
		row[0] = tempReception.getId();
		row[1] = tempReception.getRef();
		row[2] = tempReception.getSupplier().getName();
		row[3] = Tools.dateToString(tempReception.getTodayDate());
		row[4] = Tools.dateToString(tempReception.getRecDate());
		row[5] = Tools.printDecimal(tempReception.getTotalDevise());
		row[6] = Tools.printDecimal(tempReception.getTotalDinar());
		row[7] = Tools.printDecimal(tempReception.getChange());
		row[8] = Tools.printDecimal(tempReception.getPrecompteDouane());
		row[9] = Tools.printDecimal(tempReception.getTht());
		row[10] = Tools.printDecimal(tempReception.getTva());
		row[11] = Tools.printDecimal(tempReception.getTotal());
		row[12] = tempReception.getSupplier().getId();
		row[13] = Tools.printDecimal(tempReception.getRetirement());
		row[14] = Tools.printDecimal(tempReception.getTransportation());
		receptionList.getReceptionGridModel().addRow(row);
	}

}

public void fillReceptionGrid (List<Reception> li) throws Throwable {
	Object[] row = new Object[15];
	if (receptionList.getReceptionGridModel().getRowCount()>0) {receptionList.getReceptionGridModel().setRowCount(0);}
	for (Reception tempReception : li) {
		row[0] = tempReception.getId();
		row[1] = tempReception.getRef();
		row[2] = tempReception.getSupplier().getName();
		row[3] = Tools.dateToString(tempReception.getTodayDate());
		row[4] = Tools.dateToString(tempReception.getRecDate());
		row[5] = Tools.printDecimal(tempReception.getTotalDevise());
		row[6] = Tools.printDecimal(tempReception.getTotalDinar());
		row[7] = Tools.printDecimal(tempReception.getChange());
		row[8] = Tools.printDecimal(tempReception.getPrecompteDouane());
		row[9] = Tools.printDecimal(tempReception.getTht());
		row[10] = Tools.printDecimal(tempReception.getTva());
		row[11] = Tools.printDecimal(tempReception.getTotal());
		row[12] = tempReception.getSupplier().getId();
		row[13] = Tools.printDecimal(tempReception.getRetirement());
		row[14] = Tools.printDecimal(tempReception.getTransportation());
		receptionList.getReceptionGridModel().addRow(row);
	}

}

public void fillProductGrid(List<ReceptionProduct> prd) throws Throwable {
	Object[] row1 = new Object[6];
		for (ReceptionProduct tempProduct : prd) {
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
			receptionList.getProductGridModel().addRow(row1);
		}
}

public void btnSwitcher (boolean cs, boolean adu) {
	receptionList.getUpdatebtn().setEnabled(adu);
	receptionList.getAddbtn().setEnabled(adu);
	receptionList.getDeletebtn().setEnabled(adu);
	receptionList.getReceptionGrid().setEnabled(adu);
	
}

public byte[] SelectProductImg (String ref)throws Throwable  {
	Session session = hibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Product> ar =  session.createQuery("from Product where ref = '"+ref+"'").list();
		byte[] img = ar.get(0).getImg();
		session.getTransaction().commit();
		session.close();
		return img;
	}


}
