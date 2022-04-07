package com.commerciale.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.hibernateUtil;
import com.commerciale.model.user;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.view.createUser;
import com.commerciale.view.loginfrm;

public class CRUDuser {
	private user User;
	private createUser CreateUser;
	private int selectedUser = -1;
	private int selected = -1;
	boolean add = false, update = false;
	public CRUDuser(user User, createUser CreateUser) {
		this.User=User;
		this.CreateUser=CreateUser;
		this.CreateUser.addsavingListener(new SavingListener());
		this.CreateUser.addAdingListener(new AddUser());
		this.CreateUser.addUpdatingListener(new UpdateUser());
		this.CreateUser.addDeletingListener(new DeleteUser());
		this.CreateUser.addCancelingListener(new CancelUser());
		this.CreateUser.behaveWhenOpen(new BehaviorOnOpening());
		this.CreateUser.userGridMouseListener(new UserGridNavig());
	}
	/********************ON WINDOW OPEN *********************/
	class BehaviorOnOpening extends CustomActionListeners{

		@Override
		public void windowOpened(WindowEvent e) {
			Select("from user");
			selected = -1;
			selectedUser = -1;
		}
	}
	/********************delete *****************************/
	class DeleteUser implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (selected<0) {JOptionPane.showMessageDialog(null, "Veuillez cliquer sur la liste pour selectionner l'element que vous voulez le supprimer !! ");}	
			else {
				Object[] options = { "confirmer", "Annuler" };
				int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
				if (answer==0) {Delete(selectedUser);
				CreateUser.getUserNameField().setText(null);
				CreateUser.getPasswordField().setText(null);
				CreateUser.getPassConfField().setText(null);
				CreateUser.getUserNameField().setEditable(false);
				CreateUser.getPasswordField().setEditable(false);
				CreateUser.getPassConfField().setEditable(false);
				CreateUser.getAddButton().setEnabled(true);
				CreateUser.getDeleteButton().setEnabled(true);
				CreateUser.getUpdateButton().setEnabled(true);
				CreateUser.getSaveButton().setEnabled(false);
				CreateUser.getCancelButton().setEnabled(false);
				selected = -1;
				selectedUser = -1;
				Select("from user");
				}
				
				
			}
			
		}
		
	}
	/*****************Cancel ********************************/
	class CancelUser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CreateUser.getUserNameField().setEditable(false);
			CreateUser.getPasswordField().setEditable(false);
			CreateUser.getPassConfField().setEditable(false);
			CreateUser.getAddButton().setEnabled(true);
			CreateUser.getDeleteButton().setEnabled(true);
			CreateUser.getUpdateButton().setEnabled(true);
			CreateUser.getSaveButton().setEnabled(false);
			CreateUser.getCancelButton().setEnabled(false);
			update = false;
			add = false;
			selected = -1;
			selectedUser = -1;
		}
		
	}
	
	
	/*******************add *********************/
	class AddUser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CreateUser.getUserNameField().setText(null);
			CreateUser.getPasswordField().setText(null);
			CreateUser.getPassConfField().setText(null);
			CreateUser.getUserNameField().setEditable(true);
			CreateUser.getPasswordField().setEditable(true);
			CreateUser.getPassConfField().setEditable(true);
			CreateUser.getAddButton().setEnabled(false);
			CreateUser.getDeleteButton().setEnabled(false);
			CreateUser.getUpdateButton().setEnabled(false);
			CreateUser.getSaveButton().setEnabled(true);
			CreateUser.getCancelButton().setEnabled(true);
			add = true;
			update = false;
			selected = -1;
			selectedUser = -1;
		}
		
	}
	
	
	
	class SavingListener implements ActionListener {
		private String userName, password, confPass;
		@Override
		public void actionPerformed(ActionEvent e) {
			if ((CreateUser.getUserName().isEmpty())||(CreateUser.getPassword().isEmpty())||(CreateUser.getPassConf().isEmpty())) {
				JOptionPane.showMessageDialog(null, "Veuillez remplisser tous les champs svp !!");
			}else {
				userName = CreateUser.getUserName();
			    password= CreateUser.getPassword();
			    confPass=CreateUser.getPassConf();
			    
			    if (comparePassword(password, confPass) ){
			    if (add) {Insert();}
			    else if (update) { Update(selectedUser); }
			    Select("from user");
			    CreateUser.getUserNameField().setText(null);
				CreateUser.getPasswordField().setText(null);
				CreateUser.getPassConfField().setText(null);
				CreateUser.getUserNameField().setEditable(false);
				CreateUser.getPasswordField().setEditable(false);
				CreateUser.getPassConfField().setEditable(false);
				CreateUser.getAddButton().setEnabled(true);
				CreateUser.getDeleteButton().setEnabled(true);
				CreateUser.getUpdateButton().setEnabled(true);
				CreateUser.getSaveButton().setEnabled(false);
				CreateUser.getCancelButton().setEnabled(false);
				add = false;
				update = false;
				selected = -1;
				selectedUser = -1;
			    }else JOptionPane.showMessageDialog(null, "Les deux mots de passe ne sont pas identiques!!");
			    
			}
			
		}
	}
	public Boolean comparePassword(String pass, String conPass) {
		return pass.equals(conPass);
		
	}
	
	/**********************************Update user *******************************/
	class UpdateUser implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CreateUser.getUserNameField().setEditable(true);
			CreateUser.getPasswordField().setEditable(true);
			CreateUser.getPassConfField().setEditable(true);
			CreateUser.getAddButton().setEnabled(false);
			CreateUser.getDeleteButton().setEnabled(false);
			CreateUser.getUpdateButton().setEnabled(false);
			CreateUser.getSaveButton().setEnabled(true);
			CreateUser.getCancelButton().setEnabled(true);
			update = true;
			add = false;
		}
	}
	/*****************************************************************************/
	class UserGridNavig extends CustomActionListeners{
        
		@Override
		public void mouseClicked(MouseEvent e) {
			selected = CreateUser.getUserGrid().getSelectedRow();
			selectedUser = Integer.parseInt(CreateUser.getUserGridModel().getValueAt(selected, 2).toString());
			fillFields(selected);
		}
	}
	
	
	
	
	//**********insert User***********
	public void Insert() {
		String username = CreateUser.getUserName();
		String password = CreateUser.getPassword(); 
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
		session.beginTransaction();
	    List<user> existantUsers = session.createQuery("from user where userName = '"+username+"'").list();
	    if (existantUsers.size()==0) {
		user myUser = new user();
		myUser.setUserName(username);
		myUser.setPassword(password);
		session.save(myUser);}
	    else JOptionPane.showMessageDialog(null, "Ce pseudo est dejà pris !! ");
		session.getTransaction().commit();
		}
		catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, ex);
		}
		
		finally {
			session.close();
		}
				
		}
	
	//********* select User***********
	public void Select(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
		    List<user> myUser = session.createQuery(query).list();
			FillGrid(myUser);
			session.getTransaction().commit();
		}
		catch(HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e);
		}
		finally {
			session.close();
		}
				
		}
	//********update User*************
	public void Update(int sel) {
		String username = CreateUser.getUserName();
		String pass = CreateUser.getPassword();
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<user> exitantUsers = session.createQuery("from user where userName = '"+username+"'").list();
			if (exitantUsers.size()==0) {
			user myUser = session.get(user.class, sel);
			myUser.setUserName(username);
			myUser.setPassword(pass);
			session.update(myUser);}
			else {JOptionPane.showMessageDialog(null, "Ce pseudo est déjà pris !!");}
			session.getTransaction().commit();
		}
		catch(HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e);
		}
		finally {
			session.close();
		}
				
		}
	
	public void Delete(int sel) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			user myUser = session.get(user.class, sel);
			if (!(myUser==null)){
			session.delete(myUser);}
			else JOptionPane.showMessageDialog(null, "cet élement a été dejà supprimé !!");
			session.getTransaction().commit();
		}
		catch(HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e);
		}
		finally {
			session.close();
		}
				
		}

	public boolean userExist(String userName, String pass) {
		boolean existance = false;
		
		Session session = hibernateUtil.getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
		    List<user> myUser = session.createQuery("from user where userName = '"+userName +
		    		              "' AND password = '"+pass+"'").list();
			if (myUser.size()>0) {existance = true;}
			else {existance = false;}
			session.getTransaction().commit();
		}
		catch(HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e);
		}
		finally {
			session.close();
		}
		return existance;		
		}
	
	public void FillGrid(List<user> li) {
		CreateUser.getUserGridModel().setRowCount(0);
		Object[] row  = new Object[3];
		for(user User : li) {
			row[0] = User.getUserName();
			row[1] = User.getPassword();
			row[2] = User.getUserId();
			CreateUser.getUserGridModel().addRow(row);
		}
		CreateUser.SetNumberOfUsers(String.valueOf(CreateUser.getUserGrid().getRowCount()));
	}
	
	
	public void fillFields(int sel) {
		CreateUser.getUserNameField().setText(CreateUser.getUserGridModel().getValueAt(sel, 0).toString());
		CreateUser.getPasswordField().setText(CreateUser.getUserGridModel().getValueAt(sel, 1).toString());
		CreateUser.getPassConfField().setText(CreateUser.getUserGridModel().getValueAt(sel, 1).toString());
	}
}
