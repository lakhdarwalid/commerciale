package com.commerciale.model;

import java.util.Date;

public class InvStat {
    private double tva ;
    private double tvaImp ; 
    private double majoration ;
    private double dep ; 
    private double retirement ;
    private double ttc;
    private Date startDate;
	private Date endDate;
    public InvStat(double tva, double tvaImp, double majoration, double dep, double retirement, double ttc, 
    			Date startDate, Date endDate) {
		this.tva = tva;
		this.tvaImp = tvaImp;
		this.majoration = majoration;
		this.dep = dep;
		this.retirement = retirement;
		this.ttc = ttc;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public double getTva() {
		return tva;
	}

	public void setTva(double tva) {
		this.tva = tva;
	}

	public double getTvaImp() {
		return tvaImp;
	}

	public void setTvaImp(double tvaImp) {
		this.tvaImp = tvaImp;
	}

	public double getMajoration() {
		return majoration;
	}

	public void setMajoration(double majoration) {
		this.majoration = majoration;
	}

	public double getDep() {
		return dep;
	}

	public void setDep(double dep) {
		this.dep = dep;
	}

	public double getRetirement() {
		return retirement;
	}

	public void setRetirement(double retirement) {
		this.retirement = retirement;
	}

	public double getTtc() {
		return ttc;
	}

	public void setTtc(double ttc) {
		this.ttc = ttc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
    
    
}
