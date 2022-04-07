package com.commerciale.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.SaleConditions;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.view.SaleConditionsList;

public class CRUDsaleConditions {
	private SaleConditions saleConditions = new SaleConditions();
	private SaleConditionsList saleConditionList = SaleConditionsList.getInstance();
	private int selected_Id, selected = -1;
	private String addorupdate = "Add";
	public CRUDsaleConditions(SaleConditions saleConditions, SaleConditionsList saleConditionList) {
		this.saleConditions = saleConditions;
		this.saleConditionList = saleConditionList;
		this.saleConditionList.onOpeningBehaviorListener(new BehaveWhenOpen());
		this.saleConditionList.gridNavigMouseListener(new GridNav());
		this.saleConditionList.savingActionListener(new SavingListener());
		this.saleConditionList.deletingActionListener(new DeletingListener());
		this.saleConditionList.cancelingActionListener(new CancelingListener());
		this.saleConditionList.updatingActionListener(new UpdatingListener());
	}
	
class BehaveWhenOpen extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		Select();
		
	}
}

class GridNav extends CustomActionListeners {

	@Override
	public void mouseClicked(MouseEvent e) {
	selected = saleConditionList.getSaleCondGrid().getSelectedRow();
	selected_Id  = Integer.parseInt(saleConditionList.getSaleCondModel().getValueAt(selected, 1).toString());
		
	}
}
	
class SavingListener implements ActionListener{
 
	@Override
	public void actionPerformed(ActionEvent e) {
		 
		  if (saleConditionList.getCondTitle().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Veuillez remplisser tout les champs svp !!!");
		}else {if (addorupdate.equalsIgnoreCase("update")) {Update(selected_Id);addorupdate = "Add";}
			   else {Insert();}
			saleConditionList.setCondTitle(null);
			Select();
		}
		  selected = -1;
		  selected_Id = -1;
	}
	
}
	
	
class DeletingListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		Object[] options = { "Oui", "Non" };
		int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
		if (answer==0) { Delete(selected_Id);
						 Select();
		}
		selected = -1;
		selected_Id=-1;
	}
	
}	
	
class CancelingListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		saleConditionList.setCondTitle(null);
		saleConditionList.dispose();
	}
	
}	

class UpdatingListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (selected<0) {JOptionPane.showMessageDialog(null, "Selectionnez un élément svp !!");}
		else {
			addorupdate = "update";
			Session session = hibernateUtil.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				SaleConditions condition = session.get(SaleConditions.class, selected_Id);
				saleConditionList.setCondTitle(condition.getTitle());
				session.getTransaction().commit();
			}catch(HibernateException ex) {session.getTransaction().rollback();
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}finally {session.close();}
		
		}
	}
}
	
public void Select() {
	saleConditionList.getSaleCondModel().setNumRows(0);
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<SaleConditions> conditions = session.createQuery("from SaleConditions").list();
		Object[] row = new Object[2];
		for (SaleConditions sc : conditions ) {
			row[0] = sc.getTitle();
			row[1] = sc.getId();
			saleConditionList.getSaleCondModel().addRow(row);
		}
			session.getTransaction().commit();
		}catch(HibernateException ex) {session.getTransaction().rollback();
									   JOptionPane.showMessageDialog(null, ex.getMessage());
		}finally {
			session.close();
		}
}



public void Insert() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		SaleConditions saleCond = new SaleConditions();
		saleCond.setTitle(saleConditionList.getCondTitle());
		session.save(saleCond);
		session.getTransaction().commit();
	}catch(HibernateException ex) {session.getTransaction().rollback();
								   JOptionPane.showMessageDialog(null, ex.getMessage());
	}finally {
		session.close();
	}
}

public void Delete(int id) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		SaleConditions saleCond = session.get(SaleConditions.class, id);
		session.delete(saleCond);
		session.getTransaction().commit();
	}catch(HibernateException ex) {session.getTransaction().rollback();
								   JOptionPane.showMessageDialog(null, ex.getMessage());
	}finally {
		session.close();
	}
}
	
public void Update (int sel) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		SaleConditions saleCond = session.get(SaleConditions.class, sel);
		saleCond.setTitle(saleConditionList.getCondTitle());
		session.update(saleCond);
		session.getTransaction().commit();
	}catch(HibernateException ex) {session.getTransaction().rollback();
								   JOptionPane.showMessageDialog(null, ex.getMessage());
	}finally {
		session.close();
	}
	
}
}
