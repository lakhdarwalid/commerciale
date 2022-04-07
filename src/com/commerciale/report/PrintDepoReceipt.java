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
import com.commerciale.model.Deposit;
import com.commerciale.model.Invoice;
import com.commerciale.model.InvoiceProforma;
import com.commerciale.model.LineProduct;
import com.commerciale.model.LineProductProforma;
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

public class PrintDepoReceipt {
	private List<Deposit> deposits;
    private List<Client> clients  = new ArrayList<Client>();
 
    

	public List<Deposit> Select(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			deposits = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex.getMessage());
		} finally {
			session.close();
		}
		return deposits;
	}

	

	public JasperPrint print(String query, String outputFile) throws Throwable {
		deposits = Select(query);
		clients.add(deposits.get(0).getClient());

		JRBeanCollectionDataSource ItemsJRBean = new JRBeanCollectionDataSource(deposits);
		JRBeanCollectionDataSource ItemsJRBean1 = new JRBeanCollectionDataSource(clients);
		JRBeanCollectionDataSource ItemsJRBean2 = new JRBeanCollectionDataSource(clients);
		JRBeanCollectionDataSource ItemsJRBean3 = new JRBeanCollectionDataSource(clients);
		Map<String, Object> myParameters = new HashMap<String, Object>();
		myParameters.put("CollectCust", ItemsJRBean1);
		myParameters.put("CollectCustName", ItemsJRBean2);
		myParameters.put("CollectCustBanq", ItemsJRBean3);
		InputStream input = new FileInputStream(new File(outputFile));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, myParameters, ItemsJRBean);
		return jasperPrint;
	}

}
