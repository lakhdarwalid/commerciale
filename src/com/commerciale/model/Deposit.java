package com.commerciale.model;

import java.util.Date;

import javax.persistence.*;

import com.commerciale.service.Tools;

@Entity
@Table (name = "deposit")
public class Deposit {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "deposit_id")
	private int id;
	@Column (name = "deposit_date")
	private Date depositDate;
	@Column (name = "amount")
	private double amount;
	@Column (name = "debtcus")
	private double debtCus;
	@Column (name = "debtinv")
	private double debtInv;
	@Column (name = "cheq")
	private String cheq;
	@Column (name = "cheqnum")
	private String cheqNum;
	@ManyToOne
	@JoinColumn (name = "invoicepro_id")
	private InvoiceProforma invoiceProforma;
	@ManyToOne
	@JoinColumn (name = "customer_id")
	private Client client;
	@Transient
	private String depoLetter;
	public Deposit(int id, Date depositDate, double amount, double debtCus, double debtInv, String cheq, String cheqNum,
			       String depoLetter , InvoiceProforma invoiceProforma, Client client) {
		super();
		this.id = id;
		this.depositDate = depositDate;
		this.amount = amount;
		this.debtCus = debtCus;
		this.debtInv = debtInv;
		this.cheq = cheq;
		this.cheqNum = cheqNum;
		this.invoiceProforma = invoiceProforma;
		this.client = client;
		this.depoLetter = depoLetter;
	}

	public Deposit() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getDebtCus() {
		return debtCus;
	}

	public void setDebtCus(double debtCus) {
		this.debtCus = debtCus;
	}

	public double getDebtInv() {
		return debtInv;
	}

	public void setDebtInv(double debtInv) {
		this.debtInv = debtInv;
	}

	public String getCheq() {
		return cheq;
	}

	public void setCheq(String cheq) {
		this.cheq = cheq;
	}

	public String getCheqNum() {
		return cheqNum;
	}

	public void setCheqNum(String cheqNum) {
		this.cheqNum = cheqNum;
	}

	public InvoiceProforma getInvoiceProforma() {
		return invoiceProforma;
	}

	public void setInvoiceProforma(InvoiceProforma invoiceProforma) {
		this.invoiceProforma = invoiceProforma;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public String getDepoLetter() {
		
		return Tools.numberToWords(amount);
	}
	

}
