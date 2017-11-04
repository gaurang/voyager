package com.cd.voyager.mobility.data.child;

import java.io.Serializable;

public class MetaData implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String operationCode;
	
	private String moduleCode;
	
	private String appCode;
	
	private String encrKey;
	
	private String token;

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getEncrKey() {
		return encrKey;
	}

	public void setEncrKey(String encrKey) {
		this.encrKey = encrKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
