package com.cd.voyager.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="tb_driverdetails")

public class DriverDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer driverDetailId;
	
    @Id
	@Column(insertable=false, updatable=false)
	private Integer driverId;
    
	private String accessibility;
	private String licenseNo;
	
	//private String fname;

	//private String lname;
	//private String mname;
	
	private String address;
	private String Street;
	private String area;
	private String city;
	private String State;
	private String country;
	//private String pincode;

	//private Long phone;
	//private String email;
	
	//private String dob;
	//private String gender;

	private String licensePath;
	
	private String driverAuth;
	private String driverAuthPath;
	
	private String driverInsuranceNo;
	private String driverInsurancePath;
	//private  createDate;
	//private String createBy;
	//private String accesebility;
	
	
	//private String status = voyagerConstants.STATUS_ACTIVE ;
	
	//private Integer deleteFlag = voyagerConstants.DELETEFLAG_ALIVE;
	
	//private String serviceType;

	private String createBy;

	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	private String modifiedBy;
	
	
	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public Integer getDriverDetailId() {
		return driverDetailId;
	}

	public void setDriverDetailId(Integer  driverDetailId) {
		this.driverDetailId = driverDetailId;
	}

	public String getAccessibility() {
		return accessibility;
	}

	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
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
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLicensePath() {
		return licensePath;
	}

	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}

	public String getDriverAuth() {
		return driverAuth;
	}

	public void setDriverAuth(String driverAuth) {
		this.driverAuth = driverAuth;
	}

	public String getDriverAuthPath() {
		return driverAuthPath;
	}

	public void setDriverAuthPath(String driverAuthPath) {
		this.driverAuthPath = driverAuthPath;
	}

	public String getDriverInsuranceNo() {
		return driverInsuranceNo;
	}

	public void setDriverInsuranceNo(String driverInsuranceNo) {
		this.driverInsuranceNo = driverInsuranceNo;
	}

	public String getDriverInsurancePath() {
		return driverInsurancePath;
	}

	public void setDriverInsurancePath(String driverInsurancePath) {
		this.driverInsurancePath = driverInsurancePath;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	
	//private String referralCode;

	//private String countryCode;

/*	@OneToOne( cascade={CascadeType.ALL},fetch = FetchType.LAZY)
	@JoinColumn(name="driverId")
	private Driver driver;

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	*/
	
	
	
}
