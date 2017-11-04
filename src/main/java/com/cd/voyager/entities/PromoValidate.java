package com.cd.voyager.entities;

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
@Table(name="tb_promovalidate")
public class PromoValidate {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer promoValidateId;

	private Integer customerId;

	@Column(name = "validateDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date validateDate;
	
	private String promoCode;
	
	private Integer promoId;
	
	private String status= AppConstants.STATUS_ACTIVE;


	private Double promoDiscount;
	
	private String promoType;
	
	private String refStatus;
	
	@Column(name = "expDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expDate;

	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public Date getValidateDate() {
		return validateDate;
	}

	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
	}


	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public Integer getPromoId() {
		return promoId;
	}

	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPromoValidateId() {
		return promoValidateId;
	}

	public void setPromoValidateId(Integer promoValidateId) {
		this.promoValidateId = promoValidateId;
	}

	public Double getPromoDiscount() {
		return promoDiscount;
	}

	public void setPromoDiscount(Double promoDiscount) {
		this.promoDiscount = promoDiscount;
	}

	public String getPromoType() {
		return promoType;
	}

	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}

	public String getRefStatus() {
		return refStatus;
	}

	public void setRefStatus(String refStatus) {
		this.refStatus = refStatus;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	
	
	
}
