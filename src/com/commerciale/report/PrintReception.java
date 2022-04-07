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

import com.commerciale.model.Product;
import com.commerciale.model.Reception;
import com.commerciale.model.ReceptionProduct;
import com.commerciale.model.Supplier;
import com.commerciale.model.hibernateUtil;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class PrintReception {
	private List<Reception> receptions;
	private List<ReceptionProduct> receptionProducts;
    private List<Supplier> suppliers  = new ArrayList<Supplier>();
    private List<Product> products  = new ArrayList<Product>();

	public List Select(String query) {
		Session session = hibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			receptions = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			JOptionPane.showMessageDialog(null, "Erreur de selection :" + ex.getMessage());
		} finally {
			session.close();
		}
		return receptions;
	}

	public JasperPrint print(String query, String outputFile) throws Throwable {
		receptions = Select(query);
		receptionProducts = receptions.get(0).getReceptionProduct();
		suppliers.add(receptions.get(0).getSupplier());
		for (ReceptionProduct rp : receptionProducts) {
			products.add(rp.getProduct());
		}
		JRBeanCollectionDataSource ItemsJRBean = new JRBeanCollectionDataSource(receptions);
		JRBeanCollectionDataSource ItemsJRBean1 = new JRBeanCollectionDataSource(receptionProducts);
		JRBeanCollectionDataSource ItemsJRBean2 = new JRBeanCollectionDataSource(suppliers);
		JRBeanCollectionDataSource ItemsJRBean3 = new JRBeanCollectionDataSource(products);
	    Map<String, Object> myParameters = new HashMap<String, Object>();
		myParameters.put("CollectionRecPrdParm", ItemsJRBean1);
		myParameters.put("CollectSup", ItemsJRBean2);
		myParameters.put("CollectPrd", ItemsJRBean3);
		InputStream input = new FileInputStream(new File(outputFile));
		JasperDesign jasperDesign = JRXmlLoader.load(input);
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, myParameters, ItemsJRBean);
		return jasperPrint;
	}

}
