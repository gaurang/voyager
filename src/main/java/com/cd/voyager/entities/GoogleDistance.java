package com.cd.voyager.entities;

import java.math.BigDecimal;

public class GoogleDistance {

	private Double distance;
	
	private String distanceUnits;
	
	private Double  duration;
	
	private Double  durationInTraffic;
	
	private String durationUnits;
	
	private BigDecimal minFare;
	
	private BigDecimal maxFare;

	private BigDecimal baseFare;
	
	private BigDecimal bookingFee;
	
	private BigDecimal kmRate;
	
	private BigDecimal ucFee;
	
	private Long waitTime;
	
	private BigDecimal waitCharges;

	private BigDecimal travelFare;
	
	private BigDecimal othFee;
	
	private BigDecimal discountCoupon;
	
	private String tariffCode;
	
	public Double  getDistance() {
		return distance;
	}

	public void setDistance(Double  distance) {
		this.distance = distance;
	}

	public String getDistanceUnits() {
		return distanceUnits;
	}

	public void setDistanceUnits(String distanceUnits) {
		this.distanceUnits = distanceUnits;
	}

	public Double  getDuration() {
		return duration;
	}

	public void setDuration(Double  duration) {
		this.duration = duration;
	}

	public String getDurationUnits() {
		return durationUnits;
	}

	public void setDurationUnits(String durationUnits) {
		this.durationUnits = durationUnits;
	}

	public BigDecimal getMinFare() {
		return minFare;
	}

	public void setMinFare(BigDecimal minFare) {
		this.minFare = minFare;
	}

	public BigDecimal getMaxFare() {
		return maxFare;
	}

	public void setMaxFare(BigDecimal maxFare) {
		this.maxFare = maxFare;
	}

	public BigDecimal getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(BigDecimal baseFare) {
		this.baseFare = baseFare;
	}

	public BigDecimal getKmRate() {
		return kmRate;
	}

	public void setKmRate(BigDecimal kmRate) {
		this.kmRate = kmRate;
	}

	public BigDecimal getUcFee() {
		return ucFee;
	}

	public void setUcFee(BigDecimal ucFee) {
		this.ucFee = ucFee;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public BigDecimal getWaitCharges() {
		return waitCharges;
	}

	public void setWaitCharges(BigDecimal waitCharges) {
		this.waitCharges = waitCharges;
	}

	public BigDecimal getTravelFare() {
		return travelFare;
	}

	public void setTravelFare(BigDecimal travelFare) {
		this.travelFare = travelFare;
	}

	public BigDecimal getOthFee() {
		return othFee;
	}

	public void setOthFee(BigDecimal othFee) {
		this.othFee = othFee;
	}

	public BigDecimal getDiscountCoupon() {
		return discountCoupon;
	}

	public void setDiscountCoupon(BigDecimal discountCoupon) {
		this.discountCoupon = discountCoupon;
	}

	public Double getDurationInTraffic() {
		return durationInTraffic;
	}

	public void setDurationInTraffic(Double durationInTraffic) {
		this.durationInTraffic = durationInTraffic;
	}

	public BigDecimal getBookingFee() {
		return bookingFee;
	}

	public void setBookingFee(BigDecimal bookingFee) {
		this.bookingFee = bookingFee;
	}

	public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}
	
	
	
	
}
