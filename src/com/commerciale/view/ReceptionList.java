package com.commerciale.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.EventListener;

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
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.UIManager;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import java.awt.Label;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.border.BevelBorder;

public class ReceptionList extends JInternalFrame{
	private static ReceptionList RL = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JLabel lblNewLabel = new JLabel("Nombre d'articles:");
	private JLabel lblSumProduct = new JLabel("");
	private Label lblEndDate = new Label("et:");
	private Label lblStartDate = new Label("P\u00E9riode entre le :");
	private JButton btnAdd = new JButton("Ajouter");
	private JButton btnUpdate = new JButton("Modifier");
	private JButton btnSearch = new JButton("Tous");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnPrint = new JButton("Imprimer");
	private JButton btnSearchByDate = new JButton(">>");
	private JTable productGrid = new JTable();
	private JTable receptionGrid = new JTable();
	private DefaultTableModel model;
	private DefaultTableModel receptionModel = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane receptionPane = new JScrollPane();
	public static JDateChooser startDate = new JDateChooser();
	private JDateChooser endDate = new JDateChooser();
	private JTextField txtRetirement = new JTextField();
	private JTextField txtTransportation = new JTextField();
	private final JLabel lblNewLabel_1 = new JLabel("Tax 2% :");
	private final JLabel lblNewLabel_1_1 = new JLabel("Frais de Transport :");
	
	
	
	private ReceptionList() {
		txtRetirement.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRetirement.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRetirement.setBackground(Color.LIGHT_GRAY);
		txtRetirement.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtRetirement.setEditable(false);
		txtRetirement.setBounds(1101, 711, 164, 30);
		txtRetirement.setColumns(10);
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				RL = null;
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
		setTitle("Liste des Bons de Reception");
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(21, 21, 1292, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(553, 7, 91, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 522, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		startDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startDate.setDateFormatString("dd/MM/yyyy");
		startDate.setBounds(850, 8, 143, 32);
		((JTextField) startDate.getDateEditor()).setEditable(false);
		((JTextField) startDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(startDate);
		endDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		endDate.setDateFormatString("dd/MM/yyyy");
		endDate.setBounds(1049, 8, 141, 32);
		((JTextField) endDate.getDateEditor()).setEditable(false);
		((JTextField) endDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(endDate);
		lblEndDate.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblEndDate.setBounds(1017, 7, 30, 33);
		searchPanel.add(lblEndDate);
		lblStartDate.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblStartDate.setBounds(701, 7, 143, 33);
		searchPanel.add(lblStartDate);
		btnSearchByDate.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnSearchByDate.setBounds(1208, 6, 63, 35);
		searchPanel.add(btnSearchByDate);
		btnAdd.setSize(126, 30);
		btnAdd.setLocation(310, 357);
		Container.add(btnAdd);
		btnUpdate.setLocation(457, 357);
		btnUpdate.setSize(126, 30);
		Container.add(btnUpdate);
		scrollPane.setBounds(21, 408, 1292, 300);
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
		lblNewLabel.setBounds(21, 710, 210, 26);
		Container.add(lblNewLabel);
		lblSumProduct.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumProduct.setHorizontalAlignment(SwingConstants.LEFT);
		lblSumProduct.setBounds(192, 710, 186, 26);
		Container.add(lblSumProduct);
		btnDelete.setLocation(604, 357);
		btnDelete.setSize(137, 30);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		productGrid.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		productGrid.getColumnModel().getColumn(0).setPreferredWidth(5);
		productGrid.getTableHeader().setResizingAllowed(true);
		productGrid.getTableHeader().setReorderingAllowed(false);
		productGrid.setIntercellSpacing(new Dimension(30,4));
		Container.add(receptionPane);
		String[] columnReception = {"N°","REFERENCE","FOURNISSEUR","REÇU LE:","ETABLI LE:","MONTANT(€) ",
									"MONTANT(DA)","T/CHANGE","PRECOMPTE DOUANE","T.H.T","TVA 19%","T.T.C","CODE_FOURN","retirement",
									"frais de Transport"};
		receptionModel.setColumnIdentifiers(columnReception);
		receptionGrid.setBackground(Color.WHITE);
		receptionGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		receptionGrid.getTableHeader().setForeground(Color.WHITE);
		receptionGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		receptionGrid.getTableHeader().setResizingAllowed(true);
		receptionGrid.getTableHeader().setReorderingAllowed(false);		
		receptionGrid.setFont(new Font("SansSerif",Font.PLAIN, 13));
		receptionPane.setBounds(21, 70, 1292, 266);
		receptionGrid.setRowHeight(30);
		receptionGrid.setModel(receptionModel);
		receptionGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
		receptionGrid.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
		receptionGrid.removeColumn(receptionGrid.getColumnModel().getColumn(14));
		receptionGrid.removeColumn(receptionGrid.getColumnModel().getColumn(13));
		receptionGrid.removeColumn(receptionGrid.getColumnModel().getColumn(12));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		receptionGrid.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		receptionGrid.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		receptionGrid.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		DefaultTableCellRenderer coloredText = new DefaultTableCellRenderer() {
			@Override
			public void setForeground(Color c) {
				setHorizontalAlignment(JLabel.RIGHT);
				c= Color.blue;
				super.setForeground(c);
			}
			@Override
			public void setFont(Font font) {
				Font myFont = new Font("SansSerif", Font.BOLD, 15);
				super.setFont(myFont);
			}
		};
		receptionGrid.getColumnModel().getColumn(11).setCellRenderer(coloredText);
		receptionPane.setViewportView(receptionGrid);
		btnPrint.setLocation(762, 357);
		btnPrint.setSize(126, 30);
		Container.add(btnPrint);
		
		Container.add(txtRetirement);
		txtTransportation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTransportation.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTransportation.setEditable(false);
		txtTransportation.setColumns(10);
		txtTransportation.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtTransportation.setBackground(Color.LIGHT_GRAY);
		txtTransportation.setBounds(825, 710, 170, 30);
		
		Container.add(txtTransportation);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(1025, 713, 74, 26);
		
		Container.add(lblNewLabel_1);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(679, 712, 146, 26);
		
		Container.add(lblNewLabel_1_1);
	}
	
	public static ReceptionList getInstance() {
		if (RL==null) {
			synchronized (ReceptionList.class) {
				if (RL==null) RL = new ReceptionList();
			}
			 }
		return RL;
	}
	
	public JTable getproductGrid() {
		return productGrid;
	}
	
	public JTable getReceptionGrid() {
		return receptionGrid;
	}
	public DefaultTableModel getProductGridModel() {
		return model;
	}
	
	public DefaultTableModel getReceptionGridModel() {
		return receptionModel;
	}

	public JPanel getContainer() {
		return Container;
	}
	
	
	
	
	/*********************** getters for crud btns ***********************/
	
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
	/******************** End getters for crud btns ***********************/
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
	}
	
	
	public void setSumProduct(int sum) {
		lblSumProduct.setText(String.valueOf(sum));
	}

	public Date getStartDate() {
		return startDate.getDate();
	}

	public Date getEndDate() {
		return endDate.getDate();
	}
	
	public String getTRetirement() {
		return txtRetirement.getText();
	}
	
	public void setRetirement(String retirement) {
		txtRetirement.setText(retirement);
	}
	
	public String getTransport() {
		return txtTransportation.getText();
	}
	
	public void setTransport(String trans) {
		txtTransportation.setText(trans);
	}

	/*********************** btn Action Listeners ************************************************/
	public void addingReceptionListener(ActionListener addingActionListener) {
		btnAdd.addActionListener(addingActionListener);

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
	
	public void receptionGridListener(MouseListener prdGdListener) {
		receptionGrid.addMouseListener(prdGdListener);
	}
	
	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
		  ReceptionList.this.addInternalFrameListener(openingListener);
		}
	
	public void printingbtnListener(MouseListener printingLis) {
		btnPrint.addMouseListener(printingLis);
	}
	
	public void searhByDateListener(ActionListener dateChanged) {
		btnSearchByDate.addActionListener(dateChanged);
	}
}
