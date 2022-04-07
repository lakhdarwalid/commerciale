package com.commerciale.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Currency;
import com.commerciale.model.Product;
import com.commerciale.model.ProductGroup;
import com.commerciale.model.hibernateUtil;
import com.commerciale.report.PrintProduct;
import com.commerciale.report.ReportFrame;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.ThumbnailFileChooser;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateProduct;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;

public class CRUDproduct {
	private Product product;
	private CreateProduct createProduct;
	private Currency currency;
	private ProductGroup productGroup;
	private String addOrUpdate; 
	private String selected;
	private String path = ""; 
	private double change;

	public CRUDproduct(Product product, CreateProduct createProduct, Currency currency, ProductGroup productGroup) {
		this.product = product;
		this.createProduct = createProduct;
		this.currency = currency;
		this.productGroup = productGroup;
		this.createProduct.addingListener(new addProductListener());
		this.createProduct.cancelingListener(new cancelProductListener());
		this.createProduct.SavingListener(new saveProductListener());
		this.createProduct.updatingListener(new updateProductListener());
		this.createProduct.searchListener(new searchingProductListener());
		this.createProduct.searchBtnListener(new searchingbtnProductListener());
		this.createProduct.deleteBtnListener(new deletingProductListener());
		this.createProduct.imgProductListener(new imgProductMouseListener());
		this.createProduct.productGridListener(new gridNavigListener());
		this.createProduct.addCurrencyValueListener(new addExchangeValue());
		this.createProduct.printProductList(new printProductListener());
		this.createProduct.addnewProducutGroup(new addingProductGroup());
		this.createProduct.allNeddedWhenOpen(new behaveWhenOpen());
		this.createProduct.deleteProducutGroup(new DeletingGroupListener());
		this.createProduct.comboSearchingGroup(new SearchByGroup());
		this.createProduct.xlsImportingListener(new XLSImport());
	}
	
/********************Import excel file *****************/
class XLSImport implements ActionListener{
	ProductGroup groupToSave;
	byte[] data = null;
	List<byte[]> datas  = new ArrayList<byte[]>() ;
	int ImgIndex = 0;
	int numberOfGroup = 0;
	int numberOfProduct = 0;
	int numberOfGroupExisted = 0;
	int numberOfProductExisted = 0;
	@Override
	public void actionPerformed(ActionEvent e) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e1) {}
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("XLS", "XLSX");
		fileChooser.setFileFilter(extensionFilter);
		fileChooser.setDialogTitle("Selectionnez un fichier Excel");
		UIManager.put("FileChooser.cancelButtonText", "Annuler");
		UIManager.put("FileChooser.saveButtonText", "Sélectionner");
		fileChooser.updateUI();
		fileChooser.showDialog(null, "Selectionnez votre Fichier Excel ");
		File f = fileChooser.getSelectedFile();
		if (!(f==null)) {
		String myFilePath = f.getPath().toString();
		try {
			FileInputStream fis = new FileInputStream(new File(myFilePath));
			XSSFWorkbook book = new XSSFWorkbook(fis);
			/***************get all pictures in the file ************
			List lst = book.getAllPictures();
			for (Iterator it = lst.iterator(); it.hasNext(); ) {

			    PictureData pict = (PictureData)it.next();
			    data = pict.getData();
			   datas.add(data);
			}
			
			******************end list of pictures *****************/
			int rowcount = book.getSheetAt(0).getLastRowNum();
			for (int i=0 ; i<rowcount -1 ; i++) {
				
				if (book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString().isEmpty())
				{
					if (book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")).toString().isEmpty()) {continue;}
					else {
					Session session = hibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						List<ProductGroup> prdgrps = session.createQuery("from ProductGroup where desg = '"+ 
								book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")).toString()+"'").list();
						if (prdgrps.size()==0) {
							ProductGroup prdgrp = new ProductGroup();
						   prdgrp.setDesg(book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")).toString());
						   numberOfGroup ++;
						   session.save(prdgrp);
						   groupToSave = prdgrp;
						   prdgrps.clear();
						}else {groupToSave = prdgrps.get(0); numberOfGroupExisted++;}
						session.getTransaction().commit();
					} catch (HibernateException ex) {
						session.getTransaction().rollback();
						JOptionPane.showMessageDialog(null, "Erreur d'insertion d'une nouvelle famille: " + ex.getMessage());
					} finally {
						session.close();
					}
					
				}
				}else {
					
					Session session = hibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						List<Product> prdls = session.createQuery("from Product where desg = '"+
								  book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")).toString()+"'").list();
						if (prdls.size()==0) {
						Product prd = new Product();
						prd.setRef(book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString());
						prd.setDesg(book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("B")).toString());
						prd.setUnitPrice(Double.parseDouble(book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("C")).toString()));
						//prd.setImg(datas.get(ImgIndex));
						try {
							Path p = Paths.get("C:\\Image Article\\"+book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString()
									+".png");
							if(Files.exists(p)) {
							
						prd.setImg(Tools.preparingProductPic("C:\\Image Article\\"+book.getSheetAt(0).getRow(i).getCell(CellReference.convertColStringToIndex("A")).toString()
									              +".png"));}
						} catch(Exception ex) {prd.setImg(null);
							JOptionPane.showMessageDialog(null, "Fichier introuvable \n"+ ex.getMessage());}
						prd.setProductGroup(groupToSave);
						numberOfProduct++;
						session.save(prd);
						}
						else {numberOfProductExisted++; prdls.clear();}
						session.getTransaction().commit();
					} catch (HibernateException ex) {
						session.getTransaction().rollback();
						JOptionPane.showMessageDialog(null, "Erreur d'insertion d'une nouvelle famille: " + ex.getMessage());
					} finally {
						session.close();
					}
					ImgIndex++;
				}
			}
			
			book.close();
			fis.close();
			datas.clear();
			JOptionPane.showMessageDialog(null, "Opération terminée avec succèes : \n" 
											   +numberOfGroup+" Famille(s) ajoutée(s)"+"\n"
											   +numberOfProduct +" Article(s) ajouté(s)"+"\n"
											   +numberOfGroupExisted+" Famille(s) déja existée(s)"+"\n"	
											   +numberOfProductExisted+" Article(s) déja éxisté(s)");
		} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
		}				
			}
		try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Throwable e1) {}
		
	}
	
}
/********************end of xls import ***********************/
	
/**************Search By Group ***************/
class SearchByGroup implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String res = createProduct.getcomboSearchGroup().getSelectedItem().toString();
		if (!(res.equals("--Recherche par famille--"))) {
			createProduct.getProductGridModel().setRowCount(0);
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<ProductGroup> Pg =  session.createQuery("from ProductGroup where desg = '" + res + "'").list();
			fillGridSet(Pg.get(0).getProducts());
			createProduct.setSumProduct(Pg.get(0).getProducts().size());
			session.getTransaction().commit();
		}catch (HibernateException ex ) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de recherche :"+ex.getMessage());
		}
		finally {
			session.close();
		}
		
	}
	}
}
/**********End Of Search by Group ************/	
	
/********************Deleteing Group *********************/
class DeletingGroupListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		String res = createProduct.getcomboGroup().getSelectedItem().toString();
		if (!(res.equals("--Selection ou Ajouter une famille --"))) {
			Object[] options = { "confirmer", "Annuler" };
			int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			if (answer == 0) {
			Session session = hibernateUtil.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				List<ProductGroup> Pg =  session.createQuery("from ProductGroup where desg = '" + res + "'").list();
				session.delete(Pg.get(0));
				session.getTransaction().commit();
			}catch (HibernateException ex ) {
				session.getTransaction().rollback();
				JOptionPane.showMessageDialog(null, "Erreur de recherche :"+ex.getMessage());
			}
			finally {
				session.close();
			}
			}
			FillComboProductGroup();
		}else {JOptionPane.showMessageDialog(null, "Selectionnez une famille svp!!!");}
	}
}
/*****************end of deleting group ******************/
	
	class behaveWhenOpen extends CustomActionListeners{
		@Override
		public void internalFrameOpened(InternalFrameEvent e) {
			getCurrencyValue();
			Select("from Product");
			FillComboProductGroup();
			createProduct.getProductGridModel().fireTableDataChanged();
	}
}	
	/****************** PRINTING *****************/
	class printProductListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JasperPrint jasperPrint;
			PrintProduct printProduct = new PrintProduct();
			ReportFrame reportFrame = new ReportFrame();
			try {
				jasperPrint = printProduct.print("from Product", "C:\\CommercialRessources\\ProductReport.jrxml");
					//	"C:\\Users\\mehdiplay\\workspace\\Commerciale\\src\\com\\commerciale\\report\\ProductReport.jrxml");
				reportFrame.getContentPane().add(new JRViewer(jasperPrint));
				reportFrame.show();
			} catch (Throwable e1) {
				JOptionPane.showMessageDialog(null,e1.getMessage());
			}

		}

	}

	/***************** PINTING ENDS *************/
	/***************ADDING PRODUCT GROUP ************/
	class addingProductGroup implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String res = JOptionPane.showInputDialog(null, "Ajouter une famille ", "Nouvelle famille",
					JOptionPane.INFORMATION_MESSAGE);
			if (res != null) {
					Session session = hibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						productGroup.setDesg(res);
						session.save(productGroup);
						session.getTransaction().commit();
						createProduct.getcomboGroup().addItem(res);
						createProduct.getcomboSearchGroup().addItem(res);
					} catch (HibernateException ex) {
						session.getTransaction().rollback();
						JOptionPane.showMessageDialog(null, "Erreur d'insertion d'une nouvelle famille: " + ex.getMessage());
					} finally {
						session.close();
					}
				} 
		}

	}

	/***********END ADDING PRODUCT GROUP ************/
	/************ ADD ****************************/
	class addProductListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					((JTextField) comp).setEditable(true);
					((JTextField) comp).setText(null);
				}

			}
			btnSwitcher(true, false);
			addOrUpdate = "ADD";
			createProduct.getProductImageLabel().setIcon(null);
			createProduct.setUnitPrice("0,00");
			createProduct.setQuantity("0");
		}

	}

	/*********** CANCEL ***************************/
	class cancelProductListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					((JTextField) comp).setText(null);
					((JTextField) comp).setEditable(false);
					
					comp.setBackground(Color.WHITE);
				}
				if (comp instanceof JComboBox) {
					comp.setEnabled(false);
				}
			}
			btnSwitcher(false, true);
			addOrUpdate = "";
			createProduct.setUnitPrice("0,00");
			createProduct.setQuantity("0");
			createProduct.getcomboGroup().setSelectedItem("--Selection ou Ajouter une famille --");
		}

	}

	/************ SAVE ***************************/
	class saveProductListener implements ActionListener {
		private boolean empty = false;
		private byte[] productPic = null;
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					if (((JTextField) comp).getText().isEmpty()) {
						empty = true;
					}
				}
			}
			if ((empty)||(createProduct.getcomboGroup().getSelectedItem().toString().equalsIgnoreCase("--Selection ou Ajouter une famille --"))) {
				JOptionPane.showMessageDialog(null, "Veuillez remplisser toutes les cellules et choisir une famille pour votre article svp!!!");
				empty = false;
			} else { if (!(path.isEmpty())) {productPic = Tools.preparingProductPic(path);}
				if (addOrUpdate.equals("ADD")) {
					
					try { Insert(productPic);
					} catch (Throwable e1) {JOptionPane.showMessageDialog(null, "Erreur d'insertion \n"+e1.getMessage());}						
				}
				if (addOrUpdate.equals("UPDATE")) {
					try {
						Update(selected, productPic);
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(null, "Probleme de mise a jour d'un article" + e1.getMessage());
					}
				}
				btnSwitcher(false, true);
				
				for (Component comp : createProduct.getContainer().getComponents()) {
					if (comp instanceof JTextField) {
						((JTextField) comp).setText(null);
						((JTextField) comp).setEditable(false);
					
						comp.setBackground(Color.WHITE);
					}
					if (comp instanceof JComboBox) {
						comp.setEnabled(false);
					}
				}
				addOrUpdate = "";
				createProduct.getProductImageLabel().setIcon(null);
				createProduct.setUnitPrice("0,00");
				createProduct.setQuantity("0");
				createProduct.getcomboGroup().setSelectedItem("--Selection ou Ajouter une famille --");
			}

			Select("from Product");

		}

	}

	/**************** UPDATE **********************/
	class updateProductListener implements ActionListener {
		private boolean empty = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					if (((JTextField) comp).getText().isEmpty()) {
						empty = true;
					}
				}
			}
			if (empty) {
				JOptionPane.showMessageDialog(null,
						"Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le modifier !! ");
				empty = false;
			} else {
				for (Component comp : createProduct.getContainer().getComponents()) {
					if (comp instanceof JTextField) {
						((JTextField) comp).setEditable(true);
						comp.setBackground(Color.WHITE);
					}
				}
				btnSwitcher(true, false);
				addOrUpdate = "UPDATE";
			}
		}
	}

	/************* DELETE ***************************/
	class deletingProductListener implements ActionListener {
		private boolean empty = false;

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					if (((JTextField) comp).getText().isEmpty()) {
						empty = true;
					}
				}
			}
			if (empty) {
				JOptionPane.showMessageDialog(null,
						"Veuillez cliquer sur la liste des articles pour selectionner l'element que vous voulez le supprimer !! ");
				empty = false;
			} else {
				Object[] options = { "confirmer", "Annuler" };
				int answer = JOptionPane.showOptionDialog(null, "Voulez vous vraiment le supprimer ?", "Confirmation",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (answer == 0) {
					Delete(selected);
				}
				Select("from Product");
				for (Component comp : createProduct.getContainer().getComponents()) {
					if (comp instanceof JTextField) {
						((JTextField) comp).setText(null);

					}
				}
			}
		}
	}

	/************* SEARCH ***************************/
	class searchingProductListener extends CustomActionListeners {
		@Override
		public void keyReleased(KeyEvent e) {
			Select("from Product where ref LIKE '%" + createProduct.getTxtSearch() + "%'");
		}
	}

	class searchingbtnProductListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			createProduct.setTxtSearch(null);
			for (Component comp : createProduct.getContainer().getComponents()) {
				if (comp instanceof JTextField) {
					((JTextField) comp).setText(null);
				}
			}
			Select("from Product");
		}

	}

	class gridNavigListener extends CustomActionListeners {
		private String id;
		private int SR;

		@Override
		public void mouseClicked(MouseEvent e) {
			SR = createProduct.getProductGrid().getSelectedRow();
			id = createProduct.getProductGridModel().getValueAt(SR, 1).toString();
			selected = id;
			if (createProduct.getProductGrid().isEnabled()) {
				fillFields(id);
			}
		}
	}

	/***************************** add image class Listener ******************/
	class imgProductMouseListener extends CustomActionListeners {

		@Override
		public void mouseClicked(MouseEvent e) {
			try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable e1) {}
			JFileChooser TFC = new ThumbnailFileChooser();
			TFC.setDialogTitle("Selectionner une image");
	    	UIManager.put("ThumbnailFileChooser.cancelButtonText", "Annuler");
	    //	UIManager.put("JFileChooser.saveButtonText", "Selectionnez votre image");
	    //	TFC.updateUI();  
			FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Images", "JPEG", "JPG", "PNG");
			TFC.setFileFilter(extensionFilter); 
			TFC.showDialog(null, "Selectionnez votre image");
			try {
				File f = TFC.getSelectedFile();
				if(!(f==null)) {
				path = f.getPath().toString();
				createProduct.getProductImageLabel().setText(null);
				Image imageLogo = ImageIO.read(new File(path)).getScaledInstance(130, 120, Image.SCALE_SMOOTH);
				createProduct.getProductImageLabel().setIcon(new ImageIcon(imageLogo));}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Vous avez pas choisix une photo");
			}
			
			try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch (Throwable e2) {}
		}
	}

	/*************************** End of add image class Listener **************/

	class addExchangeValue implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String res = JOptionPane.showInputDialog(null, "Ajouter un taux de change ", "Nouveaux taux de change",
					JOptionPane.INFORMATION_MESSAGE);
			if (res != null) {
				try {
					double d = Double.parseDouble(res);
					Session session = hibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						currency.setMoney(d);
						session.save(currency);
						session.getTransaction().commit();
					} catch (HibernateException ex) {
						session.getTransaction().rollback();
						JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un taux de change : " + ex);
					} finally {
						session.close();
					}

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Enter a valid number ");
				}
			}
			getCurrencyValue();
			Select("from Product");
		}

	}

	/***********************************
	 * CRUD
	 **********************************************************************************************/
	/******* Insert 
	 * @throws Throwable 
	 * @throws ParseException */
	public void Insert(byte[] productPic) throws ParseException, Throwable {
		Session session = hibernateUtil.getSessionFactory().openSession(); 
		try {
			session.beginTransaction();
			List<ProductGroup> Pg =  session.createQuery("from ProductGroup where desg = '" + createProduct.getcomboGroup().getSelectedItem().toString() + "'").list();
			product.setRef(createProduct.getReference());
			product.setDesg(createProduct.getDesignation());
			product.setUnitPrice(Tools.decimalToDouble(createProduct.getUnitPrice()));
			product.setQuantity(Tools.decimalToInteger(createProduct.getQuantity()));
			product.setImg(productPic);
			product.setProductGroup(Pg.get(0));
			session.save(product);
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur d'insertion d'un article : " + ex);
		} finally {
			session.close();
		}

	}

	/******* Select */
	public void Select(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		createProduct.getProductGridModel().setRowCount(0);
		try {
			session.beginTransaction();
			List<Product> ar = session.createQuery(query).list();
			fillGrid(ar);
			session.getTransaction().commit();
			createProduct.setSumProduct(createProduct.getProductGridModel().getRowCount());
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex);
		} finally {
			session.close();
		}
	}

	/********
	 * update
	 * 
	 * @throws ParseException
	 */
	public void Update(String selected , byte[] pic) throws ParseException {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<Product> pr = session.createQuery("from Product where ref = '"+selected+"'").list();
			pr.get(0).setRef(createProduct.getReference());
			pr.get(0).setDesg(createProduct.getDesignation());
			pr.get(0).setUnitPrice((Tools.convertToNumber(createProduct.getUnitPrice())).doubleValue());
			pr.get(0).setQuantity(Integer.parseInt(createProduct.getQuantity()));
			if (!(pic == null)) {pr.get(0).setImg(pic);}
			session.update(pr.get(0));
			session.getTransaction().commit();
			JOptionPane.showMessageDialog(null, "Modification accomplie avec succès...");
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			session.close();
		}

	}

	/********* Delete */
	public void Delete(String selected) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<Product> ar = session.createQuery("from Product where ref ='"+ selected+"'").list();
			session.remove(ar.get(0));
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			session.close();
		}

	}

	/************************************
	 * End of CRUD
	 **************************************************************************************/

	public void fillFields(String row) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			List<Product> prd = session.createQuery("from Product where ref = '" + row + "'").list();
		//	for (Product tempProduct : prd) {
				createProduct.setReference(prd.get(0).getRef());
				createProduct.setDesignation(prd.get(0).getDesg());
				createProduct.setUnitPrice(printDecimal((prd.get(0).getUnitPrice())));
				createProduct.setQuantity(String.valueOf(prd.get(0).getQuantity()));
				if (!(prd.get(0).getImg()==null)){createProduct.setProductImageLabel(prd.get(0).getImg());}
				createProduct.getProductImageLabel().setText(null);
				createProduct.getcomboGroup().setSelectedItem(prd.get(0).getProductGroup().getDesg());
				session.getTransaction().commit();
		//	}
			createProduct.setSumProduct(createProduct.getProductGridModel().getRowCount());
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex);
		} finally {
			session.close();
		}

	}

	public void fillGrid(List<Product> li) {
		Object[] row = new Object[6];
		for (Product tempProduct : li) {
		//	row[0] = new ImageIcon(tempProduct.getImg());
			if (!(tempProduct.getImg() == null)) {
			try {
				row[0] = Tools.ImageModifier(tempProduct.getImg());
			} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
											+ e.getMessage());}}
			else {row[0] = null;}
			row[1] = tempProduct.getRef();
			row[2] = tempProduct.getDesg();
			row[3] = tempProduct.getQuantity();
			row[4] = printDecimal(tempProduct.getUnitPrice());
			row[5] = printDecimal(Tools.CurrencyConverter(tempProduct.getUnitPrice(), change));
			createProduct.getProductGridModel().addRow(row);
		}
	}

	public void fillGridSet(Set<Product> li) {
		Object[] row = new Object[6];
		for (Product tempProduct : li) {
			if (!(tempProduct.getImg() == null)) {
			try {
				row[0] = Tools.ImageModifier(tempProduct.getImg());
			} catch (Throwable e) {JOptionPane.showMessageDialog(null, "l'image de produit ne peut pas etre affichée :"+ "\n"
											+ e.getMessage());}}
			else {row[0] = null;}
			row[1] = tempProduct.getRef();
			row[2] = tempProduct.getDesg();
			row[3] = tempProduct.getQuantity();
			row[4] = printDecimal(tempProduct.getUnitPrice());
			row[5] = printDecimal(Tools.CurrencyConverter(tempProduct.getUnitPrice(), change));
			createProduct.getProductGridModel().addRow(row);
		}
	}
	
	
	public void btnSwitcher(boolean cs, boolean adu) {
		createProduct.getCancelbtn().setEnabled(cs);
		createProduct.getSavebtn().setEnabled(cs);
		createProduct.getUpdatebtn().setEnabled(adu);
		createProduct.getAddbtn().setEnabled(adu);
		createProduct.getDeletebtn().setEnabled(adu);
		createProduct.getProductGrid().setEnabled(adu);
		createProduct.getSearchbtn().setEnabled(adu);
		createProduct.getcomboGroup().setEnabled(cs);
		createProduct.getGroupAddbtn().setEnabled(cs);
		createProduct.getGroupDeletebtn().setEnabled(cs);
	}

	public String printDecimal(double number) {
		String result = new DecimalFormat("###,###.00").format(number);
		return result;
	}

public void getCurrencyValue() {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List <Currency> cu =  session.createQuery("from Currency").list();
		if (cu.size()<=0) {JOptionPane.showMessageDialog(null, "Le taux de change n'est pas initializer !!");}
		else {
			change = cu.get(cu.size()-1).getMoney();
			String money =printDecimal(change);
			createProduct.setChange(money);}
			session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
	}
	finally {
		session.close();
	}	
}

public void FillComboProductGroup() {
	createProduct.getcomboGroup().removeAllItems();
	createProduct.getcomboGroup().addItem("--Selection ou Ajouter une famille --");
	createProduct.getcomboSearchGroup().addItem("--Recherche par famille--");
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List <ProductGroup> PG =  session.createQuery("from ProductGroup").list();
		if (PG.size()<=0) {JOptionPane.showMessageDialog(null, "La liste des familles est vide !!");}
		else {for (ProductGroup pg: PG){createProduct.getcomboGroup().addItem(pg.getDesg());
										createProduct.getcomboSearchGroup().addItem(pg.getDesg());}}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}	


	

}
