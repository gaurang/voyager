package com.cd.voyager.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
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



@Entity
@Table(name="tb_customerbooking")
public class Booking implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8507141660402003455L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookingId;
	
	private Integer customerId;
	
	private Integer corpCustId;
	
	private String sourceLatitude;
	
	private String sourceLongitude;
	
	private String destLatitude;
	
	private String destLongitude;

	private String accountType;

	private Integer paymentId;

	private String vehicleType;

	private Integer promoId;
	
	private String cancellationReason;
	
	private BigDecimal cancellationFee;
	
	@Transient
	private BigDecimal surge;

	
	private String status = AppConstants.STATUS_ACTIVE;
	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	
	@Column(name = "modifiedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate; 

	private String srcPlace;

	private String destPlace;

	private String gcmRegId;

	@Transient
	private Integer driverId;
	
	@Transient
	private BigDecimal rideTotalAmt;

	@Transient
	private String driverName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Transient
	private Date rideDate;
	
	@Transient
	private String customerName;
	
	@Transient
	private String driverPhoto;
	
	@Transient
	private String srcArea;

	@Transient
	private String subArea;

	@Transient
	private BigDecimal tollCharges;

	@Transient
	private BigDecimal baseFare;

	
	@Transient
	private BigDecimal bookingFee;

	
	@Transient
	private BigDecimal kmTraveled;

	
	@Transient
	private BigDecimal kmCharges;
	
	@Transient
	private Date waitTime;
	
	@Transient
	private BigDecimal waitCharges;
	
	@Transient
	private String promoCode;

	@Transient
	private BigInteger phone;
	
	@Transient
	private String lat;
	
	@Transient
	private String lng;
	
	@Transient
	private String make;
	
	@Transient
	private String model;
	
	@Transient
	private String carNumber;
	
	@Transient
	private BigDecimal custRating;
	
	@Transient
	private BigDecimal driverRating;
	
	private String pin;

	@Transient
	private String currency;

	@Transient
	private Integer empId;

	@Transient
	private String email;

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getSourceLatitude() {
		return sourceLatitude;
	}

	public void setSourceLatitude(String sourceLatitude) {
		this.sourceLatitude = sourceLatitude;
	}

	public String getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(String sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public String getDestLatitude() {
		return destLatitude;
	}

	public void setDestLatitude(String destLatitude) {
		this.destLatitude = destLatitude;
	}

	public String getDestLongitude() {
		return destLongitude;
	}

	public void setDestLongitude(String destLongitude) {
		this.destLongitude = destLongitude;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getCorpCustId() {
		return corpCustId;
	}

	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}

	public String getSrcPlace() {
		return srcPlace;
	}

	public void setSrcPlace(String srcPlace) {
		this.srcPlace = srcPlace;
	}

	public String getDestPlace() {
		return destPlace;
	}

	public void setDestPlace(String destPlace) {
		this.destPlace = destPlace;
	}



	public BigDecimal getSurge() {
		return surge;
	}

	public void setSurge(BigDecimal surge) {
		this.surge = surge;
	}

	public BigDecimal getRideTotalAmt() {
		return rideTotalAmt;
	}

	public void setRideTotalAmt(BigDecimal rideTotalAmt) {
		this.rideTotalAmt = rideTotalAmt;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhoto() {
		return driverPhoto;
	}

	public void setDriverPhoto(String driverPhoto) {
		this.driverPhoto = driverPhoto;
	}

	public Integer getPromoId() {
		return promoId;
	}

	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public BigDecimal getCancellationFee() {
		return cancellationFee;
	}

	public void setCancellationFee(BigDecimal cancellationFee) {
		this.cancellationFee = cancellationFee;
	}

	public String getSrcArea() {
		return srcArea;
	}

	public void setSrcArea(String srcArea) {
		this.srcArea = srcArea;
	}

	public String getSubArea() {
		return subArea;
	}

	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}

	public BigDecimal getTollCharges() {
		return tollCharges;
	}

	public void setTollCharges(BigDecimal tollCharges) {
		this.tollCharges = tollCharges;
	}

	public BigDecimal getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(BigDecimal baseFare) {
		this.baseFare = baseFare;
	}

	public BigDecimal getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(BigDecimal bookingFee) {
		this.bookingFee = bookingFee;
	}

	public BigDecimal getKmTraveled() {
		return kmTraveled;
	}

	public void setKmTraveled(BigDecimal kmTraveled) {
		this.kmTraveled = kmTraveled;
	}

	public BigDecimal getKmCharges() {
		return kmCharges;
	}

	public void setKmCharges(BigDecimal kmCharges) {
		this.kmCharges = kmCharges;
	}

	public Date getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Date waitTime) {
		this.waitTime = waitTime;
	}

	public BigDecimal getWaitCharges() {
		return waitCharges;
	}

	public void setWaitCharges(BigDecimal waitCharges) {
		this.waitCharges = waitCharges;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getGcmRegId() {
		return gcmRegId;
	}

	public void setGcmRegId(String gcmRegId) {
		this.gcmRegId = gcmRegId;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	
	public BigInteger getPhone() {
		return phone;
	}

	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public BigDecimal getCustRating() {
		return custRating;
	}

	public void setCustRating(BigDecimal custRating) {
		this.custRating = custRating;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getDriverRating() {
		return driverRating;
	}

	public void setDriverRating(BigDecimal driverRating) {
		this.driverRating = driverRating;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}
	

	

}
