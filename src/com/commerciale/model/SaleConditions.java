package com.commerciale.model;

import javax.persistence.*;

@Entity
@Table(name ="salecond")
public class SaleConditions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "salecond_id")
	private int id;
	@Column(name = "title")
	private String title ; 
	
	
	public SaleConditions(String title, String comment) {
		this.title = title;
		
	}


	public SaleConditions() {
		super();
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	

}
