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
@Table(name="tb_promo")
public class Promo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6007649611749032478L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer promoId;
	
	private String promoCode;
	
	private String type;
	
	private String status;
	

	@Column(name = "startDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	

	@Column(name = "endDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	private String appliedRule;
	
	private String rewardType;
	
	private Double rewardAmount;
	
	private Double rewardPoints;

	private Integer validDays;

	@Column(name = "allExpDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date allExpDate;
	
	public Integer getPromoId() {
		return promoId;
	}

	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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


	public String getAppliedRule() {
		return appliedRule;
	}

	public void setAppliedRule(String appliedRule) {
		this.appliedRule = appliedRule;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public Double getRewardAmount() {
		return rewardAmount;
	}

	public void setRewardAmount(Double rewardAmount) {
		this.rewardAmount = rewardAmount;
	}

	public Double getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Double rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public Date getAllExpDate() {
		return allExpDate;
	}

	public void setAllExpDate(Date allExpDate) {
		this.allExpDate = allExpDate;
	}

	

}
