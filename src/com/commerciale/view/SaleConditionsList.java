package com.commerciale.view;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableModel;
import javax.sql.rowset.spi.SyncResolver;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class SaleConditionsList extends JInternalFrame{
	private static SaleConditionsList SCL =null;
	private JTable saleCondGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private JTextField txtTitle =  new JTextField();
	private JLabel lblNewCond = new JLabel("Ajoutez une condition de vente : ");
	private JLabel lblTitle = new JLabel("Titre : ");
	private JScrollPane scrollPane = new JScrollPane();
	private JButton btnSave = new JButton("Enregistrer");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnUpdate = new JButton("Modifier");
	
	
	
	private SaleConditionsList() {
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				SCL =null;
			}
		});
		setBounds(350, 150, 650, 526);
		setClosable(true);
		setIconifiable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Conditions de vente");
		getContentPane().setLayout(null);
		
		
		scrollPane.setBounds(21, 21, 600, 306);
		getContentPane().add(scrollPane);
		Object[] columnNames = {"CONDITIONS DE VENTE", "code"};
		model.setColumnIdentifiers(columnNames);
		saleCondGrid.setModel(model);
		saleCondGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		saleCondGrid.getTableHeader().setBackground(Color.BLACK);
		saleCondGrid.getTableHeader().setForeground(Color.white);
		saleCondGrid.getTableHeader().setReorderingAllowed(false);
		saleCondGrid.setFont(new Font("SansSerif",Font.PLAIN, 14));
		saleCondGrid.setRowHeight(30);
		scrollPane.setViewportView(saleCondGrid);
		saleCondGrid.removeColumn(saleCondGrid.getColumnModel().getColumn(1));
		
		
		lblNewCond.setBounds(21, 339, 318, 26);
		getContentPane().add(lblNewCond);
		
		
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle.setBounds(21, 373, 59, 26);
		getContentPane().add(lblTitle);
		
		
		txtTitle.setBounds(68, 368, 553, 32);
		getContentPane().add(txtTitle);
		txtTitle.setColumns(10);
		
		
		btnSave.setBounds(480, 420, 141, 35);
		getContentPane().add(btnSave);
		
		
		btnCancel.setBounds(325, 420, 141, 35);
		getContentPane().add(btnCancel);
		
		
		btnDelete.setBounds(170, 420, 141, 35);
		getContentPane().add(btnDelete);
		
		btnUpdate.setBounds(21, 420, 141, 35);
		getContentPane().add(btnUpdate);
	}
	
	public static SaleConditionsList getInstance() {
		if (SCL == null) {
			synchronized (SaleConditionsList.class) {
				if(SCL == null) SCL = new SaleConditionsList();				
			}
		}
		return SCL;
	}
	
	
	/**************Components SETTERS and GETTERS ************/
	public DefaultTableModel getSaleCondModel() {
		return model;
	}
	
	public JTable getSaleCondGrid() {
		return saleCondGrid;
	}
	
	
	/************END OF components SETTERS and GETTERS ********/
    public String getCondTitle() {
		return txtTitle.getText();
    }
	
	public void setCondTitle(String title) {
		txtTitle.setText(title);
	}
	
	public void savingActionListener(ActionListener save) {
		btnSave.addActionListener(save);
	}
	
	public void deletingActionListener(ActionListener delete) {
		btnDelete.addActionListener(delete);
	}
	
	public void cancelingActionListener(ActionListener cancel) {
		btnCancel.addActionListener(cancel);
	}
	
	public void gridNavigMouseListener(MouseListener nav) {
		saleCondGrid.addMouseListener(nav);
	}
	
	public void onOpeningBehaviorListener(InternalFrameListener open) {
		this.addInternalFrameListener(open);
	}
	
	public void updatingActionListener(ActionListener upd) {
		btnUpdate.addActionListener(upd);
	}
}
