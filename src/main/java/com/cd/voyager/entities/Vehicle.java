package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cd.voyager.common.util.AppConstants;

@Entity
@Table(name="tb_drvehicle")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vehicleId;

	private Integer driverId;

	private String vType;
	private String make;
	private String model;
	private String registrationId;

	private String registrationDocPath;
	private String carNumber;
	private Integer seatingCapacity;
	private String vehAttributes;
	private String chasisNo;
	private Integer defaultCar;
	
	//private String driverId;
	
	private String vehInsuranceNo;
	private String vehInsuranceRenewDt;
	private String vehInsurancePath;
	
	private String ownerId;
	private String ownerName;
	
	
/*	private Integer overSizeLuggage;
	private Integer animalSeat;
	private Integer babySeat;
*/
	private String status = AppConstants.STATUS_ACTIVE;
	
//	private Integer srNo;

	

	@ManyToOne
    @JoinColumn(name="driverId", insertable=false, updatable=false, nullable=false)
	private Driver driver;


	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Integer getDriverId() {
		return driverId;
	}
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	public String getvType() {
		return vType;
	}
	public void setvType(String vType) {
		this.vType = vType;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public String getRegistrationDocPath() {
		return registrationDocPath;
	}
	public void setRegistrationDocPath(String registrationDocPath) {
		this.registrationDocPath = registrationDocPath;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public Integer getSeatingCapacity() {
		return seatingCapacity;
	}
	public void setSeatingCapacity(Integer seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	public String getVehAttributes() {
		return vehAttributes;
	}
	public void setVehAttributes(String vehAttributes) {
		this.vehAttributes = vehAttributes;
	}
	public String getChasisNo() {
		return chasisNo;
	}
	public void setChasisNo(String chasisNo) {
		this.chasisNo = chasisNo;
	}
	public Integer getDefaultCar() {
		return defaultCar;
	}
	public void setDefaultCar(Integer defaultCar) {
		this.defaultCar = defaultCar;
	}
	public String getVehInsuranceNo() {
		return vehInsuranceNo;
	}
	public void setVehInsuranceNo(String vehInsuranceNo) {
		this.vehInsuranceNo = vehInsuranceNo;
	}
	public String getVehInsuranceRenewDt() {
		return vehInsuranceRenewDt;
	}
	public void setVehInsuranceRenewDt(String vehInsuranceRenewDt) {
		this.vehInsuranceRenewDt = vehInsuranceRenewDt;
	}
	public String getVehInsurancePath() {
		return vehInsurancePath;
	}
	public void setVehInsurancePath(String vehInsurancePath) {
		this.vehInsurancePath = vehInsurancePath;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	
}
