package com.commerciale.view;

import javax.swing.table.DefaultTableModel;

import com.commerciale.model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class CreateClient extends JInternalFrame {
	private static CreateClient crClient = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtName = new JTextField(30);
	private JTextField txtAdress = new JTextField(50);
	private JTextField txtMF = new JTextField(15);
	private JTextField txtRC = new JTextField(15);
	private JTextField txtNis = new JTextField(15);
	private JTextField txtArtimp = new JTextField(20);
	private JTextField txtImpexo = new JTextField(10);
	private JTextField txtTel = new JTextField(15);
	private JTextField txtEmail = new JTextField(50);
	private JTextField txtBanque = new JTextField(50);
	private JTextField txtCompte = new JTextField(30);
	private JTextField txtSearch = new JTextField(50);
	private JComboBox txtCategory = new JComboBox();
	private JComboBox txtModefact = new JComboBox();
	private JLabel lblName = new JLabel("Raison sociale :");
	private JLabel lblAdress = new JLabel("Adresse :");
	private JLabel lblMF = new JLabel("MF :");
	private JLabel lblRC = new JLabel("RC :");
	private JLabel lblNis = new JLabel("NIS :");
	private JLabel lblArtimpo = new JLabel("Art Imp :");
	private JLabel lblImpexo = new JLabel("Impo Exonoration (%) :");
	private JLabel lblCategory = new JLabel("Category :");
	private JLabel lblModefact = new JLabel("Mode de facture :");
	private JLabel lblTel = new JLabel("Tel:");
	private JLabel lblEmail = new JLabel("Email:");
	private JLabel lblBanque = new JLabel("Banque:");
	private JLabel lblCompte = new JLabel("N\u00B0 de compte:");
	private JLabel lblNewLabel = new JLabel("Nombre de Clients:");
	private JLabel lblSumClient = new JLabel("");
	private JButton btnAdd = new JButton("Ajouter");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnSave = new JButton("Enregister");
	private JButton btnUpdate = new JButton("Modifier");
	private JButton btnSearch = new JButton("Tous");
	private JButton btnDelete = new JButton("Supprimer");
	private JTable clientGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();
	
	


	private CreateClient() {
		
		setBounds(0, 0, 1350, 799);
		BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
		   Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane.getListeners(MouseMotionListener.class);

		   for (MouseMotionListener listener: motionListeners)
		      northPane.removeMouseMotionListener(listener);
		setClosable(true);
		setResizable(false);
		setIconifiable(false);
		setTitle("Liste des clients");
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(517, 228, 796, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(642, 7, 143, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 625, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setBounds(40, 15, 171, 14);
		Container.add(lblName);
		txtName.setEnabled(false);
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtName.setBounds(142, 8, 525, 28);
		Container.add(txtName);
		txtAdress.setEnabled(false);
		txtAdress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtAdress.setBounds(142, 49, 525, 28);
		Container.add(txtAdress);
		lblRC.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRC.setBounds(40, 92, 65, 21);
		Container.add(lblRC);
		txtRC.setEnabled(false);
		txtRC.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRC.setBounds(142, 88, 198, 28);
		Container.add(txtRC);
		lblMF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMF.setBounds(362, 91, 65, 18);
		Container.add(lblMF);
		txtMF.setEnabled(false);
		txtMF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMF.setBounds(416, 88, 198, 28);
		Container.add(txtMF);
		lblNis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNis.setBounds(629, 88, 59, 28);
		Container.add(lblNis);
		txtNis.setEnabled(false);
		txtNis.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNis.setBounds(698, 88, 216, 28);
		Container.add(txtNis);
		lblArtimpo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtimpo.setBounds(698, 53, 137, 21);
		Container.add(lblArtimpo);
		txtArtimp.setEnabled(false);
		txtArtimp.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtArtimp.setBounds(762, 49, 152, 28);
		Container.add(txtArtimp);
		lblImpexo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblImpexo.setBounds(372, 128, 171, 28);
		Container.add(lblImpexo);
		txtImpexo.setEnabled(false);
		txtImpexo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtImpexo.setBounds(517, 127, 65, 29);
		Container.add(txtImpexo);
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCategory.setBounds(40, 132, 96, 21);
		Container.add(lblCategory);
		txtCategory.setEnabled(false);
		txtCategory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCategory.setBounds(142, 127, 198, 29);
		txtCategory.setModel(new DefaultComboBoxModel(new String[] {"Detail", "Gros", "Entreprise nationale"}) );
		Container.add(txtCategory);
		lblAdress.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAdress.setBounds(40, 56, 112, 14);
		Container.add(lblAdress);
		Container.add(lblAdress);
		lblModefact.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblModefact.setBounds(630, 124, 158, 36);
		Container.add(lblModefact);
		txtModefact.setEnabled(false);
		txtModefact.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtModefact.setBounds(733, 127, 180, 29);
		txtModefact.setModel(new DefaultComboBoxModel(new String[] {"Gros","Detail"}));
		Container.add(txtModefact);
		btnAdd.setSize(126, 30);
		btnAdd.setLocation(193, 179);
		Container.add(btnAdd);
		btnUpdate.setLocation(340, 179);
		btnUpdate.setSize(126, 30);
		Container.add(btnUpdate);
		btnCancel.setLocation(482, 179);
		btnCancel.setEnabled(false);
		btnCancel.setSize(126, 30);
		Container.add(btnCancel);
		btnSave.setEnabled(false);
		btnSave.setLocation(624, 179);
		btnSave.setSize(126, 30);
		Container.add(btnSave);
		scrollPane.setBounds(21, 276, 1292, 431);
		Container.add(scrollPane);
		Object[] column = {"CODE","RAISON SOCIALE","ADRESSE","MF","RC","NIS","ART_IMPO","EXO_IMPO","CATEGORY","MODE DE FACTURE"};
		model.setColumnIdentifiers(column);
		clientGrid.setBackground(Color.WHITE);
		clientGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		clientGrid.getTableHeader().setForeground(Color.WHITE);
		clientGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		clientGrid.setFont(new Font("SansSerif",Font.PLAIN, 14));
		clientGrid.setRowHeight(30);
		clientGrid.setModel(model);
		scrollPane.setViewportView(clientGrid);
		lblNewLabel.setBounds(21, 710, 210, 26);
		Container.add(lblNewLabel);
		lblSumClient.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumClient.setHorizontalAlignment(SwingConstants.LEFT);
		lblSumClient.setBounds(192, 710, 186, 26);
		Container.add(lblSumClient);
		lblTel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTel.setBounds(696, 9, 47, 26);
		Container.add(lblTel);
		txtTel.setEnabled(false);
		txtTel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTel.setBounds(762, 8, 152, 27);
		Container.add(txtTel);
		txtTel.setColumns(10);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(935, 9, 65, 26);
		Container.add(lblEmail);
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtEmail.setEnabled(false);
		txtEmail.setBounds(1028, 8, 285, 28);
		Container.add(txtEmail);
		txtEmail.setColumns(10);
		lblBanque.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBanque.setBounds(935, 50, 65, 26);
		Container.add(lblBanque);
		txtBanque.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBanque.setEnabled(false);
		txtBanque.setBounds(1028, 49, 285, 28);
		Container.add(txtBanque);
		txtBanque.setColumns(10);
		lblCompte.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCompte.setBounds(935, 90, 96, 26);
		Container.add(lblCompte);
		txtCompte.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCompte.setEnabled(false);
		txtCompte.setBounds(1028, 88, 285, 28);
		Container.add(txtCompte);
		txtCompte.setColumns(10);	
		btnDelete.setLocation(771, 179);
		btnDelete.setSize(137, 30);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
	}
	
	public static CreateClient getInstance() {
		if (crClient == null) {
			synchronized (CreateClient.class) {
				if(crClient == null) crClient = new CreateClient();				
			}
		}
		return crClient;
	}
	
	
	public static void setCrClient(CreateClient crClient) {
		CreateClient.crClient = crClient;
	}

	public JTable getClientGrid() {
		return clientGrid;
	}
	public DefaultTableModel getClientGridModel() {
		return model;
	}

	public JPanel getContainer() {
		return Container;
	}
	/*********************** getters for crud btns ***********************/
	public JButton getSavebtn() {
		return btnSave;
	}

	public JButton getCancelbtn() {
		return btnCancel;
	}

	public JButton getUpdatebtn() {
		return btnUpdate;
	}

	public JButton getAddbtn() {
		return btnAdd;
	}
	
	public JButton getDeletebtn() {
		return btnDelete;
	}
	/******************** End getters for crud btns ***********************/
	public String getName() {
		return txtName.getText();
	}

	public String getAdress() {
		return txtAdress.getText();
	}

	public String getMf() {
		return txtMF.getText();
	}

	public String getRc() {
		return txtRC.getText();
	}

	public String getNis() {
		return txtNis.getText();
	}

	public String getArtimp() {
		return txtArtimp.getText();
	}

	public String getImpexo() {
		return txtImpexo.getText();
	}

	public String getCategory() {
		return txtCategory.getSelectedItem().toString();
	}

	public String getModefact() {
		return txtModefact.getSelectedItem().toString();
	}
	
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
	}
	
	public String getLblSumClient() {
		return lblSumClient.getText();
	}
	
	public void setSumClient(int sum) {
		lblSumClient.setText(String.valueOf(sum));
	}
	
	public String getTel() {
		return txtTel.getText();
	}
	
	public String getEmail() {
		return txtEmail.getText();
	}
	
	public String getBanque() {
		return txtBanque.getText();
	}
	
	public String getCompte() {
		return txtCompte.getText();
	}
	
	public void setName (String name) {
		txtName.setText(name);
	}
	
	public void setAddress(String address) {
		txtAdress.setText(address);
	}
	
	public void setTel(String tel) {
		txtTel.setText(tel);
	}
	
	public void setRc(String rc) {
		txtRC.setText(rc);
	}
	
	public void setMf(String mf) {
		txtMF.setText(mf);
	}
	
	public void setNis(String nis) {
		txtNis.setText(nis);
	}
	
	public void setArtimpo(String artImp) {
		txtArtimp.setText(artImp);
	}
	
	public void setImpexo(String impexo) {
		txtImpexo.setText(impexo);
	}
	
	public void setEmail(String email) {
		txtEmail.setText(email);
	}
	
	public void setBanque(String banque) {
		txtBanque.setText(banque);
	}
	
	public void setCompte(String compte) {
		txtCompte.setText(compte);
	} 
	
	public void setCategory(String category) {
		txtCategory.setSelectedItem(category);
	}
	
	public void setModefact(String mode) {
		txtModefact.setSelectedItem(mode);
	}

	/*********************** btn Action Listeners ************************************************/
	public void addingListener(ActionListener addingActionListener) {
		btnAdd.addActionListener(addingActionListener);

	}

	public void SavingListener(ActionListener savingActionListener) {
		btnSave.addActionListener(savingActionListener);

	}

	public void cancelingListener(ActionListener cancelingActionListener) {
		btnCancel.addActionListener(cancelingActionListener);

	}

	public void updatingListener(ActionListener updatingActionListener) {
		btnUpdate.addActionListener(updatingActionListener);

	}
	
	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void searchBtnListener(ActionListener searchingBtnActionListener) {
		btnSearch.addActionListener(searchingBtnActionListener);
	
	}
	
	public void clientGridListener(MouseListener clientGridlistener) {
		clientGrid.addMouseListener(clientGridlistener);
	}
	
	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}
}
