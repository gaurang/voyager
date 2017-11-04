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

@Entity
@Table(name="tb_regdevice")

public class RegDevice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7739226640716523986L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer deviceId;
	
	private String imei;
	
	private String mobileDeviceID;
	
	private Integer regId;
	
	private Integer status;
	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	
	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate = new Date();


	public Integer getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}


	public String getImei() {
		return imei;
	}


	public void setImei(String imei) {
		this.imei = imei;
	}


	public String getMobileDeviceID() {
		return mobileDeviceID;
	}


	public void setMobileDeviceID(String mobileDeviceID) {
		this.mobileDeviceID = mobileDeviceID;
	}


	public Integer getRegId() {
		return regId;
	}


	public void setRegId(Integer regId) {
		this.regId = regId;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	

	
}
