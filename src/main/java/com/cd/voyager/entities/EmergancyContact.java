package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cd.voyager.common.util.AppConstants;

@Entity
@Table(name="tb_emergancycontact")


public class EmergancyContact {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contactId;
	
	private Integer customerId;
	
	private Integer driverId;
	
	private String eContactName;

	private String eContactNumber;
	
	private Integer trackStatus;
	
	private String status = AppConstants.STATUS_ACTIVE;

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String geteContactName() {
		return eContactName;
	}

	public void seteContactName(String eContactName) {
		this.eContactName = eContactName;
	}

	public String geteContactNumber() {
		return eContactNumber;
	}

	public void seteContactNumber(String eContactNumber) {
		this.eContactNumber = eContactNumber;
	}

	public Integer getTrackStatus() {
		return trackStatus;
	}

	public void setTrackStatus(Integer trackStatus) {
		this.trackStatus = trackStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String 
			status) {
		this.status = status;
	}
	
	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

}
