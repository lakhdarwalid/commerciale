package com.commerciale.view;

import java.awt.Image;
import java.awt.TextArea;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.SystemColor;

public class createUser extends JFrame {
	private static createUser CU = null;
	private JPanel contentPane;
	private JTextField confPass;
	private JTextField userName;
	private JTextField password;
	private JTextField txtNbreUser = new JTextField();
	private JButton btnSave = new JButton("Enregistrer");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnUpdate = new JButton("Modifier");
	private JButton btnAdd = new JButton("Ajouter");
	private JTable userGrid = new JTable();
	private DefaultTableModel userGridModel;
	private JScrollPane scrollPane = new JScrollPane();
	
	

	private createUser() {
		setBackground(new Color(30, 144, 255));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 783, 739);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUserLogo = new JLabel("");
		lblUserLogo.setBackground(new Color(0, 0, 255));
		lblUserLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserLogo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblUserLogo.setBounds(0, 42, 329, 321);
		contentPane.add(lblUserLogo);
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(339, 80, 399, 71);
		contentPane.add(panel);
		panel.setLayout(null);

		userName = new JTextField();
		userName.setBackground(new Color(255, 255, 255));
		userName.setEditable(false);
		userName.setBorder(null);
		userName.setFont(new Font("Tahoma", Font.BOLD, 20));
		userName.setBounds(10, 11, 304, 49);
		panel.add(userName);
		userName.setColumns(10);

		JLabel lblimgUser = new JLabel("");
		lblimgUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblimgUser.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblimgUser.setHorizontalTextPosition(SwingConstants.CENTER);
		lblimgUser.setBounds(315, 0, 84, 71);
		panel.add(lblimgUser);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(339, 162, 399, 71);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		password = new JTextField();
		password.setBackground(new Color(255, 255, 255));
		password.setEditable(false);
		password.setBorder(null);
		password.setFont(new Font("Tahoma", Font.BOLD, 20));
		password.setText("");
		password.setBounds(10, 11, 300, 49);
		panel_1.add(password);
		password.setColumns(10);

		JLabel lblimgPass = new JLabel("");
		lblimgPass.setHorizontalAlignment(SwingConstants.CENTER);
		lblimgPass.setHorizontalTextPosition(SwingConstants.CENTER);
		lblimgPass.setBounds(312, 0, 87, 71);
		panel_1.add(lblimgPass);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(339, 244, 399, 71);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		confPass = new JTextField();
		confPass.setBackground(new Color(255, 255, 255));
		confPass.setEditable(false);
		confPass.setBorder(null);
		confPass.setFont(new Font("Tahoma", Font.BOLD, 20));
		confPass.setText("");
		confPass.setBounds(10, 11, 302, 49);
		panel_2.add(confPass);
		confPass.setColumns(10);

		JLabel lblimgconfPass = new JLabel("");
		lblimgconfPass.setHorizontalAlignment(SwingConstants.CENTER);
		lblimgconfPass.setHorizontalTextPosition(SwingConstants.CENTER);
		lblimgconfPass.setBounds(313, 0, 86, 71);
		panel_2.add(lblimgconfPass);
		btnSave.setIcon(new ImageIcon(createUser.class.getResource("/net/sf/jasperreports/view/images/save.GIF")));
		

		btnSave.setHorizontalAlignment(SwingConstants.LEFT);
		btnSave.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnSave.setBounds(566, 442, 172, 54);
		contentPane.add(btnSave);

		JLabel lblcancelUser = new JLabel("");
		lblcancelUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createUser.this.dispose();
			/*	loginfrm logfrm = new loginfrm();
				logfrm.setVisible(true);*/
			}
		});
		lblcancelUser.setHorizontalTextPosition(SwingConstants.CENTER);
		lblcancelUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblcancelUser.setBounds(713, 0, 70, 54);
		contentPane.add(lblcancelUser);

		try {
			Image imageLogo = ImageIO.read(new File("C:\\CommercialRessources\\res\\userB.png")).getScaledInstance(150, 250, Image.SCALE_SMOOTH);
			lblUserLogo.setIcon(new ImageIcon(imageLogo));
			Image imageUser = ImageIO.read(new File("C:\\CommercialRessources\\res\\user1.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			lblimgUser.setIcon(new ImageIcon(imageUser));
			Image imagePass = ImageIO.read(new File("C:\\CommercialRessources\\res\\password.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			lblimgPass.setIcon(new ImageIcon(imagePass));
			Image imageconfPass = ImageIO.read(new File("C:\\CommercialRessources\\res\\password.png")).getScaledInstance(30, 30,
					Image.SCALE_SMOOTH);
			lblimgconfPass.setIcon(new ImageIcon(imageconfPass));
		/*	Image imagebtnvalUser = ImageIO.read(new File("C:\\CommercialRessources\\res\\save1.png")).getScaledInstance(30, 30,
					Image.SCALE_SMOOTH);
			btnvalidateUser.setIcon(new ImageIcon(createUser.class.getResource("/net/sf/jasperreports/view/images/save.GIF")));*/
			Image imagecancelUser = ImageIO.read(new File("C:\\CommercialRessources\\res\\shut.png")).getScaledInstance(30, 30,
					Image.SCALE_SMOOTH);
			lblcancelUser.setIcon(new ImageIcon(imagecancelUser));
			btnCancel.setIcon(new ImageIcon(createUser.class.getResource("/org/apache/batik/apps/svgbrowser/resources/go-previous-small.png")));
			
			btnCancel.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnCancel.setHorizontalAlignment(SwingConstants.LEFT);
			btnCancel.setBounds(566, 664, 172, 54);
			contentPane.add(btnCancel);
			btnDelete.setIcon(new ImageIcon(createUser.class.getResource("/net/sf/jasperreports/components/headertoolbar/resources/images/delete_edit.gif")));
			
			
			btnDelete.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnDelete.setHorizontalAlignment(SwingConstants.LEFT);
			btnDelete.setBounds(566, 589, 172, 54);
			contentPane.add(btnDelete);
			btnUpdate.setIcon(new ImageIcon(createUser.class.getResource("/org/apache/batik/apps/svgbrowser/resources/undo.png")));
			
			
			btnUpdate.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnUpdate.setHorizontalAlignment(SwingConstants.LEFT);
			btnUpdate.setBounds(566, 517, 172, 54);
			contentPane.add(btnUpdate);
			
			
			scrollPane.setBounds(37, 367, 519, 332);
			scrollPane.setAutoscrolls(true);
			contentPane.add(scrollPane);
			String[] columnNames = {"Nom d'utilisateur","Mot de passe","N°"};
			userGridModel = new DefaultTableModel(null, columnNames) {
				 @Override
				    public boolean isCellEditable(int row, int column) {
				       return false;
				    }
			};
			userGridModel.setColumnIdentifiers(columnNames);
				
			userGrid.setBackground(Color.WHITE);
			userGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
			userGrid.getTableHeader().setForeground(Color.WHITE);
			userGrid.getTableHeader().setBackground(Color.DARK_GRAY);
			userGrid.getTableHeader().setResizingAllowed(true);
			userGrid.getTableHeader().setReorderingAllowed(false);
			userGrid.setAutoResizeMode(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			userGrid.setModel(userGridModel);
			userGrid.removeColumn(userGrid.getColumnModel().getColumn(2));
			userGrid.setRowHeight(30);
			scrollPane.setViewportView(userGrid);
			JLabel lblNbreUser = new JLabel("Nombre des utilisateurs : ");
			lblNbreUser.setFont(new Font("Arial", Font.PLAIN, 15));
			lblNbreUser.setForeground(Color.WHITE);
			lblNbreUser.setBounds(37, 704, 172, 26);
			contentPane.add(lblNbreUser);
			txtNbreUser.setEditable(false);
			
			
			txtNbreUser.setHorizontalAlignment(SwingConstants.CENTER);
			txtNbreUser.setFont(new Font("Tahoma", Font.PLAIN, 18));
			txtNbreUser.setText("01");
			txtNbreUser.setForeground(Color.WHITE);
			txtNbreUser.setBackground(Color.BLACK);
			txtNbreUser.setBounds(204, 698, 90, 32);
			contentPane.add(txtNbreUser);
			txtNbreUser.setColumns(10);
			
			
			btnAdd.setIcon(new ImageIcon(createUser.class.getResource("/org/apache/batik/apps/svgbrowser/resources/redo.png")));
			btnAdd.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnAdd.setHorizontalAlignment(SwingConstants.LEFT);
			btnAdd.setBounds(566, 367, 172, 54);
			contentPane.add(btnAdd);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	public static createUser getInstance() {
		if (CU==null) {
			synchronized (createUser.class) {
				if (CU==null) CU = new createUser();
			}
			 }
		return CU;
	}
/*******************************getter & setters **************************/
	public String getUserName() {
		return userName.getText();
	}

	public String getPassword() {
		return password.getText();
	}

	public String getPassConf() {
		return confPass.getText();
	}
	
	public JTextField getUserNameField() {
		return userName;
	}

	public JTextField getPasswordField() {
		return password;
	}

	public JTextField getPassConfField() {
		return confPass;
	} 
	
	public JButton getDeleteButton(){
		return btnDelete;
	} 
	
	public JButton getSaveButton(){
		return btnSave;
	} 
	
	public JButton getUpdateButton(){
		return btnUpdate;
	} 
	
	public JButton getCancelButton(){
		return btnCancel;
	} 
	
	public JButton getAddButton(){
		return btnAdd;
	} 
	
	public JTable getUserGrid() {
		return userGrid;
	}
	
	public DefaultTableModel getUserGridModel() {
		return userGridModel;
	}
	
	public void SetNumberOfUsers(String nbre) {
		txtNbreUser.setText(nbre);
	}
/**************************************************************************/
	public void addsavingListener(ActionListener sav) {
		btnSave.addActionListener(sav);
	}
	
	public void addCancelingListener(ActionListener cancl) {
		btnCancel.addActionListener(cancl);
	}
	
	public void addDeletingListener(ActionListener del) {
		btnDelete.addActionListener(del);
	}
	
	public void addAdingListener(ActionListener add) {
		btnAdd.addActionListener(add);
	}
	
	public void addUpdatingListener(ActionListener upd) {
		btnUpdate.addActionListener(upd);
	}
	
	public void behaveWhenOpen(WindowListener wl) {
		this.addWindowListener(wl);
	}
	
	public void userGridMouseListener(MouseListener m) {
		userGrid.addMouseListener(m);
	}
}
