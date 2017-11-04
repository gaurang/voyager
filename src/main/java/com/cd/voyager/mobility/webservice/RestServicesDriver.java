package com.cd.voyager.mobility.webservice;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.common.util.MD5Utils;
import com.cd.voyager.common.util.SMSUtil;
import com.cd.voyager.common.util.StringGenUtils;
import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.CustomerRide;
import com.cd.voyager.entities.DrLocation;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverBooking;
import com.cd.voyager.entities.DriverLogin;
import com.cd.voyager.entities.DriverRide;
import com.cd.voyager.entities.EmergancyContact;
import com.cd.voyager.entities.RegDevice;
import com.cd.voyager.entities.Registration;
import com.cd.voyager.entities.SupportMaster;
import com.cd.voyager.exception.InvalidCredentialsException;
import com.cd.voyager.exception.ServiceException;
import com.cd.voyager.mobility.data.RequestObj;
import com.cd.voyager.mobility.data.ResponseObj;
import com.cd.voyager.mobility.data.child.ResHeader;
import com.cd.voyager.mobility.service.IMobileService;
import com.cd.voyager.web.service.IMainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@PropertySource("classpath:messages_en.properties")
@RequestMapping("/api/cse")
public class RestServicesDriver {

	private final Logger logger = LoggerFactory.getLogger(RestServicesDriver.class);

	@Autowired
	private IMobileService mobileServiceImpl;

	@Autowired
	private IMainService mainServiceImpl;

	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;

	@RequestMapping(value = "/signup", method = RequestMethod.POST, headers = "content-type=application/json;charset=utf-8")
    public ResponseEntity<ResponseObj> signup(@Valid @RequestBody RequestObj<Registration> reqObj, UriComponentsBuilder ucBuilder) {

		
		ResponseObj resObj = new ResponseObj();
		Registration reg = reqObj.getBody();
		//validation
		ObjectMapper o = new ObjectMapper();
		try {
			System.out.println(o.writeValueAsString(reqObj));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResHeader header = new ResHeader();
		String msg = "";
		boolean err = false;
		String invalidMsg = env.getProperty("validation.empty.input" , "$${element}}}");
		
		StringGenUtils.isEmpty(reg.getEmail(), "Email", invalidMsg);
		StringGenUtils.isEmpty(reg.getMobile(), "Mobile", invalidMsg);
		
		StringGenUtils.isEmpty(reg.getFname(), "First Name", invalidMsg);
		StringGenUtils.isEmpty(reg.getLname(), "Last Name", invalidMsg);

		/*if(StringUtils.isEmpty(reg.getMobile())){
	  		header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
	  		msg += "Invalid Mobile";
	  		err = true;
		}
		
		if(StringUtils.isEmpty(reg.getFname())||StringUtils.isEmpty(reg.getLname())){
	  		header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
	  		msg += "Invalid Name";
	  		err = true;
		}
		*/
		
		if(err){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(msg);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if (mobileServiceImpl.isRegistrationExist(reg)) {//TODO check mobile duplicates 1-22-2016
			if(mobileServiceImpl.isCustomerExist(reg.getEmail())){
				
				logger.info("A User with name " + reg.getFname() + " already exist");
	    		header.setStatusCode(AppConstants.ERROR_DUPLICATE_ENRTY);
	    		header.setMessage("A User with name " + reg.getFname() + " already exist");
			
	    		resObj.setBody(reg);
	    		resObj.setHeader(header);

	    		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);	
			}else{
				//STARTED registratin but couldnot finish due to some issues may continue further with OTP
				//Send OTP
				
				reg.setOTPDate(new Date());
				
				reg.setOTP(SMSUtil.gerateOTP());
				logger.debug(" $$$$$$$$$$$$$  existing reg continue "+reg.getOTP());
				
				mobileServiceImpl.updateOTP(reg);
				SMSUtil smsUtil =new SMSUtil();
				smsUtil.sendSMS("OTP_TPL", reg.getMobile(), reg.getOTP(), env) ;

				reg = mobileServiceImpl.getIfRegistrationExist(reg); 
				resObj.setBody(reg);
				header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
				resObj.setHeader(header);
				logger.debug(" $$$$$$$$$$$$$  existing reg continue "+reqObj.getReqHeader()+"  "+reg.getRegId()+"  " +header);
	            return new ResponseEntity<ResponseObj>(resObj, HttpStatus.ALREADY_REPORTED);
			}
        }else{
			RegDevice regDevice = new RegDevice();
			/*
	        int randomPIN = (int)(Math.random()*9000)+1000;
			reg.setPin(randomPIN);
			*/
			reg.setOTPDate(new Date());	
			reg.setOTP(SMSUtil.gerateOTP());
			mobileServiceImpl.updateOTP(reg);
			SMSUtil smsUtil =new SMSUtil();
			
			if(!smsUtil.sendSMS("OTP_TPL", reg.getMobile(), reg.getOTP(), env)){
				header.setStatusCode(AppConstants.GLOBAL_PAGE_ERROR);
				resObj.setBody(env.getProperty("SMS.sent.error.msg"));
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}else{
				header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
				header.setMessage(env.getProperty("OTP.sent.msg"));
			}
			
			mobileServiceImpl.saveRegistration(reg);
			
			regDevice.setImei(reqObj.getReqHeader().getImeiNo());
			regDevice.setMobileDeviceID(reqObj.getReqHeader().getDeviceId());
			regDevice.setRegId(reg.getRegId());
	
			mobileServiceImpl.saveRegisteredDevice(regDevice);
			
			resObj.setBody(reg);
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			//header.setErrorMessage(errorMessage);
		//	reg.setMessage(voyagerConstants.GLOBAL_SUCCESS);
			
			logger.debug("$$$$$$$$$$$$$ new Reg  "+reqObj.getReqHeader()+"  "+reg.getRegId()+"  " +header);
			resObj.setHeader(header);
			
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
        }
	}
	
	@RequestMapping(value = "/resentOTP", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	resentOTP(@Valid @RequestBody RequestObj<Driver> reqObj) {
		ResponseObj resObj = new ResponseObj();
		ResHeader header = new ResHeader();
		Driver driver= reqObj.getBody();
		String opCode = reqObj.getMetaData().getOperationCode();
		
		Driver d = mobileServiceImpl.getDriver(driver.getDriverId());
		//Resent otp
		if(mobileServiceImpl.updateOTPDriver(d) == Integer.parseInt(AppConstants.GLOBAL_PAGE_ERROR) ){
			header.setStatusCode(AppConstants.GLOBAL_PAGE_ERROR);
			resObj.setBody("SMS could not be send try later");
			
		}else{
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			resObj.setBody("OTP has been sent on your registered number,please verify");
		}
		
		resObj.setHeader(header);
		
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/verifyOTP", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	verifyOTP(@Valid @RequestBody RequestObj<HashMap<String, String>> reqObj) {
		
		
		/*ObjectMapper om = new ObjectMapper();
		try {
			logger.debug(om.writeValueAsString(reqObj));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		ResponseObj resObj = new ResponseObj();
		HashMap<String, String> driverOTP = reqObj.getBody();
		
		ResHeader header = new ResHeader();
		Integer i  = mobileServiceImpl.verifyOTPDriver(driverOTP.get("driverId"), driverOTP.get("OTP"));
		Driver d = mobileServiceImpl.getDriver(Integer.parseInt(driverOTP.get("driverId")));
		if(i == AppConstants.VALID_ENTRY){
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			header.setMessage("OTP has been succesfully verified");
			resObj.setHeader(header);
		}else{
			header.setStatusCode(AppConstants.ERROR_INVALID_OTP);
			header.setMessage("Invalid OTP");
			resObj.setHeader(header);
		}
		resObj.setBody(d);
		
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	signIn(@Valid @RequestBody RequestObj<Driver> reqObj) {

		ResHeader header = new ResHeader();
		Driver driver = reqObj.getBody();
		
		String msg = "";
		boolean err = false;
		String invalidMsg = env.getProperty("validation.empty.input");
		
		ResponseObj resObj = new ResponseObj();
		Driver driverReturn  =  null;

		if(StringUtils.isEmpty(driver.getCseCode()) && StringUtils.isEmpty(driver.getEmail())
				|| StringUtils.isEmpty(driver.getPassword()))
		{
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage("Invalid Credentials");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);	
		}
		
		StringGenUtils.isEmpty(driver.getPassword(), "Password", invalidMsg);
		
		try{
			driverReturn= mobileServiceImpl.signInDriver(driver);
		}catch(InvalidCredentialsException e){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(e.getMessage());
			resObj.setHeader(header);
			//resObj.setBody(
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		resObj.setBody(driverReturn);
		resObj.setHeader(header);

		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "/signout", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	signOut(@Valid @RequestBody RequestObj<DriverLogin> reqObj) {
		
		DriverLogin driverLogin = reqObj.getBody();
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Integer driverId = driverLogin.getDriverId();
		mobileServiceImpl.deleteDriverLogin(driverId);
		DrLocation dloc = new DrLocation();
		dloc.setDriverId(driverId);
		mobileServiceImpl.deleteDrLoc(dloc);
		
		header.setMessage("Successfully loged out");
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		
		resObj.setBody("Successfully loged out");
		resObj.setHeader(header);

		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getDriverDetails", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	getDriverDetails(@Valid @RequestBody RequestObj<Driver> reqObj) {

		ResHeader header = new ResHeader();
		Driver driver = reqObj.getBody();
		String msg = "";
		boolean err = false;
		String invalidMsg = env.getProperty("validation.empty.input");
		
		ResponseObj resObj = new ResponseObj();
		Driver driverReturn  =  null;

		/*if(StringUtils.isEmpty(driver.getCseCode()) && StringUtils.isEmpty(driver.getEmail())
				|| StringUtils.isEmpty(driver.getDriverId()))
		{
			header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
			header.setMessage("Invalid Credentials");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);	
		}
		*/
		try{
			if(StringUtils.isEmpty(driver.getDriverId()))
				driverReturn = mobileServiceImpl.getDriver(driver.getDriverId());
			else if(StringUtils.isEmpty(driver.getCseCode()))
				driverReturn = mobileServiceImpl.getDriver(driver);	
			
			
		}catch(InvalidCredentialsException e){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(e.getMessage());
			resObj.setHeader(header);
			//resObj.setBody(
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		resObj.setBody(driverReturn);
		resObj.setHeader(header);

		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/updateDriver", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	updateDriver(@Valid @RequestBody RequestObj<Driver> reqObj) {

		ResHeader header = new ResHeader();
		Driver driver = reqObj.getBody();
		
		ResponseObj resObj = new ResponseObj();
		Driver driverReturn  =  null;

		if(StringUtils.isEmpty(driver.getDriverId())){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage("Invalid Service Call, Vaues not found ");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);	
		}
		
		try{
			mobileServiceImpl.updateProfileDriver(driver);
			driverReturn = mobileServiceImpl.getDriver(driver.getDriverId());
			
		}catch(InvalidCredentialsException e){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(e.getMessage());
			resObj.setHeader(header);
			//resObj.setBody(
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		resObj.setBody(driverReturn);
		resObj.setHeader(header);

		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}	

	@RequestMapping(value = "/getBookingHistory", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getBookingHistory(@RequestBody RequestObj<Driver> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Integer driverId =  reqObj.getBody().getDriverId();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		Date startDate = new Date(cal.getTimeInMillis());

		List<Booking> bookingList = mobileServiceImpl.getDriverBookingHistory(driverId, -1, -1, startDate, new Date()); 
		if(bookingList!= null&& bookingList.size()==0){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(bookingList);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getBookingDetails", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getBookingDetails(@RequestBody RequestObj<HashMap<String, String>> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		//Integer customerId =  Integer.parseInt(reqObj.getBody().get("customerId"));//
		Integer bookingId =  Integer.parseInt(reqObj.getBody().get("bookingId"));
		//Booking booking = mobileServiceImpl.bookingDetails(bookingId);

		Booking booking = mobileServiceImpl.getBookingDetails(bookingId);
		if(booking == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(booking);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}


	@RequestMapping(value = "/changeVehicle", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> changeVehicle(@RequestBody RequestObj<DrVehicle> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		DrVehicle dv =  reqObj.getBody();
		
		mobileServiceImpl.saveDrVehicle(dv) ;
		
		
		if(dv == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(dv);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getVehicleList", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getVehicleList(@RequestBody RequestObj<Driver> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Driver d =  reqObj.getBody();
		
		List<DrVehicle> drVehicleList = mobileServiceImpl.getDrVehicleList(d) ;
		
		
		if(drVehicleList == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(drVehicleList);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/dloc", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> dloc(@RequestBody RequestObj<DrLocation> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		DrLocation dloc =  reqObj.getBody();
		
		mobileServiceImpl.updateDriverLocation(dloc);
		//mobileServiceImpl.saveDrLoc(dloc) ;
		
		
		if(dloc == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody("SUCCESS");//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	@RequestMapping(value = "/getDriverVehicle", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getDriverVehicle(@RequestBody RequestObj<Driver> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Driver driver =  reqObj.getBody();
		
		List<DrVehicle> dvList = mobileServiceImpl.getDrVehicle(driver.getDriverId());
		
		if(dvList != null ){
			resObj.setBody(dvList);//Check performance or take in string
		}else{
			resObj.setBody(env.getProperty("no.data.found"));
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/dlocOn", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> dlocOn(@RequestBody RequestObj<DrLocation> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		DrLocation dloc =  reqObj.getBody();
		mobileServiceImpl.saveDrLoc(dloc) ;
		
		resObj.setBody("Success");//Check performance or tae in string

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/dlocOff", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> dlocOff(@RequestBody RequestObj<DrLocation> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		DrLocation dloc =  reqObj.getBody();
		
		mobileServiceImpl.deleteDrLoc(dloc) ;
		
		resObj.setBody("Success");//Check performance or tae in string

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	

	/**
	 * @param reqObj
	 * @return
	 */
	@RequestMapping(value = "/rideStart", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> rideStart(@RequestBody RequestObj<CustomerRide> reqObj ){
		
		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		CustomerRide customerRide =  reqObj.getBody();
		//Integer rideId;
		
		//DriverRide driverRide =  new DriverRide();
		CustomerRide cr = new CustomerRide();
		//mobileServiceImpl.deleteDrLoc(dloc) ;
		
		try {
			cr.setRideId(mobileServiceImpl.startRide(customerRide, configMap));
			
			
		} catch (InvalidCredentialsException e) {

			resObj.setBody(e.getMessage());//Check performance or tae in string
			header.setMessage(env.getProperty("booking.invalid.pin"));
			header.setStatusCode(AppConstants.ERROR_INVALID_OTP);
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			resObj.setBody(e.getMessage());//Check performance or tae in string
			header.setMessage(env.getProperty("booking.invalid.pin"));
			header.setStatusCode(AppConstants.ERROR_INVALID_OTP);
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			
		}
		String a= "1";
		
		resObj.setBody(cr);//Check performance or tae in string
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/rideEnd", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> rideEnd(@RequestBody RequestObj<CustomerRide> reqObj ){
		
		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		CustomerRide customerRide =  reqObj.getBody();
		
		//DriverRide driverRide =  new DriverRide();
		
		//mobileServiceImpl.deleteDrLoc(dloc) ;
		CustomerRide cr =  mobileServiceImpl.endJourney(customerRide, configMap);	
		
		resObj.setBody(cr);//Check performance or tae in string

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/trackBooking", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> trackBooking(@RequestBody RequestObj<Booking> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		Booking booking =  reqObj.getBody();
		
		Booking bookingDetails =  mobileServiceImpl.getBookingDetails(booking.getBookingId());
		
		if(bookingDetails != null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(bookingDetails);//Check performance or tkae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/bookingAcceptReject", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> bookingAcceptReject(@RequestBody RequestObj<DriverBooking> reqObj ) throws JsonProcessingException{
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		DriverBooking drBooking =  reqObj.getBody();
		ObjectMapper om = new ObjectMapper();
		logger.debug(om.writeValueAsString(reqObj));
		//save DriverBooking accept or reject
		DriverBooking newDrBooking =  new DriverBooking();
		try{
			if(drBooking.getStatus().equals(AppConstants.STATUS_REJECT)){
				if(StringUtils.isEmpty(drBooking.getRejectReason())){
					header.setMessage("Cannot accept empty reject reason");
					header.setStatusCode(AppConstants.GLOBAL_FAIL);
					resObj.setBody(env.getProperty("cse.unavailable"));
					return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);		
				}
				
				//newDrBooking.setBookingId(drBooking.getBookingId());
				
				Integer res = mobileServiceImpl.rejectBooking(drBooking);
				if(res == null){
					header.setMessage(env.getProperty("cse.unavailable"));
					header.setStatusCode(AppConstants.GLOBAL_FAIL);
					resObj.setBody(env.getProperty("cse.unavailable"));
					return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);		
				}
				resObj.setBody("REJECTED SUCCESS");//Check performance or tae in string
				
				
			}else if(drBooking.getStatus().equals(AppConstants.STATUS_ACCEPT)){
				resObj.setBody(mobileServiceImpl.acceptBooking(drBooking));//Check performance or tae in string
			}
		}catch (ServiceException e) {
			resObj.setBody(e.getMessage()+" - Booking Could not be completed");
			header.setMessage(env.getProperty("booking.error"));
			header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
		}
		catch (Exception e) {
			resObj.setBody(e.getMessage()+" Error ");
			e.printStackTrace();
			header.setMessage(env.getProperty("booking.error"));
			header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
		}
		
		//mobileServiceImpl.deleteDrLoc(dloc) ;
		

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	/*
	@RequestMapping(value = "/dloc", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> dlocOff(@RequestBody RequestObj<DrLocation> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(voyagerConstants.GLOBAL_SUCCESS);
		
		DrLocation dloc =  reqObj.getBody();
		
		mobileServiceImpl.deleteDrLoc(dloc) ;
		
		resObj.setBody("Success");//Check performance or tae in string

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
*/	
	

	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseObj> changePassword(@Valid @RequestBody RequestObj <HashMap<String, String>> reqObj){
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		header.setMessage(env.getProperty("change.password"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		resObj.setHeader(header);
		String email = reqObj.getBody().get("email");//.toString();
		String password = reqObj.getBody().get("password");//.toString();
		Integer d = mobileServiceImpl.checkEmail(email);
		if(d != null ){
			String password1 = MD5Utils.to_MD5(password);
			mobileServiceImpl.setPassword(d, password1);
			resObj.setBody(env.getProperty("mobile.message.changepassword"));
			
		}else{
			resObj.setBody(env.getProperty("invalid.username"));
		}
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		
	}
	

	@RequestMapping(value = "/getEContact", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getDEcontact(@RequestBody RequestObj<Driver> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
	
		List<EmergancyContact> ecList = mobileServiceImpl.getDEmargancyContact(reqObj.getBody().getDriverId());
		header.setMessage(env.getProperty("ec.data.loaded.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		if(ecList!= null&& ecList.size()==0){
			resObj.setBody(env.getProperty("ec.no.data"));
			header.setMessage(env.getProperty("ec.no.data"));
		}else{
			resObj.setBody(ecList);//Check performance or take in string
		}
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/addEContact", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> addDEcontact(@RequestBody RequestObj<EmergancyContact> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		EmergancyContact em = reqObj.getBody();
		//TODO Validation
/*		if(!ValidateUtils.validatName(em.geteContactName())){
				
		}
		if(!ValidateUtils.validatName(em.geteContactName())){
			
		}
		if(StringUtils.isEmpty(em.getContactId())){
			
		}
*/
		mobileServiceImpl.saveDEmargancyContact(em);
		header.setMessage(env.getProperty("ec.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		List<EmergancyContact> ecList = mobileServiceImpl.getDEmargancyContact(reqObj.getBody().getDriverId());
		if(ecList!= null&& ecList.size()==0){
			resObj.setBody(env.getProperty("ec.no.data"));
			header.setMessage(env.getProperty("ec.no.data"));
		}else{
			resObj.setBody(ecList);//Check performance or take in string
		}
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delEContact", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> delDEcontact(@RequestBody RequestObj<EmergancyContact> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		mobileServiceImpl.delDEmergancyContact(reqObj.getBody().getDriverId(), reqObj.getBody().getContactId());
		header.setMessage(env.getProperty("ec.deleted.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		List<EmergancyContact> ecList = mobileServiceImpl.getDEmargancyContact(reqObj.getBody().getDriverId());
		if(ecList!= null&& ecList.size()==0){
			resObj.setBody(env.getProperty("ec.no.data"));
			header.setMessage(env.getProperty("ec.no.data"));
		}else{
			resObj.setBody(ecList);//Check performance or tae in string
		}
		
		resObj.setHeader(header);
	
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/driverRating", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> driverRating(@RequestBody RequestObj<DriverRide> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		DriverRide dtd = reqObj.getBody();
		
		mobileServiceImpl.saveRatingByDriver(dtd);
		header.setMessage(env.getProperty("ec.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(env.getProperty("ec.data.updated.success"));
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	/*
	@RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> invoice() {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Integer bookingId = 2;
		mobileServiceImpl.makeInvoice(bookingId, null);
		header.setMessage(env.getProperty("ec.data.updated.success"));
		header.setStatusCode(voyagerConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(env.getProperty("ec.data.updated.success"));
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	*/
	@RequestMapping(value = "/getHelp", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getHelp(@RequestBody RequestObj<Customer> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("support.data.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		String supportFor = "DRIVER";
		List<SupportMaster> sList = mobileServiceImpl.getSupport(supportFor );
		if(sList!= null&& sList.size()==0){
			resObj.setBody(env.getProperty("support.no.data"));
		}else{
			resObj.setBody(sList);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getHelpTopic", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getHelpTopic(@RequestBody RequestObj<Customer> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("support.data.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		String supportFor = "CUSTOMER";
		List<SupportMaster> sList = mobileServiceImpl.getSupportQuestions(supportFor );
		if(sList!= null&& sList.size()==0){
			resObj.setBody(env.getProperty("support.no.data"));
		}else{
			resObj.setBody(sList);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getHelpDesc", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getHelpDesc(@RequestBody RequestObj<HashMap<String,String>> reqObj ){
		
		//TODO photo upload
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("support.data.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Integer supportId = Integer.parseInt(reqObj.getBody().get("supportId"));
		SupportMaster sMaster = mobileServiceImpl.getSupportMaster(supportId );
		if(sMaster == null){
			resObj.setBody(env.getProperty("support.no.data"));
		}else{
			resObj.setBody(sMaster );//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/startupCheck", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> startupCheck(@RequestBody RequestObj<Map> reqObj) {

		//TODO Version check
		ResHeader header = new ResHeader();
		
		ResponseObj resObj = new ResponseObj();

		
		if(reqObj.getBody().containsKey("driverId")){
			Integer driverId = Integer.parseInt(reqObj.getBody().get("driverId").toString());
			
			Map<String, Object> resultMap = new HashMap<>();
			Booking db =  mobileServiceImpl.startupCheckDriver(driverId);
			DrLocation dr = mobileServiceImpl.getDrLocation(driverId);
			if(db != null){
				resultMap.put("drLocation", dr);
				resultMap.put("driverBooking", db);
			}
			/**
			List<Booking> trackBooking =  mobileServiceImpl.getTrackBooking(customerId);
			if(trackBooking != null){
				resultMap.put("trackBooking", trackBooking);
			}
			**/
			resObj.setBody(resultMap);
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			header.setMessage("Config data loaded");
			resObj.setHeader(header);
			
		}else{
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			header.setMessage("No customerId proivided");
			resObj.setHeader(header);
			
		}
		
		//reg.setMessage(voyagerConstants.GLOBAL_SUCCESS);
		
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	@RequestMapping(value = "/aboutus", method = RequestMethod.POST)
	
    public ResponseEntity<ResponseObj> aboutus(@RequestBody RequestObj<Driver> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Driver d = reqObj.getBody();
		
		String version=env.getProperty("voyager.driver.version");
		String message=env.getProperty("voyager.driver.aboutus.message");
				
		header.setMessage(message +"\n\n "+version);
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(message +" \n\n "+version);
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
/*    @RequestMapping(value = "/getPic", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.POST)
    public @ResponseBody BufferedImage getPic(@RequestPart RequestObj<Customer> reqObj) {
        try {
    		Customer customer = mobileServiceImpl.getCustmerProfile(reqObj.getBody().getCustomerId());
    		InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+customer.getProfilePicURL());
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @RequestMapping(value = "/getDrPic", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.POST)
    public @ResponseBody BufferedImage getDrPic(@RequestPart RequestObj<Driver> reqObj) {
        try {
    		Driver driver = mobileServiceImpl.getDriver(reqObj.getBody().getDriverId());
    		InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+driver.getPhoto());
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

*/
    @RequestMapping(value = "/getPic/{customerId}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)
    public @ResponseBody byte[]   getPic(@PathVariable Integer customerId) {
        try {
    		Customer customer = mobileServiceImpl.getCustmerProfile(customerId);
    		
    		//InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+customer.getProfilePicURL());
    		InputStream in = new FileInputStream(new File(env.getProperty("profile.pic.dir.customer")+customer.getProfilePicURL()));
    		//return ImageIO.read(new FileImageInputStream(new File(env.getProperty("profile.pic.dir")+customer.getProfilePicURL())));
    		return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @RequestMapping(value = "/getDrPic/{driverId}/{type}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)
    public @ResponseBody byte[] getDrPic(@PathVariable Integer driverId,@PathVariable String type) {
        try {
    		Driver driver = mobileServiceImpl.getDriver(driverId);
//    		InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+driver.getPhoto());
    		InputStream in ;
    		if(type.equals("ID")){
    			in = new FileInputStream(new File(env.getProperty("documents.dir")+driver.getDriverDetails().getDriverAuthPath()));
    		}
    		else if(type.equals("DL")){
    			in = new FileInputStream(new File(env.getProperty("documents.dir")+driver.getDriverDetails().getLicensePath()));
    		}
    		if(type.equals("DI")){
    			in = new FileInputStream(new File(env.getProperty("documents.dir")+driver.getDriverDetails().getDriverInsurancePath()));
    		}	
    		else{
    			//PP
    			in = new FileInputStream(new File(env.getProperty("profile.pic.dir")+driver.getPhoto()));
    		}
    		//return ImageIO.read(new FileImageInputStream(new File(env.getProperty("profile.pic.dir")+customer.getProfilePicURL())));
    		return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> saveProfile(@RequestBody RequestObj<Driver> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("profile.update.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		mobileServiceImpl.updateProfileDriver(reqObj.getBody());
		
		resObj.setHeader(header);
		resObj.setBody( mobileServiceImpl.getDriver(reqObj.getBody().getDriverId()));
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> uploadPic(@RequestPart RequestObj<Driver> reqObj, @RequestPart("file") MultipartFile file){
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		
		if(processFile(file)){
			reqObj.getBody().setPhoto(reqObj.getBody().getDriverId()+".jpg");
		}

		mobileServiceImpl.updateProfileDriver(reqObj.getBody());
		
		/*//changed
		String myCurrentDir = System.getProperty("user.dir") + File.separator + System.getProperty("sun.java.command") .substring(0, System.getProperty("sun.java.command").lastIndexOf(".")) .replace(".", File.separator);
		System.out.println(myCurrentDir);
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String rootPath = System.getProperty("user.dir");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());

			}catch (Exception e) {
				header.setMessage(e.getMessage());
				resObj.setHeader(header);
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
				//return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		else
		{
			header.setMessage("File is empty");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}//changed mihir
*/		header.setMessage(env.getProperty("profile.update.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		String profilePicUrl = env.getProperty("profile.pic.url")+ file.getOriginalFilename()	;
		resObj.setBody(profilePicUrl);
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/uploadPicFile/{driverId}", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> uploadPic(@PathVariable String driverId, @RequestPart("file") MultipartFile file){
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		String myCurrentDir = System.getProperty("user.dir") + File.separator + System.getProperty("sun.java.command") .substring(0, System.getProperty("sun.java.command").lastIndexOf(".")) .replace(".", File.separator);
		System.out.println(myCurrentDir);
		Driver d = new Driver();
		try{
			d.setDriverId(Integer.parseInt(driverId));
			Long fileSize = (file.getSize()/1024)/1024;
			
			if( fileSize > 2 ){//3mb
				header.setMessage(env.getProperty("profilePic.update.fail.size"));
				header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
				//String profilePicUrl = env.getProperty("profile.pic.url")+ file.getOriginalFilename()	;
				resObj.setBody(header.getMessage());
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}
	
			if(processFile(file)){
				d.setPhoto(file.getOriginalFilename());
			}
	
			mobileServiceImpl.updateProfileDriver(d);
			
			header.setMessage(env.getProperty("profile.update.success"));
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			String profilePicUrl = env.getProperty("profile.pic.url")+ file.getOriginalFilename()	;
			resObj.setBody(profilePicUrl);
			
			resObj.setHeader(header);
		}catch (Exception ioe){
			header.setMessage(env.getProperty("profilePic.update.fail"));
			header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
			//String profilePicUrl = env.getProperty("profile.pic.url")+ file.getOriginalFilename()	;
			resObj.setBody(header.getMessage());
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		
		}
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	public Boolean processFile(MultipartFile file) {
	
		
//		final String BASE_DIR_DOWNLOAD  = "C:\\Users\\givizer 1\\workspaceNew\\downloads\\"; 
		final String BASE_DIR_DOWNLOAD  =   env.getProperty("profile.pic.dir"); 

		String fileName="",msg =""; 
		try {
			System.out.println(" DDDDDDDDDDDDDDDD" +new File(BASE_DIR_DOWNLOAD).getCanonicalPath());
	         fileName = file.getOriginalFilename();
	         byte[] bytes = file.getBytes();
	         BufferedOutputStream buffStream = 
	                 new BufferedOutputStream(new FileOutputStream(new File(BASE_DIR_DOWNLOAD + fileName)));
	         buffStream.write(bytes);
	         buffStream.close();
	         msg = "You have successfully uploaded " + fileName +"<br/>";
	         logger.debug(msg+"******************88");
	     } catch (Exception e) {
	         //return "You failed to upload " + fileName + ": " + e.getMessage() +"<br/>";
	    	 logger.debug(msg+"******************EE" +e);
	    	 return false;
	     }
		return true;
	}
	

}