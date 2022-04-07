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

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.controller.CRUDsupplierDialog;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.view.CreateReception;
import com.commerciale.view.SupplierList;

public class CRUDsupplierDialog {
	private Supplier supplier;
	private SupplierList supplierList;
	private CRUDreception crudReception;
	//private CreateReception createReception;
	//private String addOrUpdate;
	private int selected;
	//private int showSupplier = 0;
	
	public CRUDsupplierDialog(Supplier supplier, SupplierList supplierList) {
		this.supplier = supplier;
		this.supplierList = supplierList;
		this.supplierList.searchListener(new searchingSupplierListener());
		this.supplierList.searchBtnListener(new searchingbtnSupplierListener());
		this.supplierList.supplierGridListener(new SupplierGridListener());
	}
/*********************************************** CRUD btn Class *******************************************/	
/***********CANCEL ***************************/
class cancelSupplierListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}

/*************SEARCH ***************************/
class searchingSupplierListener extends CustomActionListeners{
   @Override
	public void keyReleased(KeyEvent e) {
		Select("from Supplier where name LIKE '%"+supplierList.getTxtSearch()+"%'");
		
	}
}

class searchingbtnSupplierListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		supplierList.setTxtSearch(null);
		for (Component comp : supplierList.getContainer().getComponents()) {
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
	SR= supplierList.getSupplierGrid().getSelectedRow();
	id = supplierList.getSupplierGridModel().getValueAt(SR, 0).toString();
	selected = Integer.parseInt(id);
	fillFieldsSupplier(id);
	supplierList.dispose();
	}
}

 /***********************************CRUD *************************************************************/
/*******Select*/
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	supplierList.getSupplierGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery(query).list();
		fillGrid(sup);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}
}


/************************************End of CRUD *************************************************************/

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
		supplierList.getSupplierGridModel().addRow(row);
		}
}

public void fillFieldsSupplier(String row) {   // fill fields supplier in reception view 
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Supplier> sup =  session.createQuery("from Supplier where id = "+row).list();
		for (Supplier tempSupplier : sup) {
		CreateReception.txtCodeSup.setText((String.valueOf(tempSupplier.getId())));
		CreateReception.txtSupName.setText(tempSupplier.getName());}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
	
}

}
