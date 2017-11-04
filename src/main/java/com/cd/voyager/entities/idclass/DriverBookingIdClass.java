package com.cd.voyager.entities.idclass;

import java.io.Serializable;

import javax.persistence.Id;

public class DriverBookingIdClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer driverId;
	
	private Integer bookingId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingId == null) ? 0 : bookingId.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DriverBookingIdClass other = (DriverBookingIdClass) obj;
		if (bookingId == null) {
			if (other.bookingId != null)
				return false;
		} else if (!bookingId.equals(other.bookingId))
			return false;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		return true;
	}

	
	
}
