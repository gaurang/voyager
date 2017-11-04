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
import javax.persistence.Transient;

import com.cd.voyager.common.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonRawValue;


@Entity
@Table(name="tb_payment")


public class Payment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paymentId;
	
	private String paymentType;
	private Long cardNo;
	private String cardName;
	private Integer cvv;
	
	private String expDate;
	private String issueDate;
	
	private String cardType;
	
	private String status = AppConstants.STATUS_ACTIVE;

	private Integer deleteFlag = 0;

	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate; 

		
	private Integer customerId;

	private Integer corpCustId;
	
	private String gatewayId;
	
	private String gatewayUserId;
	
	private String gatewayToken;

	@JsonRawValue
	private String authKey;
	
	@Transient
	private String email;

	@Transient
	private String pin;
	
	@Transient
	private Integer rembursed;

	private Integer defaultFlag;

	@Transient
	private String accountType;
	
	public Integer getPaymentId() {
		return paymentId;
	}

	
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public Long getCardNo() {
		return cardNo;
	}

	public void setCardNo(Long cardNo) {
		this.cardNo = cardNo;
	}



	public String getCardName() {
		return cardName;
	}


	public void setCardName(String cardName) {
		this.cardName = cardName;
	}


	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public Integer getCorpCustId() {
		return corpCustId;
	}


	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}


	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getGatewayUserId() {
		return gatewayUserId;
	}

	public void setGatewayUserId(String gatewayUserId) {
		this.gatewayUserId = gatewayUserId;
	}

	public String getGatewayToken() {
		return gatewayToken;
	}

	public void setGatewayToken(String gatewayToken) {
		this.gatewayToken = gatewayToken;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
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


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public Integer getDefaultFlag() {
		return defaultFlag;
	}


	public void setDefaultFlag(Integer defaultFlag) {
		this.defaultFlag = defaultFlag;
	}


	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public String getPin() {
		return pin;
	}


	public void setPin(String pin) {
		this.pin = pin;
	}


	public Integer getRembursed() {
		return rembursed;
	}


	public void setRembursed(Integer rembursed) {
		this.rembursed = rembursed;
	}


	public Integer getCvv() {
		return cvv;
	}


	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}
	

	
	
}
