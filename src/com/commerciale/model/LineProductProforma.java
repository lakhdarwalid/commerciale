package com.commerciale.model;

import javax.persistence.*;

@Entity
@Table (name = "lineproductsproforma")
public class LineProductProforma {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "prd_id")
	private int id;
	@Column (name = "unitpricedevise")
	private double unitPriceDevise;
	@Column (name = "unitpricedinar")
	private double unitPriceDinar;
	@Column (name = "quantity")
	private int quantity;
	@ManyToOne
	@JoinColumn (name = "invoicepro_id")
	private InvoiceProforma invoiceProforma;
	@ManyToOne
	@JoinColumn (name = "product_id")
	private Product product;
	
	public LineProductProforma(double unitPriceDevise, double unitPriceDinar, 
			int quantity, InvoiceProforma invoiceProforma, Product product) {
		this.unitPriceDevise = unitPriceDevise;
		this.unitPriceDinar = unitPriceDinar;
		this.quantity = quantity;
		this.invoiceProforma = invoiceProforma;
		this.product = product;
	}

	public LineProductProforma() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getUnitPriceDevise() {
		return unitPriceDevise;
	}

	public void setUnitPriceDevise(double unitPriceDevise) {
		this.unitPriceDevise = unitPriceDevise;
	}

	public double getUnitPriceDinar() {
		return unitPriceDinar;
	}

	public void setUnitPriceDinar(double unitPriceDinar) {
		this.unitPriceDinar = unitPriceDinar;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public InvoiceProforma getInvoice() {
		return invoiceProforma;
	}

	public void setInvoiceProforma(InvoiceProforma invoiceProforma) {
		this.invoiceProforma = invoiceProforma;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "lineProduct [unitPriceDevise=" + unitPriceDevise + ", unitPriceDinar=" + unitPriceDinar + ", quantity="
				+ quantity + "]" ;
	}
	

}
