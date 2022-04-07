package com.commerciale.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "supplier")

public class Supplier {
	@Id
	@Column(name="id")
	private int id;
	@Column(name="raisonsocial")
	private String name;
	@Column(name="adresse")
	private String adress;
	@Column(name="rc")
	private String rc;
	@Column(name="mf")
	private String mf;
	@Column(name="nis")
	private String nis;
	@Column(name="artimpo")
	private String artImpo;
	@Column(name ="tel")
	private String tel;
	@Column(name ="email")
	private String email;
	@Column(name ="banque")
	private String banque;
	@Column(name ="compte")
	private String compte;
	@OneToMany (targetEntity = Reception.class, mappedBy = "supplier", cascade = CascadeType.ALL)
	private Set<Reception> receptions = new HashSet<Reception>();
	
	public Supplier(String name, String adress, String rc, String mf, String nis, String artImpo, String tel, String email,
			String banque, String compte, Set<Reception> receptions ) {
		
		//this.id = id;
		this.name = name;
		this.adress = adress;
		this.rc = rc;
		this.mf = mf;
		this.nis = nis;
		this.artImpo = artImpo;
		this.tel = tel;
		this.email = email;
		this.banque = banque;
		this.compte = compte;
		this.receptions = receptions;
	}
	
public Supplier() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getRc() {
		return rc;
	}

	public void setRc(String rc) {
		this.rc = rc;
	}

	public String getMf() {
		return mf;
	}

	public void setMf(String mf) {
		this.mf = mf;
	}

	public String getNis() {
		return nis;
	}

	public void setNis(String nis) {
		this.nis = nis;
	}

	public String getArtImpo() {
		return artImpo;
	}

	public void setArtImpo(String artImpo) {
		this.artImpo = artImpo;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBanque() {
		return banque;
	}

	public void setBanque(String banque) {
		this.banque = banque;
	}

	public String getCompte() {
		return compte;
	}

	public void setCompte(String compte) {
		this.compte = compte;
	}
	
	public Set<Reception> getReceptions() {
		return receptions;
	}

	public void setReceptions(Set<Reception> receptions) {
		this.receptions = receptions;
	}

	@Override
	public String toString() {
		return "My Supplier [id=" + id + ", name=" + name + ", adress=" + adress + ", rc=" + rc + ", mf=" + mf + ", nis="
				+ nis + ", artImpo=" + artImpo + ", tel=" + tel + ", email=" + email + ", banque=" + banque
				+ ", compte=" + compte + "]";
	}
	
	
}
