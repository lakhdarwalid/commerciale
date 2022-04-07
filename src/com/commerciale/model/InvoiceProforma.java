package com.commerciale.model;



import java.util.*;

import javax.persistence.*;

import com.commerciale.service.Tools;

@Entity
@Table (name = "invoiceproforma")
public class InvoiceProforma {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "invoicepro_id")	
	private int id;
	@Column(name = "ref")
	private String ref;
	@Column(name = "madedate")
	private Date todayDate;
	@Column(name = "totaldevise")
	private double totalDevise;
	@Column (name = "totaldinar")
	private double totalDinar;
	@Column (name = "precomptedouane")
	private double precompteDouane;
	@Column (name = "cur_change")
	private double change;
	@Column (name = "tva")
	private double tva;
	@Column(name = "total")
	private double total;
	@Column(name = "surface")
	private int surface;
	@Column(name = "metalcolor")
	private String metalColor;
	@Column(name = "seatcolor")
	private String seatColor;
	@Column(name = "project")
	private String project;
	@Column(name = "profit")
	private double profit;
	@Column(name = "profitpercent")
	private int profitPercent;
	@Column(name = "totalht")
	private double totalHt;
	@Column(name = "retirement")
	private double retirement;
	@Column(name = "tvaimp")
	private double tvaImp;
	@Column(name = "taxdomic")
	private double taxDomic;
	@Column(name = "invoice_year")
	private int invYear;
	@Column(name = "transportation")
	private double transortation;
	@Column(name = "magtransi")
	private double magTrans;
	@Column(name = "banctax")
	private double bancTax;
	@Transient
	private String TTCLetter;
	@OneToMany (targetEntity = LineProductProforma.class, mappedBy = "invoiceProforma", cascade = CascadeType.ALL)
	private List<LineProductProforma> LineProductsProforma = new ArrayList<LineProductProforma>();
	@OneToMany (targetEntity = Deposit.class, mappedBy = "invoiceProforma", cascade = CascadeType.ALL )
	private List<Deposit> deposits = new ArrayList<Deposit>();
	@ManyToOne
	@JoinColumn (name = "customer_id")
	private Client client;
	
	public InvoiceProforma(String ref, Date todayDate, Date recDate, double totalDevise, double totalDinar,
			double precompteDouane, double change, double tva, double total, int surface, String metalColor,
			   String seatColor, String project, double profit, int profitPercent, double totalHt, String TTCLetter,
			   double retirement, double tvaImp, double taxDomic, int invYear, double transortation,
			   double magTrans, double bancTax, List<LineProductProforma> LineProductsProforma, Client client) {
		this.ref = ref;
		this.todayDate = todayDate;
		this.totalDevise = totalDevise;
		this.totalDinar = totalDinar;
		this.precompteDouane = precompteDouane;
		this.change = change;
		this.tva = tva;
		this.total = total;
		this.surface = surface;
		this.metalColor = metalColor;
		this.seatColor = seatColor;
		this.project = project;
		this.client = client ;
		this.profit = profit;
		this.profitPercent = profitPercent;
		this.totalHt = totalHt;
		this.retirement = retirement;
		this.tvaImp = tvaImp;
		this.taxDomic = taxDomic;
		this.invYear = invYear;
		this.TTCLetter = TTCLetter;
		this.transortation = transortation;
		this.magTrans = magTrans;
		this.bancTax = bancTax;
	}

	public InvoiceProforma() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public Date getTodayDate() {
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}

	public double getTotalDevise() {
		return totalDevise;
	}

	public void setTotalDevise(double totalDevise) {
		this.totalDevise = totalDevise;
	}

	public double getTotalDinar() {
		return totalDinar;
	}

	public void setTotalDinar(double totalDinar) {
		this.totalDinar = totalDinar;
	}

	public double getPrecompteDouane() {
		return precompteDouane;
	}

	public void setPrecompteDouane(double precompteDouane) {
		this.precompteDouane = precompteDouane;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}
	
	public double getTva() {
		return tva;
	}

	public void setTva(double tva) {
		this.tva = tva;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getSurface() {
		return surface;
	}

	public void setSurface(int surface) {
		this.surface = surface;
	}

	public String getMetalColor() {
		return metalColor;
	}

	public void setMetalColor(String metalColor) {
		this.metalColor = metalColor;
	}

	public String getSeatColor() {
		return seatColor;
	}

	public void setSeatColor(String seatColor) {
		this.seatColor = seatColor;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public int getProfitPercent() {
		return profitPercent;
	}

	public void setProfitPercent(int profitPercent) {
		this.profitPercent = profitPercent;
	}

	public double getTotalHt() {
		return totalHt;
	}

	public void setTotalHt(double totalHt) {
		this.totalHt = totalHt;
	}

	public double getRetirement() {
		return retirement;
	}

	public void setRetirement(double retirement) {
		this.retirement = retirement;
	}

	public double getTvaImp() {
		return tvaImp;
	}

	public void setTvaImp(double tvaImp) {
		this.tvaImp = tvaImp;
	}
	
	public double getTaxDomic() {
		return taxDomic;
	}

	public void setTaxDomic(double taxDomic) {
		this.taxDomic = taxDomic;
	}

	public int getInvYear() {
		return invYear;
	}

	public void setInvYear(int invYear) {
		this.invYear = invYear;
	}

	public double getTransortation() {
		return transortation;
	}

	public void setTransortation(double transortation) {
		this.transortation = transortation;
	}

	public double getMagTrans() {
		return magTrans;
	}

	public void setMagTrans(double magTrans) {
		this.magTrans = magTrans;
	}

	public double getBancTax() {
		return bancTax;
	}

	public void setBancTax(double bancTax) {
		this.bancTax = bancTax;
	}

	public List<LineProductProforma> getLineProductProforma() {
		return LineProductsProforma;
	}

	public void setLineProductProforma(List<LineProductProforma> LineProducts) {
		this.LineProductsProforma = LineProducts;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client Client) {
		this.client = Client;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	public String getTTCLetter() {
		
		return Tools.numberToWords(total);
	}


	@Override
	public String toString() {
		return "Invoice [id=" + id + ", ref=" + ref + ", todayDate=" + todayDate + ", totalDevise=" + totalDevise + ", totalDinar=" + totalDinar + ", precompteDouane="
				+ precompteDouane + ", change=" + change + ", TVA=" + tva + ", total=" + total + ", InvoiceProduct="
						+ LineProductsProforma.toString()  + ", Client="+ client.toString()+"]";
	}
		
}
