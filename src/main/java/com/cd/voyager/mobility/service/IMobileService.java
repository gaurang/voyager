package com.cd.voyager.mobility.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.CustomerRide;
import com.cd.voyager.entities.DrLocation;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverBooking;
import com.cd.voyager.entities.DriverEarnings;
import com.cd.voyager.entities.DriverLogin;
import com.cd.voyager.entities.DriverRide;
import com.cd.voyager.entities.EmergancyContact;
import com.cd.voyager.entities.EnquiryForm;
import com.cd.voyager.entities.Notification;
import com.cd.voyager.entities.Payment;
import com.cd.voyager.entities.Preferences;
import com.cd.voyager.entities.PromoApplied;
import com.cd.voyager.entities.PromoValidate;
import com.cd.voyager.entities.RegDevice;
import com.cd.voyager.entities.Registration;
import com.cd.voyager.entities.SupportMaster;

public interface IMobileService {

	public void saveRegistration(Registration reg) ;
	
	public void saveCustomer(Customer reg) ;
	
	public void saveRegisteredDevice(RegDevice regDevice) ;

	public boolean isRegistrationExist(Integer id);
	
	public boolean isRegistrationExist(Registration reg);

	public Registration getIfRegistrationExist(Registration reg);
	
	public Registration updateRegistration(Registration reg) ;
	
	public boolean isCustomerExist(Integer id);
	
	public boolean isCustomerExist(Customer customer);
	
	public boolean isCustomerExist(String email);
	
	public Customer signIn(Customer customer) ;
	
	public Integer savePayment(Payment payment);
	 
	public void savePref(Preferences pref);
	
	public void saveEmargancyContact(EmergancyContact ec);
	
	public List<EmergancyContact> getEmargancyContact(Integer customerId);
	
	public Integer updateOTP(Registration reg);

	public Registration verifyOTP(Registration reg);
	
	public Map<String,String> getpaymentGateways();
	
	public Customer signUpCustomer(Customer customer, Payment payment) throws Exception;
	
	public Integer getCorpCustIdFromEmail(String email);

	public Integer getCorpCustPaymentIdFromEmail(String email, String pin);

	public Integer changeCustomerPassword(Integer customerId, String oldPassword, String newPassword);
	
	public Integer changeCustomerPasswordOTP(String email, String newPassword, String OTP);
	
	public Integer changeCustomerPasswordOTPCusomertId(String custId, String newPassword, String OTP);
	
	public List<Preferences> getPrefData(Integer customerId);
	
	public List<Payment> getPaymentData(Integer customerId, String status);
	
	public Integer deletePayment(Integer customerId, Integer paymentId);
	
	public Integer deletePref(Integer customerId, String favLabel);

	public Integer delEmergancyContact(Integer customerId, Integer contactId);
	
	public Integer updateProfile(Customer customers);
	
	public Customer getCustmerProfile(Integer customerId);

	public List<Map> getAccounts(Integer customerId);

	public Integer addAccount(Integer customerId, Integer paymentId, String email, String accountType, String pin);

	public Boolean applyPromo(Integer customerId, String promoCode);

	public Integer validateRefCode(String promoCode, Integer customerId, Date expDate, Double discount);
	
	public Integer validatePromoCode(String promoCode, Integer customerId);
	
	public PromoValidate getPromoValidate( Integer promoId, Integer customerId);
	
	public PromoApplied getPromoApplied(Integer promoId, Integer bookingId);
	
	public void applyRef(PromoValidate pv);

	public Driver signInDriver(Driver driver) ;
	
	public Driver getDriver(Integer driverId) ;
	
	public Driver getDriver(Driver driver) ;
	
	public void applyPromo(PromoApplied promoApplied) ;
	
	public List<SupportMaster> getSupport(String supportFor) ;
	
	public List<SupportMaster> getSupportQuestions(String supportFor) ;
	
	public SupportMaster getSupportMaster(Integer supportId) ;
	
	public Integer updateOTPDriver(Driver driver);
	
	public Integer verifyOTPDriver(String driverId, String OTP);

	public Integer updateProfileDriver(Driver driver);
	
	public List<Booking> getBookingHistory(Integer customerId, Integer start, Integer end, Date startDate, Date endDate);
	
	public Booking getBooking(Integer bookingId);
	
	public Booking getBookingDetails(Integer bookingId);

	public List<Booking> getDriverBookingHistory(Integer driverId, Integer start, Integer end, Date startDate, Date endDate);

	public Booking insertCustomerBooking(Booking booking);
	
	public void updateCustomerBooking(Booking booking) ;
	
	public void insertDriverBooking(DriverBooking driverBooking);
	
	public void updateDriverBooking(DriverBooking driverBooking);
	
	
	public CustomerRide insertCustomerRide(CustomerRide customerRide);
	
	public void insertDriverRide(DriverRide driverRide);

	public Integer startRide(CustomerRide customerRide, LinkedHashMap<String, LinkedHashMap<String, AttrConfig>>  cnfigMap);
	
	public CustomerRide endJourney(CustomerRide custRide, LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> ConfigMap);
	
	public void endRide(CustomerRide customerRide, DriverRide driverRide);
	
	public void driverReject(DriverBooking driverBooking);
	
	public void saveDrLoc(DrLocation dloc);
	
	public void deleteDrLoc(DrLocation dloc);

	public List<DrLocation> getCSE(String lat, String lng, String place, String area, Integer radius, String vehicleType );

	public Driver getClosestDriver(Integer bookingId) throws Exception ;
	
	public Driver getClosestDriver(Booking booking) throws Exception ;

	//public Driver bookCab(Booking booking);

	public Driver bookCab(Booking booking, boolean nextCSE);

	public Integer cancelBooking(Booking Booking);

	//public Integer updateBookingStatus(Booking booking);

	public void addEnquiry(EnquiryForm enq);
	
	public List<DrVehicle> getDrVehicle(Integer driverId);
	
	public DrVehicle getDrVehicleDetails(Integer vehicleId) ;
	
	public Integer checkCse(String email, Long ph);

	public Integer checkEmail(String email);

	public void setPassword(Integer d, String password);

	public void saveDEmargancyContact(EmergancyContact em);

	public List<EmergancyContact> getDEmargancyContact(Integer driverId);

	public void delDEmergancyContact(Integer driverId, Integer contactId);

	public List<Integer> getRejectedDrivers(Integer bookingId);
	
	public Integer capturePayment(Integer bookingId,Integer rideId, String currency, BigDecimal tipAmount);
	
	public void getSendInvoice(Integer bookingId);

	public CustomerRide getCustomerRide(Integer rideId);

	public CustomerRide getCustomerRideFromBooking(Integer bookingId);

	public Integer changeDefAccount(Integer customerId, String accountType);

	public Integer getRembursed(Integer corpCustId);

	public Integer verifyEmailPin(String email, String pin);

	public DriverBooking getDriverBooking(Integer bookingId);
	
	public Driver getDriverFromBooking(Integer bookingId);

	public Map<String,Object> acceptBooking(DriverBooking db);
	
	public Integer rejectBooking(DriverBooking db);
	
	public Customer customerFromBooking(Integer bookingId);
	
	public DrVehicle saveDrVehicle(DrVehicle dv) ;

	public DrVehicle getDrVehicleDetail(Integer vehicleId);

	void saveRatingByDriver(DriverRide dtd); 
	
	public void bookingTimeOut(Booking b);

	public Integer driverRejectIfdNotAccept(Integer driverId, Integer bookingId, String status);

	public Integer nextAvailableDriverBooking(Integer bookingId);

	public Integer updateDriverBookingStatus(Booking booking, Integer driverId);
	
	//public void updateDriverLocationStatus(Integer driverId, String statusRiding);

	public Integer updateCustomerBooking(Integer bookingId, String statusNotAvailable);

	public void updateDriverLocationStatus(Integer driverId, String statusRiding, Integer bookingId);

	Integer updateBookingStatus(Booking booking, Integer driverId);
	
	public String getMobile(String email);

	public void updateDriverLocation(DrLocation dloc);

	public void saveDriverLogin(DriverLogin driverLogin);

	public void deleteDriverLogin(Integer driverId);

	public List<Booking> startupCheck(Integer customerId);

	List<Booking> getTrackBookingList(Integer bookingId);

	public DrLocation getDrLocation(Integer driverId);

	public void saveDriverEarning(DriverEarnings de);

	public Booking bookingDetails(Integer bookingId);

	public Customer getCustmerProfileByEmail(String email);

	public Integer updateOTPForgotPass(Customer c);

	public Customer verifyOTPForgotPass(Customer c);

	void saveRatingByCustomer(DriverRide dtd);

	DriverBooking getDriverBooking(Integer bookingId, Integer status);

	public void makeInvoice(Integer bookingId, Customer c);

	public List<Notification> getNotiifications();

	public List<DrVehicle> getDrVehicleList(Driver d);

	public Booking startupCheckDriver(Integer driverId);
	
	public Integer captureJourneyPayment(Integer bookingId);
		
}

