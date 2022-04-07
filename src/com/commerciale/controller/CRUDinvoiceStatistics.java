package com.commerciale.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.Client;
import com.commerciale.model.Invoice;
import com.commerciale.model.hibernateUtil;
import com.commerciale.report.PrintInvoice;
import com.commerciale.report.PrintInvoiceStatistics;
import com.commerciale.report.ReportFrame;
import com.commerciale.service.CustomActionListeners;
import com.commerciale.service.Tools;
import com.commerciale.view.CreateInvoice;
import com.commerciale.view.InvoiceStatistics;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class CRUDinvoiceStatistics {
	private Invoice invoice;
	private InvoiceStatistics invoiceStatistics;
	private CreateInvoice createInvoice;
	private String addOrUpdate;
	private int selected = -1;
	private int SR;
	private String path = "";
	private String inv_Type="";
	private String queryToPrint = "";
	public CRUDinvoiceStatistics(Invoice invoice, InvoiceStatistics invoiceStatistics) {
		this.invoice = invoice;
		this.invoiceStatistics = invoiceStatistics;
		this.invoiceStatistics.searchListener(new searchingInvoiceStatisticsener());
		this.invoiceStatistics.allNeddedWhenOpen(new behaveWhenOpen());
		this.invoiceStatistics.printingbtnListener(new printingListener());
		this.invoiceStatistics.searhByDateListener(new SearchByDateListener());
		this.invoiceStatistics.searchingAllInvoices(new SearchAllInvoices());
	}
	
	/**********PRINTING *************************/
class printingListener extends CustomActionListeners{

	@Override
	public void mouseClicked(MouseEvent e) {
      if (invoiceStatistics.getInvoiceGrid().getRowCount()>0) {
		JasperPrint jasperPrint;
	 
		PrintInvoiceStatistics printInvoiceStatistics = new PrintInvoiceStatistics();
		
		try {
			jasperPrint = printInvoiceStatistics.print(queryToPrint,"C:\\CommercialRessources\\InvoiceStatisticsReport.jrxml");
			ReportFrame reportFrame = new ReportFrame();
			reportFrame.getContentPane().add(new JRViewer(jasperPrint));
		    reportFrame.show();
		} catch (Throwable e1) {JOptionPane.showMessageDialog(null,"Erreur d'impression ... \n"+ e1.getMessage());}
	} else JOptionPane.showMessageDialog(null, "Aucune données à Imprimer !!!");
 }
} 


/******************* ON WINDOW OPEN BEHAVIOR ***************/
 class behaveWhenOpen extends CustomActionListeners{

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		try {
			Select("from Invoice");
			queryToPrint = "from Invoice";
			Statistics();
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		
	}
 }
/**********************BEHAVIOR ENDS ***********************/
	


/*************SEARCH ***************************/
/**************SEARCHING BY DATE **********************/
class SearchByDateListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			String startDate = Tools.dateToStringFlipped(invoiceStatistics.getStartDate());
			String endDate = Tools.dateToStringFlipped(invoiceStatistics.getEndDate());
			Select("from Invoice where todayDate BETWEEN '"+startDate+"' AND '"+endDate+"' ");
				queryToPrint = "from Invoice where todayDate BETWEEN '"+startDate+"' AND '"+endDate+"' " ;  

			
			Statistics();
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"Selectionez une periode SVP!! \n" +e1.getMessage());
		}
		
	}
	
}	
/***************SEARCHING ENDS ************************/
	
class searchingInvoiceStatisticsener extends CustomActionListeners{
	@Override
	public void keyReleased(KeyEvent e) {
		try {
			SelectClient("from Client where name LIKE '%"+invoiceStatistics.getTxtSearch()+"%'");
			queryToPrint ="from Client where name LIKE '%"+invoiceStatistics.getTxtSearch()+"%'" ;  
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(null,"hna "+ e1.getMessage());
		}
	}
}

class SearchAllInvoices implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
    
			try {
				Select("from Invoice");
				Statistics();	
			} catch (Throwable e1) {JOptionPane.showMessageDialog(null, e1.getMessage());
			}
	}
}


 /***********************************CRUD **********************************************************************************************/

/*******Select
 * @throws Throwable */
public void Select (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Invoice> ar =  session.createQuery(query).list();
		if (ar.size()> 0) {
		fillInvoiceGrid(ar);}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}

public void SelectClient (String query) throws Throwable {
	Session session = hibernateUtil.getSessionFactory().openSession();
	try {
		session.beginTransaction();
		List<Client> cl =  session.createQuery(query).list();
		if (cl.size()>0) {
		fillInvoiceGrid(cl.get(0).getInvoices());}
		session.getTransaction().commit();
	}catch (HibernateException ex ) {
		session.getTransaction().rollback();
		JOptionPane.showMessageDialog(null, "Erreur de selection :"+ex.getMessage());
	}
	finally {
		session.close();
	}
}



/************************************End of CRUD 
 * @throws Throwable **************************************************************************************/

public void fillInvoiceGrid (List<Invoice>  li) throws Throwable {
	Object[] row = new Object[12];
	if (invoiceStatistics.getInvoiceGridModel().getRowCount()>0) {invoiceStatistics.getInvoiceGridModel().setRowCount(0);}
	if (li.size()>0) {
		Collections.reverse(li);
	for (Invoice tempInvoice : li) {
		row[0] = tempInvoice.getRef();
		row[1] = tempInvoice.getClient().getName();
		row[2] = Tools.dateToString(tempInvoice.getTodayDate());
		row[3] = Tools.printDecimal(tempInvoice.getPrecompteDouane());
		row[4] = Tools.printDecimal(tempInvoice.getTva());
		row[5] = Tools.printDecimal(tempInvoice.getTotal());
		row[6] = tempInvoice.getProfitPercent();
		row[7] = Tools.printDecimal(tempInvoice.getProfit());
		row[8] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[9] = Tools.printDecimal(tempInvoice.getRetirement());
		row[10] = Tools.printDecimal(tempInvoice.getTvaImp());
		row[11] = Tools.printDecimal(tempInvoice.getTaxDomic());
		invoiceStatistics.getInvoiceGridModel().addRow(row);
	}
	invoiceStatistics.setGridRowCount(invoiceStatistics.getInvoiceGrid().getRowCount());
	}
}

public void fillInvoiceGrid (Set<Invoice>  li) throws Throwable {
	Object[] row = new Object[12];
	if (invoiceStatistics.getInvoiceGridModel().getRowCount()>0) {invoiceStatistics.getInvoiceGridModel().setRowCount(0);}
	if (li.size()>0) { 
	List<Invoice> tempList = new ArrayList<Invoice>();
	tempList.addAll(li);
	Collections.reverse(tempList);
	for (Invoice tempInvoice : tempList) {
		row[0] = tempInvoice.getRef();
		row[1] = tempInvoice.getClient().getName();
		row[2] = Tools.dateToString(tempInvoice.getTodayDate());
		row[3] = Tools.printDecimal(tempInvoice.getPrecompteDouane());
		row[4] = Tools.printDecimal(tempInvoice.getTva());
		row[5] = Tools.printDecimal(tempInvoice.getTotal());
		row[6] = tempInvoice.getProfitPercent();
		row[7] = Tools.printDecimal(tempInvoice.getProfit());
		row[8] = Tools.printDecimal(tempInvoice.getTotalHt());
		row[9] = Tools.printDecimal(tempInvoice.getRetirement());
		row[10] = Tools.printDecimal(tempInvoice.getTvaImp());
		row[11] = Tools.printDecimal(tempInvoice.getTaxDomic());
		invoiceStatistics.getInvoiceGridModel().addRow(row);
	}
	invoiceStatistics.setGridRowCount(invoiceStatistics.getInvoiceGrid().getRowCount());
	}
}

public void Statistics() {
	double tva = 0, tvaImp = 0 , majoration = 0, dep = 0, retirement = 0, ttc = 0, domic = 0; 
  if (invoiceStatistics.getInvoiceGrid().getRowCount()> 0) {
	for (int i=0 ; i<invoiceStatistics.getInvoiceGrid().getRowCount(); i++) {
		try {
			tva  = tva + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 4).toString());
			tvaImp  = tvaImp + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 10).toString());
			majoration  = majoration + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 7).toString());
			dep  = dep + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 3).toString());
			retirement  = retirement + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 9).toString());
			ttc  = ttc + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 5).toString());
			domic  = domic + Tools.decimalToDouble(invoiceStatistics.getInvoiceGridModel().getValueAt(i, 11).toString());
		} catch (Throwable e) {JOptionPane.showMessageDialog(null, "Probleme de calculation des sommes "+ e.getMessage());}
  }
	}
	invoiceStatistics.setSumDep(Tools.printDecimal(dep));
	invoiceStatistics.setSumMaj(Tools.printDecimal(majoration));
	invoiceStatistics.setSumTva(Tools.printDecimal(tva));
	invoiceStatistics.setTvaImpo(Tools.printDecimal(tvaImp));
	invoiceStatistics.setTaxRetirement(Tools.printDecimal(retirement));
	invoiceStatistics.setGrossSale(Tools.printDecimal(ttc));
	invoiceStatistics.setDomic(Tools.printDecimal(domic));
	
}

}
