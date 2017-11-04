package com.cd.voyager.entities;

import java.math.BigDecimal;
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
@Table(name="tb_customerridedetails")
public class CustomerRide {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rideId;

	private Integer bookingId;
	private Integer driverId;

/*	@Column(name = "rideDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rideDate;
*/
	
	@Column(name = "rideStartDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rideStartDate ;

	@Column(name = "rideEndDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rideEndDate;

	private String tariffCode;

	private BigDecimal baseFare;
	
	private BigDecimal perKmFare;
	
	private BigDecimal waitTimeFare;
	
	private BigDecimal bookingFee;
	
	private BigDecimal kmCharges;
	
	private BigDecimal waitCharges;
	
	private BigDecimal CCCharges;
	
	private BigDecimal discount;

	private BigDecimal otherCharges;
	
	private BigDecimal taxPaid;

	private BigDecimal rideTotalAmt;

	private String paymentStatus;
	
	private Long waitTime;
	
	@Transient
	private String pin;
	
	@Transient
	private Double kmTravelled;
	
	private Integer deleteflag = AppConstants.DELETEFLAG_ALIVE;
	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	
	private String status = AppConstants.STATUS_CONFIRMED;

	private BigDecimal tollCharges;

	private String currency;
	
	public Integer getRideId() {
		return rideId;
	}

	public void setRideId(Integer rideId) {
		this.rideId = rideId;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}



	public Date getRideStartDate() {
		return rideStartDate;
	}

	public void setRideStartDate(Date rideStartDate) {
		this.rideStartDate = rideStartDate;
	}

	public Date getRideEndDate() {
		return rideEndDate;
	}

	public void setRideEndDate(Date rideEndDate) {
		this.rideEndDate = rideEndDate;
	}

	public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}

	public BigDecimal getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(BigDecimal baseFare) {
		this.baseFare = baseFare;
	}

	public BigDecimal getPerKmFare() {
		return perKmFare;
	}

	public void setPerKmFare(BigDecimal perKmFare) {
		this.perKmFare = perKmFare;
	}

	public BigDecimal getWaitTimeFare() {
		return waitTimeFare;
	}

	public void setWaitTimeFare(BigDecimal waitTimeFare) {
		this.waitTimeFare = waitTimeFare;
	}

	public BigDecimal getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(BigDecimal bookingFee) {
		this.bookingFee = bookingFee;
	}


	public BigDecimal getKmCharges() {
		return kmCharges;
	}

	public void setKmCharges(BigDecimal kmCharges) {
		this.kmCharges = kmCharges;
	}

	public BigDecimal getWaitCharges() {
		return waitCharges;
	}

	public void setWaitCharges(BigDecimal waitCharges) {
		this.waitCharges = waitCharges;
	}

	public BigDecimal getCCCharges() {
		return CCCharges;
	}

	public void setCCCharges(BigDecimal cCCharges) {
		CCCharges = cCCharges;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(BigDecimal otherCharges) {
		this.otherCharges = otherCharges;
	}

	public BigDecimal getTaxPaid() {
		return taxPaid;
	}

	public void setTaxPaid(BigDecimal taxPaid) {
		this.taxPaid = taxPaid;
	}


	public BigDecimal getRideTotalAmt() {
		return rideTotalAmt;
	}

	public void setRideTotalAmt(BigDecimal rideTotalAmt) {
		this.rideTotalAmt = rideTotalAmt;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}


	public Integer getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTollCharges() {
		return tollCharges;
	}

	public void setTollCharges(BigDecimal tollCharges) {
		this.tollCharges = tollCharges;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public Double getKmTravelled() {
		return kmTravelled;
	}

	public void setKmTravelled(Double kmTravelled) {
		this.kmTravelled = kmTravelled;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
}
