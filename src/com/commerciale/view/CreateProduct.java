package com.commerciale.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.dnd.Autoscroll;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import com.commerciale.service.FormatterFactoryField;
import com.commerciale.service.MultiLineCellRenderer;

import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
import javax.swing.UIManager;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.CellEditorListener;
import javax.swing.event.InternalFrameAdapter;

public class CreateProduct extends JInternalFrame{
	private static CreateProduct CP = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel productGroupPnl = new JPanel();
	private JTextField txtRef = new JTextField(30);
	private JTextField txtDesignation = new JTextField(50);
	private JFormattedTextField txtUnitPrice = new JFormattedTextField();
	private JTextField txtQuantity = new JTextField("0");
	private JTextField txtSearch = new JTextField(50);
	private JTextField boxChange = new JTextField(30);
	private JLabel lblRef = new JLabel("Référence :");
	private JLabel lblDesignation = new JLabel("Désignation :");
	private JLabel lblUnitPrice = new JLabel("Prix unitaire (devise) :");
	private JLabel lblQuantity = new JLabel("Quantité :");
	private JLabel lblNewLabel = new JLabel("Nombre d'articles:");
	private JLabel lblSumProduct = new JLabel("");
	private JLabel lblPoductImg = new JLabel("photo");
	private JLabel lblchange = new JLabel("Taux de change (DA) :");
	private JLabel lblGroup = new JLabel("Famille :");
	private JLabel lblSearchGroup = new JLabel("Recherche par famille :");
	private JLabel lblSearchDesg = new JLabel("Recherche par référence:");
	private JButton btnAdd = new JButton("Ajouter");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnSave = new JButton("Enregister");
	private JButton btnUpdate = new JButton("Modifier");
	private JButton btnSearch = new JButton("Tous");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnAddChange = new JButton("+");
	private JButton btnPrint = new JButton("Imprimer ");
	private JButton btnAddprdgrp = new JButton("+");
	private JButton btnDeleteGroup = new JButton("-");
	private JButton btnXLImport = new JButton("Importer XLS");
	private JTable productGrid = new JTable();
	private DefaultTableModel model;
	private JScrollPane scrollPane = new JScrollPane();
	private JComboBox productGroupBox = new JComboBox();
	private JComboBox comboSearchGroup = new JComboBox();
	
	
			
	private CreateProduct() {
		
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				CP = null;
			}
		});
		setBounds(0, 0, 1350, 799);
		BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
		   Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane.getListeners(MouseMotionListener.class);

		   for (MouseMotionListener listener: motionListeners)
		      northPane.removeMouseMotionListener(listener);
		setClosable(true);
		setResizable(false);
		setIconifiable(false);
		setTitle("Liste des articles");
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(12, 227, 1317, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(1157, 11, 143, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(756, 11, 392, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		comboSearchGroup.setBounds(164, 5, 346, 39);
		comboSearchGroup.setBackground(Color.white);
		searchPanel.add(comboSearchGroup);
		lblSearchGroup.setBounds(15, 14, 144, 29);
		searchPanel.add(lblSearchGroup);
		lblSearchGroup.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSearchDesg.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSearchDesg.setBounds(591, 16, 159, 29);
		
		searchPanel.add(lblSearchDesg);
		lblRef.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRef.setBounds(75, 56, 76, 14);
		Container.add(lblRef);
		txtRef.setEditable(false);
		txtRef.setBackground(Color.white);
		txtRef.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRef.setBounds(149, 49, 180, 28);
		Container.add(txtRef);
		txtDesignation.setEditable(false);
		txtDesignation.setBackground(Color.white);
		txtDesignation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDesignation.setBounds(149, 90, 525, 28);
		Container.add(txtDesignation);
		lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQuantity.setBounds(734, 94, 65, 21);
		Container.add(lblQuantity);
		txtQuantity.setEditable(false);
		txtQuantity.setBackground(Color.white);
		txtQuantity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtQuantity.setBounds(804, 90, 198, 28);
		Container.add(txtQuantity);
		lblUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnitPrice.setBounds(21, 135, 126, 18);
		Container.add(lblUnitPrice);
		txtUnitPrice.setEditable(false);
		txtUnitPrice.setBackground(Color.white);
		txtUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtUnitPrice.setBounds(149, 130, 180, 28);
		txtUnitPrice.setFormatterFactory(new FormatterFactoryField());
		Container.add(txtUnitPrice);
		
		
		lblDesignation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDesignation.setBounds(68, 97, 83, 14);
		Container.add(lblDesignation);
		
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
		String[] column = {"PHOTO","REFERENCE","DESIGNATION","QUANTITE","PRIX UNITAIRE (€)", "PRIX UNITAIRE (DA)"};
		model = new DefaultTableModel(null,column){
			 @Override
	            public Class<?> getColumnClass(int column) {
	                if (column==0) { return ImageIcon.class;}
	               
	                return Object.class;
	            }
			
		};
		model.setColumnIdentifiers(column);
	    productGrid.setBackground(Color.WHITE);
		productGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		productGrid.getTableHeader().setForeground(Color.WHITE);
		productGrid.getTableHeader().setBackground(Color.black);
		//productGrid.getTableHeader().setBackground((Color) UIManager.get("TableHeader.background"));
		productGrid.setFont(new Font("SansSerif",Font.PLAIN, 20));
		productGrid.setRowHeight(100);
		productGrid.setModel(model);
		scrollPane.setViewportView(productGrid);
		lblNewLabel.setBounds(21, 710, 210, 26);
		Container.add(lblNewLabel);
		lblSumProduct.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumProduct.setHorizontalAlignment(SwingConstants.LEFT);
		lblSumProduct.setBounds(192, 710, 186, 26);
		Container.add(lblSumProduct);
	//	productGrid.getColumn("REFERENCE").setCellEditor(new DefaultCellEditor(txtRef));
		btnDelete.setLocation(771, 179);
		btnDelete.setSize(137, 30);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
		btnPrint.setLocation(927, 179);
		btnPrint.setSize(141, 30);
		Container.add(btnPrint);
		
		lblPoductImg.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPoductImg.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPoductImg.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoductImg.setBackground(UIManager.getColor("InternalFrame.activeTitleGradient"));
		lblPoductImg.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		lblPoductImg.setBounds(1089, 8, 224, 197);
		Container.add(lblPoductImg);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		productGrid.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		productGrid.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		productGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		productGrid.getColumnModel().getColumn(0).setPreferredWidth(5);
		productGrid.getTableHeader().setResizingAllowed(true);
		productGrid.getTableHeader().setReorderingAllowed(false);
		productGrid.setIntercellSpacing(new Dimension(30,4));
		Color lightBlue = new Color(173, 216, 230);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(347, 130, 404, 28);
		Container.add(panel);
		panel.setLayout(null);
		lblchange.setBounds(21, 10, 127, 15);
		panel.add(lblchange);
		lblchange.setFont(new Font("Tahoma", Font.PLAIN, 12));
		boxChange.setBounds(152, 0, 174, 28);
		panel.add(boxChange);
		boxChange.setFont(new Font("Tahoma", Font.BOLD, 20));
		boxChange.setBackground(lightBlue);
		boxChange.setEditable(false);
		btnAddChange.setBounds(336, 0, 65, 28);
		panel.add(btnAddChange);
		btnAddChange.setFont(new Font("Tahoma", Font.BOLD, 14));
		productGroupPnl.setBounds(21, 4, 805, 39);
		Container.add(productGroupPnl);
		productGroupPnl.setLayout(null);
		productGroupBox.setEnabled(false);
		productGroupBox.setBounds(127, 3, 525, 32);
		productGroupBox.setBackground(Color.white);
		productGroupPnl.add(productGroupBox);
		btnAddprdgrp.setEnabled(false);
		btnAddprdgrp.setBounds(659, 0, 60, 35);
		productGroupPnl.add(btnAddprdgrp);
		lblGroup.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblGroup.setBounds(75, 11, 49, 14);
		productGroupPnl.add(lblGroup);
		btnDeleteGroup.setEnabled(false);
		btnDeleteGroup.setBounds(732, 0, 63, 35);
		productGroupPnl.add(btnDeleteGroup);
		
		
		btnXLImport.setBounds(880, 8, 157, 30);
		Container.add(btnXLImport);
		/*try {
			UIManager.put("TableHeader.background", Color.BLACK);
			UIManager.put("Table.background", Color.red);
		//	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel(UIManager.getLookAndFeel());
			
			
		} catch (Throwable e1) {}*/
		
	}
	
	public static CreateProduct getInstance() {
		if (CP==null) {
			synchronized (CreateProduct.class) {
				if (CP==null) CP = new CreateProduct();
			}
			 }
		return CP;
	}
	
	public JTable getProductGrid() {
		return productGrid;
	}
	public DefaultTableModel getProductGridModel() {
		return model;
	}

	public JPanel getContainer() {
		return Container;
	}
	
	public JLabel getProductImageLabel() {
		return lblPoductImg;
	}
	
	public void setProductImageLabel(byte[] img) {
		ByteArrayInputStream myImg = new ByteArrayInputStream(img);
		try {
			Image productImg = ImageIO.read(myImg).getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			lblPoductImg.setIcon(new ImageIcon(productImg));
		}catch(IOException e) {JOptionPane.showMessageDialog(null, e.getMessage());}
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
	
	public JButton getSearchbtn() {
		return btnSearch;
	}
	
	public JButton getGroupAddbtn() {
		return btnAddprdgrp;
	}
	
	public JButton getGroupDeletebtn() {
		return btnDeleteGroup;
	}
	
	public JComboBox getcomboGroup() {
		return productGroupBox;
	}
	
	public JComboBox getcomboSearchGroup() {
		return comboSearchGroup;
	}
	/******************** End getters for crud btns ***********************/
	public String getReference() {
		return txtRef.getText();
	}

	public String getDesignation() {
		return txtDesignation.getText();
	}

	public String getUnitPrice() {
		return txtUnitPrice.getText();
	}

	public String getQuantity() {
		return txtQuantity.getText();
	}

	
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
	}
	
	
	public void setSumProduct(int sum) {
		lblSumProduct.setText(String.valueOf(sum));
	}
	
	public void setReference (String ref) {
		txtRef.setText(ref);
	}
	
	public void setDesignation(String desg) {
		txtDesignation.setText(desg);
	}
	
	public void setUnitPrice(String unitPrice) {
		txtUnitPrice.setText(unitPrice);
	}
	
	public void setQuantity(String qte) {
		txtQuantity.setText(qte);
	}
	
	public String getChange() {
		return boxChange.getText() ;
	} 
	
	public void setChange(String change) {
		boxChange.setText(change);
	}
	
	public JButton getBtnAddChange() {
		return btnAddChange;
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
	
	public void productGridListener(MouseListener prdGdListener) {
		productGrid.addMouseListener(prdGdListener);
	}
	
	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}
	
	public void imgProductListener(MouseListener imgListener) {
		lblPoductImg.addMouseListener(imgListener);
	}
	
	public void addCurrencyValueListener(ActionListener changeListener) {
		btnAddChange.addActionListener(changeListener);
	}
	public void addchange(MouseListener chgListener) {
		btnAddChange.addMouseListener(chgListener);
	}
	
	public void printProductList(ActionListener printPrd) {
		btnPrint.addActionListener(printPrd);
	}
	
	public void addnewProducutGroup(ActionListener prdgrp) {
		btnAddprdgrp.addActionListener(prdgrp);
	}
	
	public void deleteProducutGroup(ActionListener prdgrp) {
		btnDeleteGroup.addActionListener(prdgrp);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
		  CreateProduct.this.addInternalFrameListener(openingListener);
	}
	
	public void comboSearchingGroup(ActionListener groupSearch) {
		comboSearchGroup.addActionListener(groupSearch);
	}
	
	public void xlsImportingListener(ActionListener imp) {
		btnXLImport.addActionListener(imp);
	}
}
