package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_supportmaster")

public class SupportMaster {


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer supportId;
	
	private String supportType;
	
	private String supportQuestion;

	private String description;
	
	private String supportFor;

	public Integer getSupportId() {
		return supportId;
	}

	public void setSupportId(Integer supportId) {
		this.supportId = supportId;
	}

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}

	public String getSupportQuestion() {
		return supportQuestion;
	}

	public void setSupportQuestion(String supportQuestion) {
		this.supportQuestion = supportQuestion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSupportFor() {
		return supportFor;
	}

	public void setSupportFor(String supportFor) {
		this.supportFor = supportFor;
	}

	
}
