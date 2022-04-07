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
import java.util.concurrent.CancellationException;

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
import javax.swing.JRadioButton;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;

public class InvoiceList extends JInternalFrame{
	private static InvoiceList IL = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JTextField txtTvaImpo = new JTextField();
	private JTextField txtRetirement = new JTextField();
	private JTextField txtTransp = new JTextField();
	private JTextField lblSumProduct = new JTextField("");
	private JTextField txtSumInvoices = new JTextField("");
	private JTextField txtDomicil = new JTextField();
	private JTextField txtBancTax = new JTextField();
	private JTextField txtMagTrans = new JTextField();
	private JTextField txtProject = new JTextField();
	private JTextField txtSuperficie = new JTextField();
	private JLabel lblTvaImpo = new JLabel("TVA IMPO :");
	private JLabel lbltaxRetirement = new JLabel("Tax 2% :");
	private JLabel lblNewLabel = new JLabel("Nombre d'articles:");
	private Label lblEndDate = new Label("jusq'au:");
	private Label lblStartDate = new Label("Du:");
	private JLabel lblTransp = new JLabel("Frais de transport :");
	private Label lblSearchCust = new Label("Recherche par nom de client :");
	private JLabel lblNombreDeFactures = new JLabel("Nombre de factures:");
	private JLabel lblDomicil = new JLabel("TAX DE DOMIC(0.05%) :");
	private JLabel lblBancTax = new JLabel("Tax de Banque (0.01%) : ");
	private JLabel lblMagTrans = new JLabel("Magazinage et honoraire de transitaire :");
	private JLabel lblProject = new JLabel("Projet :");
	private JLabel lblSuperficie = new JLabel("Superficie :");
	private JButton btnAdd = new JButton("Ajouter");
	private JButton btnUpdate = new JButton("Modifier");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnPrint = new JButton("Imprimer");
	private JButton btnSearchByDate = new JButton("");
	private JButton btnReceptioner = new JButton("Receptioner");
	private JButton btnPrintNoImg = new JButton("Impr/sans image");
	private JButton btnPrintNoPrice = new JButton("impr/sans prix");
	private JButton btnFinal = new JButton("Finaliser");
	private JButton btnAllInv = new JButton("Tous");
	private JButton btnXls = new JButton("");
	private JButton btnCopy = new JButton("Copier la facture");
	private JTable productGrid = new JTable();
	private JTable invoiceGrid = new JTable();
	private DefaultTableModel model;
	private DefaultTableModel invoiceModel = new DefaultTableModel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane invoicePane = new JScrollPane();
	public static JDateChooser startDate = new JDateChooser();
	private JDateChooser endDate = new JDateChooser();
	private final JLabel lblM = new JLabel("M\u00B2");
	
	
	
	
	
	
	
	 
	 
	
	private InvoiceList() {
		setFrameIcon(new ImageIcon(InvoiceList.class.getResource("/net/sf/jasperreports/engine/images/crosstab-16.png")));
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				IL = null;
			}
		});
		setBounds(0, 0, 1350, 841);
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
		searchPanel.setBounds(21, 3, 1292, 47);
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		txtSearch.setBounds(211, 7, 340, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		startDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		startDate.setDateFormatString("dd/MM/yyyy");
		startDate.setBounds(796, 8, 143, 32);
		((JTextField) startDate.getDateEditor()).setEditable(false);
		((JTextField) startDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(startDate);
		endDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		endDate.setDateFormatString("dd/MM/yyyy");
		endDate.setBounds(1049, 8, 141, 32);
		((JTextField) endDate.getDateEditor()).setEditable(false);
		((JTextField) endDate.getDateEditor()).setBackground(Color.WHITE);
		searchPanel.add(endDate);
		lblEndDate.setBounds(959, 7, 84, 33);
		searchPanel.add(lblEndDate);
		lblStartDate.setBounds(754, 7, 53, 33);
		searchPanel.add(lblStartDate);
		btnSearchByDate.setIcon(new ImageIcon(InvoiceList.class.getResource("/org/apache/batik/apps/svgbrowser/resources/system-search.png")));
		btnSearchByDate.setBounds(1208, 6, 63, 35);
		searchPanel.add(btnSearchByDate);
		lblSearchCust.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblSearchCust.setBounds(10, 7, 202, 33);
		searchPanel.add(lblSearchCust);
		
		btnAllInv.setIcon(new ImageIcon(InvoiceList.class.getResource("/org/apache/batik/apps/svgbrowser/resources/system-search.png")));
		btnAllInv.setBounds(559, 6, 105, 35);
		searchPanel.add(btnAllInv);
		
		btnAdd.setSize(101, 30);
		btnAdd.setLocation(21, 357);
		Container.add(btnAdd);
		btnUpdate.setLocation(125, 357);
		btnUpdate.setSize(113, 30);
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
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
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
		lblNewLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(21, 710, 147, 26);
		Container.add(lblNewLabel);
		lblSumProduct.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblSumProduct.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumProduct.setHorizontalAlignment(SwingConstants.CENTER);
		lblSumProduct.setBackground(new Color(240, 240, 240));
		lblSumProduct.setEditable(false);
		lblSumProduct.setBounds(167, 710, 113, 26);
		Container.add(lblSumProduct);
		btnDelete.setLocation(241, 357);
		btnDelete.setSize(137, 30);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		productGrid.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		productGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		productGrid.getColumnModel().getColumn(2).setCellRenderer(new MultiLineCellRenderer());
		productGrid.getColumnModel().getColumn(0).setPreferredWidth(5);
		productGrid.getTableHeader().setResizingAllowed(true);
		productGrid.getTableHeader().setReorderingAllowed(false);
		productGrid.setIntercellSpacing(new Dimension(30,4));
		
		invoicePane.setAutoscrolls(true);
		invoicePane.setPreferredSize(new Dimension(1000, 1000));
		
		Container.add(invoicePane);
		String[] columnReception = {"REFERENCE","CLIENT","ETABLI LE:","S-TOTAL(€) ",
											"PRECOMPTE DOUANE","T/CHANGE","TOTAL HT","TVA 19%","TOTAL TTC",
											"CODE_CL","SURFACE", "Couleur de metal", "Couleur des coussins","Maj %",
											"Majoration","TOTALHT","TAX 2%", "TVA_Imp","Transportation","N°", "refyear","Domic", "magTrans",
											"bancTax", "project"};
		invoiceModel = new DefaultTableModel(null,column){
			 
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
			    }
			
		};
		invoiceModel.setColumnIdentifiers(columnReception);
		invoiceGrid.setBackground(Color.WHITE);
		invoiceGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		invoiceGrid.getTableHeader().setForeground(Color.WHITE);
		invoiceGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		invoiceGrid.getTableHeader().setResizingAllowed(true);
		invoiceGrid.getTableHeader().setReorderingAllowed(false);	
		invoiceGrid.setAutoResizeMode(invoicePane.VERTICAL_SCROLLBAR_ALWAYS);
		invoicePane.setBounds(21, 52, 1292, 269);
		
		invoiceGrid.setRowHeight(30);
		invoiceGrid.setModel(invoiceModel);
		invoiceGrid.getColumnModel().getColumn(0).setPreferredWidth(60);
		invoiceGrid.getColumnModel().getColumn(1).setPreferredWidth(200);
		invoiceGrid.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(24));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(23));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(22));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(21));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(20));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(19));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(18));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(17));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(16));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(15));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(14));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(13));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(12));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(11));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(10));
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(9));
		
		
	/*	DefaultTableCellRenderer multiColorRenderer = new DefaultTableCellRenderer() {
			 @Override
			    public Component getTableCellRendererComponent(JTable table, Object value,
			            boolean isSelected, boolean hasFocus, int row, int column) {
				 
			        Component c = super.getTableCellRendererComponent(table, value, isSelected,
			                hasFocus, row, column);
			       setHorizontalAlignment(JLabel.CENTER);
			        String versionVal = (String) value;
			        if (versionVal.equalsIgnoreCase("PROFORMA")) {
		                c.setForeground(Color.RED);
		                c.setBackground(Color.LIGHT_GRAY);}
			        else {c.setForeground(Color.BLACK);
			        	  c.setBackground(Color.white);}
					return c;

		    }
		};*/
		
		invoiceGrid.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		invoiceGrid.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		invoicePane.getVerticalScrollBar();
		invoicePane.setViewportView(invoiceGrid);
		btnPrint.setIcon(new ImageIcon(InvoiceList.class.getResource("/net/sf/jasperreports/view/images/print.GIF")));
	    
		btnPrint.setLocation(654, 357);
		btnPrint.setSize(148, 30);
		Container.add(btnPrint);
		btnReceptioner.setBounds(500, 357, 148, 30);
		Container.add(btnReceptioner);
		btnPrintNoImg.setIcon(new ImageIcon(InvoiceList.class.getResource("/net/sf/jasperreports/view/images/print.GIF")));
		
		
		btnPrintNoImg.setBounds(805, 357, 224, 30);
		Container.add(btnPrintNoImg);
		btnPrintNoPrice.setIcon(new ImageIcon(InvoiceList.class.getResource("/net/sf/jasperreports/view/images/print.GIF")));
		
		btnPrintNoPrice.setBounds(1035, 357, 210, 30);
		Container.add(btnPrintNoPrice);
		txtTvaImpo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTvaImpo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		txtTvaImpo.setEditable(false);
		txtTvaImpo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtTvaImpo.setBackground(Color.LIGHT_GRAY);
		txtTvaImpo.setBounds(884, 710, 126, 29);
		Container.add(txtTvaImpo);
		txtTvaImpo.setColumns(10);
		txtRetirement.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtRetirement.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		txtRetirement.setEditable(false);
		txtRetirement.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtRetirement.setBackground(Color.LIGHT_GRAY);
		txtRetirement.setColumns(10);
		txtRetirement.setBounds(672, 710, 126, 29);
		Container.add(txtRetirement);
		
		
		lblTvaImpo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTvaImpo.setBounds(813, 710, 73, 26);
		Container.add(lblTvaImpo);
	
		lbltaxRetirement.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbltaxRetirement.setBounds(611, 711, 60, 26);
		Container.add(lbltaxRetirement);
		
		lblTransp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTransp.setBounds(347, 711, 113, 26);
		Container.add(lblTransp);
		
		txtTransp.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTransp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTransp.setEditable(false);
		txtTransp.setColumns(10);
		txtTransp.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtTransp.setBackground(Color.LIGHT_GRAY);
		txtTransp.setBounds(461, 710, 126, 29);
		Container.add(txtTransp);
		btnFinal.setFont(new Font("Tahoma", Font.BOLD, 17));
		
		btnFinal.setForeground(Color.WHITE);
		btnFinal.setBackground(new Color(255,192,203));
		btnFinal.setEnabled(true);
		btnFinal.setBounds(382, 357, 113, 30);
		Container.add(btnFinal);
		
		lblNombreDeFactures.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNombreDeFactures.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblNombreDeFactures.setBounds(21, 322, 163, 26);
		Container.add(lblNombreDeFactures);
		
		txtSumInvoices.setHorizontalAlignment(SwingConstants.CENTER);
		txtSumInvoices.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		txtSumInvoices.setEditable(false);
		txtSumInvoices.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSumInvoices.setBackground(SystemColor.menu);
		txtSumInvoices.setBounds(183, 322, 113, 26);
		Container.add(txtSumInvoices);
		lblDomicil.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDomicil.setBounds(1036, 710, 147, 26);
		
		Container.add(lblDomicil);
		txtDomicil.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDomicil.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtDomicil.setEditable(false);
		txtDomicil.setColumns(10);
		txtDomicil.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtDomicil.setBackground(Color.LIGHT_GRAY);
		txtDomicil.setBounds(1187, 709, 126, 29);
		
		Container.add(txtDomicil);
		btnXls.setIcon(new ImageIcon(InvoiceList.class.getResource("/net/sf/jasperreports/engine/images/chart-16.png")));
		btnXls.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnXls.setBounds(1250, 356, 61, 30);
		
		Container.add(btnXls);
		
		
		txtBancTax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBancTax.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtBancTax.setEditable(false);
		txtBancTax.setColumns(10);
		txtBancTax.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtBancTax.setBackground(Color.LIGHT_GRAY);
		txtBancTax.setBounds(1187, 752, 126, 29);
		Container.add(txtBancTax);
		
		
		lblBancTax.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBancTax.setBounds(1032, 753, 152, 26);
		Container.add(lblBancTax);
		
		txtMagTrans.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMagTrans.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtMagTrans.setEditable(false);
		txtMagTrans.setColumns(10);
		txtMagTrans.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtMagTrans.setBackground(Color.LIGHT_GRAY);
		txtMagTrans.setBounds(884, 753, 126, 29);
		Container.add(txtMagTrans);
		
		
		lblMagTrans.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMagTrans.setBounds(662, 753, 224, 26);
		Container.add(lblMagTrans);
		lblProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblProject.setBounds(21, 752, 60, 26);
		
		Container.add(lblProject);
		txtProject.setHorizontalAlignment(SwingConstants.CENTER);
		txtProject.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtProject.setEditable(false);
		txtProject.setColumns(10);
		txtProject.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtProject.setBackground(Color.LIGHT_GRAY);
		txtProject.setBounds(73, 752, 265, 29);
		
		Container.add(txtProject);
		
		
		lblSuperficie.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSuperficie.setBounds(388, 753, 73, 26);
		Container.add(lblSuperficie);
		
		
		txtSuperficie.setHorizontalAlignment(SwingConstants.CENTER);
		txtSuperficie.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSuperficie.setEditable(false);
		txtSuperficie.setColumns(10);
		txtSuperficie.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtSuperficie.setBackground(Color.LIGHT_GRAY);
		txtSuperficie.setBounds(461, 752, 101, 29);
		Container.add(txtSuperficie);
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblM.setBounds(570, 753, 23, 26);
		
		Container.add(lblM);
		
		
		btnCopy.setBounds(1037, 322, 276, 30);
		Container.add(btnCopy);
		
	}
	
	public static InvoiceList getInstance() {
		if (IL==null) {
			synchronized (InvoiceList.class) {
				if (IL==null) IL = new InvoiceList();
			}
			 }
		return IL;
	}
	
	public static void setIL(InvoiceList iL) {
		IL = iL;
	}

	public JTable getproductGrid() {
		return productGrid;
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

	public JPanel getContainer() {
		return Container;
	}
	
	public void changeBackgroundColor(Color col) {
		Container.setBackground(col);
		searchPanel.setBackground(col);
		lblEndDate.setBackground(col);
		lblStartDate.setBackground(col);
		lblSearchCust.setBackground(col);
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
	
	public JButton getFinalBtn() {
		return btnFinal;
	}
	
	public JButton getReceptionBtn() {
		return btnReceptioner;
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

	public String getTaxRetirement() {
		return txtRetirement.getText();
	}
	
	public void setTaxRetirement(String retirement) {
		txtRetirement.setText(retirement);
	}
	
	public String getTvaImpo() {
		return txtTvaImpo.getText();
	}
	
	public void setTvaImpo(String tvaImpo) {
		txtTvaImpo.setText(tvaImpo);
	}
	
	public String getTransport() {
		return txtTransp.getText();
	}
	
	public void setTransport(String trans) {
		txtTransp.setText(trans);
	}
	
	public void setSumInvoices(String sumInv) {
		txtSumInvoices.setText(sumInv);
	}
	
	public void setTaxDomic(String domic) {
		txtDomicil.setText(domic);
	}
	
	public void setBancTax(String bancTax) {
		txtBancTax.setText(bancTax);
	}
	
	public void setMagTrans(String magTrans) {
		txtMagTrans.setText(magTrans);
	}
	
	public void setProject(String project) {
		txtProject.setText(project);
	}
	
	public void setSuperficie(String Superficie) {
		txtSuperficie.setText(Superficie);
	}
	/*********************** btn Action Listeners ************************************************/
	public void addingInvoiceListener(ActionListener addingActionListener) {
		btnAdd.addActionListener(addingActionListener);

	}

	public void updatingListener(ActionListener updatingActionListener) {
		btnUpdate.addActionListener(updatingActionListener);

	}
	
	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void invoiceGridListener(MouseListener prdGdListener) {
		invoiceGrid.addMouseListener(prdGdListener);
	}
	
	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
		  InvoiceList.this.addInternalFrameListener(openingListener);
		}
	
	public void printingbtnListener(MouseListener printingLis) {
		btnPrint.addMouseListener(printingLis);
	}
	
	public void printingNoImagebtnListener(MouseListener printingnoimg) {
		btnPrintNoImg.addMouseListener(printingnoimg);
	}
	
	public void printingNoPricebtnListener(MouseListener printingnopr) {
		btnPrintNoPrice.addMouseListener(printingnopr);
	}
	
	public void searhByDateListener(ActionListener dateChanged) {
		btnSearchByDate.addActionListener(dateChanged);
	}
	
	
	public void receptionerBtnListener(ActionListener rec) {
		btnReceptioner.addActionListener(rec);
	}
	
	public void finaliserBtnListener(ActionListener finaliser) {
		btnFinal.addActionListener(finaliser);
	}
	
	public void findAllInvBtnListener(ActionListener allInv) {
		btnAllInv.addActionListener(allInv);
	}
	
	public void TransferToExcel(ActionListener exl) {
		btnXls.addActionListener(exl);
	}
	
	public void copyInvoice(ActionListener cop) {
		btnCopy.addActionListener(cop);
	}
}
