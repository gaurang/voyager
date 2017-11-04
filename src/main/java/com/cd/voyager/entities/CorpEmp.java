package com.cd.voyager.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cd.voyager.common.util.AppConstants;


@Entity
@Table(name="tb_corpemp")
public class CorpEmp implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer corpEmpId;
	
	private Integer corpCustId;
	
	private String fname;

	private String lname;
	
	private String email;
	
	private String pin;
	private String status = AppConstants.STATUS_ACTIVE;

	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	public Integer getCorpEmpId() {
		return corpEmpId;
	}

	public void setCorpEmpId(Integer corpEmpId) {
		this.corpEmpId = corpEmpId;
	}

	public Integer getCorpCustId() {
		return corpCustId;
	}

	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
	
}
