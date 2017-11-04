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

import com.cd.voyager.common.util.AppConstants;

@Entity
@Table(name="tb_drivertripdetails")
public class DriverRide {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer driverRideId;

	private Integer bookingId;
	  
	private Integer  driverId;

	private String  vehicleId;
	
	private Double  kmTraveled;

	private BigDecimal  travelTime;
	
	private String  waitTime;
	
	private Integer  driverRating;
	private Integer  custRating;
	
	private Double  custRewards;
	
	private Double  corpRewards;
	
	private Double  driverRewards;
	
	private BigDecimal  voyagerFee;

	private BigDecimal  driverTip;
	
	private BigDecimal  tripEarning;
	
	private String  feedback;

	
	private Double  kmTraveledGDM;
	
	private String  drPaymentStatus = AppConstants.PAYMENT_PENDING;
	
	private BigDecimal rideFare;

	


	public Integer getDriverRideId() {
		return driverRideId;
	}

	public void setDriverRideId(Integer driverRideId) {
		this.driverRideId = driverRideId;
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

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}


	public Double getKmTraveled() {
		return kmTraveled;
	}

	public void setKmTraveled(Double kmTraveled) {
		this.kmTraveled = kmTraveled;
	}

	public BigDecimal getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(BigDecimal travelTime) {
		this.travelTime = travelTime;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public Integer getDriverRating() {
		return driverRating;
	}

	public void setDriverRating(Integer driverRating) {
		this.driverRating = driverRating;
	}


	public Double getCustRewards() {
		return custRewards;
	}

	public void setCustRewards(Double custRewards) {
		this.custRewards = custRewards;
	}

	public Double getCorpRewards() {
		return corpRewards;
	}

	public void setCorpRewards(Double corpRewards) {
		this.corpRewards = corpRewards;
	}

	public Double getDriverRewards() {
		return driverRewards;
	}

	public void setDriverRewards(Double driverRewards) {
		this.driverRewards = driverRewards;
	}

	public BigDecimal getvoyagerFee() {
		return voyagerFee;
	}

	public void setvoyagerFee(BigDecimal voyagerFee) {
		this.voyagerFee = voyagerFee;
	}

	public BigDecimal getDriverTip() {
		return driverTip;
	}

	public void setDriverTip(BigDecimal driverTip) {
		this.driverTip = driverTip;
	}

	public BigDecimal getTripEarning() {
		return tripEarning;
	}

	public void setTripEarning(BigDecimal tripEarning) {
		this.tripEarning = tripEarning;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getCustRating() {
		return custRating;
	}

	public void setCustRating(Integer custRating) {
		this.custRating = custRating;
	}

	public Double getKmTraveledGDM() {
		return kmTraveledGDM;
	}

	public void setKmTraveledGDM(Double kmTraveledGDM) {
		this.kmTraveledGDM = kmTraveledGDM;
	}
	

	public String getDrPaymentStatus() {
		return drPaymentStatus;
	}

	public void setDrPaymentStatus(String drPaymentStatus) {
		this.drPaymentStatus = drPaymentStatus;
	}

	public BigDecimal getRideFare() {
		return rideFare;
	}

	public void setRideFare(BigDecimal rideFare) {
		this.rideFare = rideFare;
	}


}
