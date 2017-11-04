package com.cd.voyager.mobility.data;

import com.cd.voyager.mobility.data.child.ResHeader;

public class ResponseObj {

	private ResHeader header;
	
	private Object body;

	public ResHeader getHeader() {
		return header;
	}

	public void setHeader(ResHeader header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	
	
}
