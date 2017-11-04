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
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name="tb_registration")

public class Registration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1537862738188983638L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer regId;
	
	@Email
	private String email;
	
	private String mobile;
	
	private String fname;
	
	private String lname;
	
	private String password;
	
    
	private String OTP;
	
	private String authKey; //Facebook or google unique key
	
	private String signInVia; //F- facebook , G - google

	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "OTPDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date OTPDate;

	private String countryCode; //F- facebook , G - google

	private String status = AppConstants.STATUS_ACTIVE;  

	public Integer getRegId() {
		return regId;
	}


	public void setRegId(Integer regId) {
		this.regId = regId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
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


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getOTP() {
		return OTP;
	}


	@JsonProperty("OTP")
	public void setOTP(String oTP) {
		OTP = oTP;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getOTPDate() {
		return OTPDate;
	}


	public void setOTPDate(Date oTPDate) {
		OTPDate = oTPDate;
	}


	public String getAuthKey() {
		return authKey;
	}


	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}


	public String getSignInVia() {
		return signInVia;
	}


	public void setSignInVia(String signInVia) {
		this.signInVia = signInVia;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}


	
	
}
