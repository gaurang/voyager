package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.cd.voyager.common.util.AppConstants;

@Entity
@Table(name="tb_corporatecustomer")
public class CorporateCustomer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer corpCustId;
	
	//private String fname;
	
	//private String mname;
	
	//private String lname;
	
	private String email;
	
	private String phone;
	
	private String corpId;
	
	//private String empId;

	
	
	//For corporate
	
	private String abnAcnNo;
	private String corporateName;
	private String address;
	private String street;
	private String area;
	private String city;
	private String state;
	private String country;
	private String pincode;
	//private String secondaryEmail;
	//private String secondaryPhone;
	//
	private String logoPath;
	
	private String createBy;
	private String createDate;
	
	private String modifiedBy;
	private String modifiedDate;
	
	private String status = AppConstants.STATUS_ACTIVE;
	
	private String referralCode;

	private String rewardPoints;

	private Integer rembursed = 0;

	private String modeOfPayment;

	public Integer getCorpCustId() {
		return corpCustId;
	}
	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}

	/*public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}*/
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	/*public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}*/
	public String getAbnAcnNo() {
		return abnAcnNo;
	}
	public void setAbnAcnNo(String abnAcnNo) {
		this.abnAcnNo = abnAcnNo;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	/*public String getSecondaryEmail() {
		return secondaryEmail;
	}
	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = secondaryEmail;
	}
	public String getSecondaryPhone() {
		return secondaryPhone;
	}
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}*/
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	public String getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(String rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
//	public String getRembursed() {
//		return rembursed;
//	}
//	public void setRembursed(String rembursed) {
//		this.rembursed = rembursed;
//	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Integer getRembursed() {
		return rembursed;
	}
	public void setRembursed(Integer rembursed) {
		this.rembursed = rembursed;
	}
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	
	
	
	
	
}
