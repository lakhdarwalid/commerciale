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

public class ClientList extends JDialog {
	private static ClientList clientL = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JButton btnSearch = new JButton("Tous");
	private JButton btnNewCus = new JButton("Nouveau");
	private JTable customerGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();	


	private ClientList() {
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBackground(new Color(255, 204, 204));
		setAlwaysOnTop(true);
		setResizable(false);
		setBounds(200, 200, 830, 494);
		setResizable(false);
		setTitle("Liste des fournisseurs");
		getContentPane().setBackground(new Color(255, 204, 204));
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(10, 21, 809, 47);
		searchPanel.setBackground(new Color(255, 204, 204));
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(657, 7, 141, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 626, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		Container.setBackground(new Color(255, 204, 204));
		scrollPane.setBackground(new Color(47,141,255));
		scrollPane.setBounds(21, 73, 785, 359);
		Container.add(scrollPane);
		Object[] column = {"RAISON SOCIALE","ADRESSE","CODE"};
		model.setColumnIdentifiers(column);
		customerGrid.setForeground(Color.BLACK);
		customerGrid.setGridColor(new Color(240,240,240));
		customerGrid.setBackground(Color.WHITE);
		customerGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		customerGrid.getTableHeader().setForeground(Color.WHITE);
		customerGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		customerGrid.setFont(new Font("SansSerif",Font.PLAIN, 14));
		customerGrid.setRowHeight(30);
		customerGrid.setModel(model);
		customerGrid.removeColumn(customerGrid.getColumnModel().getColumn(2));
		scrollPane.setViewportView(customerGrid);
		btnNewCus.setVisible(false);
		btnNewCus.setBounds(603, 0, 136, 33);
		Container.add(btnNewCus);
	
	}
	
	public static ClientList getInstance() {
		if (clientL == null) {
			synchronized (ClientList.class) {
				if(clientL == null) clientL = new ClientList();				
			}
		}
		return clientL;
	}
	
	public JTable getCustomerGrid() {
		return customerGrid;
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
	
	public void customerGridListener(MouseListener customerGridlistener) {
		customerGrid.addMouseListener(customerGridlistener);
	}
	
	public void addNewCustListener (ActionListener cust) {
		btnNewCus.addActionListener(cust);
	}
}
