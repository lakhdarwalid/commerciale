package com.commerciale.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name="product")
public class Product {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name="product_id")
	private int id;
	@Column(name="ref")
	private String ref;
	@Column(name="desig")
	private String desg;
	@Column(name="unitprice")
	private double unitPrice;
	@Column(name="Quantity")
	private int quantity;
	@Column(name="image")
	private byte[] img;
	@ManyToOne
	@JoinColumn (name = "prdgroup_id")
	private ProductGroup productGroup;
	@OneToMany (targetEntity = ReceptionProduct.class, mappedBy = "product", cascade = CascadeType.ALL)
	private Set<ReceptionProduct> receptionProducts = new HashSet<ReceptionProduct>(); 
	@Transient
    private InputStream image;
	public Product(String ref, String desg, double unitPrice, int quantity, byte[] img, InputStream image,
			     				ProductGroup productGroup, Set<ReceptionProduct> receptionProducts ) {
		
		this.ref = ref;
		this.desg = desg;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.img = img;
		this.image = new ByteArrayInputStream(this.img);
		this.productGroup = productGroup;
		this.receptionProducts = receptionProducts;
	}
	
	public Product() {
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
	
	public String getDesg() {
		return desg;
	}
	
	public void setDesg(String desg) {
		this.desg = desg;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public byte[] getImg() {
		return img;
	}
	
	public void setImg(byte[] img) {
		this.img = img;
	}
		
	public InputStream getImage() throws IOException {
		if (this.img == null) {image = null;}
		else {
		image = new ByteArrayInputStream(this.img);}
		return image ;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	public Set<ReceptionProduct> getReceptionProducts() {
		return receptionProducts;
	}

	public void setReceptionProducts(Set<ReceptionProduct> receptionProducts) {
		this.receptionProducts = receptionProducts;
	}

	@Override
	public String toString() {
		return "Product [ref=" + ref + ", desg=" + desg + ", unitPrice=" + unitPrice + ", quantity=" + quantity + "]";
	}
	
	
	

}
