package com.cd.voyager.entities.idclass;

import java.io.Serializable;

public class PreferenceId  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1351475970014553182L;

	/**
	 * 
	 */

	private Integer customerId;
	
	
	private String favLabel;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result
				+ ((favLabel == null) ? 0 : favLabel.hashCode());
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
		PreferenceId other = (PreferenceId) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (favLabel == null) {
			if (other.favLabel != null)
				return false;
		} else if (!favLabel.equals(other.favLabel))
			return false;
		return true;
	}
	
	
	
	

}
