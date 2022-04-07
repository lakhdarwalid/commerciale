package com.commerciale.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.transaction.Transactional.TxType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Synchronize;
import org.mozilla.javascript.tools.shell.ConsoleTextArea;

import com.commerciale.controller.CRUDuser;
import com.commerciale.model.DataBackRes;
import com.commerciale.model.hibernateUtil;
import com.commerciale.model.user;
import com.commerciale.service.Tools;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JProgressBar;

public class loginfrm extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField password;
	private JLabel loginLogo = new JLabel("");
	public static JLabel lblBackupComment = new JLabel("");
	
	public loginfrm() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 433);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 139, 139));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		loginLogo.setBounds(231, 21, 144, 165);
		contentPane.add(loginLogo);
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(104, 215, 340, 64);
		contentPane.add(panel);
		panel.setLayout(null);
	
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		JLabel lblOk = new JLabel("");
		lblOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!((textField.getText().isEmpty())||(password.getText().isEmpty()))) {
				user User = new user();
				createUser CreateUser = createUser.getInstance();
				CRUDuser CRUDUser = new CRUDuser(User, CreateUser);
				if (CRUDUser.userExist(textField.getText(), password.getText())) {
				
				loginfrm.this.dispose();
				 //CreateUser.setVisible(true);
				MainMenu mainMenu = new MainMenu();
				mainMenu.setVisible(true);
				
				}else {JOptionPane.showMessageDialog(null, " Connexion échouée, Verifier le mot"
						+ " de passe ou le nom d'utilisateur !! ");}
			}}
		});
		lblOk.setBounds(466, 229, 98, 96);
		contentPane.add(lblOk);
		JLabel lblClose = new JLabel("");
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				loginfrm.this.dispose();
			}

		});
		lblClose.setBounds(525, 0, 75, 53);
		contentPane.add(lblClose);

		
		
	
		
		textField = new JTextField();
		textField.setBorder(null);
		textField.setBounds(10, 11, 247, 40);
		panel.add(textField);
		textField.setFont(new Font("Tahoma", Font.BOLD, 20));
		textField.setColumns(10);

		JLabel lblusername = new JLabel("");
		lblusername.setBounds(265, 0, 75, 64);
		panel.add(lblusername);
		lblusername.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(104, 296, 340, 69);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		password = new JPasswordField();
		password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					int i =0;
					if (!((textField.getText().isEmpty())||(password.getText().isEmpty()))) {
						//Tools.dataBackUp();	
						try {
						 Loader.run();
						}
						catch(Throwable ex) {}
						
						Logger.start();	
					}
				}
		//		JOptionPane.showMessageDialog(null, Logger.isAlive());
			}
		});
		password.setBorder(null);
		password.setBounds(10, 11, 247, 43);
		panel_1.add(password);
		password.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblpassword = new JLabel("");
		lblpassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, Logger.isAlive()+"    "+ Loader.isAlive());
			}
		});
		lblpassword.setBounds(264, 1, 76, 68);
		panel_1.add(lblpassword);
		lblpassword.setHorizontalAlignment(SwingConstants.CENTER);

		try {
			Image imageLogo = ImageIO.read(new File("C:\\CommercialRessources\\res\\enter.png")).getScaledInstance(120, 160, Image.SCALE_SMOOTH);
			loginLogo.setIcon(new ImageIcon(imageLogo));
			Image imageUser = ImageIO.read(new File("C:\\CommercialRessources\\res\\user1.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblusername.setIcon(new ImageIcon(imageUser));
			Image imagePass = ImageIO.read(new File("C:\\CommercialRessources\\res\\password.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblpassword.setIcon(new ImageIcon(imagePass));
			Image imageOk = ImageIO.read(new File("C:\\CommercialRessources\\res\\ok.png")).getScaledInstance(90, 90, Image.SCALE_SMOOTH);
			lblOk.setIcon(new ImageIcon(imageOk));
			Image imageClose = ImageIO.read(new File("C:\\CommercialRessources\\res\\shut.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			lblClose.setIcon(new ImageIcon(imageClose));
			
			
			lblBackupComment.setForeground(Color.ORANGE);
			lblBackupComment.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblBackupComment.setBounds(21, 386, 568, 26);
			contentPane.add(lblBackupComment);
			
			
		} catch (Throwable e) {

			e.printStackTrace();
		}
	
	}
	
/*	public void setBackupComment(String comment) {
		lblNewLabel.setText(comment);
	}*/
	
	public void LoginToMyApp() {
		
		user User = new user();
		createUser CreateUser = createUser.getInstance();
		CRUDuser CRUDUser = new CRUDuser(User, CreateUser);
		if (CRUDUser.userExist(textField.getText(), password.getText())) {
			
		loginfrm.this.dispose();
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);
		
		}else {JOptionPane.showMessageDialog(null, " Connexion échouée, Verifier le mot"
				+ " de passe ou le nom d'utilisateur !! ");}
	}
	
	Thread Logger = new Thread(new Runnable() {
		@Override
		public void run () {
			/********************************************************************************************/
			 Date todayDate = new Date();
			 String savingDate = Tools.dateToString(todayDate);
			 String BackupResPath="E:/CommercialRessourcesSauvegardes/Sauvegarde_" + savingDate +".sql";
			 Session session = hibernateUtil.getSessionFactory().openSession();
			  try {
				 session.beginTransaction();
				 List <DataBackRes> dataBack = session.createQuery("from DataBackRes").list();
				 if (dataBack.size()>6) {
					 Date lastBackupDate = dataBack.get(dataBack.size()-1).getBackUpDate();
				//	 JOptionPane.showMessageDialog(null, lastBackupDate);
					 long diff = todayDate.getTime() - lastBackupDate.getTime(); 
					 long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				//	 JOptionPane.showMessageDialog(null, days);
					 if (days>0) {
					
					 loginfrm.lblBackupComment.setText("Veuillez patienter svp  le systeme est entrain de cr\u00E9er une sauvegarde de donn\u00E9es ...");
					
					 Tools.sqlDumbDB();
					     DataBackRes dt = new DataBackRes();
						 dt.setBackUpDate(new Date());
						 dt.setBackFilePath(BackupResPath);
						 session.save(dt);
					 }
				 }
				 else {
					 loginfrm.lblBackupComment.setText("Veuillez patienter svp le systeme est entrain de cr\\u00E9er une sauvegarde de donn\\u00E9es ...");
					 Tools.sqlDumbDB();
					 DataBackRes dt = new DataBackRes();
					 dt.setBackUpDate(new Date());
					 dt.setBackFilePath(BackupResPath);
					 session.save(dt);
				 }
				 session.getTransaction().commit();
			 }catch (HibernateException ex) {
				 session.getTransaction().rollback();
				 ex.printStackTrace();
			 }finally {
				 session.close();
			 }
			/********************************************************************************************/
			  try {
				Logger.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
			if (userExist(textField.getText(), password.getText())) {
				loginfrm.this.dispose();
				MainMenu mainMenu = new MainMenu();
				mainMenu.setVisible(true);
				
				}else {
					try {
						Image imageLogo = ImageIO.read(new File("C:\\CommercialRessources\\res\\enter.png")).getScaledInstance(120, 160, Image.SCALE_SMOOTH);
						loginLogo.setIcon(new ImageIcon(imageLogo));}
					catch (IOException ex) {}
						JOptionPane.showMessageDialog(null, " Connexion échouée, Verifier le mot"
						+ " de passe ou le nom d'utilisateur !! ");
						password.setText(null);
				}
		
			
			
		}
	});
	
	Thread Loader  = new Thread(new Runnable() {
		@Override
		public void run(){
			try {
				URL url = new URL("file:///C:/CommercialRessources/res/circle1.gif");
		    	loginLogo.setIcon(new ImageIcon(url));
		  } catch (IOException ex ) {}
			
		}
	});
	
	
	
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
}
