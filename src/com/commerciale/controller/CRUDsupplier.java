package com.commerciale.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.controller.CRUDsupplier;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.view.CreateReception;
import com.commerciale.view.CreateSupplier;

public class CRUDsupplier {
	private Supplier supplier;
	private CreateSupplier createSupplier;
	private CreateReception createReception;
	private String addOrUpdate;
	private int selected;
	private int showSupplier = 0;
	
	public CRUDsupplier(Supplier supplier, CreateSupplier createSupplier, CreateReception createReception) {
		this.supplier = supplier;
		this.createSupplier = createSupplier;
		this.createReception = createReception;
		this.createSupplier.addingListener(new addSupplierListener());
		this.createSupplier.cancelingListener(new cancelSupplierListener());
		this.createSupplier.SavingListener(new saveSupplierListener());
		this.createSupplier.updatingListener(new updateSupplierListener());
		this.createSupplier.searchListener(new searchingSupplierListener());
		this.createSupplier.searchBtnListener(new searchingbtnSupplierListener());
		this.createSupplier.supplierGridListener(new SupplierGridListener());
		this.createSupplier.deleteBtnListener(new deletingSupplierListener());
		this.createSupplier.allNeddedWhenOpen(new BehaviorWhenOpened());
	}
class BehaviorWhenOpened extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		Select("from Supplier");
		
	}
	
}	
	
/*********************************************** CRUD btn Class *******************************************/	
/************ADD ****************************/
class addSupplierListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				comp.setEnabled(true);
				((JTextField) comp).setText(null);
			}
		} 
		btnSwitcher(true, false);	
		addOrUpdate = "ADD";
		}
	
}
/***********CANCEL ***************************/
class cancelSupplierListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
				comp.setEnabled(false);
			}
		} 
		btnSwitcher(false, true);
		addOrUpdate="";
	}
	
}
/************SAVE ***************************/
class saveSupplierListener implements ActionListener{
      private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez remplisser toutes les cellules svp !!!"); empty = false;}
		else {
		if (addOrUpdate=="ADD") {Insert();}
		if (addOrUpdate=="UPDATE") {Update(selected);}
		btnSwitcher(false, true);
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
				comp.setEnabled(false);
			}
			if (comp instanceof JComboBox) {comp.setEnabled(false);}
		} 
		addOrUpdate="";
		}

		Select("from Supplier");
		
	}
	
}
/****************UPDATE **********************/
class updateSupplierListener implements ActionListener{
	private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des fournisseurs pour selectionner l'element que vous voulez le modifier !! "); empty = false;}
		else {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				comp.setEnabled(true);
			}
		} 
		btnSwitcher(true,false);
		addOrUpdate = "UPDATE";
	}
	}
}
/*************DELETE ***************************/
class deletingSupplierListener implements ActionListener{
	private boolean empty = false;
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des fournisseurs pour selectionner l'element que vous voulez le supprimer !! "); empty = false;}
		else {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {Delete(selected);}
			Select("from Supplier");
			for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
		
			}
			}
		}	
	}
}
/*************SEARCH ***************************/
class searchingSupplierListener extends CustomActionListeners{
    @Override
	public void keyReleased(KeyEvent e) {
		Select("from Supplier where name LIKE '%"+createSupplier.getTxtSearch()+"%'");
		
	}
}

class searchingbtnSupplierListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		createSupplier.setTxtSearch(null);
		for (Component comp : createSupplier.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
			}
		}
		Select("from Supplier");
		
	}
	
}

class SupplierGridListener extends CustomActionListeners{
    private String id ;
    private int SR;
	@Override
	public void mouseClicked(MouseEvent e) {
	SR= createSupplier.getSupplierGrid().getSelectedRow();
	id = createSupplier.getSupplierGridModel().getValueAt(SR, 0).toString();
	selected = Integer.parseInt(id);
	createSupplier.setTxtSearch(id);
	if (createSupplier.getSupplierGrid().isEnabled()) {
	fillFields(id);}
	if (createSupplier.getShowSupplier()==1) {fillFieldsSupReception(id);
											  createSupplier.setShowSupplier(0);
												} 
	}
}

 /***********************************CRUD **********************************************************************************************/
/*******Insert */
public void Insert() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		supplier.setName(createSupplier.getName());
		supplier.setAdress(createSupplier.getAdress());
		supplier.setRc(createSupplier.getRc());
		supplier.setMf(createSupplier.getMf());
		supplier.setNis(createSupplier.getNis());
		supplier.setArtImpo(createSupplier.getArtimp());
		supplier.setTel(createSupplier.getTel());
		supplier.setEmail(createSupplier.getEmail());
		supplier.setBanque(createSupplier.getBanque());
		supplier.setCompte(createSupplier.getCompte());
		session.save(supplier);
		session.getTransaction().commit();
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un fournisseur : "+ex);
	}
	finally {
		session.close();
	}
	
}
/*******Select*/
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	createSupplier.getSupplierGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery(query).list();
		fillGrid(sup);
		session.getTransaction().commit();
		createSupplier.setSumSupplier(createSupplier.getSupplierGridModel().getRowCount());
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
}



/********update */
public void Update(int selected) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Supplier sup = session.get(Supplier.class, selected);
		createSupplier.setTxtSearch(sup.getName());
		sup.setName(createSupplier.getName());
		sup.setAdress(createSupplier.getAdress());
		sup.setRc(createSupplier.getRc());
		sup.setMf(createSupplier.getMf());
		sup.setNis(createSupplier.getNis());
		sup.setArtImpo(createSupplier.getArtimp());
		sup.setTel(createSupplier.getTel());
		sup.setEmail(createSupplier.getEmail());
		sup.setBanque(createSupplier.getBanque());
		sup.setCompte(createSupplier.getCompte());
		session.update(sup);
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
/*********Delete */
public void Delete(int selected) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		Supplier sup = session.get(Supplier.class, selected);
		session.remove(sup);
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

/************************************End of CRUD **************************************************************************************/

public void fillFields(String row) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery("from Supplier where id = "+row).list();
		for (Supplier tempSupplier : sup) {
		createSupplier.setName(tempSupplier.getName());
		createSupplier.setAddress(tempSupplier.getAdress());
	    createSupplier.setRc(tempSupplier.getRc());
		createSupplier.setMf(tempSupplier.getMf());
		createSupplier.setNis(tempSupplier.getNis());
		createSupplier.setArtimpo(tempSupplier.getArtImpo());
		createSupplier.setTel(tempSupplier.getTel());
		createSupplier.setEmail(tempSupplier.getEmail());
		createSupplier.setBanque(tempSupplier.getBanque());
		createSupplier.setCompte(tempSupplier.getCompte());}
		session.getTransaction().commit();
		createSupplier.setSumSupplier(createSupplier.getSupplierGridModel().getRowCount());
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
	
}

public void fillFieldsSupReception(String row) {   // fill fields supplier in reception view 
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery("from Supplier where id = "+row).list();
		for (Supplier tempSupplier : sup) {
		createReception.setSupId(String.valueOf(tempSupplier.getId()));
		createReception.setSupName(tempSupplier.getName());}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
	
}

public void fillGrid (List<Supplier> li) {
	Object[] row = new Object[10];
	for (Supplier tempSupplier : li) {
		row[0] = tempSupplier.getId();
		row[1] = tempSupplier.getName();
		row[2] = tempSupplier.getAdress();
		row[3] = tempSupplier.getMf();
		row[4] = tempSupplier.getRc();
		row[5] = tempSupplier.getNis();
		row[6] = tempSupplier.getArtImpo();
		createSupplier.getSupplierGridModel().addRow(row);
		}
}
public void btnSwitcher (boolean cs, boolean adu) {
	createSupplier.getCancelbtn().setEnabled(cs);
	createSupplier.getSavebtn().setEnabled(cs);
	createSupplier.getUpdatebtn().setEnabled(adu);
	createSupplier.getAddbtn().setEnabled(adu);
	createSupplier.getDeletebtn().setEnabled(adu);
	createSupplier.getSupplierGrid().setEnabled(adu);
}

}
