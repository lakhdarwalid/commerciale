package com.commerciale.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "productgroup")
public class ProductGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prdgroup_id")
	private int id;
	@Column(name = "desg")
	private String desg;
	@OneToMany (targetEntity= Product.class  , mappedBy = "productGroup", cascade = CascadeType.ALL)
	private Set<Product> products = new HashSet<Product>();
	
	public ProductGroup( String desg, Set<Product> products) {
		this.desg = desg;
		this.products = products;
	}
	
	public ProductGroup() {
		super();
	}
	
	
	public int getId() {
		return id;
	}
	
	public String getDesg() {
		return desg;
	}
	public void setDesg(String desg) {
		this.desg = desg;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	
	
	

}
	
	
