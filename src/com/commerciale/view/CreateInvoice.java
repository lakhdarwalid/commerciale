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
import java.util.EventListener;

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
import com.commerciale.service.FormatterFactoryInteger;
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
import java.awt.Button;

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
import javax.swing.JToggleButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.BevelBorder;
import java.awt.SystemColor;
import javax.swing.JCheckBox;



public class CreateInvoice extends JInternalFrame {
	
	private static CreateInvoice CI = null;
	private JPanel Container = new JPanel();
	private JPanel panelDate = new JPanel();
	private JPanel panelChange = new JPanel();
	private JTextField txtD10 = new JTextField(50);
	private JDateChooser txtDate = new JDateChooser();
	public static JTextField txtCustName = new JTextField(15);
	public static JTextField txtCodeCust = new JTextField(15);
	public static JTextField boxChange = new JTextField(15);
	public static JTextField txtMontant = new JTextField("0,00");
	public static JTextField txtTVA = new JTextField("0,00");
	public static JTextField txtMontantFinal = new JTextField("0,00");
	public static JTextField txtRetirement = new JTextField("0,00");
	public static JTextField txtMajResult = new JTextField("0,00");
	public static JTextField txtHT = new JTextField("0,00");
	private JTextField txtDiffTva = new JTextField("0,00");
	private JTextField txtMetalColor = new JTextField();
    private JTextField txtSeatColor = new JTextField();
    private JTextField txtDomic = new JTextField("0,00");
    private JTextField txtBancTax = new JTextField("0,00");
    private JTextField txtProject = new JTextField();
	public static JFormattedTextField txtPrecompteDouane = new JFormattedTextField();
	private JFormattedTextField txtSurface = new JFormattedTextField();
	private JFormattedTextField txtMaj = new JFormattedTextField();
	private JFormattedTextField txtTransportation = new JFormattedTextField();
	private JFormattedTextField txtMagTrans = new JFormattedTextField();
	private JLabel lblMaj = new JLabel("Majoration :");
	private JLabel lblRef = new JLabel("Facture N\u00B0 :");
	private JLabel lblUnitPrice = new JLabel("Raison Sociale :");
	private JLabel lblCodeSup = new JLabel("Code Client :");
	private JLabel lblNewLabel = new JLabel("Nombre d'articles:");
	private JLabel lblDate = new JLabel("Aujourdhuit le :");
	private JLabel lbTotal = new JLabel("Sous-total en devise :");
	private JLabel lblchange = new JLabel("Taux de change (DA) :");
	private JLabel lblTva = new JLabel("TVA 19% : ");
	private JLabel lblPrcompteDouane = new JLabel("Pr\u00E9compte Douane :");
	private JLabel lblMontantFinale = new JLabel("TOTAL TTC : ");
	private JLabel lblSurface = new JLabel("Superficie de la salle d'entrainement :");
	private JLabel lblMetalColor = new JLabel("Couleur du m\u00E9tal :");
	private JLabel lblSeatColor = new JLabel("Couleur des coussins :");
	private JLabel lblM = new JLabel("m\u00B2");
	private	JLabel lblRetirement = new JLabel("Tax 4% :");
	private JLabel lblHT = new JLabel("TOTAL HT : ");
	private JLabel lblRefYear = new JLabel("");
	private JLabel lblDiffTva = new JLabel("Diff\u00E9rence de TVA a payer :");
	private JLabel lblTransportation = new JLabel("");
	private JLabel lblTvaDiff = new JLabel("Frais de Transport : ");
	private JLabel lblTotalDeviseTrans = new JLabel("");
	private JLabel lblTaxDomic = new JLabel("Tax Domic.. 0,05% :");
	private JLabel lblMagTrans = new JLabel("Frais de transitaire et magazinage:");
	private JLabel lblTaxDeBanque = new JLabel("Tax de Banque (0.01%):");
	private JLabel lblProject = new JLabel("Projet :");
	
	public static JTextField lblSumProduct = new JTextField("");
	
	private JButton btnAddProduct = new JButton("Ajouter un article");
	private JButton btnCancel = new JButton("Annuler");
	private JButton btnSave = new JButton("Enregistrer");
	private JButton btnDelete = new JButton("Supprimer un article");
	private JButton btnAddChange = new JButton("+");
	private JButton btnAddCustomer = new JButton("S\u00E9l\u00E9ctionnez un client");
	private JButton btnExcel = new JButton("Importer Excel");
	private JButton btnMaj = new JButton("");
	private JButton btnCalcTransport = new JButton("");
	private JRadioButton rdbtnDa = new JRadioButton("DA");
	private JRadioButton rdbtnDevise = new JRadioButton("Devise");
	
	
	public static JTable productGrid = new JTable();
	public static DefaultTableModel productGridModel;
	private JScrollPane scrollPane = new JScrollPane();	
	private ButtonGroup groupSelect = new ButtonGroup();
	private ButtonGroup proFinalGroup = new ButtonGroup();
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem mntmNewMenuItem = new JMenuItem("Modifer");
	private JCheckBox fob = new JCheckBox("F.O.B");
	
	
	
	
	
	private CreateInvoice() {
		setFrameIcon(new ImageIcon(CreateInvoice.class.getResource("/net/sf/jasperreports/view/images/actualsize.GIF")));
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				CI = null;
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
		setTitle("Nouvelle Facture");
		Container.setBackground(new Color(240,248,255));
		getContentPane().add(Container);
		Container.setLayout(null);
		txtD10.setHorizontalAlignment(SwingConstants.RIGHT);
		txtD10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtD10.setBounds(514, 10, 88, 28);
		Container.add(txtD10);
		lblCodeSup.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCodeSup.setBounds(174, 56, 74, 21);
		Container.add(lblCodeSup);
		txtCodeCust.setBackground(Color.WHITE);
		txtCodeCust.setEditable(false);
		txtCodeCust.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCodeCust.setBounds(250, 49, 126, 28);
		Container.add(txtCodeCust);
		lblUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnitPrice.setBounds(420, 56, 88, 18);
		Container.add(lblUnitPrice);
		txtCustName.setBackground(Color.WHITE);
		txtCustName.setEditable(false);
		txtCustName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCustName.setBounds(514, 49, 309, 28);
		Container.add(txtCustName);
		lblRef.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRef.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRef.setBounds(377, 17, 127, 14);
		Container.add(lblRef);
		btnAddProduct.setSize(210, 30);
		btnAddProduct.setLocation(21, 129);
		Container.add(btnAddProduct);
		btnCancel.setIcon(new ImageIcon(CreateInvoice.class.getResource("/org/apache/batik/apps/svgbrowser/resources/process-stop.png")));
		btnCancel.setLocation(1158, 672);
		btnCancel.setSize(155, 54);
		Container.add(btnCancel);
		btnSave.setIcon(new ImageIcon(CreateInvoice.class.getResource("/net/sf/jasperreports/view/images/save.GIF")));
		//btnSave.setEnabled(false);
		btnSave.setLocation(973, 672);
		btnSave.setSize(164, 54);
		Container.add(btnSave);
		scrollPane.setBounds(21, 171, 1292, 337);
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
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
			    }
		};
		productGridModel.setColumnIdentifiers(column);
		productGrid.setBackground(Color.WHITE);
		productGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		productGrid.getTableHeader().setForeground(Color.WHITE);
		productGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		addPopup(productGrid, popupMenu);
		
		mntmNewMenuItem.setBackground(new Color(250,212,147));
		mntmNewMenuItem.setForeground(Color.BLACK);
		popupMenu.add(mntmNewMenuItem);
		productGrid.setFont(new Font("SansSerif", Font.PLAIN, 20));
		productGrid.setRowHeight(100);
		productGrid.setModel(productGridModel);
		scrollPane.setViewportView(productGrid);
		lblNewLabel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(21, 509, 119, 26);
		Container.add(lblNewLabel);
		lblSumProduct.setEditable(false);
		lblSumProduct.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblSumProduct.setBackground(Color.LIGHT_GRAY);
		lblSumProduct.setFont(new Font("Times New Roman", Font.PLAIN, 21));
		lblSumProduct.setHorizontalAlignment(SwingConstants.CENTER);
		lblSumProduct.setBounds(138, 509, 88, 26);
		Container.add(lblSumProduct);
		btnDelete.setIcon(new ImageIcon(CreateInvoice.class.getResource("/net/sf/jasperreports/components/sort/resources/images/delete_edit.gif")));
		btnDelete.setLocation(233, 129);
		btnDelete.setSize(256, 30);
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
		lbTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lbTotal.setBounds(658, 546, 126, 14);

		Container.add(lbTotal);
		txtMontant.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMontant.setBackground(Color.WHITE);
		txtMontant.setEditable(false);
		txtMontant.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMontant.setBounds(788, 537, 186, 28);
		Container.add(txtMontant);
		lblTva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTva.setBounds(1024, 587, 69, 21);

		Container.add(lblTva);
		txtTVA.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTVA.setBackground(Color.WHITE);
		txtTVA.setEditable(false);
		txtTVA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTVA.setBounds(1103, 580, 210, 28);
		Container.add(txtTVA);
		lblPrcompteDouane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrcompteDouane.setBounds(663, 630, 119, 14);

		Container.add(lblPrcompteDouane);
		txtPrecompteDouane.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPrecompteDouane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPrecompteDouane.setBounds(787, 623, 186, 28);
		txtPrecompteDouane.setFormatterFactory(new FormatterFactoryField() );
		Container.add(txtPrecompteDouane);
		lblMontantFinale.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMontantFinale.setBounds(1011, 630, 88, 21);

		Container.add(lblMontantFinale);
		txtMontantFinal.setForeground(Color.RED);
		txtMontantFinal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMontantFinal.setBackground(Color.WHITE);
		txtMontantFinal.setEditable(false);
		txtMontantFinal.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtMontantFinal.setBounds(1103, 623, 210, 40);
		Container.add(txtMontantFinal);
		btnAddCustomer.setIcon(null);
		btnAddCustomer.setBounds(832, 46, 253, 35);
		Container.add(btnAddCustomer);
		panelDate.setBackground(new Color(240,248,255));
		panelDate.setBorder(null);
		panelDate.setBounds(127, 10, 253, 35);
		Container.add(panelDate);
		panelDate.setLayout(null);
		txtDate.setDateFormatString("dd-MM-yyyy");
		txtDate.setBounds(123, 0, 126, 28);
		panelDate.add(txtDate);
		//txtDate.setText(Tools.dateToString(new Date()));
		txtDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		//txtDate.setEditable(false);
		txtDate.setBackground(Color.white);
		lblDate.setBounds(33, 7, 94, 14);
		panelDate.add(lblDate);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelChange.setBackground(new Color(240,248,255));
		panelChange.setBorder(null);
		panelChange.setBounds(929, 135, 394, 34);
		Container.add(panelChange);
		panelChange.setLayout(null);
		lblchange.setBounds(0, 0, 142, 26);
		panelChange.add(lblchange);
		lblchange.setFont(new Font("Tahoma", Font.PLAIN, 12));
		boxChange.setBounds(132, 0, 180, 28);
		panelChange.add(boxChange);
		boxChange.setFont(new Font("Tahoma", Font.PLAIN, 16));
		boxChange.setEditable(false);
		boxChange.setBackground(lightBlue);
		btnAddChange.setBounds(322, 0, 65, 28);
		panelChange.add(btnAddChange);
		btnAddChange.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSurface.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSurface.setBounds(40, 95, 220, 14);
		Container.add(lblSurface);
		lblMetalColor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMetalColor.setBounds(402, 95, 112, 14);
		Container.add(lblMetalColor);
		lblSeatColor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSeatColor.setBounds(665, 95, 126, 14);
		Container.add(lblSeatColor);
		txtSurface.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSurface.setHorizontalAlignment(SwingConstants.CENTER);
		txtSurface.setBounds(250, 87, 95, 28);
		txtSurface.setFormatterFactory(new FormatterFactoryInteger());
		Container.add(txtSurface);
		txtMetalColor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMetalColor.setBounds(514, 87, 137, 28);
		Container.add(txtMetalColor);
		txtMetalColor.setColumns(10);
		txtSeatColor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSeatColor.setColumns(10);
		txtSeatColor.setBounds(793, 87, 137, 28);
		Container.add(txtSeatColor);
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblM.setBounds(352, 95, 20, 14);
		
		Container.add(lblM);
		txtMaj.setBounds(377, 642, 53, 28);
		txtMaj.setFormatterFactory(new FormatterFactoryInteger() );
		Container.add(txtMaj);
		lblMaj.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMaj.setBounds(305, 649, 69, 14);
		Container.add(lblMaj);
		btnMaj.setIcon(new ImageIcon(CreateInvoice.class.getResource("/org/apache/batik/apps/svgbrowser/resources/go-next.png")));
		btnMaj.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnMaj.setBounds(434, 642, 44, 28);
		Container.add(btnMaj);
		txtRetirement.setHorizontalAlignment(SwingConstants.RIGHT);
		txtRetirement.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtRetirement.setEditable(false);
		txtRetirement.setBackground(Color.WHITE);
		txtRetirement.setBounds(787, 580, 186, 28);
		Container.add(txtRetirement);
		lblRetirement.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRetirement.setBounds(726, 587, 60, 14);
		Container.add(lblRetirement);
		txtMajResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMajResult.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMajResult.setEditable(false);
		txtMajResult.setBackground(Color.WHITE);
		txtMajResult.setBounds(481, 642, 155, 28);
		Container.add(txtMajResult);
		
		lblHT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHT.setBounds(1019, 544, 71, 21);
		Container.add(lblHT);
		txtHT.setHorizontalAlignment(SwingConstants.RIGHT);
		txtHT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtHT.setEditable(false);
		txtHT.setBackground(Color.WHITE);
		txtHT.setBounds(1103, 537, 210, 28);
		Container.add(txtHT);
		lblRefYear.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRefYear.setBounds(606, 13, 74, 21);
		Container.add(lblRefYear);
		lblDiffTva.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDiffTva.setBounds(325, 698, 164, 14);
		
		Container.add(lblDiffTva);
		txtDiffTva.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDiffTva.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDiffTva.setEditable(false);
		txtDiffTva.setBackground(Color.WHITE);
		txtDiffTva.setBounds(481, 691, 155, 28);
		
		Container.add(txtDiffTva);
		
		lblTvaDiff.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTvaDiff.setBounds(98, 580, 114, 21);
		Container.add(lblTvaDiff);
		rdbtnDa.setBackground(new Color(240,248,255));
		
		rdbtnDa.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDa.setActionCommand("DA");
		rdbtnDa.setBounds(288, 574, 65, 35);
		Container.add(rdbtnDa);
		rdbtnDevise.setBackground(new Color(240,248,255));
		
		rdbtnDevise.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDevise.setActionCommand("DEVISE");
		rdbtnDevise.setBounds(355, 574, 90, 35);
		Container.add(rdbtnDevise);
		groupSelect.add(rdbtnDa);
		groupSelect.add(rdbtnDevise);
		txtTransportation.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTransportation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTransportation.setEditable(false);
		txtTransportation.setBackground(Color.WHITE);
		txtTransportation.setBounds(449, 578, 186, 28);
		txtTransportation.setFormatterFactory(new FormatterFactoryField());
		Container.add(txtTransportation);
		btnCalcTransport.setIcon(new ImageIcon(CreateInvoice.class.getResource("/org/apache/batik/apps/svgbrowser/resources/go-next-small.png")));
		
		btnCalcTransport.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCalcTransport.setBounds(640, 576, 49, 32);
		Container.add(btnCalcTransport);
		
		lblTransportation.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransportation.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTransportation.setBounds(451, 606, 184, 28);
		Container.add(lblTransportation);
		
		
		lblTotalDeviseTrans.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalDeviseTrans.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalDeviseTrans.setBounds(790, 510, 184, 28);
		Container.add(lblTotalDeviseTrans);
		
		lblTaxDomic.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTaxDomic.setBounds(662, 698, 126, 14);
		Container.add(lblTaxDomic);
		
		txtDomic.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDomic.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDomic.setEditable(false);
		txtDomic.setBackground(Color.WHITE);
		txtDomic.setBounds(788, 691, 155, 28);
		Container.add(txtDomic);
		
		
		btnExcel.setEnabled(true);
		btnExcel.setBounds(494, 129, 199, 30);
		Container.add(btnExcel);
		
		lblMagTrans.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMagTrans.setBounds(250, 546, 199, 14);
		Container.add(lblMagTrans);
		txtMagTrans.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMagTrans.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtMagTrans.setBounds(449, 537, 186, 28);
		txtMagTrans.setFormatterFactory(new FormatterFactoryField() );
		Container.add(txtMagTrans);
		lblTaxDeBanque.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTaxDeBanque.setBounds(21, 698, 143, 14);
		Container.add(lblTaxDeBanque);
		txtBancTax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBancTax.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBancTax.setEditable(false);
		txtBancTax.setBackground(Color.WHITE);
		txtBancTax.setBounds(164, 691, 143, 28);
		Container.add(txtBancTax);
		
		
		lblProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblProject.setBounds(743, 17, 48, 14);
		Container.add(lblProject);
		
		
		txtProject.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtProject.setColumns(10);
		txtProject.setBounds(793, 10, 292, 28);
		Container.add(txtProject);
		
		
		fob.setBounds(217, 580, 69, 23);
		Container.add(fob);
		
	

	}

	public static CreateInvoice getInstance() {
		if (CI==null) {
			synchronized (CreateInvoice.class) {
				if (CI==null) CI= new CreateInvoice();
			}
		}
		return CI;
	}
	
	public CreateInvoice getCI() {
		return CI;
	}

	public void setCI(CreateInvoice cI) {
		CI = cI;
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
	
	public void changeBackgroundColor(Color col) {
		Container.setBackground(col);
		panelChange.setBackground(col);
		panelDate.setBackground(col);
		rdbtnDa.setBackground(col);
		rdbtnDevise.setBackground(col);
	}
	
	public JLabel FactureN() {
		return lblRef;
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
	
	public String getCustomerId() {
		return txtCodeCust.getText();
	}

	public void setCustomerId(String custId) {
		txtCodeCust.setText(custId);
	}

	public String getChange() {
		return boxChange.getText();
	}

	public void setChange(String change) {
		boxChange.setText(change);
	}

	public String getCustomerName() {
		return txtCustName.getText();
	}

	public void setCustomerName(String custName) {
		this.txtCustName.setText(custName);
	}
	
	public Date getTodayDate() {
		return txtDate.getDate();
	}
	
	public void setTodayDate(Date todayDate) {
		txtDate.setDate(todayDate);
	}
	
	public String getTotalDevise() {
		return txtMontant.getText();
	}
	
	public void setTotalDevise(String totalDevise) {
		txtMontant.setText(totalDevise);
	}
	
	public String getMagTrans() {
		return txtMagTrans.getText();
	}
	
	public void setMagTrans(String magTrans) {
		txtMagTrans.setText(magTrans);
	}
	
	public String getPrecompteDouane() {
		return txtPrecompteDouane.getText();
	}
	
	public void setPrecompteDouane(String precompteDouane) {
		txtPrecompteDouane.setText(precompteDouane);
	}
	
	public String getMajoration() {
		return txtMaj.getText();
	}
	
	public void setMajoration(String maj) {
		txtMaj.setText(maj);
	}
	
	public String getMajResult() {
		return txtMajResult.getText();
	}
	
	public void setMajResult(String majr) {
		txtMajResult.setText(majr);
	}
	
	public String getRetirement() {
		return txtRetirement.getText();
	}
	
	public void setRetirement(String ret) {
		txtRetirement.setText(ret);
	}
	
	public String getTotalHt() {
		return txtHT.getText();
	}
	
	public void setTotalHt(String ht) {
		txtHT.setText(ht);
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
	
	public String getSurface(){
		return txtSurface.getText();
	}
	
	public void setSurface(String Surface) {
		txtSurface.setText(Surface);
	}
	
	public String getMetalColor() {
		return txtMetalColor.getText();
	}
	
	public void setMetalColor(String Metal) {
		txtMetalColor.setText(Metal);
	}
	
	public String getSeatColor() {
		return txtSeatColor.getText();
	}
	
	public void setSeatColor(String seat) {
		txtSeatColor.setText(seat);
	}
	
	public String getRefYear() {
		String[] invYear = lblRefYear.getText().split("/");
		return invYear[1];
	}
	
	public void setRefYear(String ye) {
		lblRefYear.setText(ye);
	}
	
	public String getDiffTva() {
		return txtDiffTva.getText();
	}
	
	public void setDiffTva(String dif) {
		txtDiffTva.setText(dif);
	}
	
	public String getTransportation() {
		return txtTransportation.getText();
	}

	public void setTransportation(String transpor) {
		txtTransportation.setText(transpor);
	}
	
	public void setTranspAmount(String transpAmount) {
		lblTransportation.setText(transpAmount);
	}
	
	public String getTranspAmount() {
		return lblTransportation.getText();
	}
	
	public void setFactureNlbl(String factn) {
		lblRef.setText(factn);
	}
	
	public void setTotalDeviseTransp(String tot) {
		lblTotalDeviseTrans.setText(tot);
	}
	
	public void setDomic(String dom) {
		txtDomic.setText(dom);
	}
	
	public String getDomic() {
		return txtDomic.getText();
	}
	
	public void setBancTax(String bancTax) {
		txtBancTax.setText(bancTax);
	}
	
	public String getBancTax() {
		return txtBancTax.getText();
	}
	
	public void setProject(String project) {
		txtProject.setText(project);
	}
	
	public String getProject() {
		return txtProject.getText();
	}
	
	public Boolean getFob() {
		return fob.isSelected();
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

	public void addCustomer(ActionListener addCust) {
		btnAddCustomer.addActionListener(addCust);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
	  CreateInvoice.this.addInternalFrameListener(openingListener);
	}
	
	public void majoringListener(ActionListener maj) {
		btnMaj.addActionListener(maj);
	}
	
	public void fraitRdbtnSwitch (ActionListener rdlistn) {
		rdbtnDa.addActionListener(rdlistn);
		rdbtnDevise.addActionListener(rdlistn);
	}
	
	public void calculateAfterTransportation(ActionListener trans) {
		btnCalcTransport.addActionListener(trans);
	}
	
	public void importingExcelInvoice (ActionListener exF) {
		btnExcel.addActionListener(exF);
	}
	
	public void popUpUpdateQte(ActionListener updQte) {
		mntmNewMenuItem.addActionListener(updQte);
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
