package com.commerciale.model;

import javax.persistence.*;

@Entity
@Table(name = "user")

public class user {
	@Id
	@Column(name="id")
	private int userId;
	@Column(name="username")	
	private String userName;
	@Column(name="password")
	private String password;
	
	
	public user(int userId, String userName, String password) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
	}
	
	public user() {
		
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
