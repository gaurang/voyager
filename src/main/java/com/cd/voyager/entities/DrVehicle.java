package com.cd.voyager.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cd.voyager.common.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tb_drvehicle")
public class DrVehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vehicleId;
	//private String id;

	private Integer driverId;
	
	//private String registrationId;
	private String vType;
	private String make;
	private String model;

	//private String registrationDocPath;
	private String carNumber;
	private Integer seatingCapacity;
	private String vehAttributes;
	private String chasisNo;
	
	private Integer defaultCar = AppConstants.STATUS_GLOBAL_ON;
	private String vehInsuranceNo;
	private String vehinsurancePath;
	private String vehinsuranceRenewDt;
	private String ownerName;
	private String ownerId;
	private String registrationId;
	private String registrationDocPath;
	private String status = AppConstants.STATUS_ACTIVE;
	//private String insuarancePolicyNo;
	//private String chasisNo;
	//private String vType;
	//private String make;
	//private String model;
	
	//private Integer noOfPassanger;
	//private Integer overSizeLuggage;
	//private Integer animalSeat;
	//private Integer babySeat;

	
	
//	private Integer srNo;

	
	/*public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInsuarancePolicyNo() {
		return insuarancePolicyNo;
	}
	public void setInsuarancePolicyNo(String insuarancePolicyNo) {
		this.insuarancePolicyNo = insuarancePolicyNo;
	}
	public String getInsuranceRenewalDate() {
		return insuranceRenewalDate;
	}
	public void setInsuranceRenewalDate(String insuranceRenewalDate) {
		this.insuranceRenewalDate = insuranceRenewalDate;
	}*/
	public String getChasisNo() {
		return chasisNo;
	}
	public void setChasisNo(String chasisNo) {
		this.chasisNo = chasisNo;
	}
	public String getvType() {
		return vType;
	}
	public void setvType(String vType) {
		this.vType = vType;
	}
	
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	/*public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}*/
	
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	/*public String getInsuranceDocPath() {
		return insuranceDocPath;
	}
	public void setInsuranceDocPath(String insuranceDocPath) {
		this.insuranceDocPath = insuranceDocPath;
	}*/
/*	public Integer getSrNo() {
		return srNo;
	}
	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}
*/	/*public Integer getDefaultCar() {
		return defaultCar;
	}
	public void setDefaultCar(Integer defaultCar) {
		this.defaultCar = defaultCar;
	}
	public Integer getNoOfPassanger() {
		return noOfPassanger;
	}
	public void setNoOfPassanger(Integer noOfPassanger) {
		this.noOfPassanger = noOfPassanger;
	}
	public Integer getOverSizeLuggage() {
		return overSizeLuggage;
	}
	public void setOverSizeLuggage(Integer overSizeLuggage) {
		this.overSizeLuggage = overSizeLuggage;
	}
	public Integer getAnimalSeat() {
		return animalSeat;
	}
	public void setAnimalSeat(Integer animalSeat) {
		this.animalSeat = animalSeat;
	}
	public Integer getBabySeat() {
		return babySeat;
	}
	public void setBabySeat(Integer babySeat) {
		this.babySeat = babySeat;
	}*/
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
	
	@ManyToOne
    @JoinColumn(name="driverId", insertable=false, updatable=false, nullable=false)
	@JsonIgnore
	private Driver driver;

	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public Integer getDriverId() {
		return driverId;
	}
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
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
	public String getVehInsuranceNo() {
		return vehInsuranceNo;
	}
	public void setVehInsuranceNo(String vehInsuranceNo) {
		this.vehInsuranceNo = vehInsuranceNo;
	}
	public String getVehinsurancePath() {
		return vehinsurancePath;
	}
	public void setVehinsurancePath(String vehinsurancePath) {
		this.vehinsurancePath = vehinsurancePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVehinsuranceRenewDt() {
		return vehinsuranceRenewDt;
	}
	public void setVehinsuranceRenewDt(String vehinsuranceRenewDt) {
		this.vehinsuranceRenewDt = vehinsuranceRenewDt;
	}
	public Integer getDefaultCar() {
		return defaultCar;
	}
	public void setDefaultCar(Integer defaultCar) {
		this.defaultCar = defaultCar;
	}
	
	
}
