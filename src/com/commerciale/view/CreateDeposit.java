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

import org.hibernate.criterion.RowCountProjection;

import com.commerciale.service.FormatterFactoryField;
import com.commerciale.service.MultiLineCellRenderer;
import com.commerciale.service.Tools;

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
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JFormattedTextField;

public class CreateDeposit extends JInternalFrame{
	private static CreateDeposit CD = null;
	private JPanel Container = new JPanel();
	private JPanel searchPanel = new JPanel();
	private JPanel newPayment = new JPanel();
	private JTextField txtSearch = new JTextField(50);
	private JTextField txtCheqN = new JTextField();
	private JTextField txtNbrDepo = new JTextField();
	private JTextField txtTotalDepo = new JTextField();
	private JFormattedTextField depoAmount = new JFormattedTextField();
	private JLabel lblNewDepo = new JLabel("Versement pour :");
	private JLabel lblDepoDate = new JLabel("Aujourd'huit le : ");
	private JLabel lblDepoAmount = new JLabel("Montant :");
	private JLabel lblDepoCustomer = new JLabel("");
	private JLabel lblNewLabel = new JLabel("Liste des factures par client :");
	private JLabel lblCheq = new JLabel("");
	private JLabel lblCheqN = new JLabel("N\u00B0 de Ch\u00E8que :");
	private JLabel lblNewLabel_1 = new JLabel("Nbr/Vrs : ");
	private JLabel lblNewLabel_1_1 = new JLabel("Total des versements :");
	private JButton btnAdd = new JButton("Transaction");
	private JButton btnSearch = new JButton("Tous");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnPrint = new JButton("Imprimer");
	private JButton btnDepoSave = new JButton("Transf\u00E9rer");
	private JButton btnDepoCancel = new JButton("Annuler");
	private JButton btnAddCheq = new JButton("Ajouter une Image");
	private JButton btnUpdate = new JButton("Modifier");
	private JTable invoiceGrid = new JTable();
	private JTable customerGrid = new JTable();
	private JTable paymentGrid = new JTable();
	private DefaultTableModel model = new DefaultTableModel();
	private DefaultTableModel invoiceModel = new DefaultTableModel();
	private DefaultTableModel customerModel = new DefaultTableModel();
	private JScrollPane InvoicePane = new JScrollPane();
	private JScrollPane customerPane = new JScrollPane();
	private JScrollPane paymentPane = new JScrollPane();
	private JDateChooser depoDate = new JDateChooser();
	
	
	
	
	
	
	
	private CreateDeposit() {
		addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				CD = null;
			}
		});
		setBounds(0, 0, 1289, 826);
		BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
		   Component northPane = ui.getNorthPane();
		MouseMotionListener[] motionListeners = (MouseMotionListener[]) northPane.getListeners(MouseMotionListener.class);

		   for (MouseMotionListener listener: motionListeners)
		      northPane.removeMouseMotionListener(listener);
		setClosable(true);
		setResizable(false);
		setIconifiable(false);
		setTitle("Caisse");
		Container.setBackground(new Color(240,255,255));
		getContentPane().add(Container);
		Container.setLayout(null);
		searchPanel.setBounds(0, 0, 586, 774);
		searchPanel.setBackground(new Color(176,196,222));
		Container.add(searchPanel);
		searchPanel.setLayout(null);
		btnSearch.setBounds(484, 7, 91, 33);
		searchPanel.add(btnSearch);
		txtSearch.setBounds(10, 7, 463, 32);
		searchPanel.add(txtSearch);
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 22));
		txtSearch.setColumns(10);
		btnAdd.setSize(140, 41);
		btnAdd.setLocation(591, 542);
		Container.add(btnAdd);
		String[] column = {"DATE","VERSEMENT","DETTE","N° DE CHEQUE","Cheq","depo_id"};
		model.setColumnIdentifiers(column);
		paymentGrid.setBackground(Color.WHITE);
		paymentGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		paymentGrid.getTableHeader().setForeground(Color.WHITE);
		paymentGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		paymentGrid.getTableHeader().setResizingAllowed(true);
		paymentGrid.getTableHeader().setReorderingAllowed(false);	
		paymentGrid.setFont(new Font("SansSerif",Font.PLAIN, 16));
		paymentGrid.setRowHeight(30);
		paymentGrid.setModel(model);
		paymentPane.setBounds(593, 269, 669, 227);
		Container.add(paymentPane);
		paymentPane.setViewportView(paymentGrid);
		paymentGrid.removeColumn(paymentGrid.getColumnModel().getColumn(5));
		paymentGrid.removeColumn(paymentGrid.getColumnModel().getColumn(4));
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		btnDelete.setLocation(857, 542);
		btnDelete.setSize(134, 41);
		btnDelete.setEnabled(true);
		Container.add(btnDelete);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		paymentGrid.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		paymentGrid.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		paymentGrid.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		paymentGrid.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		Container.add(InvoicePane);
		String[] columnReception = {"REFERENCE","ETABLI LE","MONTANT FINAL","CODE_FACT"};
		invoiceModel.setColumnIdentifiers(columnReception);
		invoiceGrid.setBackground(Color.WHITE);
		invoiceGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		invoiceGrid.getTableHeader().setForeground(Color.WHITE);
		invoiceGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		invoiceGrid.getTableHeader().setResizingAllowed(true);
		invoiceGrid.getTableHeader().setReorderingAllowed(false);	
		invoiceGrid.setFont(new Font("SansSerif",Font.PLAIN, 16));
		InvoicePane.setBounds(593, 42, 669, 220);
		invoiceGrid.setRowHeight(30);
		invoiceGrid.setModel(invoiceModel);
		invoiceGrid.removeColumn(invoiceGrid.getColumnModel().getColumn(3));
		invoiceGrid.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		invoiceGrid.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		invoiceGrid.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		InvoicePane.setViewportView(invoiceGrid);
		customerPane.setBounds(10, 41, 565, 499);
		//Container.add(customerPane);
		String[] columnCustomer = {"NOM OU RAISON SOCIALE", "MONTANT ORIGINAL","VERSEMENT","DETTE","Client_ID"};
		customerModel.setColumnIdentifiers(columnCustomer);
		customerGrid.setBackground(Color.WHITE);
		customerGrid.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		customerGrid.getTableHeader().setForeground(Color.WHITE);
		customerGrid.getTableHeader().setBackground(Color.DARK_GRAY);
		customerGrid.getTableHeader().setResizingAllowed(true);
		customerGrid.getTableHeader().setReorderingAllowed(false);	
		customerGrid.setFont(new Font("SansSerif",Font.PLAIN, 16));
		customerGrid.setBounds(20, 70, 737, 266);
		customerGrid.setRowHeight(30);
		customerGrid.setModel(customerModel);
		DefaultTableCellRenderer multiColorRenderer = new DefaultTableCellRenderer() {
			 @Override
			    public Component getTableCellRendererComponent(JTable table, Object value,
			            boolean isSelected, boolean hasFocus, int row, int column) {
				 
			        Component c = super.getTableCellRendererComponent(table, value, isSelected,
			                hasFocus, row, column);
			       setHorizontalAlignment(JLabel.RIGHT);
			       try {
			       String versionVal = (String) value;
			       double vers = Tools.decimalToDouble(versionVal);
			        if (vers>0) {
		                c.setForeground(Color.RED);
		              //  c.setBackground(Color.LIGHT_GRAY);}
			        } else {c.setForeground(new Color(50,205,50));}
			        	  //c.setBackground(Color.white);}
			       }catch (Exception ex) {JOptionPane.showMessageDialog(null, ex.getMessage());} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					return c;

		    }
		};
		customerGrid.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		customerGrid.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		customerGrid.getColumnModel().getColumn(3).setCellRenderer(multiColorRenderer);
		customerGrid.removeColumn(customerGrid.getColumnModel().getColumn(4));
		customerPane.setViewportView(customerGrid);
		searchPanel.add(customerPane);
		
		
		lblCheq.setBounds(10, 561, 565, 199);
		searchPanel.add(lblCheq);
		btnPrint.setLocation(996, 542);
		btnPrint.setSize(134, 41);
		Container.add(btnPrint);
		newPayment.setVisible(false);
		newPayment.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		newPayment.setBounds(593, 583, 669, 173);
		newPayment.setBackground(new Color(240,255,255));
		Container.add(newPayment);
		newPayment.setLayout(null);
		lblNewDepo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewDepo.setBounds(3, 6, 152, 26);
		newPayment.add(lblNewDepo);
		lblDepoDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDepoDate.setBounds(13, 42, 106, 26);
		newPayment.add(lblDepoDate);
		lblDepoAmount.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDepoAmount.setBounds(48, 134, 61, 26);
		newPayment.add(lblDepoAmount);
		depoDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		depoDate.setDateFormatString("dd/MM/yyyy");
		depoDate.setBounds(111, 36, 163, 32);
		newPayment.add(depoDate);
		depoAmount.setBounds(111, 129, 163, 31);
		depoAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		depoAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		depoAmount.setFormatterFactory(new FormatterFactoryField() );
		depoAmount.setEditable(false);
		newPayment.add(depoAmount);
		btnDepoSave.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnDepoSave.setBounds(289, 128, 122, 32);
		newPayment.add(btnDepoSave);
		btnDepoCancel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnDepoCancel.setBounds(418, 128, 96, 32);
		newPayment.add(btnDepoCancel);
		lblDepoCustomer.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblDepoCustomer.setBounds(152, 6, 422, 26);
		newPayment.add(lblDepoCustomer);
		btnAddCheq.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		
		btnAddCheq.setBounds(289, 79, 225, 32);
		newPayment.add(btnAddCheq);
		
		lblCheqN.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCheqN.setBounds(16, 84, 90, 26);
		newPayment.add(lblCheqN);
		
		txtCheqN.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCheqN.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCheqN.setBounds(111, 79, 163, 31);
		newPayment.add(txtCheqN);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		
		lblNewLabel.setBounds(593, 16, 300, 26);
		Container.add(lblNewLabel);
		
		
		btnUpdate.setBounds(737, 542, 114, 41);
		Container.add(btnUpdate);
		
		
		txtNbrDepo.setEditable(false);
		txtNbrDepo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtNbrDepo.setHorizontalAlignment(SwingConstants.CENTER);
		txtNbrDepo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNbrDepo.setBackground(Color.LIGHT_GRAY);
		txtNbrDepo.setBounds(659, 502, 86, 32);
		Container.add(txtNbrDepo);
		txtNbrDepo.setColumns(10);
		
		
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(598, 502, 61, 26);
		Container.add(lblNewLabel_1);
		
		
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(912, 506, 145, 26);
		Container.add(lblNewLabel_1_1);
		
		
		txtTotalDepo.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTotalDepo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTotalDepo.setEditable(false);
		txtTotalDepo.setColumns(10);
		txtTotalDepo.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtTotalDepo.setBackground(Color.LIGHT_GRAY);
		txtTotalDepo.setBounds(1055, 502, 207, 32);
		Container.add(txtTotalDepo);
		
		
	}
	
	public static CreateDeposit getInstance() {
		if (CD==null) {
			synchronized (CreateDeposit.class) {
				if (CD==null) CD = new CreateDeposit();
			}
			 }
		return CD;
	}
	
	public JTable getDepositGrid() {
		return paymentGrid;
	}
	
	public JTable getInvoiceGrid() {
		return invoiceGrid;
	}
	
	public JTable getCustomerGrid() {
		return customerGrid;
	}
	
	public DefaultTableModel getDepositGridModel() {
		return model;
	}
	
	public DefaultTableModel getInvoiceGridModel() {
		return invoiceModel;
	}
	
	public DefaultTableModel getcustomerGridModel() {
		return customerModel;
	}

	public JPanel getContainer() {
		return Container;
	}
	
	public JPanel getNewDepoContainer() {
		return newPayment;
	}
	
	public JFormattedTextField getDepoAmount() {
		return depoAmount;
	}
	
	public JDateChooser getDepoDate() {
		return depoDate;
	}
	
	public JLabel getCheqImageLabel() {
		return lblCheq;
	}
	
	public void setCustomerNameForDepo(String custName) {
		lblDepoCustomer.setText(custName);
	}
	
	public String getCustomerNameForDepo() {
		return lblDepoCustomer.getText();
	}
	/*********************** getters for crud btns ***********************/

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
	
	public String getCheqNum() {
		return txtCheqN.getText();
	}
	
	public void setCheqNum(String cheqnum) {
		txtCheqN.setText(cheqnum);
	}
	
	public void setTotalDepo(String totdep) {
		txtTotalDepo.setText(totdep);
	}
	
	public void setNbreDepo(String totdebt) {
		txtNbrDepo.setText(totdebt);
	}
	

	/*********************** btn Action Listeners ************************************************/
	public void addingDepoListener(ActionListener addingActionListener) {
		btnAdd.addActionListener(addingActionListener);

	}

	public void searchListener(KeyListener searchingListener) {
		txtSearch.addKeyListener(searchingListener);
	}
	
	public void searchBtnListener(ActionListener searchingBtnActionListener) {
		btnSearch.addActionListener(searchingBtnActionListener);
	}
	
	public void invoiceGridListener(MouseListener prdGdListener) {
		invoiceGrid.addMouseListener(prdGdListener);
	}
	
	public void customerGridListener(MouseListener cusGdListener) {
		customerGrid.addMouseListener(cusGdListener);
	}
	
	public void depositGridListener(MouseListener depoGdListener) {
		paymentGrid.addMouseListener(depoGdListener);
	}
	
	public void deleteBtnListener(ActionListener deletingListener) {
		btnDelete.addActionListener(deletingListener);
	}
	
	public void allNeddedWhenOpen(InternalFrameListener openingListener) {
		  CreateDeposit.this.addInternalFrameListener(openingListener);
		}
	
	public void printingbtnListener(ActionListener printingLis) {
		btnPrint.addActionListener(printingLis);
	}
	
	public void hidePaymentPanel(ActionListener hidePanel) {
		btnDepoCancel.addActionListener(hidePanel);
	}
	
	public void savingDepo(ActionListener save) {
		btnDepoSave.addActionListener(save);
	}
	
	public void addingCheq(ActionListener cheq) {
		btnAddCheq.addActionListener(cheq);
	}
	
	public void updatingDeposit(ActionListener upd) {
		btnUpdate.addActionListener(upd);
	}
}

