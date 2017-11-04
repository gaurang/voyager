package com.cd.voyager.entities.idclass;

import java.io.Serializable;


public class MasterAttr implements Serializable {
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attrName == null) ? 0 : attrName.hashCode());
		result = prime * result
				+ ((masterAttr == null) ? 0 : masterAttr.hashCode());
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
		MasterAttr other = (MasterAttr) obj;
		if (attrName == null) {
			if (other.attrName != null)
				return false;
		} else if (!attrName.equals(other.attrName))
			return false;
		if (masterAttr == null) {
			if (other.masterAttr != null)
				return false;
		} else if (!masterAttr.equals(other.masterAttr))
			return false;
		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6394297114775802794L;

	private String masterAttr;
	
	private String attrName;
	
	
	
}
