package com.cd.voyager.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="tb_corpcustreward")
public class CorpCustReward {

	@Id
	private Integer CorpCustId;
	
	private Double pointsEarned;
	
	private Double pointsRedeemed;
	
	private Double pointsBalnced;
	
	private String currentTier;
	
	private String previousTier;
	
	private String modifiedBy;
	
	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate; 


	
	public Integer getCorpCustId() {
		return CorpCustId;
	}
	public void setCorpCustId(Integer corpCustId) {
		CorpCustId = corpCustId;
	}
	public Double getPointsEarned() {
		return pointsEarned;
	}
	public void setPointsEarned(Double pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
	public Double getPointsRedeemed() {
		return pointsRedeemed;
	}
	public void setPointsRedeemed(Double pointsRedeemed) {
		this.pointsRedeemed = pointsRedeemed;
	}
	public Double getPointsBalnced() {
		return pointsBalnced;
	}
	public void setPointsBalnced(Double pointsBalnced) {
		this.pointsBalnced = pointsBalnced;
	}
	public String getCurrentTier() {
		return currentTier;
	}
	public void setCurrentTier(String currentTier) {
		this.currentTier = currentTier;
	}
	public String getPreviousTier() {
		return previousTier;
	}
	public void setPreviousTier(String previousTier) {
		this.previousTier = previousTier;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
}
