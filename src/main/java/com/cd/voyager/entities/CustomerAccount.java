package com.cd.voyager.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.entities.idclass.CustomerAccountIdClass;

@Entity
@IdClass(CustomerAccountIdClass.class)
@Table(name="tb_customeraccount")

public class CustomerAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer customerId;
	
	@Id
	private String accountType; // C  For corporate and  P for Personal 
	
	@Id
	private String email;
	
	
	private Integer defaultPayment; // if accountType is C this is corpCustId else paymentId 
	
	private String status = AppConstants.STATUS_ACTIVE;
	
	private Integer deleteflag = AppConstants.DELETEFLAG_ALIVE;

	private Integer defaultAccount = AppConstants.STATUS_GLOBAL_ON;

	private Integer corpCustId ;
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDefaultPayment() {
		return defaultPayment;
	}

	public void setDefaultPayment(Integer defaultPayment) {
		this.defaultPayment = defaultPayment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

	public Integer getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(Integer defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public Integer getCorpCustId() {
		return corpCustId;
	}

	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}
	
	
	
}
