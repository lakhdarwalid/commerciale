package com.commerciale.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.hibernateUtil;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.view.ClientList;
import com.commerciale.view.CreateClient;
import com.commerciale.view.CreateInvoice;
import com.commerciale.view.CreateReception;
import com.commerciale.view.MainMenu;

public class CRUDclientDialog {
	private Client client;
	private ClientList clientList;
	private CRUDinvoice crudInvoice;
	private int selected;
	
	public CRUDclientDialog(Client client, ClientList clientList) {
		this.client = client;
		this.clientList = clientList;
		this.clientList.searchListener(new searchingCustomerListener());
		this.clientList.searchBtnListener(new searchingbtnCustomerListener());
		this.clientList.customerGridListener(new CustomerGridListener());
		this.clientList.addNewCustListener(new addingNewCustomerListener());
	}
/*********************************************** CRUD btn Class *******************************************/	
/**********************adding new customer ******************/
class addingNewCustomerListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
	/*	Client cl = new Client();
		CreateClient createClient = CreateClient.getInstance();
		CRUDclient crudClient = new CRUDclient(cl, createClient);
		if (!createClient.isVisible()) { MainMenu.desktopPane.add(createClient);
		  								clientList.setModal(false);	
		  								clientList.setAlwaysOnTop(false);
										createClient.show();*/
			
		
	}
	
}
	
/***********CANCEL ***************************/
class cancelCustomerListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}

/*************SEARCH ***************************/
class searchingCustomerListener extends CustomActionListeners{
   @Override
	public void keyReleased(KeyEvent e) {
		Select("from Client where name LIKE '%"+clientList.getTxtSearch()+"%'");
		
	}
}

class searchingbtnCustomerListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		clientList.setTxtSearch(null);
		for (Component comp : clientList.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
			}
		}
		Select("from Client");
		
	}
	
}

class CustomerGridListener extends CustomActionListeners{
    private String id ;
    private int SR;
	@Override
	public void mouseClicked(MouseEvent e) {
	SR= clientList.getCustomerGrid().getSelectedRow();
	id = clientList.getCustomerGridModel().getValueAt(SR, 2).toString();
	selected = Integer.parseInt(id);
	fillFieldsClient(id);
	clientList.dispose();
	}
}

 /***********************************CRUD *************************************************************/
/*******Select*/
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	clientList.getCustomerGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery(query).list();
		fillGrid(cl);
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}


/************************************End of CRUD *************************************************************/

public void fillGrid (List<Client> li) {
	Object[] row = new Object[3];
	for (Client tempClient : li) {
		row[0] = tempClient.getName();
		row[1] = tempClient.getAdress();
		row[2] = tempClient.getId();
		clientList.getCustomerGridModel().addRow(row);
		}
}

public void fillFieldsClient(String row) {   // fill fields client in reception view 
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery("from Client where id = "+row).list();
		for (Client tempClient : cl) {
		CreateInvoice.txtCustName.setText(tempClient.getName());
		CreateInvoice.txtCodeCust.setText((String.valueOf(tempClient.getId())));}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}	
	
}

}
