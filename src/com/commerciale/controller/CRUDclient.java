package com.commerciale.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JComboBox;
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


public class CRUDclient {
	private Client client;
	private CreateClient createClient;
	@SuppressWarnings("unused")
	private CreateInvoice createInvoice;
	private String addOrUpdate;
	private int selected;
	public CRUDclient(Client client, CreateClient createClient, CreateInvoice crearteInvoice) {
		this.client = client;
		this.createClient = createClient;
		this.createClient.addingListener(new addClientListener());
		this.createClient.cancelingListener(new cancelClientListener());
		this.createClient.SavingListener(new saveClientListener());
		this.createClient.updatingListener(new updateClientListener());
		this.createClient.searchListener(new searchingClientListener());
		this.createClient.searchBtnListener(new searchingbtnClientListener());
		this.createClient.clientGridListener(new clientGridListener());
		this.createClient.deleteBtnListener(new deletingClientListener());
	}
	public CRUDclient(Client client, CreateClient createClient) {
		this.client = client;
		this.createClient = createClient;
		this.createClient.addingListener(new addClientListener());
		this.createClient.cancelingListener(new cancelClientListener());
		this.createClient.SavingListener(new saveClientListener());
		this.createClient.updatingListener(new updateClientListener());
		this.createClient.searchListener(new searchingClientListener());
		this.createClient.searchBtnListener(new searchingbtnClientListener());
		this.createClient.clientGridListener(new clientGridListener());
		this.createClient.deleteBtnListener(new deletingClientListener());
	}
/*********************************************** CRUD btn Class *******************************************/	
/************ADD ****************************/
class addClientListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createClient.getContainer().getComponents()) {
			if ((comp instanceof JTextField)||(comp instanceof JComboBox)) {
				comp.setEnabled(true);
			}
			
			if (comp instanceof JTextField) {((JTextField) comp).setText(null);}
		} 
		btnSwitcher(true, false);	
		addOrUpdate = "ADD";
		if (ClientList.getInstance().isActive()) {
			JOptionPane.showMessageDialog(null, "rahi visible");
		}
		}
	
}
/***********CANCEL ***************************/
class cancelClientListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
				comp.setEnabled(false);
			}
			if (comp instanceof JComboBox) {comp.setEnabled(false);}
		} 
		btnSwitcher(false, true);
		addOrUpdate="";
		selected = -1;
	}
	
}
/************SAVE ***************************/
class saveClientListener implements ActionListener{
      private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
	/*	for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()){empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez remplisser toutes les cellules svp !!!"); empty = false;}*/
		if (createClient.getName().isEmpty()) {JOptionPane.showMessageDialog(null, "Veuillez remplisser toutes les cellules svp !!!");}
		else { if (createClient.getImpexo().isEmpty()) {createClient.setImpexo("0");}
		if (addOrUpdate=="ADD") {Insert();}
		if (addOrUpdate=="UPDATE") {Update(selected);}
		btnSwitcher(false, true);
		for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
				comp.setEnabled(false);
			}
			if (comp instanceof JComboBox) {comp.setEnabled(false);}
		} 
		addOrUpdate="";
		selected = -1;
		}

		Select("from Client");
		
	}
	
}
/****************UPDATE **********************/
class updateClientListener implements ActionListener{
	private boolean empty= false;
	@Override
	public void actionPerformed(ActionEvent e) {
	/*	for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des clients pour selectionner l'element que vous voulez le modifier !! "); empty = false;}
	*/  if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des clients pour selectionner l'element que vous voulez le modifier !! ");}
		else {
		for (Component comp : createClient.getContainer().getComponents()) {
			if ((comp instanceof JTextField)||(comp instanceof JComboBox)) {
				comp.setEnabled(true);
			}
		} 
		btnSwitcher(true,false);
		addOrUpdate = "UPDATE";
	}
	}
}
/*************DELETE ***************************/
class deletingClientListener implements ActionListener{
	private boolean empty = false;
	@Override
	public void actionPerformed(ActionEvent e) {
	/*	for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {empty = true;}
			}
		} 
		if (empty) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des clients pour selectionner l'element que vous voulez le supprimer !! "); empty = false;}
	*/  if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste des clients pour selectionner l'element que vous voulez le supprimer !! ");}	
		else {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
			if (answer==0) {Delete(selected);}
			Select("from Client");
			for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
		
			}
			}
			selected=-1;
		}	
	}
}
/*************SEARCH ***************************/
class searchingClientListener extends CustomActionListeners{
    @Override
	public void keyReleased(KeyEvent e) {
		Select("from Client where name LIKE '%"+createClient.getTxtSearch()+"%'");
		
	}
}

class searchingbtnClientListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		createClient.setTxtSearch(null);
		for (Component comp : createClient.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				((JTextField) comp).setText(null);
			}
		}
		Select("from Client");
		
	}
	
}

class clientGridListener extends CustomActionListeners{
    private String id ;
    private int SR;
	@Override
	public void mouseClicked(MouseEvent e) {
	SR= createClient.getClientGrid().getSelectedRow();
	id = createClient.getClientGridModel().getValueAt(SR, 0).toString();
	selected = Integer.parseInt(id);
	//createClient.setTxtSearch(id);
	if (createClient.getClientGrid().isEnabled()) {
	fillFields(id);}
	}
}

 /***********************************CRUD **********************************************************************************************/
/*******Insert */
public void Insert() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		client.setName(createClient.getName());
		client.setAdress(createClient.getAdress());
		client.setRc(createClient.getRc());
		client.setMf(createClient.getMf());
		client.setNis(createClient.getNis());
		client.setArtImpo(createClient.getArtimp());
		client.setCategory(createClient.getCategory());
		client.setImpExon(createClient.getImpexo());
		client.setModeFacture(createClient.getModefact());
		client.setTel(createClient.getTel());
		client.setEmail(createClient.getEmail());
		client.setBanque(createClient.getBanque());
		client.setCompte(createClient.getCompte());
		session.save(client);
		session.getTransaction().commit();
		JOptionPane.showMessageDialog(null, "Enregistrement avec succès ...");
	}catch (HibernateException ex) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un client : "+ex);
	}
	finally {
		session.close();
	}
	
}
/*******Select*/
@SuppressWarnings("unchecked")
public void Select (String query) {
	Session session = hibernateUtil.getSessionFactory().openSession();
	createClient.getClientGridModel().setRowCount(0);
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery(query).list();
		fillGrid(cl);
		session.getTransaction().commit();
		createClient.setSumClient(createClient.getClientGridModel().getRowCount());
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
		Client cl = session.get(Client.class, selected);
	//	createClient.setTxtSearch(cl.getName());
		cl.setName(createClient.getName());
		cl.setAdress(createClient.getAdress());
		cl.setRc(createClient.getRc());
		cl.setMf(createClient.getMf());
		cl.setNis(createClient.getNis());
		cl.setArtImpo(createClient.getArtimp());
		cl.setCategory(createClient.getCategory());
		cl.setImpExon(createClient.getImpexo());
		cl.setModeFacture(createClient.getModefact());
		cl.setTel(createClient.getTel());
		cl.setEmail(createClient.getEmail());
		cl.setBanque(createClient.getBanque());
		cl.setCompte(createClient.getCompte());
		session.update(cl);
		session.getTransaction().commit();
		JOptionPane.showMessageDialog(null, "Modification enregistrée avec succès ...");
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
		Client cl = session.get(Client.class, selected);
		session.remove(cl);
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
		List<Client> cli =  session.createQuery("from Client where id = "+row).list();
		for (Client tempClient : cli) {
		createClient.setName(tempClient.getName());
		createClient.setAddress(tempClient.getAdress());
	    createClient.setRc(tempClient.getRc());
		createClient.setMf(tempClient.getMf());
		createClient.setNis(tempClient.getNis());
		createClient.setArtimpo(tempClient.getArtImpo());
		createClient.setCategory(tempClient.getCategory());
		createClient.setImpexo(tempClient.getImpExon());
		createClient.setModefact(tempClient.getModeFacture());
		createClient.setTel(tempClient.getTel());
		createClient.setEmail(tempClient.getEmail());
		createClient.setBanque(tempClient.getBanque());
		createClient.setCompte(tempClient.getCompte());}
		session.getTransaction().commit();
		createClient.setSumClient(createClient.getClientGridModel().getRowCount());
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
	
}
public void fillGrid (List<Client> li) {
	Object[] row = new Object[10];
	for (Client tempClient : li) {
		row[0] = tempClient.getId();
		row[1] = tempClient.getName();
		row[2] = tempClient.getAdress();
		row[3] = tempClient.getMf();
		row[4] = tempClient.getRc();
		row[5] = tempClient.getNis();
		row[6] = tempClient.getArtImpo();
		row[7] = tempClient.getImpExon();
		row[8] = tempClient.getCategory();
		row[9] = tempClient.getModeFacture();
		createClient.getClientGridModel().addRow(row);
		}
}
public void btnSwitcher (boolean cs, boolean adu) {
	createClient.getCancelbtn().setEnabled(cs);
	createClient.getSavebtn().setEnabled(cs);
	createClient.getUpdatebtn().setEnabled(adu);
	createClient.getAddbtn().setEnabled(adu);
	createClient.getDeletebtn().setEnabled(adu);
	createClient.getClientGrid().setEnabled(adu);
}
}
