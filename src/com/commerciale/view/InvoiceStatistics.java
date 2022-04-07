package com.commerciale.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.ImageIcon;

public class InvoiceStatistics extends JInternalFrame{
	private static InvoiceStatistics IS = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JTextField txtSumTvaImpo = new JTextField();
	private JTextField txtSumRet = new JTextField();
	private JTextField txtSumDep = new JTextField();
	private JTextField txtSumTVA = new JTextField();
	private JTextField txtGrossSale = new JTextField();;
	private JTextField txtSumMaj = new JTextField();
	private JTextField lblRowCount = new JTextField("");
	private JTextField txtDomic = new JTextField();
	private JLabel lblTvaImpo = new JLabel("Total des TVA IMPO :");
	private JLabel lbltaxRetirement = new JLabel("Total des Tax 2% :");
	private JLabel lblNewLabel = new JLabel("Nombre de facture:");
	private Label lblStartDate_1 = new Label("Recherche par nom de client :");
	private JLabel lblTaxDeDomiciliation = new JLabel("Tax de domiciliation 0,5% :");
	private Label lblEndDate = new Label("jusq'au:");
	private Label lblStartDate = new Label("Du:");
	private JLabel lblTotalDesMajorations = new JLabel("Total des Majorations :");
	private JLabel lblChiffreDaffaire = new JLabel("Chiffre d'affaire :");
	private JLabel lblTotalDesTva = new JLabel("Total des TVA19% :");
	private JLabel lblTotalDesFraits = new JLabel("Total des Fraits :");
	private JButton btnPrint = new JButton("Imprimer");
	private JButton btnSearchByDate = new JButton("");
	private JTable invoiceGrid = new JTable();
	private DefaultTableModel model;
	private DefaultTableModel invoiceModel = new DefaultTableModel();
	private JScrollPane invoicePane = new JScrollPane();
	public static JDateChooser startDate = new JDateChooser();
	private JDateChooser endDate = new JDateChooser();
	private ButtonGroup groupSelect = new ButtonGroup();
	private JButton btnAll = new JButton("Tous");
	
	
	
	private InvoiceStatistics() {
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				IS = null;
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
		setTitle("Liste des Factures");
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(21, 21, 1292, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		txtSearch.setBounds(236, 8, 340, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		startDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startDate.setDateFormatString("dd/MM/yyyy");
		startDate.setBounds(822, 8, 143, 32);
		((JTextField) startDate.getDateEditor()).setEditable(false);
		((JTextField) startDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(startDate);
		endDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		endDate.setDateFormatString("dd/MM/yyyy");
		endDate.setBounds(1049, 8, 141, 32);
		((JTextField) endDate.getDateEditor()).setEditable(false);
		((JTextField) endDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(endDate);
		lblEndDate.setFont(new Font("Dialog", Font.BOLD, 15));
		lblEndDate.setBounds(980, 7, 63, 33);
		searchPanel.add(lblEndDate);
		lblStartDate.setFont(new Font("Dialog", Font.BOLD, 15));
		lblStartDate.setBounds(784, 7, 32, 33);
		searchPanel.add(lblStartDate);
		btnSearchByDate.setIcon(new ImageIcon(InvoiceStatistics.class.getResource("/org/apache/batik/apps/svgbrowser/resources/system-search.png")));
		btnSearchByDate.setBounds(1208, 6, 63, 35);
		searchPanel.add(btnSearchByDate);
		btnAll.setIcon(new ImageIcon(InvoiceStatistics.class.getResource("/org/apache/batik/apps/svgbrowser/resources/system-search.png")));
		btnAll.setBounds(592, 7, 106, 35);
		
		searchPanel.add(btnAll);
		lblStartDate_1.setFont(new Font("Dialog", Font.BOLD, 15));
		lblStartDate_1.setBounds(10, 7, 226, 33);
		
		searchPanel.add(lblStartDate_1);
		lblNewLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		lblNewLabel.setBounds(21, 467, 162, 26);
		Container.add(lblNewLabel);
		lblRowCount.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblRowCount.setEditable(false);
		lblRowCount.setFont(new Font("Arial", Font.PLAIN, 21));
		lblRowCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblRowCount.setBounds(184, 467, 113, 26);
		Container.add(lblRowCount);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		
		invoicePane.setAutoscrolls(true);
		invoicePane.setPreferredSize(new Dimension(1000, 1000));
		
		Container.add(invoicePane);
		String[] columnReception = {"REFERENCE","CLIENT","ETABLI LE:","FRAIS","TVA 19%","TOTAL TTC",
											
											"Maj %","Majoration","TOTAL HT","TAX 2%", "TVA_Imp","TAX_DOMIC"};
		invoiceModel.setColumnIdentifiers(columnReception);
		invoiceGrid.setBackground(Color.WHITE);
		invoiceGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		invoiceGrid.getTableHeader().setForeground(Color.WHITE);
		invoiceGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		invoiceGrid.getTableHeader().setResizingAllowed(true);
		invoiceGrid.getTableHeader().setReorderingAllowed(false);	
		invoiceGrid.setAutoResizeMode(invoicePane.VERTICAL_SCROLLBAR_ALWAYS);
		invoicePane.setBounds(21, 70, 1292, 392);
		
		invoiceGrid.setRowHeight(30);
		invoiceGrid.setModel(invoiceModel);
		invoiceGrid.getColumnModel().getColumn(1).setPreferredWidth(200);
		invoiceGrid.getColumnModel().getColumn(0).setPreferredWidth(60);

		invoiceGrid.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		invoiceGrid.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		invoiceGrid.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		invoiceGrid.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		invoicePane.getVerticalScrollBar();
		invoicePane.setViewportView(invoiceGrid);
	    
		btnPrint.setLocation(1122, 671);
		btnPrint.setSize(191, 47);
		Container.add(btnPrint);
		txtSumTvaImpo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSumTvaImpo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		txtSumTvaImpo.setEditable(false);
		txtSumTvaImpo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumTvaImpo.setBackground(Color.LIGHT_GRAY);
		txtSumTvaImpo.setBounds(1122, 476, 191, 29);
		Container.add(txtSumTvaImpo);
		txtSumTvaImpo.setColumns(10);
		txtSumRet.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSumRet.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		txtSumRet.setEditable(false);
		txtSumRet.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumRet.setBackground(Color.LIGHT_GRAY);
		txtSumRet.setColumns(10);
		txtSumRet.setBounds(1123, 563, 190, 29);
		Container.add(txtSumRet);
		
		
		lblTvaImpo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTvaImpo.setBounds(997, 476, 126, 26);
		Container.add(lblTvaImpo);
	
		lbltaxRetirement.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbltaxRetirement.setBounds(1010, 563, 113, 26);
		Container.add(lbltaxRetirement);
		
		txtSumDep.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSumDep.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSumDep.setEditable(false);
		txtSumDep.setColumns(10);
		txtSumDep.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumDep.setBackground(Color.LIGHT_GRAY);
		txtSumDep.setBounds(1123, 520, 190, 29);
		Container.add(txtSumDep);
		
		lblTotalDesFraits.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalDesFraits.setBounds(1025, 521, 98, 26);
		Container.add(lblTotalDesFraits);
		
		txtSumTVA.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSumTVA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSumTVA.setEditable(false);
		txtSumTVA.setColumns(10);
		txtSumTVA.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumTVA.setBackground(Color.LIGHT_GRAY);
		txtSumTVA.setBounds(766, 476, 190, 29);
		Container.add(txtSumTVA);
		
		lblTotalDesTva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalDesTva.setBounds(647, 476, 124, 26);
		Container.add(lblTotalDesTva);
		
		txtGrossSale.setForeground(Color.BLUE);
		txtGrossSale.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGrossSale.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtGrossSale.setEditable(false);
		txtGrossSale.setColumns(10);
		txtGrossSale.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtGrossSale.setBackground(Color.LIGHT_GRAY);
		txtGrossSale.setBounds(1070, 613, 243, 37);
		Container.add(txtGrossSale);
		
		lblChiffreDaffaire.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblChiffreDaffaire.setBounds(955, 623, 113, 26);
		Container.add(lblChiffreDaffaire);
		
		
		txtSumMaj.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSumMaj.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSumMaj.setEditable(false);
		txtSumMaj.setColumns(10);
		txtSumMaj.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumMaj.setBackground(Color.LIGHT_GRAY);
		txtSumMaj.setBounds(766, 563, 190, 29);
		Container.add(txtSumMaj);
		
		
		lblTotalDesMajorations.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalDesMajorations.setBounds(636, 564, 126, 26);
		Container.add(lblTotalDesMajorations);
		lblTaxDeDomiciliation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTaxDeDomiciliation.setBounds(612, 521, 149, 26);
		
		Container.add(lblTaxDeDomiciliation);
		txtDomic.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDomic.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtDomic.setEditable(false);
		txtDomic.setColumns(10);
		txtDomic.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtDomic.setBackground(Color.LIGHT_GRAY);
		txtDomic.setBounds(766, 520, 190, 29);
		
		Container.add(txtDomic);
		
	}
	
	public static InvoiceStatistics getInstance() {
		if (IS==null) {
			synchronized (InvoiceStatistics.class) {
				if (IS==null) IS = new InvoiceStatistics();
			}
			 }
		return IS;
	}
	
	public static void setIL(InvoiceStatistics iS) {
		IS = iS;
	}

	
	public JTable getInvoiceGrid() {
		return invoiceGrid;
	}
	public DefaultTableModel getProductGridModel() {
		return model;
	}
	
	public DefaultTableModel getInvoiceGridModel() {
		return invoiceModel;
	}


	
	
	/*********************** getters for crud btns ***********************/

	
	
	/******************** End getters for crud btns ***********************/
	public String getTxtSearch() {
		return txtSearch.getText();
	}
	
	public void setTxtSearch(String searchTxt) {
		txtSearch.setText(searchTxt);
	}
	
	
	public void setGridRowCount(int sum) {
		lblRowCount.setText(String.valueOf(sum));
	}

	public Date getStartDate() {
		return startDate.getDate();
	}

	public Date getEndDate() {
		return endDate.getDate();
	}

	public String getTaxRetirement() {
		return txtSumRet.getText();
	}
	
	public void setTaxRetirement(String retirement) {
		txtSumRet.setText(retirement);
	}
	
	public String getTvaImpo() {
		return txtSumTvaImpo.getText();
	}
	
	public void setTvaImpo(String tvaImpo) {
		txtSumTvaImpo.setText(tvaImpo);
	}
	public String getSumTva() {
		return txtSumTVA.getText();
	}
	
	public void setSumTva(String tva) {
		txtSumTVA.setText(tva);
	}
	
	public String getGrosSale() {
		return txtGrossSale.getText();
	}
	
	public void setGrossSale(String gros) {
		txtGrossSale.setText(gros);
	}
	
	public String getSumMaj() {
		return txtSumMaj.getText();
	}
	
	public void setSumMaj(String maj) {
		txtSumMaj.setText(maj);
	}
	
	public String getSumDep() {
		return txtSumDep.getText();
	}
	
	public void setSumDep(String dep) {
		txtSumDep.setText(dep);
	}
	
	public String getDomic() {
		return txtDomic.getText();
	}
	
	public void setDomic(String dom) {
		txtDomic.setText(dom);
	}
	
	/*********************** btn Action Listeners ************************************************/
	
	
	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void invoiceGridListener(MouseListener prdGdListener) {
		invoiceGrid.addMouseListener(prdGdListener);
	}
	
	
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
		  InvoiceStatistics.this.addInternalFrameListener(openingListener);
		}
	
	public void printingbtnListener(MouseListener printingLis) {
		btnPrint.addMouseListener(printingLis);
	}
	
	
	public void searhByDateListener(ActionListener dateChanged) {
		btnSearchByDate.addActionListener(dateChanged);
	}
	
	public void searchingAllInvoices (ActionListener findAll) {
		btnAll.addActionListener(findAll);
	}
}
