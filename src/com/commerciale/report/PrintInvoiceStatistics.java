package com.commerciale.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.InvStat;
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

public class PrintInvoiceStatistics {
	private List<Invoice> invoices;
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
		double tva = 0, tvaImp = 0 , majoration = 0, dep = 0, retirement = 0, ttc = 0; 
		Date startDate = null , endDate = null;
		//conditions = SelectCond("from SaleConditions");
		invoices = Select(query);
		List<InvStat> statistics = new ArrayList<InvStat>();
		for (Invoice inv : invoices) {
			tva = tva+inv.getTva();
			tvaImp = tvaImp + inv.getTvaImp();
			majoration = majoration + inv.getProfit();
			dep = dep + inv.getPrecompteDouane();
			retirement = retirement + inv.getRetirement();
			ttc = ttc + inv.getTotal();
		}
		startDate = invoices.get(0).getTodayDate();
		endDate = invoices.get(invoices.size()-1).getTodayDate();
		InvStat invStat = new InvStat(tva, tvaImp, majoration, dep, retirement, ttc, startDate, endDate);
		statistics.add(invStat);
		JRBeanCollectionDataSource ItemsJRBean = new JRBeanCollectionDataSource(invoices);
		JRBeanCollectionDataSource ItemsJRBean1 = new JRBeanCollectionDataSource(statistics);
	    Map<String, Object> myParameters = new HashMap<String, Object>();
	    
		myParameters.put("CollectInvoices", ItemsJRBean);
		InputStream input = new FileInputStream(new File(outputFile));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, myParameters, ItemsJRBean1);
		statistics.clear();
		return jasperPrint;
	}

}
