package com.commerciale.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.Invoice;
import com.commerciale.model.LineProduct;
import com.commerciale.model.Product;
import com.commerciale.model.SaleConditions;
import com.commerciale.model.hibernateUtil;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class PrintInvoice {
	private List<Invoice> invoices;
	private List<LineProduct> lineProducts;
    private List<Client> clients  = new ArrayList<Client>();
    private List<Product> products  = new ArrayList<Product>();
    private List<SaleConditions> conditions;

	public List<Invoice> Select(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			invoices = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex.getMessage());
		} finally {
			session.close();
		}
		return invoices;
	}
	
	public List<SaleConditions> SelectCond(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			conditions = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex.getMessage());
		} finally {
			session.close();
		}
		return conditions;
	}
	

	public JasperPrint print(String query, String outputFile) throws Throwable {
		conditions = SelectCond("from SaleConditions");
		invoices = Select(query);
		lineProducts = invoices.get(0).getLineProduct();
		clients.add(invoices.get(0).getClient());
		for (LineProduct lp : lineProducts) {
			products.add(lp.getProduct());
		}
		JRBeanCollectionDataSource ItemsJRBean = new JRBeanCollectionDataSource(invoices);
		JRBeanCollectionDataSource ItemsJRBean1 = new JRBeanCollectionDataSource(lineProducts);
		JRBeanCollectionDataSource ItemsJRBean2 = new JRBeanCollectionDataSource(clients);
		JRBeanCollectionDataSource ItemsJRBean3 = new JRBeanCollectionDataSource(products);
		JRBeanCollectionDataSource ItemsJRBean4 = new JRBeanCollectionDataSource(conditions);
	    Map<String, Object> myParameters = new HashMap<String, Object>();
		myParameters.put("CollectionLinePrdParm", ItemsJRBean1);
		myParameters.put("CollectCust", ItemsJRBean2);
		myParameters.put("CollectPrd", ItemsJRBean3);
		myParameters.put("CollectConditions", ItemsJRBean4);
		InputStream input = new FileInputStream(new File(outputFile));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, myParameters, ItemsJRBean);
		return jasperPrint;
	}

}
