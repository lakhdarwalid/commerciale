package com.commerciale.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Policy.Parameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Product;
import com.commerciale.model.hibernateUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrintProduct {
	private List<Object> obj;
	
	public List Select (String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
		    obj =  session.createQuery(query).list();
			session.getTransaction().commit();
		}catch (HibernateException ex ) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex);
		}
		finally {
			session.close();
		}
		return obj;
	}
	
	public JasperPrint print(String query, String outputFile) throws Throwable {
	obj = Select(query);
	JRBeanCollectionDataSource ItemsJRBean = new JRBeanCollectionDataSource(obj);
	
	Map<String, Object> myParameters = new HashMap<String, Object>();
	 myParameters.put("CollectionPrdParm", ItemsJRBean);
	 
	 InputStream input = new FileInputStream(new File(outputFile)); 
	 JasperDesign jasperDesign = JRXmlLoader.load(input);
	 JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
	 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, myParameters, new JREmptyDataSource());
	// JasperViewer.viewReport(jasperPrint);
	 return jasperPrint;
	}
	
}
