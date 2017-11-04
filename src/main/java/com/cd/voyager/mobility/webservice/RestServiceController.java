package com.cd.voyager.mobility.webservice;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cd.voyager.common.util.GoogleServicesWrapper;
import com.cd.voyager.common.util.SMSUtil;
import com.cd.voyager.common.util.StringGenUtils;
import com.cd.voyager.common.util.ValidateUtils;
import com.cd.voyager.email.MailSender;
import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.CustomerAccount;
import com.cd.voyager.entities.DrLocation;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverRide;
import com.cd.voyager.entities.EmergancyContact;
import com.cd.voyager.entities.EnquiryForm;
import com.cd.voyager.entities.GoogleDistance;
import com.cd.voyager.entities.Payment;
import com.cd.voyager.entities.Preferences;
import com.cd.voyager.entities.RegDevice;
import com.cd.voyager.entities.Registration;
import com.cd.voyager.entities.SupportMaster;
import com.cd.voyager.exception.CorporateSignupWIthoutPayment;
import com.cd.voyager.exception.InvalidCredentialsException;
import com.cd.voyager.exception.ServiceException;
import com.cd.voyager.mobility.data.RequestObj;
import com.cd.voyager.mobility.data.ResponseObj;
import com.cd.voyager.mobility.data.child.ResHeader;
import com.cd.voyager.mobility.service.IMobileService;
import com.cd.voyager.web.service.IMainService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;

@RestController
@RequestMapping("/api")
public class RestServiceController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IMobileService mobileServiceImpl;

	@Autowired
	private IMainService mainServiceImpl;

	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;

	@Autowired
	private MailSender mailSenderPooled;

	@RequestMapping(value = "/register", method = RequestMethod.POST, headers = "content-type=application/json;charset=utf-8")
    public ResponseEntity<ResponseObj> register(@Valid @RequestBody RequestObj<Registration> reqObj, UriComponentsBuilder ucBuilder) {

		
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
				//STARTED registration but could not finish due to some issues may continue further with OTP
				//Send OTP
				
				reg.setOTPDate(new Date());
				reg.setOTP(SMSUtil.gerateOTP());
				logger.debug(" $$$$$$$$$$$$$  existing reg continue "+reg.getOTP());
				
				//mobileServiceImpl.updateOTP(reg);
				Registration reg2 = mobileServiceImpl.updateRegistration(reg);
				SMSUtil smsUtil = new SMSUtil();
				smsUtil.sendSMS("OTP_TPL", reg.getMobile(), reg.getOTP(), env ) ;

				resObj.setBody(reg2);
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
    public ResponseEntity<ResponseObj> 	resentOTP(@Valid @RequestBody RequestObj<Registration> reqObj) {
		ResponseObj resObj = new ResponseObj();
		ResHeader header = new ResHeader();
		Registration reg = reqObj.getBody();
		String opCode = reqObj.getMetaData().getOperationCode();
		
		reg.setOTPDate(new Date());	
		reg.setOTP(SMSUtil.gerateOTP());
		mobileServiceImpl.updateOTP(reg);
		Registration regObj = mobileServiceImpl.getIfRegistrationExist(reg);
		//Resent otp
		SMSUtil smsUtil =  new SMSUtil();
		if(!smsUtil.sendSMS(env.getProperty("OTP_TPL"),  regObj.getCountryCode()+regObj.getMobile(), reg.getOTP(), env)){
			header.setStatusCode(AppConstants.GLOBAL_PAGE_ERROR);
			resObj.setBody("SMS could not be send try later");
			//TODO
		}else{
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			resObj.setBody("OTP has been sent on your registered number,please verify");
			//TODO
		}
		
		resObj.setHeader(header);
		
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	@RequestMapping(value = "/forgotPassVerifyPin", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	forgotPassVerifyPin(@Valid @RequestBody RequestObj<Customer> reqObj) {
		
		/*
		//ObjectMapper om = new ObjectMapper();
		try {
			logger.debug(om.writeValueAsString(reqObj));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		ResponseObj resObj = new ResponseObj();
		Customer  c= reqObj.getBody();
		
		ResHeader header = new ResHeader();
		Customer customer = mobileServiceImpl.verifyOTPForgotPass(c);
		if(customer != null){
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			header.setMessage("OTP has been succesfully verified");
			resObj.setHeader(header);
		}else{
			header.setStatusCode(AppConstants.ERROR_INVALID_OTP);
			header.setMessage("Invalid OTP");
			resObj.setHeader(header);
		}
		resObj.setBody(customer);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/verifyOTP", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	verifyOTP(@Valid @RequestBody RequestObj<Registration> reqObj) {
		
		/*
		//ObjectMapper om = new ObjectMapper();
		try {
			logger.debug(om.writeValueAsString(reqObj));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		ResponseObj resObj = new ResponseObj();
		Registration reg = reqObj.getBody();
		
		ResHeader header = new ResHeader();
		Registration regRet = mobileServiceImpl.verifyOTP(reg);
		if(regRet != null){
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			header.setMessage("OTP has been succesfully verified");
			resObj.setHeader(header);
		}else{
			header.setStatusCode(AppConstants.ERROR_INVALID_OTP);
			header.setMessage("Invalid OTP");
			resObj.setHeader(header);
		}
		resObj.setBody(regRet);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/register/customer", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> registerCustomer(@Valid @RequestBody RequestObj<Customer> reqObj) {

		//mobileServiceImpl.savePayment(payment);
		//reg.setMessage(voyagerConstants.GLOBAL_SUCCESS);
		ObjectMapper o = new ObjectMapper();
		try {
			logger.debug(o.writeValueAsString(reqObj));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//validation
		ResponseObj resObj = new ResponseObj();
		Customer customer = reqObj.getBody();
		//validation

		
		ResHeader header = new ResHeader();
		List<String> msg = new ArrayList<String>();
		boolean err = false;
		String invalidMsg = env.getProperty("validation.empty.input");
		Payment payment= customer.getPaymentList().iterator().next();

		//Validate
		if(StringUtils.isEmpty(customer.getEmail()) && !ValidateUtils.validatEmail(customer.getEmail())){
			msg.add(StringGenUtils.replaceProperty(invalidMsg, "Email"));
			err = false;
		}
		if(StringUtils.isEmpty(customer.getPassword())){
			msg.add(StringGenUtils.replaceProperty(invalidMsg, "Password"));
			err = false;
		}
		if(StringUtils.isEmpty(customer.getMobile()) && !ValidateUtils.validatCellNumber(customer.getMobile())){
			msg.add(StringGenUtils.replaceProperty(invalidMsg, "Mobile"));
			err = false;
		}
		if(StringUtils.isEmpty(customer.getFname()) && !ValidateUtils.validatName(customer.getFname())){
			msg.add(StringGenUtils.replaceProperty(invalidMsg, "First Name"));
			err = false;
		}
		if(StringUtils.isEmpty(customer.getLname()) && !ValidateUtils.validatName(customer.getLname())){
			msg.add(StringGenUtils.replaceProperty(invalidMsg, "Last Name"));
			err = false;
		}
	
			//Validate Payment
		
			//To validate payment method 
			/**
			 * TODO 
			 * (1) Validate Payment gatewayID with paymentgateway table
			 * (2) pre auth or validate payment gateway
			 */
			
			
			//Check customerId
			/*if(StringUtils.isEmpty(payment.getCustomerId())){
				
				header.setStatusCode(voyagerConstants.ERROR_INVALID_SERVICE_CALL);
				resObj.setBody("Invalid GATEWAY");
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}*/
			
			if(!AppConstants.ACCOUNT_TYPE_CORPORATE.equals(payment.getAccountType()) && !AppConstants.ACCOUNT_TYPE_PERSONAL.equals(payment.getAccountType())){
				header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
				header.setMessage("Invalid Account Type");
				resObj.setBody("Invalid Account Type");
				resObj.setHeader(header);
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}
			
			if(AppConstants.ACCOUNT_TYPE_PERSONAL.equals(payment.getAccountType()) && StringUtils.isEmpty(payment.getGatewayId()) ){
				
				header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
				header.setMessage("Invalid GATEWAY");
				resObj.setBody("Invalid GATEWAY");
				resObj.setHeader(header);

				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}
		//validation.empty.input.general
			Customer customerObj;
			if(!mobileServiceImpl.isCustomerExist(customer.getEmail())){	
				try {					
					customerObj = mobileServiceImpl.signUpCustomer(customer, payment);
					resObj.setBody(customerObj);
					header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
					header.setMessage("Customer Registration Success");
					logger.debug("CustomerObj-- "+customer.getCustomerId());
				}catch (CorporateSignupWIthoutPayment e) {
					resObj.setBody(e.getMessage());
					header.setStatusCode(AppConstants.GLOBAL_REDIRACTION);
					header.setMessage(e.getMessage());
					logger.debug("CorporateSignupWIthoutPayment-- "+e.getMessage());
					e.printStackTrace();
				} 
				catch (ServiceException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					logger.debug("ServiceException-- "+e.getMessage());
					
					resObj.setBody(e.getMessage());
					header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
					header.setMessage(e.getMessage());
					e.printStackTrace();
				}catch (Exception e) {
					logger.debug("ERROR -- "+e.getMessage());
					e.printStackTrace();
				}
				
			}else{
				resObj.setBody("Email already Registered, Please sign in");
				header.setStatusCode(AppConstants.ERROR_DUPLICATE_ENRTY);
				header.setMessage("Email already Registered, Please sign in");
				
			}
		resObj.setHeader(header);
		
        return new ResponseEntity<ResponseObj>(	resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> 	signIn(@Valid @RequestBody RequestObj<Customer> reqObj) {

		ResHeader header = new ResHeader();
		Customer customer = reqObj.getBody();
		
		String msg = "";
		boolean err = false;
		String invalidMsg = env.getProperty("validation.empty.input");
		
		ResponseObj resObj = new ResponseObj();
		Customer cusReturn  =  null;

		StringGenUtils.isEmpty(customer.getEmail(), "Email", invalidMsg);
		StringGenUtils.isEmpty(customer.getPassword(), "Password", invalidMsg);
		try{
			cusReturn = mobileServiceImpl.signIn(customer);
		}catch(InvalidCredentialsException e){
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(e.getMessage());
			
			logger.debug(e.getMessage()+"  $$$$$$$$$$$$ ");
			resObj.setHeader(header);
			//resObj.setBody(
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		resObj.setBody(cusReturn);
		resObj.setHeader(header);

		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}	
	
	/**
	 * Add Payment
	 * @param payment
	 * @return
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> payment(@RequestBody RequestObj<Payment> reqObj) {
		Payment payment = reqObj.getBody();
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		//To validate payment method 
		/**
		 * TODO 
		 * (1) Validate Payment gatewayID with paymentgateway table
		 * (2) pre auth or validate payment gateway
		 */
		if(StringUtils.isEmpty(payment.getGatewayId()) ){
			
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			resObj.setBody("Invalid GATEWAY");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		//Check customerId
		if(StringUtils.isEmpty(payment.getCustomerId())){
			
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			header.setMessage("Invalid GATEWAY");
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		if(!AppConstants.ACCOUNT_TYPE_CORPORATE.equals(payment.getAccountType()) && !AppConstants.ACCOUNT_TYPE_PERSONAL.equals(payment.getAccountType())){
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			header.setMessage("Invalid Account Type");
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		mobileServiceImpl.savePayment(payment);
		
		List<Payment> pList = mobileServiceImpl.getPaymentData(reqObj.getBody().getCustomerId(), AppConstants.STATUS_ACTIVE );
		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("payment.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or tae in string
		}
		
		resObj.setHeader(header);
		//reg.setMessage(voyagerConstants.GLOBAL_SUCCESS);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delPayment", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> delPayment(@Valid @RequestBody RequestObj<Payment> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Integer i = mobileServiceImpl.deletePayment(reqObj.getBody().getCustomerId(), reqObj.getBody().getPaymentId());
		if(i == AppConstants.INVALID_ENTRY){
			header.setMessage(env.getProperty("payment.data.delete.notallowd"));
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			resObj.setBody(header.getMessage());
	        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		header.setMessage(env.getProperty("payment.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		List<Payment> pList = mobileServiceImpl.getPaymentData(reqObj.getBody().getCustomerId(), AppConstants.STATUS_ACTIVE );
		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("payment.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or tae in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/config", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> config(@RequestBody RequestObj<Map> reqObj) {

		//TODO Version check
		ResHeader header = new ResHeader();
		
		ResponseObj resObj = new ResponseObj();

		//if(version==null){ //&& check version from database){
			Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
			resObj.setBody(mainServiceImpl.getGlobalConfig(zoneId));
		/*}else{
			header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
			header.setMessage("Version info missing for the retrive");
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}*/
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		header.setMessage("Config data loaded");
		resObj.setHeader(header);
		
		//reg.setMessage(voyagerConstants.GLOBAL_SUCCESS);
		
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/startupCheck", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> startupCheck(@RequestBody RequestObj<Map> reqObj) {

		//TODO Version check
		ResHeader header = new ResHeader();
		
		ResponseObj resObj = new ResponseObj();

		
		if(reqObj.getBody().containsKey("customerId")){
			Integer customerId = Integer.parseInt(reqObj.getBody().get("customerId").toString());
			
			Map<String, List> resultMap = new HashMap<>();
			List<Booking> pendingRating =  mobileServiceImpl.startupCheck(customerId);
			
			if(pendingRating != null){
				resultMap.put("pendingRating", pendingRating);
			}
			List<Booking> trackBooking =  mobileServiceImpl.getTrackBookingList(customerId);
			
			if(trackBooking != null){
				resultMap.put("trackBooking", trackBooking);
			}
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

	
	
	@RequestMapping(value = "/forgotPwOTP", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> forgotPassword(@Valid @RequestBody RequestObj<Map<String,String>> reqObj) {
		ResponseObj resObj = new ResponseObj();
		ResHeader header = new ResHeader();
		Map<String,String> map = reqObj.getBody();
		String opCode = reqObj.getMetaData().getOperationCode();
		
		Customer c = new Customer();
		//c.setPinDate(new Date()); current date initialize at object creation so this not required
		String pin = SMSUtil.gerateOTP();
		c.setPin(pin);
		c.setEmail(map.get("email"));
		mobileServiceImpl.updateOTPForgotPass(c);
		
		c = mobileServiceImpl.getCustmerProfileByEmail(map.get("email"));

		SMSUtil smsUtil =  new SMSUtil();
		boolean send = false;
		if(null != map.get("sendEmail") && "1".equals(map.get("sendEmail").trim())){
			HashMap<String,String>  propertyMap = new HashMap<>();
			propertyMap.put("[PIN]", pin);
			try {
				mailSenderPooled.sendEMail(env.getProperty("uc.forgotPassword.email.subject"),
											env.getProperty("uc.email.admin"),
											c.getEmail(), "", env.getProperty("uc.email.replyTo"), MailSenderPooled.FORGOT_PASS_TPL, propertyMap);
				send = true;
			} catch (IOException | MessagingException e) {
				logger.debug("Error in email"+e.getMessage());
				e.printStackTrace();
			}
		}
		
		if(null != map.get("sendMobile") && "1".equals(map.get("sendMobile").trim())){
			send = true;
			smsUtil.sendSMS(env.getProperty("OTP_TPL"),  c.getCountryCode()+c.getMobile(), pin, env);
		}
		//Resent otp
		if(!send){
			header.setStatusCode(AppConstants.GLOBAL_PAGE_ERROR);
			resObj.setBody("Pin could not be send try later or check the details you enter");
			//TODO
		}else{
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			resObj.setBody("OTP has been sent on your registered number,please verify");
			//TODO
		}
		
		resObj.setHeader(header);
		
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/cpOTP", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> changePasswordOTP(@RequestBody RequestObj<HashMap<String,String>> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
	
		HashMap<String, String> cp =  reqObj.getBody();
		//if( cp.get("password").equals(cp.get("Password")))
		if(mobileServiceImpl.changeCustomerPasswordOTP(cp.get("email"), cp.get("password"), cp.get("OTP"))==1){
			header.setMessage(env.getProperty("mobile.message.changepassword"));
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			resObj.setBody(env.getProperty("mobile.message.changepassword"));//Check performance or tae in string
		}else{
			header.setMessage(AppConstants.ERROR_INVALID_OTP);
			header.setStatusCode(env.getProperty("mobile.message.changepassword.error.OTP"));
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cp", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> changePassword(@RequestBody RequestObj<HashMap<String,String>> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		HashMap<String, String> cp =  reqObj.getBody();
		
		if(mobileServiceImpl.changeCustomerPassword(Integer.parseInt(cp.get("customerId")), cp.get("oldPassword"), cp.get("newPassword"))==1){
			header.setMessage(env.getProperty("mobile.message.changepassword"));
			header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
			resObj.setBody(env.getProperty("mobile.message.changepassword"));//Check performance or tae in string
		}else{
			header.setMessage(env.getProperty("mobile.message.changepassword.error.password"));
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAccounts", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getAccounts(@RequestBody RequestObj<Customer> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		
		List<Map> caList =  mobileServiceImpl.getAccounts(reqObj.getBody().getCustomerId());
		header.setMessage(env.getProperty("cutAccounts.data.loaded.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		if(caList != null&& caList.size()==0){
			resObj.setBody(env.getProperty("accounts.no.data"));
			header.setMessage(env.getProperty("accounts.no.data"));
		}else{
			resObj.setBody(caList);//Check performance or take in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/addUpdateAccount", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> addUpdateAccounts(@RequestBody RequestObj<Map> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		String pin = "";
		if(null != reqObj.getBody().get("pin") ){
			pin =reqObj.getBody().get("pin").toString() ;
		}
		Integer rembursed = Integer.parseInt(reqObj.getBody().get("rembursed").toString());
		
		mobileServiceImpl.addAccount(Integer.valueOf(reqObj.getBody().get("customerId").toString()), Integer.valueOf(reqObj.getBody().get("paymentId").toString()),reqObj.getBody().get("email").toString(), reqObj.getBody().get("accountType").toString(), pin  );
		
		List<Map> caList =  mobileServiceImpl.getAccounts(Integer.valueOf(reqObj.getBody().get("customerId").toString()));
		
		header.setMessage(env.getProperty("cutAccounts.data.loaded.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		if(caList != null&& caList.size()==0){
			resObj.setBody(env.getProperty("accounts.no.data"));
			header.setMessage(env.getProperty("accounts.no.data"));
		}else{
			resObj.setBody(caList);//Check performance or take in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPref", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getPref(@RequestBody RequestObj<Customer> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		List<Preferences> pList = mobileServiceImpl.getPrefData(reqObj.getBody().getCustomerId());
		header.setMessage(env.getProperty("pref.data.loaded.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);

		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("pref.no.data"));
			header.setMessage(env.getProperty("pref.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or take in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/addPref", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> addPref(@Valid @RequestBody RequestObj<Preferences> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		//TODO
		
		mobileServiceImpl.savePref(reqObj.getBody());
		header.setMessage(env.getProperty("pref.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		List<Preferences> pList = mobileServiceImpl.getPrefData(reqObj.getBody().getCustomerId());
		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("pref.no.data"));
			header.setMessage(env.getProperty("pref.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or take in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	@RequestMapping(value = "/delPref", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> delPref(@Valid @RequestBody RequestObj<Preferences> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		mobileServiceImpl.deletePref(reqObj.getBody().getCustomerId(), reqObj.getBody().getFavLabel());
		header.setMessage(env.getProperty("pref.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		List<Preferences> pList = mobileServiceImpl.getPrefData(reqObj.getBody().getCustomerId());
		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("pref.no.data"));
			header.setMessage(env.getProperty("pref.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or tae in string
		}
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	@RequestMapping(value = "/getEContact", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getEcontact(@RequestBody RequestObj<Customer> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
	
		List<EmergancyContact> ecList = mobileServiceImpl.getEmargancyContact(reqObj.getBody().getCustomerId());
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
    public ResponseEntity<ResponseObj> addEcontact(@RequestBody RequestObj<EmergancyContact> reqObj) {
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
		if(em.getTrackStatus()== null)
			em.setTrackStatus(AppConstants.VALID_ENTRY);
		em.setStatus(AppConstants.STATUS_ACTIVE);
		mobileServiceImpl.saveEmargancyContact(em);
		header.setMessage(env.getProperty("ec.data.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		List<EmergancyContact> ecList = mobileServiceImpl.getEmargancyContact(reqObj.getBody().getCustomerId());
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
    public ResponseEntity<ResponseObj> delEcontact(@RequestBody RequestObj<EmergancyContact> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		mobileServiceImpl.delEmergancyContact(reqObj.getBody().getCustomerId(), reqObj.getBody().getContactId());
		header.setMessage(env.getProperty("ec.deleted.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		List<EmergancyContact> ecList = mobileServiceImpl.getEmargancyContact(reqObj.getBody().getCustomerId());
		if(ecList!= null&& ecList.size()==0){
			resObj.setBody(env.getProperty("ec.no.data"));
			header.setMessage(env.getProperty("ec.no.data"));
		}else{
			resObj.setBody(ecList);//Check performance or tae in string
		}
		
		resObj.setHeader(header);
	
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/locateCab", method = RequestMethod.POST)
    public ResponseEntity<EmergancyContact> locateCab(@RequestBody EmergancyContact ec) {

		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		resObj.setHeader(header);
		mobileServiceImpl.saveEmargancyContact(ec);
        return new ResponseEntity<EmergancyContact>(ec, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveProfile", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> saveProfile(@RequestBody RequestObj<Customer> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("profile.update.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		mobileServiceImpl.updateProfile(reqObj.getBody());
		
		resObj.setHeader(header);
		resObj.setBody( mobileServiceImpl.getCustmerProfile(reqObj.getBody().getCustomerId()));
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getPayments", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getPayments(@RequestBody RequestObj<Customer> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("profile.update.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		List<Payment> pList = mobileServiceImpl.getPaymentData(reqObj.getBody().getCustomerId(), AppConstants.STATUS_ACTIVE );
		if(pList!= null&& pList.size()==0){
			resObj.setBody(env.getProperty("payment.no.data"));
		}else{
			resObj.setBody(pList);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getHelp", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getHelp(@RequestBody RequestObj<Customer> reqObj ){
		
		//TODO photo upload
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("support.data.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		String supportFor = "CUSTOMER";
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

	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> uploadPic(@RequestPart RequestObj<Customer> reqObj, @RequestPart("file") MultipartFile file){
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		
		if(processFile(file)){
			reqObj.getBody().setProfilePicURL(reqObj.getBody().getCustomerId()+".jpg");
		}

		mobileServiceImpl.updateProfile(reqObj.getBody());
		
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

	@RequestMapping(value = "/uploadPicFile/{customerId}", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> uploadPic(@PathVariable String customerId, @RequestPart("file") MultipartFile file){
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		String myCurrentDir = System.getProperty("user.dir") + File.separator + System.getProperty("sun.java.command") .substring(0, System.getProperty("sun.java.command").lastIndexOf(".")) .replace(".", File.separator);
		System.out.println(myCurrentDir);
		Customer c = new Customer();
		try{
			c.setCustomerId(Integer.parseInt(customerId));
			Long fileSize = (file.getSize()/1024)/1024;
			
			if( fileSize > 2 ){//3mb
				header.setMessage(env.getProperty("profilePic.update.fail.size"));
				header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
				//String profilePicUrl = env.getProperty("profile.pic.url")+ file.getOriginalFilename()	;
				resObj.setBody(header.getMessage());
				return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
			}
	
			if(processFile(file)){
				c.setProfilePicURL(file.getOriginalFilename());
			}
	
			mobileServiceImpl.updateProfile(c);
			
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
/*
	@RequestMapping(value = "/getPic", method = RequestMethod.POST)
    public ResponseEntity<byte[]>  getPic(@RequestPart RequestObj<Customer> reqObj) {
		
		final HttpHeaders headers = new HttpHeaders();
		
		try {
		//Analytics
			
		Customer customer = mobileServiceImpl.getCustmerProfile(reqObj.getBody().getCustomerId());
		InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+customer.getProfilePicURL());
//		InputStream in = context.getResourceAsStream("C:/Users//givizer 1//Desktop/"+"aaa.jpg");
		//System.out.println("C:\\Users\\givizer 1\\Desktop\\"+"voyager_Template_for_mail Dipen 2nd Cut.jpg");
		
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.add("content-Diposition", "inline:fileName=123.png");
		return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<byte[]>(null, headers, HttpStatus.NOT_FOUND);
		}
		
	}
*/
	/*    @RequestMapping(value = "/getPic/{customerId}/{type}", 
            method = RequestMethod.GET)
	public HttpEntity getFile(HttpServletResponse response,
		                      @PathVariable Integer customerId, 
		                      @PathVariable String type) {
    	
		Customer customer = mobileServiceImpl.getCustmerProfile(customerId);
		String base64 = "foo"; // get base-64 encoded string from db
		byte[] bytes = Base64.decode(base64);
		try {
			InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir.customer")+customer.getProfilePicURL());
			
			StreamUtils.copy(in, response.getOutputStream());
		    response.setContentType(MediaType.IMAGE_PNG_VALUE);
		} catch (IOException e) {
		    // handle
		}
		return new ResponseEntity(HttpStatus.OK);
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
    @RequestMapping(value = "/getDrPic/{driverId}", headers = "Accept=image/jpeg, image/jpg, image/png, image/gif", method = RequestMethod.GET)
    public @ResponseBody byte[] getDrPic(@PathVariable Integer driverId) {
        try {
    		Driver driver = mobileServiceImpl.getDriver(driverId);
//    		InputStream in = context.getResourceAsStream(env.getProperty("profile.pic.dir")+driver.getPhoto());
    		InputStream in = new FileInputStream(new File(env.getProperty("profile.pic.dir")+driver.getPhoto()));
    		//return ImageIO.read(new FileImageInputStream(new File(env.getProperty("profile.pic.dir")+customer.getProfilePicURL())));
    		return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


	@RequestMapping(value = "/getProfile", method = RequestMethod.POST, produces = "image/*")
    public ResponseEntity<ResponseObj> getProfile(@RequestPart RequestObj<Customer> reqObj){
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		try {
		//Analytics
			
		Customer customer = mobileServiceImpl.getCustmerProfile(reqObj.getBody().getCustomerId());
		
		resObj.setHeader(header);
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		resObj.setBody(customer);
		//resObj.setBody(
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);

		} catch (Exception e) {
			//e.printStackTrace();
			resObj.setBody(e.getMessage());
			header.setMessage(e.getMessage());
			header.setStatusCode(AppConstants.ERROR_INVALID_SERVICE_CALL);
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
	}

	@RequestMapping(value = "/applyPromo", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> applyPromo(@RequestBody RequestObj<Map<String, String>> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		//TODO check from referal code
		String promoCode = 	reqObj.getBody().get("promoCode");
		Integer customerId = 	Integer.parseInt(reqObj.getBody().get("customerId"));
		

		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
	
		header.setMessage(env.getProperty("profile.update.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		if(promoCode.startsWith(AppConstants.REFERAL_CODE_FORMAT)){
			Double discount = 5d;//deafult discount
			Integer expDays = 30;//default days 30 days
			
			if(configMap.containsKey("REF") && configMap.get("REF").containsKey("REF_DISCOUNT")){
				discount = Double.valueOf(configMap.get("REF").get("REF_DISCOUNT").getAttrValue1().toString());
			} 
			if(configMap.containsKey("REF") && configMap.get("REF").containsKey("REF_DISCOUNT")){
				expDays = Integer.valueOf(configMap.get("REF").get("REF_EXP_DAYS").getAttrValue1().toString());
			}
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, expDays);
			
			Date expDate = new Date(cal.getTimeInMillis());
					
			Integer i = mobileServiceImpl.validateRefCode(promoCode, customerId, expDate, discount);
			if(i == AppConstants.VALID_ENTRY)
				resObj.setBody(env.getProperty("refcode.invalid"));
			else
				resObj.setBody(env.getProperty("refcode.applied.success"));
		}else{
			
			Integer j = mobileServiceImpl.validatePromoCode(promoCode, customerId);
			if(j == AppConstants.VALID_ENTRY){
				resObj.setBody(env.getProperty("promo.applied.success"));
			}else{
				resObj.setBody(env.getProperty("promo.invalid"));
				header.setMessage(env.getProperty("promo.invalid"));
				header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			}
		}
		
		resObj.setHeader(header);
		
//		resObj.setBody( );
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getBookingHistory", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getBookingHistory(@RequestBody RequestObj<Customer> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Integer customerId =  reqObj.getBody().getCustomerId();
		List<Booking> bookingList = mobileServiceImpl.getBookingHistory(customerId, 
				AppConstants.NO_FILTER , AppConstants.NO_FILTER, null, null);
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
		
		Integer customerId =  Integer.parseInt(reqObj.getBody().get("customerId"));
		Integer bookingId = 0;
		try{
			bookingId =  Integer.parseInt(reqObj.getBody().get("bookingId"));
			try {
				logger.debug("Booking ID for the Booking is  --- >  "+bookingId + "  "+new ObjectMapper().writeValueAsString(reqObj).toString());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(NullPointerException e){

			header.setMessage(env.getProperty("error.missing.booking"));
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			resObj.setHeader(header);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);			
		}
		
		Booking booking = mobileServiceImpl.getBookingDetails(bookingId);
		if(booking == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(booking);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getDriverDetails", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getDriverDetails(@RequestBody RequestObj<HashMap<String, String>> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		Driver d;
		
		d = mobileServiceImpl.getDriver(Integer.parseInt(reqObj.getBody().get("driverId")));
			
		List opBody =  new ArrayList<>();	
		//opBody.add(drLocList);
		//opBody.add(d);
		
		if(d == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(d);//Check performance or take in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCSE", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> getCSE(@RequestBody RequestObj<HashMap<String, String>> reqObj ){
		
		//TODO CACHING
		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
		Integer bookRadius = Integer.parseInt(configMap.get("BOOK").get("RADIUS").getAttrValue1());
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		HashMap<String, String> loc =  reqObj.getBody();
		
		List<DrLocation>  drLocList =  mobileServiceImpl.getCSE(loc.get("lat"), loc.get("lng"), loc.get("place"), loc.get("area"), bookRadius, loc.get("vehicleType"));
		Driver d;
			d = new Driver();
			if(drLocList.size()>0){
				d = mobileServiceImpl.getDriver(drLocList.get(0).getDriverId());
			}
			
		List opBody =  new ArrayList<>();	
		//opBody.add(drLocList);
		//opBody.add(d);
		
		if(drLocList == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(drLocList);//Check performance or take in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> book(@RequestBody RequestObj<Booking> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Booking booking =  reqObj.getBody();
		booking.setStatus(AppConstants.STATUS_BOOKED);
		Driver d = new Driver();
		try {
			mobileServiceImpl.bookCab(booking, false);
		}catch (ServiceException e) {
			resObj.setBody(e.getMessage()+" - Booking Could not be completed");
			header.setMessage(env.getProperty("booking.error"));
			header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		catch (Exception e) {
			resObj.setBody(e.getMessage()+" Booking Error");
			header.setMessage(env.getProperty("booking.error"));
			header.setStatusCode(AppConstants.GLOBAL_SERVER_ERROR);
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		if(booking == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			
			resObj.setBody(booking);//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/cancelBooking", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> cancelBooking(@RequestBody RequestObj<Booking> reqObj ){
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Booking booking =  reqObj.getBody();
		booking.setStatus(AppConstants.STATUS_CANCELLED);
		if(StringUtils.isEmpty(booking.getCancellationReason())){
			header.setMessage(env.getProperty("error.invalid.cancelreason"));
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			resObj.setBody(env.getProperty("error.invalid.cancelreason"));
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		
		Integer result = mobileServiceImpl.cancelBooking(booking);
		
		if(result <= 0){
			header.setMessage(env.getProperty("error.cannot.cancel"));
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			resObj.setBody(env.getProperty("error.cannot.cancel"));
			return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
		}
		if(booking != null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(booking);//Check performance or tae in string
		}

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
		DrLocation drLoc = new DrLocation();
		drLoc = mobileServiceImpl.getDrLocation(bookingDetails.getDriverId());
		Integer vehicleId = drLoc.getVehicleId();
		DrVehicle dv = new DrVehicle();
		dv = mobileServiceImpl.getDrVehicleDetails(vehicleId);
		bookingDetails.setMake(dv.getMake());
		bookingDetails.setModel(dv.getModel());
		bookingDetails.setCarNumber(dv.getCarNumber());
		bookingDetails.setLat(drLoc.getLat());
		bookingDetails.setLng(drLoc.getLng());
		if(bookingDetails == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(bookingDetails);//Check performance or tkae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/trackOpenBooking", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> trackOpenBooking(@RequestBody RequestObj<Customer> reqObj ){
		
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		Customer customer =  reqObj.getBody();
		
		/*//Booking bookingDetails =  mobileServiceImpl.getBookingDetails(booking.getBookingId());
		
		if(bookingDetails != null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(bookingDetails);//Check performance or tkae in string
		}
*/
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/rideEstimate", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> rideEstimate(@RequestBody RequestObj<Booking> reqObj ){
		
		//TODO CACHING
		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
//		Integer bookRadius = Integer.parseInt(configMap.get("BOOK").get("RADIUS").getAttrValue1());
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		Booking booking = reqObj.getBody();
		
		GeoApiContext context = new GeoApiContext().setApiKey(env.getProperty("google.api.key"));
		GoogleDistance gd  = GoogleServicesWrapper.getDistanceFromGoogle(context, new LatLng(Double.valueOf(booking.getSourceLatitude()) , Double.valueOf(booking.getSourceLongitude())),
				new LatLng(Double.valueOf(booking.getDestLatitude()) , Double.valueOf(booking.getDestLongitude())));
		Long baseWaitTime = 15l;
		
		Date currDt = new Date();
		try{
			String txi = booking.getVehicleType();
			if(txi == null ){
				txi = "TXI";
			}
			Collection<AttrConfig>  configList = configMap.get("TARIFF").values();
			for (AttrConfig attrConfig : configList) {
				String[] frTime = attrConfig.getAttrValue1().split(":");
				String[] toTime = attrConfig.getAttrValue2().split(":");
				
				if( currDt.getDay() > 5 ){
					if(attrConfig.getAttrName().contains("TARIFF01")){
						continue;
					}else{
						gd.setTariffCode(attrConfig.getAttrName());
						gd.setBaseFare(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_FARE").getAttrValue1()));
						gd.setKmRate(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_KM_FARE").getAttrValue1()));
						gd.setWaitCharges(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_WAIT_CHRG").getAttrValue1()));
//						gd.setBookingFee(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_CHRG").getAttrValue1()));
						baseWaitTime  = new Long(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_WAIT_TIME").getAttrValue1());
						break;
					}
					
				}
				
				
				if( (Integer.valueOf(frTime[0]) < Integer.valueOf(toTime[0]) &&
						currDt.getHours() >= Integer.valueOf(frTime[0]) && currDt.getHours() <= Integer.valueOf(toTime[0]))
						||
					(Integer.valueOf(frTime[0]) > Integer.valueOf(toTime[0]) &&	
						currDt.getHours() >= Integer.valueOf(frTime[0]) || currDt.getHours() <= Integer.valueOf(toTime[0]))
						){
					gd.setTariffCode(attrConfig.getAttrName());
					gd.setBaseFare(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_FARE").getAttrValue1()));
					gd.setKmRate(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_KM_FARE").getAttrValue1()));
					gd.setWaitCharges(new BigDecimal(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_WAIT_CHRG").getAttrValue1()));
//					gd.setBookingFee(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_CHRG").getAttrValue1()));
					baseWaitTime  = new Long(configMap.get(attrConfig.getAttrName()+"-"+txi).get("BASE_WAIT_TIME").getAttrValue1());
					break;
				}
				//configMap.get("FARE"+"_"+attrConfig.getAttrName()).get("BASE_FARE").getAttrValue1();
			} 
		}catch(Exception e){
			logger.error(e.getMessage()+"   ----   ");
			gd.setBaseFare(new BigDecimal(configMap.get("FARE").get("BASE_FARE").getAttrValue1()));
			gd.setKmRate(new BigDecimal(configMap.get("FARE").get("BASE_KM_FARE").getAttrValue1()));
			gd.setWaitCharges(new BigDecimal(configMap.get("FARE").get("BASE_WAIT_CHRG").getAttrValue1()));
			baseWaitTime  = new Long(configMap.get("FARE").get("BASE_WAIT_TIME").getAttrValue1());
			e.printStackTrace();
		}
		
		gd.setBookingFee(new BigDecimal(configMap.get("FARE").get("BOOKING_FEE").getAttrValue1()));
		
		BigDecimal kmRate = new BigDecimal(0);
		if(gd.getDistanceUnits().contains("km")){
			double perkmRate = gd.getDistance() - Double.valueOf(configMap.get("FARE").get("BASE_KM").getAttrValue1());
			kmRate = gd.getKmRate()
					.multiply(
							new BigDecimal(perkmRate)
							);
		}
		
		BigDecimal minRate =  gd.getBookingFee().add(gd.getBaseFare()).add(kmRate);
		
		BigDecimal maxRate =   gd.getBookingFee().add(gd.getBaseFare()).add(kmRate)
								.add(new BigDecimal(gd.getDuration()/2).multiply(gd.getWaitCharges()));
		
		gd.setMinFare(minRate);
		gd.setMaxFare(maxRate);
		
		//Logic can be changes to TRAFIC TIME GOOGLE
		if(gd == null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(gd);//Check performance or take in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/fareCard", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> fareCard(@RequestBody RequestObj<Booking> reqObj ){
		//TODO CACHING
		Integer zoneId = Integer.parseInt(env.getRequiredProperty("default.zone"));
		LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = mainServiceImpl.getGlobalConfig(zoneId);
//		Integer bookRadius = Integer.parseInt(configMap.get("BOOK").get("RADIUS").getAttrValue1());
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Booking booking =  reqObj.getBody();
		//Set<AttrConfig>  configList = (Set<AttrConfig>) configMap.get("TARIFF_CODE").values();
		List  fareCardList = new ArrayList();
		for (AttrConfig attrConfig : configMap.get("TARIFF").values()) {
			attrConfig.setSubAttributes(configMap.get(attrConfig.getAttrName()+"-"+ booking.getVehicleType()));
			fareCardList.add( attrConfig);
		}
		
		logger.debug("SIZE of map "+fareCardList.size()+"   --  "+configMap.get("TARIFF").values().size());
		HashMap resBody = new HashMap<>();
		resBody.put("TARIFF", fareCardList);
		
		resObj.setBody(resBody);
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/payRideFare", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> payRideFare(@RequestBody RequestObj<Booking> reqObj ){
		
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();

		header.setMessage(env.getProperty("success.message"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		
		Booking booking =  reqObj.getBody();
		
		Integer i  = mobileServiceImpl.captureJourneyPayment(booking.getBookingId());
		
		if(i != null ){
			resObj.setBody(env.getProperty("no.data.found"));
		}else{
			resObj.setBody(env.getProperty("ride.payment.success"));//Check performance or tae in string
		}

		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
		
	public Boolean processFile(MultipartFile file) {
	
		
//		final String BASE_DIR_DOWNLOAD  = "C:\\Users\\givizer 1\\workspaceNew\\downloads\\"; 
		final String BASE_DIR_DOWNLOAD  =   env.getProperty("profile.pic.dir.customer"); 

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
	
	@RequestMapping(value = "/saveEnquiry", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> saveEnquiry(@Valid @RequestBody RequestObj<EnquiryForm> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		//TODO
		
		mobileServiceImpl.addEnquiry(reqObj.getBody());
		header.setMessage(env.getProperty("enquiry.form.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		resObj.setHeader(header);
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value="/changeDefaultAccount", method = RequestMethod.POST)
	public ResponseEntity<ResponseObj> changeDefaultAccount(@RequestBody RequestObj<CustomerAccount> reqObj)
	{
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		CustomerAccount ca= reqObj.getBody();
		
		Integer n = mobileServiceImpl.changeDefAccount(ca.getCustomerId(),ca.getAccountType());
		if(n == 1){
		header.setMessage(env.getProperty("default.payment.updated.success"));
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
		resObj.setHeader(header);
		resObj.setBody(env.getProperty("default.payment.updated.success"));
		}
		else
		{
			header.setMessage(env.getProperty("payment.type.not.exists"));
			resObj.setHeader(header);
			resObj.setBody(env.getProperty("payment.type.not.exists"));
		}
        return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value="/verifyEmailId", method = RequestMethod.POST)
	public ResponseEntity<ResponseObj> verifyEmailId(@RequestBody RequestObj<Customer> reqObj)
	{
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		Customer c = reqObj.getBody();
		Payment payment = c.getPaymentList().iterator().next();
		Integer corpCustId = mobileServiceImpl.verifyEmailPin(payment.getEmail(), payment.getPin());
		if(corpCustId != null)
		{
			Integer rembursed = mobileServiceImpl.getRembursed(corpCustId);
			if(rembursed == 1 ){
				return registerCustomer(reqObj);
			}else if(rembursed == 0){
				header.setMessage(env.getProperty("email.verified"));
				header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
				//ObjectMapper om = new ObjectMapper();
				Map<String,Integer> newMap = new HashMap<>();
				newMap.put("rembursed", rembursed);
				resObj.setBody(newMap);
			}else{
				header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
				header.setMessage("INVALID");
				resObj.setBody("INVALID SERVICE CALL");
				
			}
		}
		else
		{
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(env.getProperty("invalid.email.pin"));
			resObj.setBody(env.getProperty("invalid.email.pin"));
		}
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value="/sendRembursed", method = RequestMethod.POST)
	public ResponseEntity<ResponseObj> sendRembursed(@RequestBody RequestObj<Customer> reqObj)
	{
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		Customer c = reqObj.getBody();
		Payment payment = c.getPaymentList().iterator().next();
		Integer corpCustId = mobileServiceImpl.verifyEmailPin(payment.getEmail(), payment.getPin());
		if(corpCustId != null)
		{
			Integer rembursed = mobileServiceImpl.getRembursed(corpCustId);
			if(rembursed == 1 ){
				return registerCustomer(reqObj);
			}else if(rembursed == 0){
				header.setMessage(env.getProperty("email.verified"));
				header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
				resObj.setBody(rembursed);
			}else{
				header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
				header.setMessage("INVALID");
				resObj.setBody("INVALID SERVICE CALL");
				
			}
		}
		else
		{
			header.setStatusCode(AppConstants.ERROR_INVALID_INPUT);
			header.setMessage(env.getProperty("invalid.email.pin"));
			resObj.setBody(env.getProperty("invalid.email.pin"));
		}
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	
	public static void main(String[] args) throws IOException {
		//String path = new File("").getAbsolutePath().replace("\\", "/") +"../"+"downloads" ;
		String normalized;
			System.out.println(new File("../downloads").getCanonicalPath());
	}
	
	

	@RequestMapping(value = "/customerRating", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> customerRating(@RequestBody RequestObj<DriverRide> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		DriverRide dtd = reqObj.getBody();
		
		mobileServiceImpl.saveRatingByCustomer(dtd);
		header.setMessage(env.getProperty("ec.data.updated.success"));	
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(env.getProperty("ec.data.updated.success"));
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}

	@RequestMapping(value = "/notification", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> notification(@RequestBody RequestObj<Customer> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Customer c = reqObj.getBody();
		
		header.setMessage("");
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(mobileServiceImpl.getNotiifications());
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/aboutus", method = RequestMethod.POST)
    public ResponseEntity<ResponseObj> aboutus(@RequestBody RequestObj<Customer> reqObj) {
		ResHeader header = new ResHeader();
		ResponseObj resObj = new ResponseObj();
		
		Customer c = reqObj.getBody();
		
		String version=env.getProperty("voyager.customer.version");
		String message=env.getProperty("voyager.customer.aboutus.message");
				
		header.setMessage(message +" \n\n "+version);
		header.setStatusCode(AppConstants.GLOBAL_SUCCESS);
	
		resObj.setBody(message +"\n\n "+version);
		
		resObj.setHeader(header);
		return new ResponseEntity<ResponseObj>(resObj, HttpStatus.OK);
	}
	
}
