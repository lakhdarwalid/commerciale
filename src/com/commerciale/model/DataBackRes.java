package com.commerciale.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "databackupres")
public class DataBackRes {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "back_id")
	private int id;
	@Column (name = "back_date")
	private Date backUpDate;
	@Column (name = "back_file")
	private String backFilePath;
	
	public DataBackRes(int id, Date backUpDate, String backFilePath) {
		this.id = id;
		this.backUpDate = backUpDate;
		this.backFilePath = backFilePath;
	}

	public DataBackRes() {
		super();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getBackUpDate() {
		return backUpDate;
	}

	public void setBackUpDate(Date backUpDate) {
		this.backUpDate = backUpDate;
	}

	public String getBackFilePath() {
		return backFilePath;
	}

	public void setBackFilePath(String backFilePath) {
		this.backFilePath = backFilePath;
	}
	
	

}
