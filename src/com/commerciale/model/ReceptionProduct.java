package com.commerciale.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "receptionproduct")
public class ReceptionProduct {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "prd_id")
	private int id;
	@Column (name = "ref")
	private String ref;
	@Column (name = "imag")
	private byte[] productImg;
	@Column (name = "designation")
	private String desg;
	@Column (name = "unitpricedevise")
	private double unitPriceDevise;
	@Column (name = "unitpricedinar")
	private double unitPriceDinar;
	@Column (name = "quantity")
	private int quantity;
	@ManyToOne
	@JoinColumn (name = "reception_id")
	private Reception reception;
	@ManyToOne
	@JoinColumn (name = "product_id")
	private Product product;
	
	public ReceptionProduct(String ref, byte[] productImg, String desg, double unitPriceDevise, double unitPriceDinar,
			int quantity, Reception reception, Product product) {
		this.ref = ref;
		this.productImg = productImg;
		this.desg = desg;
		this.unitPriceDevise = unitPriceDevise;
		this.unitPriceDinar = unitPriceDinar;
		this.quantity = quantity;
		this.reception = reception;
		this.product = product;
	}

	public ReceptionProduct() {
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

	public byte[] getProductImg() {
		return productImg;
	}

	public void setProductImg(byte[] productImg) {
		this.productImg = productImg;
	}

	public String getDesg() {
		return desg;
	}

	public void setDesg(String desg) {
		this.desg = desg;
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
	
	public Reception getReception() {
		return reception;
	}

	public void setReception(Reception reception) {
		this.reception = reception;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ReceptionProduct [ref=" + ref + ", desg=" + desg
				+ ", unitPriceDevise=" + unitPriceDevise + ", unitPriceDinar=" + unitPriceDinar + ", quantity="
				+ quantity + "]" ;
	}
	

}
