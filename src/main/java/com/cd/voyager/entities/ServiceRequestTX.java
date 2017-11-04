package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_servicerequesttx")
public class ServiceRequestTX {

	@Id
	private String SRNo;
	
	private String vehicleId;
	
	private String driverId;
	
	
	
}
