package com.commerciale.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.commerciale.controller.CRUDclient;
import com.commerciale.controller.CRUDdeposit;
import com.commerciale.controller.CRUDinvoiceList;
import com.commerciale.controller.CRUDinvoiceListProforma;
import com.commerciale.controller.CRUDinvoiceStatistics;
import com.commerciale.controller.CRUDproduct;
import com.commerciale.controller.CRUDreception;
import com.commerciale.controller.CRUDreceptionList;
import com.commerciale.controller.CRUDsaleConditions;
import com.commerciale.controller.CRUDsupplier;
import com.commerciale.controller.CRUDuser;
import com.commerciale.model.Client;
import com.commerciale.model.Currency;
import com.commerciale.model.Deposit;
import com.commerciale.model.Invoice;
import com.commerciale.model.InvoiceProforma;
import com.commerciale.model.Product;
import com.commerciale.model.ProductGroup;
import com.commerciale.model.Reception;
import com.commerciale.model.SaleConditions;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;
import com.commerciale.model.user;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.apache.poi.hssf.eventusermodel.AbortableHSSFListener;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainMenu extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenu mnNewMenu_1;
	private JMenuItem mntmNewMenuItem_4;
	public static JDesktopPane desktopPane;//= new JDesktopPane();
	private Image imageBackGround;
	
	public MainMenu() {
		
		setMinimumSize(new Dimension(1000, 1000));
		setState(Frame.ICONIFIED);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.pack();
		MouseMotionListener [] i  = this.getMouseMotionListeners();
		 for (MouseMotionListener listener: i)
		      this.removeMouseMotionListener(listener);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocation(0, 0);
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			 Image imageLogo = ImageIO.read(new File("C:\\CommercialRessources\\res\\Logo.jpg")).getScaledInstance(120, 160, Image.SCALE_SMOOTH);
			setIconImage(imageLogo);
			imageBackGround = ImageIO.read(new File("C:\\CommercialRessources\\res\\Logo.jpg"));
			
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, "Logo introuvable (Logo.jpg) \n" +e1.getMessage());
		}
		
		setTitle("ManageMyBusiness version: 1.01");
		
		desktopPane = intializeDesktop(desktopPane, imageBackGround, 1400, 900);
		//desktopPane.setPreferredSize(new Dimension(900, 900));
		desktopPane.setSize(1000, 1000);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				 int confirm = JOptionPane.showOptionDialog(null,
	                        "Voulez vous vraiment quitter ?",
	                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
	                        JOptionPane.QUESTION_MESSAGE, null, null, null);
	                if (confirm == JOptionPane.YES_OPTION) {
	                    System.exit(1);
	                }
			}
			@Override
			public void windowOpened(WindowEvent e) {
				Session session = hibernateUtil.getSessionFactory().openSession();
				try {
					session.beginTransaction();
					List<Product> pd =  session.createQuery("from Product").list();
					session.getTransaction().commit();
				}catch (HibernateException ex ) {
					session.getTransaction().rollback();
					JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
				}
				finally {
					session.close();
				}
			}
		});
		
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//setSize(screenSize.width, screenSize.height);
		
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		desktopPane.setMinimumSize(new Dimension(1000, 1000));
		
		
		desktopPane.setBounds(0, 0, 2710, 1713);
		contentPane.add(desktopPane);
		desktopPane.setLayout(null);
	}

	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.setMinimumSize(new Dimension(1000, 2));
			menuBar.add(getMnNewMenu());
			menuBar.add(getMnNewMenu_1());
			
			JMenu mnNewMenu_2 = new JMenu("Export");
			menuBar.add(mnNewMenu_2);
			
			JMenuItem mntmNewMenuItem_6 = new JMenuItem("Facture");
			mntmNewMenuItem_6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Invoice invoice = new Invoice();
					InvoiceList invoiceList = InvoiceList.getInstance();
					CreateInvoice createInvoice = CreateInvoice.getInstance();
					CRUDinvoiceList crudInvoiceList = new CRUDinvoiceList(invoice, invoiceList);
					if (!invoiceList.isVisible()) {desktopPane.add(invoiceList);
														invoiceList.getFinalBtn().setEnabled(false);
														invoiceList.getReceptionBtn().setEnabled(true);
														invoiceList.setTitle("Liste des Factures");
														invoiceList.show();
					}
				}
			});
			
			JMenuItem mntmNewMenuItem_12 = new JMenuItem("Facture Proforma");
			mntmNewMenuItem_12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					InvoiceProforma invoiceProforma = new InvoiceProforma();
					InvoiceList invoiceList = InvoiceList.getInstance();
					//CreateInvoice createInvoice = CreateInvoice.getInstance();
					CRUDinvoiceListProforma crudInvoiceListProforma = new CRUDinvoiceListProforma(invoiceProforma, invoiceList);
					if (!invoiceList.isVisible()) {desktopPane.add(invoiceList);
														invoiceList.getFinalBtn().setEnabled(true);
														invoiceList.getReceptionBtn().setEnabled(false);
														invoiceList.setTitle("Liste des Factures Proforma");
														invoiceList.show();
				}
				}
			});
			mnNewMenu_2.add(mntmNewMenuItem_12);
			mnNewMenu_2.add(mntmNewMenuItem_6);
			
			JMenu mnNewMenu_3 = new JMenu("Caisse");
			menuBar.add(mnNewMenu_3);
			
			JMenuItem mntmNewMenuItem_7 = new JMenuItem("D\u00E9tail");
			mntmNewMenuItem_7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Deposit deposit = new Deposit();
					Client client = new Client();
					Invoice invoice = new Invoice();
					CreateDeposit createDeposit = CreateDeposit.getInstance();
					CRUDdeposit crudDeposit = new CRUDdeposit(deposit, createDeposit, invoice, client);
					if (!createDeposit.isVisible()) {desktopPane.add(createDeposit);
													 createDeposit.show();
					}
				}
			});
		
			mnNewMenu_3.add(mntmNewMenuItem_7);
			
			JMenu mnNewMenu_4 = new JMenu("Conditions");
			menuBar.add(mnNewMenu_4);
			
			JMenuItem mntmNewMenuItem_8 = new JMenuItem("Conditions de vente");
			mntmNewMenuItem_8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SaleConditions saleConditions = new SaleConditions();
					SaleConditionsList saleConditionsList = SaleConditionsList.getInstance();
					CRUDsaleConditions crudSaleConditions = new CRUDsaleConditions(saleConditions, saleConditionsList);
					if (!saleConditionsList.isVisible()) {desktopPane.add(saleConditionsList);
					                                      saleConditionsList.show();
}
				}
			});
			mnNewMenu_4.add(mntmNewMenuItem_8);
			
			JMenu mnNewMenu_6 = new JMenu("Rapports et Statistiques");
			menuBar.add(mnNewMenu_6);
			
			JMenuItem mntmNewMenuItem_10 = new JMenuItem("Statistiques");
			mntmNewMenuItem_10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Invoice invoice = new Invoice();
					InvoiceStatistics invoiceStatistics = InvoiceStatistics.getInstance();
					CRUDinvoiceStatistics crudInvoiceStatistics = new CRUDinvoiceStatistics(invoice, invoiceStatistics);
					if (!invoiceStatistics.isVisible()) {desktopPane.add(invoiceStatistics);
														 invoiceStatistics.show();
					}
				}
			});
			mnNewMenu_6.add(mntmNewMenuItem_10);
			
			JMenu mnNewMenu_5 = new JMenu("Calculations");
			menuBar.add(mnNewMenu_5);
			
			JMenuItem mntmNewMenuItem_9 = new JMenuItem("Calculatrice");
			mntmNewMenuItem_9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						Process proc = Runtime.getRuntime().exec("calc.exe");
					} catch (IOException e2) {						
						JOptionPane.showMessageDialog(null, "La calculatrice n'est pas disponible pour le moment ! \n"+e2.getMessage());
					}
				}
			});
			mnNewMenu_5.add(mntmNewMenuItem_9);
			
			JMenu mnNewMenu_7 = new JMenu("Param\u00E8tres");
			menuBar.add(mnNewMenu_7);
			
			JMenuItem mntmNewMenuItem_11 = new JMenuItem("Utilisateurs");
			mntmNewMenuItem_11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				user myUser = new user();
				createUser createuser = createUser.getInstance();
				CRUDuser crudUser = new CRUDuser(myUser, createuser);
				if (!(createuser.isVisible())) {createuser.setVisible(true);}
				}
			});
			mnNewMenu_7.add(mntmNewMenuItem_11);
		}
		return menuBar;
	}

	private JMenu getMnNewMenu() {
		if (mnNewMenu == null) {
			mnNewMenu = new JMenu("Fichier");
			mnNewMenu.add(getMntmNewMenuItem());
			mnNewMenu.add(getMntmNewMenuItem_1());
			mnNewMenu.add(getMntmNewMenuItem_2());
			mnNewMenu.add(getMntmNewMenuItem_3());
		}
		return mnNewMenu;
	}

	private JMenuItem getMntmNewMenuItem() {
		if (mntmNewMenuItem == null) {
			mntmNewMenuItem = new JMenuItem("Articles");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Product product = new Product();
					CreateProduct createProduct = CreateProduct.getInstance();
					Currency currency = new Currency();
					ProductGroup productGroup = new ProductGroup();
					CRUDproduct crudProduct = new CRUDproduct(product, createProduct, currency, productGroup );
					
					
					if (!createProduct.isVisible()) {desktopPane.add(createProduct);
														createProduct.show();}
					}
			});
		}
		return mntmNewMenuItem;
	}

	private JMenuItem getMntmNewMenuItem_1() {
		if (mntmNewMenuItem_1 == null) {
			mntmNewMenuItem_1 = new JMenuItem("Clients");
			mntmNewMenuItem_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Client client = new Client();
					CreateClient createClient = CreateClient.getInstance();
					CreateInvoice createInvoice = CreateInvoice.getInstance();
					CRUDclient crudClient = new CRUDclient(client, createClient, createInvoice);
					crudClient.Select("from Client");
					if (!createClient.isVisible()) {desktopPane.add(createClient);
													createClient.show();
					}
				}
			});
		}
		return mntmNewMenuItem_1;
	}
	private JMenuItem getMntmNewMenuItem_2() {
		if (mntmNewMenuItem_2 == null) {
			mntmNewMenuItem_2 = new JMenuItem("Fournisseurs");
			mntmNewMenuItem_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					callSupplier ();
				}
			});
		}
		return mntmNewMenuItem_2;
	}
	private JMenuItem getMntmNewMenuItem_3() {
		if (mntmNewMenuItem_3 == null) {
			mntmNewMenuItem_3 = new JMenuItem("Quitter");
			mntmNewMenuItem_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[] options = { "Quitter", "Annuler" };
					int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment Quitter ?", "Confirmation",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, options, options[0]);
					if (answer==0) {System.exit(0);}
				}
			});
		}
		return mntmNewMenuItem_3;
	}
	

	private JMenu getMnNewMenu_1() {
		if (mnNewMenu_1 == null) {
			mnNewMenu_1 = new JMenu("Import");
			mnNewMenu_1.add(getMntmNewMenuItem_4());
			
			JMenuItem mntmNewMenuItem_5 = new JMenuItem("Liste des Bons");
			mntmNewMenuItem_5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Reception reception = new Reception();
					ReceptionList receptionList = ReceptionList.getInstance();
					CreateReception createReception = CreateReception.getInstance();
					CRUDreceptionList crudReceptionList = new CRUDreceptionList(reception, receptionList);
					if (!receptionList.isVisible()) {desktopPane.add(receptionList);
														receptionList.show();
					}
				}
			});
			mnNewMenu_1.add(mntmNewMenuItem_5);
		}
		return mnNewMenu_1;
	}
	private JMenuItem getMntmNewMenuItem_4() {
		if (mntmNewMenuItem_4 == null) {
			mntmNewMenuItem_4 = new JMenuItem("Bon de Reception");
			mntmNewMenuItem_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Reception reception = new Reception();
					CreateReception createReception = CreateReception.getInstance();
					Currency currency = new Currency();
					Supplier supplier = new Supplier();
					CreateSupplier createSupplier = CreateSupplier.getInstance();
					CRUDsupplier crudSupplier = new CRUDsupplier(supplier, createSupplier,createReception);
					CRUDreception crudReception = new CRUDreception( reception, createReception, currency, crudSupplier);
					if (!createReception.isVisible()) {desktopPane.add(createReception);
													   createReception.show();
					}
				}
			});
		}
		return mntmNewMenuItem_4;
	}
	
	public void callSupplier () {
		Supplier supplier = new Supplier();
		CreateSupplier createSupplier = CreateSupplier.getInstance();
		CreateReception createReception = CreateReception.getInstance();
		CRUDsupplier crudSupplier = new CRUDsupplier(supplier, createSupplier,createReception);
		if (!createSupplier.isVisible()) {desktopPane.add(createSupplier);
										  createSupplier.show();
		}
	}
	
	
	private JDesktopPane intializeDesktop(JDesktopPane mydesktop,Image image,int scalx,int scaly) {

        // A specialized layered pane to be used with JInternalFrames
        mydesktop = new JDesktopPane() {
        //    ImageIcon icon = new ImageIcon(imagePath);
        //    Image image = icon.getImage();

            Image newimage = image.getScaledInstance(scalx, scaly, Image.SCALE_SMOOTH);

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(newimage, 0, 0, this);
            }
        };

        return mydesktop;
    }
}
