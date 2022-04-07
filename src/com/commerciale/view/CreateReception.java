package com.commerciale.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.hibernate.type.LocalDateType;

import com.commerciale.model.Reception;
import com.commerciale.service.MultiLineCellRenderer;
import com.commerciale.service.FormatterFactoryField;
import com.commerciale.service.Tools;
import com.toedter.calendar.JCalendar;

import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.UIManager;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;

import java.awt.BorderLayout;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.event.AncestorListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.toedter.calendar.JDateChooser;
import javax.swing.JFrame;
import javax.swing.JRadioButton;



public class CreateReception extends JInternalFrame {
	
	private static CreateReception CR = null;
	private JPanel Container = new JPanel();
	private JPanel panelDate = new JPanel();
	private JPanel panelChange = new JPanel();
	private JTextField txtD10 = new JTextField(50);
	private JTextField txtDate = new JTextField();
	private JTextField txtRetirement = new JTextField("0,00");
	public static JTextField txtSupName = new JTextField(15);
	public static JTextField txtCodeSup = new JTextField(15);
	public static JTextField boxChange = new JTextField(15);
	public static JTextField txtMontant = new JTextField("0,00");
	public static JTextField txtMontantDa = new JTextField("0,00");
	public static JTextField txtTVA = new JTextField("0,00");
	public static JTextField txtMontantFinal = new JTextField("0,00");
	private JTextField txtTHT = new JTextField("0,00");
	private JFormattedTextField txtTransportation = new JFormattedTextField();
	public static JFormattedTextField txtPrecompteDouane = new JFormattedTextField();
	private JLabel lblRef = new JLabel("R\u00E9f\u00E9rence D10:");
	private JLabel lblUnitPrice = new JLabel("Raison Sociale :");
	private JLabel lblCodeSup = new JLabel("Code Fournisseur :");
	private JLabel lblNewLabel = new JLabel("Nombre d'articles:");
	private JLabel lblDate = new JLabel("Aujourdhuit le :");
	private JLabel lbTotal = new JLabel("Montant en devise :");
	private JLabel lblchange = new JLabel("Taux de change (DA) :");
	private JLabel lblTva = new JLabel("TVA 19% : ");
	private JLabel lblMontantEnDa = new JLabel("Montant en DA :");
	private JLabel lblPrcompteDouane = new JLabel("Pr\u00E9compte Douane :");
	private JLabel lblMontantFinale = new JLabel("T.T.C : ");
	private JLabel lblTvaDiff = new JLabel("Frais de Transport : ");
	private JLabel lblTHT = new JLabel("Total HT: ");
	public static JLabel lblSumProduct = new JLabel("");
	private JLabel lblRetirement = new JLabel("Tax 2% : ");
	private JLabel lblTransportation = new JLabel("");
	private JLabel lblSumTransDev = new JLabel("");
	
	private JButton btnAddProduct = new JButton("Ajouter un article");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnSave = new JButton("Finaliser le Bon");
	private JButton btnDelete = new JButton("Supprimer un article");
	private JButton btnAddChange = new JButton("+");
	private JButton btnAddSupplier = new JButton("Ajouter un fournisseur");
	private JButton btnCalcTransport = new JButton("=");
	public static JTable productGrid = new JTable();
	public static DefaultTableModel productGridModel;
	private JScrollPane scrollPane = new JScrollPane();	
	
	private JDateChooser textDateRec = new JDateChooser();
	private JRadioButton rdbtnDa = new JRadioButton("DA");
	private JRadioButton rdbtnDevise = new JRadioButton("Devise");
	private ButtonGroup groupSelect = new ButtonGroup();
	
	
	
	

	private CreateReception() {
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				CR=null;
			}
		});
		BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
		   Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane.getListeners(MouseMotionListener.class);

		   for (MouseMotionListener listener: motionListeners)
		      northPane.removeMouseMotionListener(listener);
		setBounds(0, 0, 1350, 799);
		setClosable(true);
		setResizable(false);
		setIconifiable(false);
		setTitle("Nouveau Bon de Reception");
		getContentPane().add(Container);
		Container.setLayout(null);
		txtD10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtD10.setBounds(142, 49, 126, 28);
		Container.add(txtD10);
		lblCodeSup.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCodeSup.setBounds(40, 92, 126, 21);
		Container.add(lblCodeSup);
		txtCodeSup.setBackground(Color.WHITE);
		txtCodeSup.setEditable(false);
		txtCodeSup.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCodeSup.setBounds(142, 88, 126, 28);
		Container.add(txtCodeSup);
		lblUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnitPrice.setBounds(385, 93, 126, 18);
		Container.add(lblUnitPrice);
		txtSupName.setBackground(Color.WHITE);
		txtSupName.setEditable(false);
		txtSupName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSupName.setBounds(487, 88, 400, 28);
		Container.add(txtSupName);
		lblRef.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRef.setBounds(40, 56, 112, 14);
		Container.add(lblRef);
		btnAddProduct.setSize(210, 30);
		btnAddProduct.setLocation(21, 137);
		Container.add(btnAddProduct);
		btnCancel.setLocation(922, 672);
		btnCancel.setSize(142, 54);
		Container.add(btnCancel);
		//btnSave.setEnabled(false);
		btnSave.setLocation(1085, 672);
		btnSave.setSize(228, 54);
		Container.add(btnSave);
		scrollPane.setBounds(21, 179, 1292, 337);
		Container.add(scrollPane);
		String[] column = { "PHOTO", "REFERENCE", "DESIGNATION", "QUANTITE", "PRIX UNITAIRE (€)", "PRIX UNITAIRE (DA)",
				"TOTAL(DVISE)", "TOTAL(DA)" };
		productGridModel = new DefaultTableModel(null, column) {
			@Override
			public Class<?> getColumnClass(int column) {
				if (column == 0)
					return ImageIcon.class;
				if (column ==2)
					return TextArea.class;
				return Object.class;
			}

		};
		productGridModel.setColumnIdentifiers(column);
		productGrid.setBackground(Color.WHITE);
		productGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		productGrid.getTableHeader().setForeground(Color.WHITE);
		productGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		productGrid.setFont(new Font("SansSerif", Font.PLAIN, 20));
		productGrid.setRowHeight(100);
		productGrid.setModel(productGridModel);
		scrollPane.setViewportView(productGrid);
		lblNewLabel.setBounds(21, 533, 210, 26);
		Container.add(lblNewLabel);
		lblSumProduct.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumProduct.setHorizontalAlignment(SwingConstants.LEFT);
		lblSumProduct.setBounds(200, 533, 126, 26);
		Container.add(lblSumProduct);
		btnDelete.setLocation(252, 137);
		btnDelete.setSize(232, 30);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		productGrid.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		productGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(0).setPreferredWidth(5);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		productGrid.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		productGrid.getTableHeader().setResizingAllowed(false);
		productGrid.setIntercellSpacing(new Dimension(30, 4));
		Color lightBlue = new Color(173, 216, 230);

		JLabel lblDateRec = new JLabel("Etabli le :");
		lblDateRec.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDateRec.setBounds(385, 56, 112, 14);
		Container.add(lblDateRec);
		textDateRec.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textDateRec.setDateFormatString("dd/MM/yyyy");

		// textDateRec.setFont(new Font("Tahoma", Font.PLAIN, 14));
		// textDateRec.setEnabled(false);
		textDateRec.setBounds(487, 49, 126, 28);
		Container.add(textDateRec);
		lbTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbTotal.setBounds(357, 545, 123, 14);

		Container.add(lbTotal);
		txtMontant.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMontant.setBackground(Color.WHITE);
		txtMontant.setEditable(false);
		txtMontant.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMontant.setBounds(475, 537, 172, 28);
		Container.add(txtMontant);
		lblMontantEnDa.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMontantEnDa.setBounds(675, 545, 103, 14);

		Container.add(lblMontantEnDa);
		txtMontantDa.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMontantDa.setBackground(Color.WHITE);
		txtMontantDa.setEditable(false);
		txtMontantDa.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMontantDa.setBounds(779, 537, 180, 28);
		Container.add(txtMontantDa);
		lblTva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTva.setBounds(1019, 586, 83, 21);

		Container.add(lblTva);
		txtTVA.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTVA.setBackground(Color.WHITE);
		txtTVA.setEditable(false);
		txtTVA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTVA.setBounds(1103, 580, 210, 28);
		Container.add(txtTVA);
		lblPrcompteDouane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrcompteDouane.setBounds(653, 590, 128, 14);

		Container.add(lblPrcompteDouane);
		txtPrecompteDouane.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPrecompteDouane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPrecompteDouane.setBounds(779, 580, 180, 28);
		txtPrecompteDouane.setFormatterFactory(new FormatterFactoryField() );
		Container.add(txtPrecompteDouane);
		lblMontantFinale.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMontantFinale.setBounds(1041, 630, 57, 21);

		Container.add(lblMontantFinale);
		txtMontantFinal.setForeground(Color.RED);
		txtMontantFinal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMontantFinal.setBackground(Color.WHITE);
		txtMontantFinal.setEditable(false);
		txtMontantFinal.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtMontantFinal.setBounds(1103, 623, 210, 40);
		Container.add(txtMontantFinal);
		btnAddSupplier.setBounds(908, 82, 277, 35);
		Container.add(btnAddSupplier);
		panelDate.setBorder(null);
		panelDate.setBounds(360, 10, 253, 35);
		Container.add(panelDate);
		panelDate.setLayout(null);
		txtDate.setBounds(123, 0, 126, 28);
		panelDate.add(txtDate);
		txtDate.setText(Tools.dateToString(new Date()));
		txtDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDate.setEditable(false);
		txtDate.setBackground(Color.white);
		lblDate.setBounds(21, 7, 171, 14);
		panelDate.add(lblDate);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelChange.setBorder(null);
		panelChange.setBounds(929, 143, 394, 34);
		Container.add(panelChange);
		panelChange.setLayout(null);
		lblchange.setBounds(0, 0, 142, 26);
		panelChange.add(lblchange);
		lblchange.setFont(new Font("Tahoma", Font.PLAIN, 12));
		boxChange.setBounds(132, 0, 180, 28);
		panelChange.add(boxChange);
		boxChange.setFont(new Font("Tahoma", Font.BOLD, 20));
		boxChange.setEditable(false);
		boxChange.setBackground(lightBlue);
		btnAddChange.setBounds(322, 0, 65, 28);
		panelChange.add(btnAddChange);
		btnAddChange.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		lblTvaDiff.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTvaDiff.setBounds(21, 629, 114, 21);
		Container.add(lblTvaDiff);
		
		txtTransportation.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTransportation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTransportation.setEditable(false);
		txtTransportation.setBackground(Color.WHITE);
		txtTransportation.setBounds(303, 626, 210, 28);
		txtTransportation.setFormatterFactory(new FormatterFactoryField());
		Container.add(txtTransportation);
		
		lblRetirement.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRetirement.setBounds(716, 631, 57, 21);
		Container.add(lblRetirement);

		txtRetirement.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRetirement.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRetirement.setEditable(false);
		txtRetirement.setBackground(Color.WHITE);
		txtRetirement.setBounds(780, 625, 179, 28);
		Container.add(txtRetirement);
		
		
		rdbtnDa.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDa.setBounds(142, 622, 65, 35);
		rdbtnDa.setActionCommand("DA");
		Container.add(rdbtnDa);
		
		rdbtnDevise.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDevise.setBounds(209, 622, 90, 35);
		rdbtnDevise.setActionCommand("DEVISE");
		Container.add(rdbtnDevise);
		groupSelect.add(rdbtnDa);
		groupSelect.add(rdbtnDevise);
		
		
		btnCalcTransport.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCalcTransport.setBounds(520, 624, 49, 32);
		Container.add(btnCalcTransport);
		
		txtTHT.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTHT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTHT.setEditable(false);
		txtTHT.setBackground(Color.WHITE);
		txtTHT.setBounds(1103, 537, 210, 28);
		Container.add(txtTHT);
		
		lblTHT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTHT.setBounds(1029, 543, 63, 21);
		Container.add(lblTHT);
	
		lblTransportation.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransportation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTransportation.setBounds(313, 664, 184, 28);
		Container.add(lblTransportation);
	
		lblSumTransDev.setHorizontalAlignment(SwingConstants.CENTER);
		lblSumTransDev.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSumTransDev.setBounds(475, 575, 172, 21);
		Container.add(lblSumTransDev);
		
		JLabel lblMontantEnDevise = new JLabel("Montant en devise + frais de Transpot :");
		lblMontantEnDevise.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMontantEnDevise.setBounds(250, 575, 228, 21);
		Container.add(lblMontantEnDevise);
	}

	public static CreateReception getInstance() {
		if (CR==null) {
			synchronized (CreateReception.class) {
				if (CR==null) CR= new CreateReception();
			}
		}
		return CR;
	}
	
	public JTable getProductGrid() {
		return productGrid;
	}

	public DefaultTableModel getProductGridModel() {
		return productGridModel;
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

	public JButton getAddbtn() {
		return btnAddProduct;
	}

	public JButton getDeletebtn() {
		return btnDelete;
	}
	
	public JButton getBtnAddChange() {
		return btnAddChange;
	}
	
	public JFormattedTextField getTxtTransportation() {
		return txtTransportation;
	}

	/******************** End getters for crud btns ***********************/
	public String getReference() {
		return txtD10.getText();
	}
	public void setReference(String ref) {
		txtD10.setText(ref);
	}

	public void setSumProduct(int sum) {
		lblSumProduct.setText(String.valueOf(sum));
	}
	
	public String getSupId() {
		return txtCodeSup.getText();
	}

	public void setSupId(String supId) {
		txtCodeSup.setText(supId);
	}

	public String getChange() {
		return boxChange.getText();
	}

	public void setChange(String change) {
		boxChange.setText(change);
	}

	public String getSupName() {
		return txtSupName.getText();
	}

	public void setSupName(String SupName) {
		this.txtSupName.setText(SupName);
	}
	
	public String getTodayDate() {
		return txtDate.getText();
	}
	
	public void setTodayDate(String todayDate) {
		txtDate.setText(todayDate);
	}
	
	public Date getRecDate() {
		return textDateRec.getDate();
	}
	
	public void setRecDate(Date recDate) {
		textDateRec.setDate(recDate);
	}
	
	public String getTotalDevise() {
		return txtMontant.getText();
	}
	
	public void setTotalDevise(String totalDevise) {
		txtMontant.setText(totalDevise);
	}
	
	public String getTotalDa() {
		return txtMontantDa.getText();
	}
	
	public void setTotalDa(String totalDa) {
		txtMontantDa.setText(totalDa);
	}
	
	public String getPrecompteDouane() {
		return txtPrecompteDouane.getText();
	}
	
	public void setPrecompteDouane(String precompteDouane) {
		txtPrecompteDouane.setText(precompteDouane);
	}
	
	public String getTVA() {
		return txtTVA.getText();
	}

	public void setTVA(String tva) {
		txtTVA.setText(tva);
	}

	public String getMontantFinal() {
		return txtMontantFinal.getText();
	}

	public void setMontantFinal(String montantFinal) {
		txtMontantFinal.setText(montantFinal);
	}
	
	public String getTransportation() {
		return txtTransportation.getText();
	}

	public void setTransportation(String transpor) {
		txtTransportation.setText(transpor);
	}
	
	public String getRetirement() {
		return txtRetirement.getText();
	}

	public void setRetirement(String retirement) {
		txtRetirement.setText(retirement);
	}
	
	public String getTHT() {
		return txtTHT.getText();
	}

	public void setTHT(String tht) {
		txtTHT.setText(tht);
	}
	
	public void setTranspAmount(String transpAmount) {
		lblTransportation.setText(transpAmount);
	}
	
	public String getTranspAmount() {
		return lblTransportation.getText();
	}
	
	public void setSumTransDev(String sumTrans) {
		lblSumTransDev.setText(sumTrans);
	}
	
	/***********************
	 * btn Action Listeners
	 ************************************************/
	public void addingProductListener(ActionListener addingActionListener) {
		btnAddProduct.addActionListener(addingActionListener);

	}

	public void SavingListener(ActionListener savingActionListener) {
		btnSave.addActionListener(savingActionListener);

	}

	public void cancelingListener(ActionListener cancelingActionListener) {
		btnCancel.addActionListener(cancelingActionListener);

	}

	public void productGridListener(MouseListener prdGdListener) {
		productGrid.addMouseListener(prdGdListener);
	}

	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}

	public void addCurrencyValueListener(ActionListener changeListener) {
		btnAddChange.addActionListener(changeListener);
	}

	public void addSupplier(ActionListener addSup) {
		btnAddSupplier.addActionListener(addSup);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
	  CreateReception.this.addInternalFrameListener(openingListener);
	}
	
	public void fraitRdbtnSwitch (ActionListener rdlistn) {
		rdbtnDa.addActionListener(rdlistn);
		rdbtnDevise.addActionListener(rdlistn);
	}
	
	public void calculateAfterTransportation(ActionListener trans) {
		btnCalcTransport.addActionListener(trans);
	}
}
