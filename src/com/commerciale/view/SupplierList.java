package com.commerciale.view;

import javax.swing.table.DefaultTableModel;

import com.commerciale.model.Supplier;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Window.Type;

public class SupplierList extends JDialog {

	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JButton btnSearch = new JButton("Tous");
	private JTable supplierGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();	


	public SupplierList() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(new Color(173, 216, 230));
		setAlwaysOnTop(true);
		setBounds(0, 200, 1350, 494);
		setResizable(false);
		setTitle("Liste des fournisseurs");
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(239, 21, 796, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(642, 7, 143, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 625, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		Container.setBackground(new Color(240,240,240));
		scrollPane.setBackground(new Color(47,141,255));
		scrollPane.setBounds(21, 73, 1312, 359);
		Container.add(scrollPane);
		Object[] column = {"CODE","RAISON SOCIALE","ADRESSE","MF","RC","NIS","ART_IMPO"};
		model.setColumnIdentifiers(column);
		supplierGrid.setForeground(Color.BLACK);
		supplierGrid.setGridColor(new Color(240,240,240));
		supplierGrid.setBackground(Color.WHITE);
		supplierGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		supplierGrid.getTableHeader().setForeground(Color.WHITE);
		supplierGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		supplierGrid.setFont(new Font("SansSerif",Font.PLAIN, 14));
		supplierGrid.setRowHeight(30);
		supplierGrid.setModel(model);
		scrollPane.setViewportView(supplierGrid);
	
	}
	public JTable getSupplierGrid() {
		return supplierGrid;
	}
	public DefaultTableModel getSupplierGridModel() {
		return model;
	}

	public JPanel getContainer() {
		return Container;
	}
	
	
	/*********************** getters for crud btns ***********************/
	
	
		
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
	}
	
	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void searchBtnListener(ActionListener searchingBtnActionListener) {
		btnSearch.addActionListener(searchingBtnActionListener);
	
	}
	
	public void supplierGridListener(MouseListener supplierGridlistener) {
		supplierGrid.addMouseListener(supplierGridlistener);
	}
	
}
