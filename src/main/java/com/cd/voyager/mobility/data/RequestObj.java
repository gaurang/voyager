package com.cd.voyager.mobility.data;

import java.io.Serializable;

import com.cd.voyager.mobility.data.child.MetaData;
import com.cd.voyager.mobility.data.child.ReqHeader;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestObj<T> implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8404575431276563086L;

	private ReqHeader reqHeader;
	
	private MetaData metaData;
	
	private T body;


	public ReqHeader getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(ReqHeader reqHeader) {
		this.reqHeader = reqHeader;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
	
	
	

}
