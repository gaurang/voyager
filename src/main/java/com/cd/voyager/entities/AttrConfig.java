package com.cd.voyager.entities;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.entities.idclass.MasterAttr;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@IdClass(MasterAttr.class)
@Table(name="tb_attrconfig")
public class AttrConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String masterAttr;
	
	@Id
	private String attrName;
	
	private String attrValue1;
	
	private String attrValue2;
	
	private String type;
	
	private String level;
	
	private String unit;
	
	@JsonIgnore
	private String flag;
	
	@JsonIgnore
	private Integer zoneId;
	
	@JsonIgnore
	private Integer sort;
	
	@JsonIgnore
	private String status =  AppConstants.STATUS_ACTIVE;

	@Transient
	private Map<String, AttrConfig> subAttributes ;

	public String getMasterAttr() {
		return masterAttr;
	}

	public void setMasterAttr(String masterAttr) {
		this.masterAttr = masterAttr;
	}

	public String getAttrValue1() {
		return attrValue1;
	}

	public void setAttrValue1(String attrValue1) {
		this.attrValue1 = attrValue1;
	}

	public String getAttrValue2() {
		return attrValue2;
	}

	public void setAttrValue2(String attrValue2) {
		this.attrValue2 = attrValue2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrName() {
		return attrName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Map<String, AttrConfig> getSubAttributes() {
		return subAttributes;
	}

	public void setSubAttributes(Map<String, AttrConfig> subAttributes) {
		this.subAttributes = subAttributes;
	}
	
	
}
