package com.cd.voyager.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.common.util.MD5Utils;

@Entity
@Table(name="tb_customer")

public class Customer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1537862738188983638L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerId;
	
	@Email
	private String email;
	
	private String mobile;
	
	private String fname;
	
	private String lname;
	
	private String mname;
	
	private String password;
	
	private String pin;

	private String status = AppConstants.STATUS_ACTIVE;
	
	private Integer deleteFlag = AppConstants.DELETEFLAG_ALIVE;
	
	private Long rewardPoints;
	
	private String referralCode;
	
	private Integer regId;
	
	private String profilePicURL;
	
	private String authKeyFb; //Facebook 
	
	private String authKeyG; //Google unique key

	private String signInVia; //F- facebook , G - google

	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate; 

	private String countryCode;
		
	@Column(name = "pinDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pinDate = new Date();
	
	@Transient
	private String message;
	
	@Transient
	private Integer rembursed;
	
	private String refCodeApp;


	@Column(name = "refCodeDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date refCodeDate; 
	
	private String gcmRegId;
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = MD5Utils.to_MD5(password);
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


	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

/*	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
*/
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


	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public String getProfilePicURL() {
		return profilePicURL;
	}

	public void setProfilePicURL(String profilePicURL) {
		this.profilePicURL = profilePicURL;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name="customerId")
	@JsonProperty("paymentList")
	@Fetch(FetchMode.JOIN)
	private Set<Payment> paymentList;

	@JsonProperty("CustomerReward")
	@OneToOne(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name="customerId")
	private CustomerReward customerReward;

	public Set<Payment> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(Set<Payment> paymentList) {
		this.paymentList = paymentList;
	}


	public String getAuthKeyFb() {
		return authKeyFb;
	}

	public void setAuthKeyFb(String authKeyFb) {
		this.authKeyFb = authKeyFb;
	}

	public String getAuthKeyG() {
		return authKeyG;
	}

	public void setAuthKeyG(String authKeyG) {
		this.authKeyG = authKeyG;
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

	public CustomerReward getCustomerReward() {
		return customerReward;
	}

	public void setCustomerReward(CustomerReward customerReward) {
		this.customerReward = customerReward;
	}


	public String getRefCodeApp() {
		return refCodeApp;
	}

	public void setRefCodeApp(String refCodeApp) {
		this.refCodeApp = refCodeApp;
	}

	public Date getRefCodeDate() {
		return refCodeDate;
	}

	public void setRefCodeDate(Date refCodeDate) {
		this.refCodeDate = refCodeDate;
	}

	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	public Integer getRembursed() {
		return rembursed;
	}

	public void setRembursed(Integer rembursed) {
		this.rembursed = rembursed;
	}

	public Date getPinDate() {
		return pinDate;
	}

	public void setPinDate(Date pinDate) {
		this.pinDate = pinDate;
	}

	public String getGcmRegId() {
		return gcmRegId;
	}

	public void setGcmRegId(String gcmRegId) {
		this.gcmRegId = gcmRegId;
	}

	
}
