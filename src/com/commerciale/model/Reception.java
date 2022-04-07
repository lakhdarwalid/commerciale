package com.commerciale.model;



import java.util.*;

import javax.persistence.*;

@Entity
@Table (name = "reception")
public class Reception {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name = "reception_id")	
	private int id;
	@Column(name = "ref")
	private String ref;
	@Column (name = "entrydate")
	private Date todayDate;
	@Column(name = "madedate")
	private Date recDate;
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
	@Column(name = "tht")
	private double tht;
	@Column(name = "retirement")
	private double retirement;
	@Column(name = "transportation")
	private double transportation;
	@OneToMany (targetEntity = ReceptionProduct.class, mappedBy = "reception", cascade = CascadeType.ALL)
	private List<ReceptionProduct> receptionProducts = new ArrayList<ReceptionProduct>();
	@ManyToOne
	@JoinColumn (name = "supplier_id")
	private Supplier supplier;
	
	public Reception(String ref, Date todayDate, Date recDate, double totalDevise, double totalDinar,
			double precompteDouane, double change, double tva, double total, double tht, double retirement,
			double transportation, List<ReceptionProduct> receptionProducts, Supplier supplier) {
		this.ref = ref;
		this.todayDate = todayDate;
		this.recDate = recDate;
		this.totalDevise = totalDevise;
		this.totalDinar = totalDinar;
		this.precompteDouane = precompteDouane;
		this.change = change;
		this.tva = tva;
		this.total = total;
		this.supplier = supplier ;
		this.tht = tht; 
		this.retirement = retirement;
		this.transportation = transportation;
	}

	public Reception() {
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

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
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

	public double getTht() {
		return tht;
	}

	public void setTht(double tht) {
		this.tht = tht;
	}

	public double getRetirement() {
		return retirement;
	}

	public void setRetirement(double retirement) {
		this.retirement = retirement;
	}

	public double getTransportation() {
		return transportation;
	}

	public void setTransportation(double transportation) {
		this.transportation = transportation;
	}

	public List<ReceptionProduct> getReceptionProduct() {
		return receptionProducts;
	}

	public void setReceptionProduct(List<ReceptionProduct> receptionProducts) {
		this.receptionProducts = receptionProducts;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return "Reception [id=" + id + ", ref=" + ref + ", todayDate=" + todayDate + ", recDate="
				+ recDate + ", totalDevise=" + totalDevise + ", totalDinar=" + totalDinar + ", precompteDouane="
				+ precompteDouane + ", change=" + change + ", TVA=" + tva + ", total=" + total + ", receptionProduct="
						+ receptionProducts.toString()  + ", Supplier="+ supplier.toString()+"]";
	}
		
}
