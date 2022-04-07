package com.commerciale.model;

import javax.persistence.*;

@Entity
@Table (name = "lineproduct")
public class LineProduct {
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
	@JoinColumn (name = "invoice_id")
	private Invoice invoice;
	@ManyToOne
	@JoinColumn (name = "product_id")
	private Product product;
	
	public LineProduct(double unitPriceDevise, double unitPriceDinar, 
			int quantity, Invoice invoice, Product product) {
		this.unitPriceDevise = unitPriceDevise;
		this.unitPriceDinar = unitPriceDinar;
		this.quantity = quantity;
		this.invoice = invoice;
		this.product = product;
	}

	public LineProduct() {
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
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
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
