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

@Entity
@Table(name="tb_driverearnings")

public class DriverEarnings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer earningId;
	
	private Integer driverId;
	
	@Column(name = "rideDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rideDate = new Date();
	
	private BigDecimal fareTotal;
	
	private BigDecimal tollTotal;
	
	private BigDecimal otherExp = new BigDecimal(0);
	
	private BigDecimal bookingFeeTotal;
	
	private BigDecimal voyagerFee;
	
	private BigDecimal driverEarning;
	
	private String status;
	
	@Transient
	private BigDecimal totalEarning;
	
	@Column(name = "createDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	public Integer getEarningId() {
		return earningId;
	}

	public void setEarningId(Integer earningId) {
		this.earningId = earningId;
	}

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}
	
	public BigDecimal getFareTotal() {
		return fareTotal;
	}

	public void setFareTotal(BigDecimal fareTotal) {
		this.fareTotal = fareTotal;
	}

	public BigDecimal getTollTotal() {
		return tollTotal;
	}

	public void setTollTotal(BigDecimal tollTotal) {
		this.tollTotal = tollTotal;
	}

	public BigDecimal getOtherExp() {
		return otherExp;
	}

	public void setOtherExp(BigDecimal otherExp) {
		this.otherExp = otherExp;
	}

	public BigDecimal getBookingFeeTotal() {
		return bookingFeeTotal;
	}

	public void setBookingFeeTotal(BigDecimal bookingFeeTotal) {
		this.bookingFeeTotal = bookingFeeTotal;
	}

	public BigDecimal getvoyagerFee() {
		return voyagerFee;
	}

	public void setvoyagerFee(BigDecimal voyagerFee) {
		this.voyagerFee = voyagerFee;
	}

	public BigDecimal getDriverEarning() {
		return driverEarning;
	}

	public void setDriverEarning(BigDecimal driverEarning) {
		this.driverEarning = driverEarning;
	}

	public BigDecimal getTotalEarning() {
		return totalEarning;
	}

	public void setTotalEarning(BigDecimal totalEarning) {
		this.totalEarning = totalEarning;
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

	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	
	
	
	
}
