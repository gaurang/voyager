package com.cd.voyager.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cd.voyager.common.util.AppConstants;


@Entity
@Table(name="tb_enquiryform")

public class EnquiryForm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enquiryId;
	private String city;
	private String cseType1;
	private String cseType2;
	private String cseType3;
	private String cseType4;
	private String firstName;
	private String lastName;
	private String email;
	private Long mobile;
	private String aboutUs;
	private Integer countryCode;
	private String driveWith;
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getAboutUs() {
		return aboutUs;
	}
	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}
	public Integer getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}
	public String getCseType1() {
		return cseType1;
	}
	public void setCseType1(String cseType1) {
		this.cseType1 = cseType1;
	}
	public String getCseType2() {
		return cseType2;
	}
	public void setCseType2(String cseType2) {
		this.cseType2 = cseType2;
	}
	public String getCseType3() {
		return cseType3;
	}
	public void setCseType3(String cseType3) {
		this.cseType3 = cseType3;
	}
	public String getCseType4() {
		return cseType4;
	}
	public void setCseType4(String cseType4) {
		this.cseType4 = cseType4;
	}
	public String getDriveWith() {
		return driveWith;
	}
	public void setDriveWith(String driveWith) {
		this.driveWith = driveWith;
	}
	public Integer getEnquiryId() {
		return enquiryId;
	}
	public void setEnquiryId(Integer enquiryId) {
		this.enquiryId = enquiryId;
	}
	
	
}