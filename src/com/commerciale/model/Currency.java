package com.commerciale.model;

import javax.persistence.*;

@Entity
@Table(name = "currency")
public class Currency {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "value")
	private double money;
	
	public Currency(int id, double money) {
		this.id = id;
		this.money = money;
	}
		
	public Currency() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	

}
