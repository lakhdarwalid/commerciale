package com.commerciale.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "client")

public class Client {
	@Id
	@Column(name="id")
	private int id;
	@Column(name="raisonsocial")
	private String name;
	@Column(name="adresse")
	private String adress;
	@Column(name="rc")
	private String rc;
	@Column(name="mf")
	private String mf;
	@Column(name="nis")
	private String nis;
	@Column(name="artimpo")
	private String artImpo;
	@Column(name="category")
	private String category;
	@Column(name="impexon")
	private String impExon;
	@Column(name="modefactufe")
	private String modeFacture;
	@Column(name ="tel")
	private String tel;
	@Column(name ="email")
	private String email;
	@Column(name ="banque")
	private String banque;
	@Column(name ="compte")
	private String compte;
	@Column (name = "debt")
	private double debt;
	@Column (name = "depo")
	private double depo;
	@OneToMany (targetEntity = Invoice.class, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<Invoice> invoices = new HashSet<Invoice>();
	
	@OneToMany (targetEntity = InvoiceProforma.class, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<InvoiceProforma> invoicesPro = new HashSet<InvoiceProforma>();
	
	@OneToMany (targetEntity = Deposit.class, mappedBy = "client", cascade = CascadeType.ALL)
	private Set<Deposit> deposits = new HashSet<Deposit>();
	
	public Client(String name, String adress, String rc, String mf, String nis, String artImpo, String category,
			String impExon, String modeFacture, Set<Invoice> invoices, Set<Deposit> deposits , double credit, double debt, double depo, 
			String tel,	String email, String banque, String compte) {
		
		//this.id = id;
		this.name = name;
		this.adress = adress;
		this.rc = rc;
		this.mf = mf;
		this.nis = nis;
		this.artImpo = artImpo;
		this.category = category;
		this.impExon = impExon;
		this.modeFacture = modeFacture;
		this.tel = tel;
		this.email = email;
		this.banque = banque;
		this.compte = compte;
		this.invoices = invoices;
		this.deposits = deposits;
		this.debt = debt;
		this.depo = depo;
	}
	
public Client() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public String getMf() {
		return mf;
	}

	public void setMf(String mf) {
		this.mf = mf;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public String getArtImpo() {
		return artImpo;
	}

	public void setArtImpo(String artImpo) {
		this.artImpo = artImpo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImpExon() {
		return impExon;
	}

	public void setImpExon(String impExon) {
		this.impExon = impExon;
	}

	public String getModeFacture() {
		return modeFacture;
	}

	public void setModeFacture(String modeFacture) {
		this.modeFacture = modeFacture;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBanque() {
		return banque;
	}

	public void setBanque(String banque) {
		this.banque = banque;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}

	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Set<InvoiceProforma> getInvoicesPro() {
		return invoicesPro;
	}

	public void setInvoicesPro(Set<InvoiceProforma> invoicesPro) {
		this.invoicesPro = invoicesPro;
	}

	public double getDebt() {
		return debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public double getDepo() {
		return depo;
	}

	public void setDepo(double depo) {
		this.depo = depo;
	}

	public Set<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(Set<Deposit> deposits) {
		this.deposits = deposits;
	}

	
	
	
}
