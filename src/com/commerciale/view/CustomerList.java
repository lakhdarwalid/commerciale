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

public class CustomerList extends JDialog {

	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JButton btnSearch = new JButton("Tous");
	private JTable CustomerGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();	


	public CustomerList() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(new Color(173, 216, 230));
		setAlwaysOnTop(true);
		setBounds(0, 200, 1350, 494);
		setResizable(false);
		setTitle("Liste des clients");
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
		CustomerGrid.setForeground(Color.BLACK);
		CustomerGrid.setGridColor(new Color(240,240,240));
		CustomerGrid.setBackground(Color.WHITE);
		CustomerGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		CustomerGrid.getTableHeader().setForeground(Color.WHITE);
		CustomerGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		CustomerGrid.setFont(new Font("SansSerif",Font.PLAIN, 14));
		CustomerGrid.setRowHeight(30);
		CustomerGrid.setModel(model);
		scrollPane.setViewportView(CustomerGrid);
	
	}
	public JTable getCustomerGrid() {
		return CustomerGrid;
	}
	public DefaultTableModel getCustomerGridModel() {
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
	
	public void CustomerGridListener(MouseListener customerGridlistener) {
		CustomerGrid.addMouseListener(customerGridlistener);
	}
	
}
