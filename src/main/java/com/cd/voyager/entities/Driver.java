package com.cd.voyager.entities;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.cd.voyager.common.util.AppConstants;


@Entity
@Table(name="tb_driver")
public class Driver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer driverId;
	
	//@Formula()
	@Column(unique = true )
	private String cseCode;
	private int pincode;
	@Column(unique = true )
	private String email;
	private String password;
	private String fname;
	private String mname;
	private String lname;
	private String dob;
	private String gender;
	private Integer countryCode;

	private BigInteger phone;
	private String photo;
	//private String serviceTypeDriver;
	
	
	private String serviceType;
	
	@Transient
	private String deviceId;

	private String referralCode;
	private String referralCodeApp;
	private String referralAppDate;
	private String rewardsPoints;
	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate; 

	private Integer createdBy;
	
	private Integer modifiedBy;
	
	//private String status = voyagerConstants.STATUS_ACTIVE;
	//private Double pointsEarned;
	
	//private Double pointsRedeemed;
		
	//private Double pointsBalnced;
	
	private Integer deleteFlag = AppConstants.DELETEFLAG_ALIVE ;

	@Transient
	private String vehicleType;

	@Transient
	private Integer vehicleId;

	@Transient
	private String gcmRegId;
	
	@Transient
	private String defaultVehicle;
	
	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getCseCode() {
		return cseCode;
	}

	public void setCseCode(String cseCode) {
		this.cseCode = cseCode;
	}

	public int getPincode() {
		return pincode;
	}


	public void setPincode(int pincode) {
		this.pincode = pincode;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFname() {
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
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public Integer getCountryCode() {
		return countryCode;
	}


	public void setCountrCode(Integer countryCode) {
		this.countryCode = countryCode;
	}


	public BigInteger getPhone() {
		return phone;
	}


	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getReferralCode() {
		return referralCode;
	}


	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}


	public String getReferralCodeApp() {
		return referralCodeApp;
	}


	public void setReferralCodeApp(String referralCodeApp) {
		this.referralCodeApp = referralCodeApp;
	}



	public String getReferralAppDate() {
		return referralAppDate;
	}

	public void setReferralAppDate(String referralAppDate) {
		this.referralAppDate = referralAppDate;
	}

	public String getRewardsPoints() {
		return rewardsPoints;
	}


	public void setRewardsPoints(String rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}


	public Integer getDeleteFlag() {
		return deleteFlag;
	}


	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	@OneToMany(cascade={CascadeType.ALL},fetch = FetchType.LAZY, mappedBy="driver" )
//	@JoinColumn(name="driverId")
	@OrderBy("defaultCar desc")
	private Set<DrVehicle> drVehicles = new HashSet<DrVehicle>();
	

	public Set<DrVehicle> getDrVehicles() {
		return drVehicles;
	}

	public void setDrVehicles(Set<DrVehicle> drVehicles) {
		this.drVehicles = drVehicles;
	}
	
	@OneToMany(cascade={CascadeType.ALL},fetch = FetchType.LAZY, mappedBy="driver")
	//@JoinColumn(name="driverId")
	private Set<BankDetails> bankDetails = new HashSet<BankDetails>();

	public Set<BankDetails> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(Set<BankDetails> bankDetails) {
		this.bankDetails = bankDetails;
	}
	

	@OneToOne( cascade={CascadeType.ALL}, optional=false, fetch=FetchType.EAGER)
	@JoinColumn(name="driverId")
	private DriverDetails driverDetails;

	public DriverDetails getDriverDetails() {
		return driverDetails;
	}

	public void setDriverDetails(DriverDetails driverDetails) {
		this.driverDetails = driverDetails;
	}


	public String getVehicleType() {
		return vehicleType;
	}


	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}


	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}


	public String getGcmRegId() {
		return gcmRegId;
	}


	public void setGcmRegId(String gcmRegId) {
		this.gcmRegId = gcmRegId;
	}


	public Integer getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}


	public String getDefaultVehicle() {
		return defaultVehicle;
	}

	public void setDefaultVehicle(String defaultVehicle) {
		this.defaultVehicle = defaultVehicle;
	}

/*	public String getServiceTypeDriver() {
		return serviceTypeDriver;
	}

	public void setServiceTypeDriver(String serviceTypeDriver) {
		this.serviceTypeDriver = serviceTypeDriver;
	}*/

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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	
	
}
