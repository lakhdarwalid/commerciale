package com.commerciale.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.commerciale.model.DataBackRes;
import com.commerciale.model.hibernateUtil;
import com.commerciale.view.CreateReception;
import com.commerciale.view.loginfrm;

public class Tools {
	private static String BackupResPath = "";

	public static double CurrencyConverter (double cur1 , double cur2) {
		 return cur1 * cur2;
	 }
	
	 public static double calculTva(double montant) {
		 return (montant*19/100);
	 }
	 
	 public static double calculDifferenceTva(double tvaSale, double tvaBuy) {
		 return tvaSale - tvaBuy; 
	 }

	 public static String printDecimal(Number number) {
		 String result  = new DecimalFormat("###,###.00").format(number);
		 return result;
	 }
	 
	 public static String printDecimalPoint(double number) {
		 String result  = new DecimalFormat("######.00").format(number);
		 return result;
	 }
	 
	 public static Number convertToNumber(String str) throws ParseException {
		 DecimalFormat num = new DecimalFormat();
		 return num.parse(str);
	 }
	 
	 public static double decimalToDouble(String str) throws Throwable, ParseException{
	
			return Double.parseDouble(String.valueOf(convertToNumber(str)));
		
	 }
	 
	 public static int decimalToInteger(String str) throws Throwable, ParseException{
			
			return Integer.parseInt(String.valueOf(convertToNumber(str)));
		
	 }
	 
	 public static byte[] preparingProductPic(String picPath) {
			File file = new File(picPath);
			byte[] productPicture  = new byte[(int)file.length()];
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(productPicture);
				fileInputStream.close();
			}catch(Exception ex) {JOptionPane.showMessageDialog(null, "Erreur de transfer de l'image"+ex.getMessage());}
			return productPicture;
			
		}
	 
	 public static byte[] preparingProductPic(File file) {
			byte[] productPicture  = new byte[(int)file.length()];
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				fileInputStream.read(productPicture);
				fileInputStream.close();
			}catch(Exception ex) {JOptionPane.showMessageDialog(null, "Erreur de transfer de l'image"+ex.getMessage());}
			return productPicture;
			
		}
	 
	 public static byte[] imageToByte(ImageIcon icon) {
		 byte[] bytes = null;
		 BufferedImage img1 = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		 Graphics2D g2d = img1.createGraphics();
		 icon.paintIcon(null, g2d, 0, 0);
		 g2d.dispose();

		 try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		     ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
		     try {
		         ImageIO.write(img1, "png", ios);
		     } finally {
		         ios.close();
		     }
		      bytes = baos.toByteArray();
		 } catch (IOException ex) {
		     ex.printStackTrace();
		 }
		 return bytes;
	    }
	 
	 public static String dateToString (Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(date);
	 }
	 
	 public static String dateToStringFlipped (Date date) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(date);
		 } 
	 
	 public static Date formatDate(Date date) throws ParseException{
		// SimpleDateFormat date=new SimpleDateFormat("yyyy/MM/dd");
		// return date.parse(str);
		 DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		 Date dateWithZeroTime = formatter.parse(formatter.format(date));
		 return dateWithZeroTime;
	 }
	 
	 public static Date stringToDate(String str) throws ParseException {
		 Date date=new SimpleDateFormat("dd-MM-yyyy").parse(str);
		 return date;
	 }
	 
	 public static ImageIcon ImageModifier(byte[] img) throws Throwable {
			ByteArrayInputStream myImg = new ByteArrayInputStream(img);
			Image productImg = ImageIO.read(myImg).getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			return new ImageIcon(productImg);
		}
	 
	 public static String numberToWords(double num) {
		 long result = 0, resultDiv = 0;
		 long [] divisionaire = {(long) 1000000000, (long) 1000000, (long) 1000, (long) 100, (long) 10, (long) 1};
		 String tempPhrase = "", phrase = "", Num1 ="", Num2="", finalPhrase="";
		 String[] strNum1 = null;
		 Map<String, Long> group = new HashMap<String, Long>();
		 Map<String , String> phraseMap = new HashMap<String, String>();
		 Map<Integer , String> numbersNames = new HashMap<Integer, String>();
		 numbersNames.put(0, "ZERO");
		 numbersNames.put(1, "UN");
		 numbersNames.put(2, "DEUX");
		 numbersNames.put(3, "TROIS");
		 numbersNames.put(4, "QUATRE");
		 numbersNames.put(5, "CINQ");
		 numbersNames.put(6, "SIX");
		 numbersNames.put(7, "SEPT");
		 numbersNames.put(8, "HUIT");
		 numbersNames.put(9, "NEUF");
		 numbersNames.put(10, "DIX");
		 numbersNames.put(11, "ONZE");
		 numbersNames.put(12, "DOUZE");
		 numbersNames.put(13, "TREISE");
		 numbersNames.put(14, "QUATORZE");
		 numbersNames.put(15, "QUINZE");
		 numbersNames.put(16, "SEIZE");
		 numbersNames.put(17, "DIX-SEPT");
		 numbersNames.put(18, "DIX-HUIT");
		 numbersNames.put(19, "DIX-NEUF");
		 numbersNames.put(20, "VINGT");
		 numbersNames.put(30, "TRENTE");
		 numbersNames.put(40, "QUARANTE");
		 numbersNames.put(50, "CINQUANTE");
		 numbersNames.put(60, "SOIXANTE");
		 numbersNames.put(70, "SOIXANTE-DIX");
		 numbersNames.put(80, "QUATRE-VINGTS");
		 numbersNames.put(90, "QUATRE-VINGT-DIX");
		 numbersNames.put(100, "CENT");
		 numbersNames.put(1000, "MILLE");
		 numbersNames.put(1000000, "MILLION");
		 numbersNames.put(1000000000, "MILLIARD");
		 DecimalFormat df = new DecimalFormat("#.##");
	     df.setMaximumFractionDigits(2);
		 String myNum =df.format(num);
		 System.out.println(myNum);
		 if(myNum.contains(".")) {
			 strNum1 = myNum.split("\\.");
		//	 Num1 = strNum1[0];
		//	 Num2 = strNum1[1]; 
			 if (strNum1[1].length()==1) {strNum1[1] = strNum1[1]+"0";}}
		 else {strNum1[0] = myNum; strNum1[1] ="00";}
		 System.out.println("Num1 = "+strNum1[0]+"   , Num2= "+strNum1[1]);
		 for (int j=0; j<2; j++) {
		 long myNumber = Long.parseLong(strNum1[j]);
		 for (int i = 0; i<6; i++) {
			 result = (myNumber/divisionaire[i]);
			 if (result >0) {
				 int div = (int)divisionaire[i];
				 if (divisionaire[i] ==10) {group.put((String)numbersNames.get(div), myNumber);}
				 else { group.put((String)numbersNames.get(div), result);}
				 myNumber = myNumber - (result * divisionaire[i]) ;
			 }
		 }
		 System.out.println("group = "+group.toString());

		for (Map.Entry<String, Long> entry : group.entrySet()) {	 
		 String category = entry.getKey(); 
		 myNumber = (entry.getValue()); 
		 long tempNumber = myNumber;
	     for (int i = 3; i<6 ; i++) {
			 result = (myNumber/divisionaire[i]);
			  if (result > 0) {
			 int res =(int)result;
			 int div = (int)divisionaire[i];
			 if ((category=="CENT")&&(tempNumber==1)) {tempPhrase = tempPhrase +"CENT";}
			 else if ((category=="MILLE")&&(tempNumber==1)) {tempPhrase = tempPhrase +"MILLE ";}
			 else if (((divisionaire[i]==100)||(divisionaire[i]==1000))&&(result==1)){
	    	      tempPhrase =tempPhrase +numbersNames.get(div)+" ";  
	    	      myNumber =myNumber - ( result * divisionaire[i] );
				 }
			 else if (divisionaire[i]==10) {div = (int) (result * 10);
			    	    if (numbersNames.containsKey((int)myNumber)) {
			    	       tempPhrase =tempPhrase+numbersNames.get((int)myNumber) +" ";
			    	       myNumber = 0;
			    	    }else {
			    	    	if ((result == 7)||(result ==9)) {
			    	    		tempPhrase =tempPhrase +numbersNames.get(div-10);
			    	    		myNumber = myNumber - (result * 10);
					    	       if ((myNumber>0) && ((result == 7)||(result ==9))) {
					    	    	   tempPhrase =tempPhrase +"-"+numbersNames.get((int)(myNumber+10)) +" ";
					    	      myNumber = 0;
					    	       }
			    	    	}
			    	    	else {
			    	       tempPhrase =tempPhrase +numbersNames.get(div) +" ";//+"-"+numbersNames.get((int)myNumber); 
			    	       myNumber = myNumber - (result * 10);
			    	      
			    	       }
			    	    	   
			    	    }
		     	   
			 		}
			 else if (divisionaire[i]==1) { 
				 
				   tempPhrase =tempPhrase +numbersNames.get((int)myNumber)+" ";  
			 }
		     else {	
		     		tempPhrase =tempPhrase + numbersNames.get(res)+" "+numbersNames.get(div)+" " ; 
		     		myNumber = myNumber - (result * divisionaire[i]);
		     	   }
			}
			}
			  phraseMap.put(category, tempPhrase);
		
		 tempPhrase = "";
		}

	if (Long.parseLong(strNum1[j]) == 0) {phrase = "ZERO ";}	
	if (phraseMap.containsKey("MILLIARD")) {phrase = phrase+phraseMap.get("MILLIARD") + "MILLIARD "; }
	if (phraseMap.containsKey("MILLION")) {phrase = phrase +phraseMap.get("MILLION") +"MILLION "; }
	if (phraseMap.containsKey("MILLE")) {
		if (group.get("MILLE")==1) {phrase = phrase +phraseMap.get("MILLE");}
		else {phrase = phrase +phraseMap.get("MILLE") +"MILLE "; }}
	if (phraseMap.containsKey("CENT")) {
		if (group.get("CENT")==1) {phrase = phrase +phraseMap.get("CENT")+" "; }
		else {phrase = phrase +phraseMap.get("CENT")+"CENT "; }}					
	if (phraseMap.containsKey("DIX")) {phrase = phrase +phraseMap.get("DIX"); }
	else {
	if (phraseMap.containsKey("UN")) {phrase = phrase +phraseMap.get("UN"); }}
	     if (j==0) {finalPhrase = phrase+ "DINAR(S)";}
	     else if (j==1 ) {finalPhrase = finalPhrase + " ET "+phrase +"CENTIME(s)."; }
	     phrase="";
	     group.clear();
	     phraseMap.clear();
		 }
		 
		return finalPhrase;
				//(phrase+"DINAR(S) ET " + Num2+" CENTIME(s).");
		
	 }
	 
	 
	 /***********************        BACK UP DATA ***********************************/
	 public static void dataBackUp() {
		 Date todayDate = new Date();
		 Session session = hibernateUtil.getSessionFactory().openSession();
		  try {
			 session.beginTransaction();
			 List <DataBackRes> dataBack = session.createQuery("from DataBackRes").list();
			 if (dataBack.size()>0) {
				 Date lastBackupDate = dataBack.get(dataBack.size()-1).getBackUpDate();
				 long diff = todayDate.getTime() - todayDate.getTime(); 
				 long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				// if (days>6) {
				
				 loginfrm.lblBackupComment.setText("Veuillez patienter svp  le systeme est entrain de cr\u00E9er une sauvegarde de donn\u00E9es ...");
				// JOptionPane.showMessageDialog(null, "patienter"); 
				 sqlDumbDB();
				     DataBackRes dt = new DataBackRes();
					 dt.setBackUpDate(new Date());
					 dt.setBackFilePath(BackupResPath);
					 session.save(dt);
			//	 }
			 }
			 else {
				 loginfrm.lblBackupComment.setText("Veuillez patienter svp le systeme est entrain de cr\\u00E9er une sauvegarde de donn\\u00E9es ...");
				 sqlDumbDB();
				 DataBackRes dt = new DataBackRes();
				 dt.setBackUpDate(new Date());
				 dt.setBackFilePath(BackupResPath);
				 session.save(dt);
			 }
			 session.getTransaction().commit();
		 }catch (HibernateException ex) {
			 session.getTransaction().rollback();
			 ex.printStackTrace();
		 }finally {
			 session.close();
		 }
	}
	 
	public static void sqlDumbDB() {
		String todayDate = dateToString(new Date());
		String path ="E:/CommercialRessourcesSauvegardes/Sauvegarde_" + todayDate +".sql";
	    BackupResPath = path;
		Process p = null;// 
		try {
			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec("C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump.exe -uroot -pwalid --add-drop-database -B comerciale -r"+path);
			int processComplete = p.waitFor();
			if(processComplete == 0) {loginfrm.lblBackupComment.setText("Sauvegard a ete crée ...");loginfrm.lblBackupComment.setForeground(Color.GREEN);}
			else {loginfrm.lblBackupComment.setText("Sauvegard n'a pas ete crée ...");loginfrm.lblBackupComment.setForeground(Color.RED);}
		}catch (Exception e) {e.printStackTrace();
			
		}
	}
	
	static Thread CommentLoader  = new Thread(new Runnable() {
		@Override
		public void run(){
			loginfrm.lblBackupComment.setVisible(true);			
		}
	});
}