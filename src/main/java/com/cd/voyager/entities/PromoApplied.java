package com.cd.voyager.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="tb_promoapplied")
public class PromoApplied {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer promoAppId;
	
	private Integer promoValidateId;

	@Column(name = "appliedDate", columnDefinition="DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date appliedDate;

	private Integer bookingId;
	
	public Integer getPromoAppId() {
		return promoAppId;
	}

	public void setPromoAppId(Integer promoAppId) {
		this.promoAppId = promoAppId;
	}

	public Integer getPromoValidateId() {
		return promoValidateId;
	}

	public void setPromoValidateId(Integer promoValidateId) {
		this.promoValidateId = promoValidateId;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
	
	
}
