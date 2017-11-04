package com.cd.voyager.web.formWrappers;

import com.cd.voyager.entities.BankDetails;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverDetails;

public class DriverForm {
	
	
	
	private Driver driver = new Driver();
	
	private DriverDetails driverDetails = new DriverDetails();
	
	private BankDetails bank = new BankDetails();
	
	private DrVehicle drVehicle =  new DrVehicle();
	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public BankDetails getBank() {
		return bank;
	}

	public void setBank(BankDetails bank) {
		this.bank = bank;
	}

	public DrVehicle getDrVehicle() {
		return drVehicle;
	}

	public void setDrVehicle(DrVehicle drVehicle) {
		this.drVehicle = drVehicle;
	}

	public DriverDetails getDriverDetails() {
		return driverDetails;
	}

	public void setDriverDetails(DriverDetails driverDetails) {
		this.driverDetails = driverDetails;
	}

	
	

	
}
