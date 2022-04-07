package com.commerciale.model;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



public class hibernateUtil {
	private  static SessionFactory sessionFactory ;
	static {
		try {
		sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		}
	 catch (Throwable exc) {JOptionPane.showMessageDialog(null, exc.getMessage());
		throw new ExceptionInInitializerError(exc);
	}
	}
	public static SessionFactory getSessionFactory() {
		
		return sessionFactory;
	}

}
