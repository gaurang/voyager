
package com.cd.voyager.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_notification")

public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notifyId;

	private String textMsg;

	private String status;
	
	private Date notifyEndDate ;

	public Integer getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Integer notifyId) {
		this.notifyId = notifyId;
	}

	public String getTextMsg() {
		return textMsg;
	}

	public void setTextMsg(String textMsg) {
		this.textMsg = textMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getNotifyEndDate() {
		return notifyEndDate;
	}

	public void setNotifyEndDate(Date notifyEndDate) {
		this.notifyEndDate = notifyEndDate;
	}


	
	
}
