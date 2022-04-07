package com.commerciale.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

import com.commerciale.service.MultiLineCellRenderer;

import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.UIManager;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class ProductList extends JDialog{
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JTextField boxChange = new JTextField(30);
	private JButton btnSearch = new JButton("Tous");
	private JButton btnAddChange = new JButton("+");
	private JTable productGrid = new JTable();
	private DefaultTableModel model;
	private JScrollPane scrollPane = new JScrollPane();
	
	
	public ProductList() {
		setBounds(200, 200, 892, 559);
		setResizable(false);
		setTitle("Liste des articles");
		Container.setBackground(new Color(255, 204, 204));
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBackground(new Color(255, 204, 204));
		searchPanel.setBounds(246, 21, 327, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(244, 7, 78, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 230, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		scrollPane.setBounds(10, 68, 865, 431);
		Container.add(scrollPane);
		String[] column = {"PHOTO","REFERENCE","DESIGNATION","QUANTITE","PRIX UNITAIRE (€)", "PRIX UNITAIRE (DA)"};
		model = new DefaultTableModel(null,column){
			 @Override
	            public Class<?> getColumnClass(int column) {
	                if (column==0) return ImageIcon.class;
	                return Object.class;
	            }
			
		};
		model.setColumnIdentifiers(column);
		productGrid.setBackground(Color.WHITE);
		productGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		productGrid.getTableHeader().setForeground(Color.WHITE);
		productGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		productGrid.setFont(new Font("SansSerif",Font.PLAIN, 20));
		productGrid.setRowHeight(100);
		productGrid.setModel(model);
		scrollPane.setViewportView(productGrid);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		productGrid.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		productGrid.getColumnModel().getColumn(0).setPreferredWidth(5);
		productGrid.removeColumn(productGrid.getColumnModel().getColumn(5));
		productGrid.removeColumn(productGrid.getColumnModel().getColumn(4));
		productGrid.removeColumn(productGrid.getColumnModel().getColumn(3));
		productGrid.getTableHeader().setResizingAllowed(false);
		productGrid.setIntercellSpacing(new Dimension(30,4));
		boxChange.setFont(new Font("Tahoma", Font.BOLD, 20));
		boxChange.setEditable(false);
		Color lightBlue = new Color(173, 216, 230);
		boxChange.setBackground(lightBlue);
		boxChange.setBounds(703, 29, 95, 28);
		Container.add(boxChange);
		btnAddChange.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAddChange.setBounds(810, 27, 65, 30);
		Container.add(btnAddChange);
		
		JLabel lblNewLabel = new JLabel("Taux de change :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(598, 31, 105, 26);
		Container.add(lblNewLabel);
		
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
	
	/*********************** getters for crud btns ***********************/
	
	public JButton getSearchbtn() {
		return btnSearch;
	}
	/******************** End getters for crud btns ***********************/
	
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
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
	
	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void searchBtnListener(ActionListener searchingBtnActionListener) {
		btnSearch.addActionListener(searchingBtnActionListener);
	
	}
	
	public void productGridListener(MouseListener prdGdListener) {
		productGrid.addMouseListener(prdGdListener);
	}
	
	public void addCurrencyValueListener(ActionListener changeListener) {
		btnAddChange.addActionListener(changeListener);
	}
	public void addchange(ActionListener chgListener) {
		btnAddChange.addActionListener(chgListener);
	}
}
