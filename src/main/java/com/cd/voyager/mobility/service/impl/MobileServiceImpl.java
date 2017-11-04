package com.cd.voyager.mobility.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.common.util.GCMSender;
import com.cd.voyager.common.util.GoogleServicesWrapper;
import com.cd.voyager.common.util.MD5Utils;
import com.cd.voyager.common.util.SMSUtil;
import com.cd.voyager.common.util.StringGenUtils;
import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.CustomerAccount;
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
import com.cd.voyager.entities.GoogleDistance;
import com.cd.voyager.entities.Notification;
import com.cd.voyager.entities.Payment;
import com.cd.voyager.entities.Preferences;
import com.cd.voyager.entities.Promo;
import com.cd.voyager.entities.PromoApplied;
import com.cd.voyager.entities.PromoValidate;
import com.cd.voyager.entities.RegDevice;
import com.cd.voyager.entities.Registration;
import com.cd.voyager.entities.SupportMaster;
import com.cd.voyager.exception.InvalidCredentialsException;
import com.cd.voyager.exception.MailException;
import com.cd.voyager.exception.ServiceException;
import com.cd.voyager.mobility.service.IMobileService;
import com.cd.voyager.utils.payment.paypal.PaypalFuturePaymentUtils;
import com.cd.voyager.utils.payment.payway.PayWayAPIUtil;
import com.cd.voyager.web.service.dao.AbstractDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import com.paypal.base.rest.PayPalRESTException;


@Repository
@PropertySource("classpath:messages_en.properties")
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class MobileServiceImpl extends AbstractDao implements IMobileService {
	
	private final Logger logger = LoggerFactory.getLogger(MobileServiceImpl.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private MailSenderPooled mailSenderPooled;

    //@Transactional
    public void saveRegistration(Registration reg) {
    	
		getSession().saveOrUpdate(reg);
    }
    public void saveRegisteredDevice(RegDevice regDevice) {
    	
		getSession().saveOrUpdate(regDevice);
    }

    public boolean isRegistrationExist(Integer id) {
    	
    	if(getSession().createCriteria(Registration.class).add(Restrictions.eq("regid", id)).list().size() > 0){
    		return true;
    	}else{
    		return false;
    	}
     }
	public Registration getIfRegistrationExist(Registration reg) {
	    	
	    	List<Registration> regList = getSession().createCriteria(Registration.class).add(Restrictions.eq("email", reg.getEmail())).list();
	    	if(regList.size() > 0){
	    		return regList.get(0);
	    	}else{
	    		return null;
	    	}
	}
	public Registration updateRegistration(Registration reg) {
    	
    	Registration reg2 = getIfRegistrationExist(reg);
    	reg2.setFname(reg.getFname());
    	reg2.setLname(reg.getLname());
    	reg2.setMobile(reg.getMobile());
    	reg2.setPassword(reg.getPassword());
    	reg2.setCountryCode(reg.getCountryCode());
    	reg2.setOTP(reg.getOTP());
    	reg2.setOTPDate(reg.getOTPDate());

    	getSession().saveOrUpdate(reg2);
    	
    	return reg2;
}
	    
    public boolean isRegistrationExist(Registration reg) {
    	
    	List<Registration> regList = getSession().createCriteria(Registration.class).add(Restrictions.eq("email", reg.getEmail())).list();
    	if(regList.size() > 0){
    		return true;
    	}else{
    		return false;
    	}
     }
    
    @SuppressWarnings("unchecked")
	public Customer signIn(Customer customer) {
    	Customer cus = null;
    	List<Customer> cList;
    	 
    	if(getSession().createCriteria(Customer.class).add(Restrictions.eq("email", customer.getEmail())).list().size() > 0){
        	if(customer.getSignInVia()!= null &&  customer.getSignInVia().equals(AppConstants.LOGIN_WITH_FACEBOOK)){
        		
        		cList = getSession().createCriteria(Customer.class)		
        				.createAlias("paymentList", "payment")
        				.add(Restrictions.eq("email", customer.getEmail())).add(Restrictions.eq("authKeyFb", customer.getAuthKeyFb())).list();
		
    		}
    		else if(customer.getSignInVia()!= null &&  customer.getSignInVia().equals(AppConstants.LOGIN_WITH_FACEBOOK)){
	    		cList = getSession().createCriteria(Customer.class)		
	    				.createAlias("paymentList", "payment")
	    				.add(Restrictions.eq("email", customer.getEmail())).add(Restrictions.eq("authKeyG", customer.getAuthKeyG())).list();
	    		 
    				
    		}else{

	    		cList = getSession().createCriteria(Customer.class)		
	    				.createAlias("paymentList", "payment", JoinType.LEFT_OUTER_JOIN)
	    				.add(Restrictions.eq("email", customer.getEmail())).add(Restrictions.eq("password", customer.getPassword())).list();
    		}
    		 if(cList !=null && cList.size() >0){
    			cus = cList.get(0);
    			cus.setProfilePicURL(env.getProperty("profile.pic.url")+ cus.getProfilePicURL());
    			if(!StringUtils.isEmpty(customer.getGcmRegId())){
    				updateCustGCMRegId(cus.getCustomerId(), customer.getGcmRegId());
    			}
    			return cus;
    		 }else{
    			 System.out.println("INVALID EXCEPTION 2");
    	    	 throw new InvalidCredentialsException("Password is worng ! ");
    	    			 
    		 }
    		 
    	}else{
			System.out.println("INVALID EXCEPTION 2");
    		throw new InvalidCredentialsException("User Does not Exists! ");
    	}
    	
     }
    
    
    public void updateCustGCMRegId(Integer customerId, String gcmRegId) {
		String hql = "UPDATE Customer set gcmRegId = :gcmRegId, modifiedDate = :modifiedDate where customerId  = :customerId ";

		Query query = getSession().createQuery(hql)
					.setParameter("gcmRegId",  gcmRegId)
					.setParameter("modifiedDate", new Date())
						.setParameter("customerId", customerId);
		int result = query.executeUpdate();
		
		
	}
	public Integer savePayment(Payment payment) {
    	
		//getSession().saveOrUpdate(payment);
		//TODO CHECK generation of uniqueness
		
    	/*String hql = "select max(paymentId) from Payment";
		Query query = getSession().createQuery(hql);
		String paymentID = (String)query.uniqueResult();
		if(paymentID != null)
			paymentID = "P"+(Integer.parseInt(paymentID.substring(1))+1);
		else
			paymentID = "P"+10000;*/
    	
    	
		//payment.setPaymentId(paymentID);
		
		//Payment payment= customer.getPaymentList().iterator().next();
		CustomerAccount custAcc =  new CustomerAccount();
		custAcc.setCustomerId(payment.getCustomerId());
		custAcc.setEmail(payment.getEmail());
		custAcc.setAccountType(payment.getAccountType());
		
		//TODO check with gatewayId duplication
		if(AppConstants.ACCOUNT_TYPE_CORPORATE.equals(payment.getAccountType()) && !StringUtils.isEmpty(payment.getEmail()) ){
			
			Integer corpCustId = getCorpCustPaymentIdFromEmail(payment.getEmail(), payment.getPin());
			Integer paymentId = getCorpCustPaymentIdFromEmail(payment.getEmail(), payment.getPin());
			
			Integer rembursed = getRembursed(corpCustId);
			if(rembursed == 1){
				custAcc.setDefaultPayment(paymentId);
				custAcc.setCorpCustId(getCorpCustIdFromEmail(payment.getEmail()));
			}else
				return AppConstants.CUSTOMER_CORP_EMAIL_SIGNUP_FAIL; 
		}else if(!StringUtils.isEmpty(payment.getGatewayId())){
			
			
			getSession().saveOrUpdate(payment);
			custAcc.setDefaultPayment(payment.getPaymentId());
		}else{
			throw new ServiceException("Invalid payment Details");
		}
		
		//getSession().saveOrUpdate(custAcc);
		
		return AppConstants.CUSTOMER_SIGNUP_SUCCESS;
    }
	@Override
	public void savePref(Preferences pref) {
		getSession().saveOrUpdate(pref);
	}
	
	@Override
	public void saveEmargancyContact(EmergancyContact ec) {
		getSession().saveOrUpdate(ec);
	}
	
	@Override
	public List<EmergancyContact> getEmargancyContact(Integer customerId) {
		return getSession().createCriteria(EmergancyContact.class)
				.add(Restrictions.eq("customerId", customerId))
				.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE))
				.addOrder(Order.asc("contactId")).list();
		
	}
	@Override
	public void saveCustomer(Customer reg) {
		getSession().saveOrUpdate(reg);
		
	}
	@Override
	public boolean isCustomerExist(Integer id) {
		if(getSession().createCriteria(Customer.class).add(Restrictions.eq("customerId", id)).list().size() > 0){
    		return true;
    	}else{
    		return false;
    	}
    }
	@Override
	public boolean isCustomerExist(Customer customer) {

    	return isCustomerExist(customer.getEmail());
	}

	@Override
	public boolean isCustomerExist(String email) {

    	if(getSession().createCriteria(Customer.class).add(Restrictions.eq("email", email)).list().size() > 0){
    		return true;
    	}else{
    		return false;
    	}
	}
	@Override
	public Integer updateOTP(Registration reg) {
		// TODO Auto-generated method stub
		String hql = "UPDATE Registration set OTP = :OTP, OTPDate = :OTPDate "  + 
	             "WHERE email = :email ";
		Query query = getSession().createQuery(hql)
					.setParameter("OTP",  reg.getOTP())
					.setParameter("OTPDate", reg.getOTPDate())
						.setParameter("email", reg.getEmail());
		int result = query.executeUpdate();
		
		//getCustmerProfileFromEmail(reg.getEmail());
		
		return result;
	}
	@Override
	public Customer getCustmerProfileByEmail(String email) {
		return (Customer)getSession().createCriteria(Customer.class)
				/*.setProjection(Projections.projectionList()
					.add(Projections.property("customerId"))
					.add(Projections.property("email"))
					.add(Projections.property("fname"))
					.add(Projections.property("lname"))
					.add(Projections.property("rewardPoints"))
					.add(Projections.property("referralCode"))
					.add(Projections.property("paymentList"))
				).setResultTransformer(Transformers.aliasToBean(Customer.class))
				*/
	//.createAlias("paymentList", "payment", JoinType.LEFT_OUTER_JOIN)

	.add(Restrictions.eq("email",email)).uniqueResult();

	}
	@Override
	public Registration verifyOTP(Registration reg) {
		String hql = " from Registration "  + 
	             "WHERE email = :email ";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date currentDate = new Date(cal.getTimeInMillis());
		Query query = getSession().createQuery(hql)
								.setParameter("email",  reg.getEmail());

		Registration regRet =  (Registration)query.uniqueResult();
		if (regRet != null){
			System.out.println("OTP --"+regRet.getOTP()+" and existing + "+reg.getOTP()+"  -- Date compare "+(currentDate.before(regRet.getOTPDate())+"   date-- > "+currentDate.toString()));
		}
		
		if (regRet != null && (regRet.getOTP().equals(reg.getOTP()) && currentDate.before(regRet.getOTPDate()))){
			System.out.println("OTP --"+regRet.getOTP()+" and existing + "+reg.getOTP()+"  -- Date compare "+(currentDate.before(regRet.getOTPDate())+"   date-- > "+currentDate.toString()));
			return regRet;
		}else{
			return null;
		}
	}
	
	@Override
	public Map<String, String> getpaymentGateways() {
	/*	String hql = "select gatwayId, gatewayURL from  "  + 
	             "WHERE email = :email and OTP = :otp and OTPDate < :currentDate ";
			}
	*/
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Customer signUpCustomer(Customer customer, Payment payment) throws Exception {
		
		customer.setPin(StringGenUtils.geratePIN());
		
		//TODO CHECK generation of uniqueness
	/*	String hql = "select max(customerId) from Customer ";
		Query query = getSession().createQuery(hql);
		String customerID = (String)query.uniqueResult();
		if(customerID!=null)
			customerID = "CUS"+(Integer.parseInt(customerID.substring(3))+1);
		else
			customerID = "CUS"+10000;
		customer.setCustomerId(customerID);
	 */
		
		customer.setReferralCode(StringGenUtils.gerateRefCode(customer.getFname().toUpperCase(), customer.getLname()).toUpperCase());
		//customer.getPaymentList().;
		
		//TODO CHECK generation of uniqueness
		//TODO ****IMPORTANT!!! data should go in this table if Corporate email is payment option****
		
/*		hql = "select max(paymentId) from Payment";
		query = getSession().createQuery(hql);
		String paymentID = (String)query.uniqueResult();
		if(paymentID != null)
			paymentID = "P"+(Integer.parseInt(paymentID.substring(1))+1);
		else
			paymentID = "P"+10000;
		payment.setPaymentId(paymentID);
*/		
		
		customer.setPaymentList(null);
		
		getSession().saveOrUpdate(customer);
		
		payment.setCustomerId(customer.getCustomerId());
		payment.setDefaultFlag(AppConstants.STATUS_GLOBAL_ON);
		System.out.println(customer.getCustomerId()+"SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSs");
		//Payment payment= customer.getPaymentList().iterator().next();
		CustomerAccount custAcc =  new CustomerAccount();
		custAcc.setCustomerId(customer.getCustomerId());
		custAcc.setEmail((StringUtils.isEmpty(payment.getEmail()!=null)?payment.getEmail():customer.getEmail()));
		custAcc.setAccountType(payment.getAccountType());
		custAcc.setDefaultAccount(1);
		
		//TODO check with gatewayId duplication
		Integer rembursed = -1;
		if(AppConstants.ACCOUNT_TYPE_CORPORATE.equals(payment.getAccountType())){
			
			Integer corpCustId = getCorpCustIdFromEmail(payment.getEmail());
			Integer paymentId = getCorpCustPaymentIdFromEmail(payment.getEmail(), payment.getPin());
			
			rembursed = getRembursed(corpCustId);
			if(rembursed == 1){
				custAcc.setDefaultPayment(paymentId);
			}
			custAcc.setCorpCustId(getCorpCustIdFromEmail(payment.getEmail()));
		}
		
		if(!StringUtils.isEmpty(payment.getGatewayId())){
			if(AppConstants.GATEWAY_PAYWAY.equalsIgnoreCase(payment.getGatewayId())){
				PayWayAPIUtil payway = new PayWayAPIUtil();
				String res = payway.addCustomer(payment);
				if(!"0".equals(res)){
					throw new ServiceException("Invalid Payment details for Payway");
				}
			}else if(AppConstants.GATEWAY_PAYPAL.equalsIgnoreCase(payment.getGatewayId())){
				if(StringUtils.isEmpty(payment.getAuthKey())){
					throw new ServiceException("Invalid Payment details for PayPal");
				}
			}else if(rembursed == 0) {
				throw new ServiceException("Corporate rembursed not Selected & personal payment not set");	
			}else{
				throw new ServiceException("Invalid payment Details");
			}
			
			getSession().saveOrUpdate(payment);
			custAcc.setDefaultPayment(payment.getPaymentId());
		}else if(rembursed != 1){
			throw new ServiceException("Invalid payment Details");
		}
		
	/*	
	 * CODE CHANGE on 25/06
	 * 
	 * if(voyagerConstants.ACCOUNT_TYPE_CORPORATE.equals(payment.getAccountType())){
			Integer paymentId = getCorpCustPaymentIdFromEmail(payment.getEmail(), payment.getPin());
			if(paymentId != null){
				custAcc.setDefaultPayment(paymentId);
				//getSession().saveOrUpdate(custAcc);
			}
			else{
				throw new ServiceException("Account created but payment method not added"); 
			}
			//getSession().saveOrUpdate(payment);
		}else{
			getSession().saveOrUpdate(payment);
			custAcc.setDefaultPayment(payment.getPaymentId());
//			custAcc.setDefaultPayment(payment.getPaymentId());
		}
	*/	
		getSession().saveOrUpdate(custAcc);
		//getSession().flush();
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7"+customer.getCustomerId());
		return getCustmerProfile(customer.getCustomerId());
		//return customer;
	}
	@Override
	public Integer getCorpCustIdFromEmail(String email) {
		String hql = "select corpCustId from CorpEmp "  + 
	             "WHERE email = :email ";
		Query query = getSession().createQuery(hql)
				.setParameter("email", email);		
		Integer corpCustId = Integer.parseInt(query.uniqueResult().toString());
		if(corpCustId != null){
			return corpCustId ;
		}else{
			return null;
		}
				
	}
	@Override
	public Integer getCorpCustPaymentIdFromEmail(String email, String pin) {
		
		String sql = "select paymentId from tb_corpemp as c Join tb_payment as p on c.corpCustId = p.corpCustId "  + 
	             "WHERE email = :email and pin =:pin";
		Query query = getSession().createSQLQuery(sql)
				.setParameter("email", email)	
				.setParameter("pin", pin);		
		Integer paymentId = (Integer)query.uniqueResult();
		if(paymentId != null){
			return paymentId ;
		}else{
			return null;
		}
				
	}
	@Override
	public Integer changeCustomerPassword(Integer customerId, String oldPassword,
			String newPassword) {
		String hql = "UPDATE Customer as c set password = md5(:password), modifiedDate = :modifiedDate "  + 
	             "where c.customerId = :customerId and c.password = md5(:oldPassword) ";
		//inner join Registration as r ON c.regId = r.regId 
		Query query = getSession().createQuery(hql)
					.setParameter("password",  newPassword)
					.setParameter("modifiedDate",  new Date())
						.setParameter("customerId", customerId)
						.setParameter("oldPassword", oldPassword);
		
		int result = query.executeUpdate();
		return result;
	}
	@Override
	public Integer changeCustomerPasswordOTP(String email, String newPassword,
			String OTP) {
		String hql = "UPDATE Customer as c set password = md5(:password), modifiedDate = :modifiedDate "  + 
	             " where c.email = :email and c.pin = :OTP and c.pinDate > :currDate ";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date currentDate = new Date(cal.getTimeInMillis());
		
		Query query = getSession().createQuery(hql)
					.setParameter("password",  newPassword)
					.setParameter("modifiedDate",  new Date())
						.setParameter("email", email)
						.setParameter("OTP", OTP)
						.setParameter("currDate", currentDate);
		
		int result = query.executeUpdate();
		return result;
	}
	@Override
	public Integer changeCustomerPasswordOTPCusomertId(String customerId,
			String newPassword, String OTP) {
		String hql = "UPDATE Customer as c set password = md5(:password), modifiedDate = :modifiedDate "  + 
	             " where c.customerId = :customerId and c.pin = :OTP and c.pinDate > :currDate ";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date currentDate = new Date(cal.getTimeInMillis());
		
		Query query = getSession().createQuery(hql)
					.setParameter("password",  newPassword)
					.setParameter("modifiedDate",  new Date())
						.setParameter("customerId", customerId)
						.setParameter("OTP", OTP)
						.setParameter("OTPDate", currentDate);
		
		int result = query.executeUpdate();
		return result;
		
	}
	@Override
	public List<Preferences> getPrefData(Integer customerId) {
		return getSession().createCriteria(Preferences.class)
				.add(Restrictions.eq("customerId", customerId))
				.addOrder(Order.asc("favLabel")).list();
		
	}
	@Override
	public List<Payment> getPaymentData(Integer customerId, String status) {
		 Criteria c =  getSession().createCriteria(Payment.class);
		 c.add(Restrictions.eq("customerId", customerId))
			.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE))
			.add(Restrictions.eq("deleteFlag", AppConstants.DELETEFLAG_ALIVE));
		
		/* c.setProjection(Projections.projectionList()
						.add(Projections.property("paymentId"))
						.add(Projections.property("accountType"))
						.add(Projections.property("gatewayId"))
						.add(Projections.property("gatewayUserId"))
						.add(Projections.property("paymentType"))
					).setResultTransformer(Transformers.aliasToBean(Payment.class))
					;
		*/ 
		 return 	c.addOrder(Order.asc("paymentId"))
					.list();
		
	}
	@Override
	public Integer deletePayment(Integer customerId, Integer paymentId) {
		String hql = "select * from tb_Payment "  + 
	             "WHERE customerId = :customerId and deleteFlag !=:deleteFlag AND status =:status";
		Query query = getSession().createQuery(hql)
				.setParameter("deleteFlag", AppConstants.DELETEFLAG_DELETED)
					.setParameter("customerId",  customerId)
						.setParameter("status", AppConstants.STATUS_ACTIVE);
		List paymentObj = query.list();
		if(paymentObj.size()<=1){
			return AppConstants.INVALID_ENTRY;
		}
		
		// TODO Auto-generated method stub
		hql = "UPDATE Payment set deleteFlag = 1, modifiedDate = :modifiedDate "  + 
	             "WHERE customerId = :customerId and paymentId=:paymentId";
		
		query = getSession().createQuery(hql)
				.setParameter("modifiedDate", new Date())
					.setParameter("customerId",  customerId)
						.setParameter("paymentId", paymentId);
		
		return query.executeUpdate();
	
	}
	@Override
	public Integer deletePref(Integer customerId, String favLabel) {
		String hql = "DELETE from Preferences "  + 
	             "WHERE customerId = :customerId and favLabel=:favLabel";
		
		Query query = getSession().createQuery(hql)
					.setParameter("customerId",  customerId)
						.setParameter("favLabel", favLabel);
		return query.executeUpdate();
	}
	@Override
	public Integer delEmergancyContact(Integer customerId, Integer contactId) {
		String hql = "DELETE from EmergancyContact "  + 
	             "WHERE customerId = :customerId and contactId=:contactId";
		
		Query query = getSession().createQuery(hql)
					.setParameter("customerId",  customerId)
						.setParameter("contactId", contactId);
		
		return query.executeUpdate();
	}
	
	@Override
	public Integer updateProfile(Customer customers) {
		
		String updateStr = "";
		
		if(!StringUtils.isEmpty(customers.getMobile())){
			updateStr = " mobile = :mobile , ";
		}
		if(!StringUtils.isEmpty(customers.getFname())){
			updateStr += " fname = :fname , ";
		}
		if(!StringUtils.isEmpty(customers.getLname())){
			updateStr += " lname = :lname , ";
		}
		if(!StringUtils.isEmpty(customers.getProfilePicURL())){
			updateStr += " profilePicURL = :profilePicURL , ";
		}
		
		if(!StringUtils.isEmpty(updateStr)){
			String hql = "UPDATE Customer set  "+updateStr+" modifiedDate = :modifiedDate "  + 
		             "WHERE customerId = :customerId ";
			Query query = getSession().createQuery(hql)
					.setParameter("modifiedDate", new Date())
						.setParameter("customerId", customers.getCustomerId());
			if(!StringUtils.isEmpty(customers.getMobile())){
				query.setParameter("mobile",customers.getMobile());
			}
			if(!StringUtils.isEmpty(customers.getFname())){
				query.setParameter("fname",customers.getFname());
			}
			if(!StringUtils.isEmpty(customers.getLname())){
				query.setParameter("lname",customers.getLname());
			}
			if(!StringUtils.isEmpty(customers.getProfilePicURL())){
				query.setParameter("profilePicURL",customers.getProfilePicURL());
			}		
			return query.executeUpdate();
			
		}else{
			return 0;
		}
	
	}
	@Override
	public Customer getCustmerProfile(Integer customerId) {
	
		return (Customer)getSession().createCriteria(Customer.class)
					/*.setProjection(Projections.projectionList()
						.add(Projections.property("customerId"))
						.add(Projections.property("email"))
						.add(Projections.property("fname"))
						.add(Projections.property("lname"))
						.add(Projections.property("rewardPoints"))
						.add(Projections.property("referralCode"))
						.add(Projections.property("paymentList"))
					).setResultTransformer(Transformers.aliasToBean(Customer.class))
					*/
		.createAlias("paymentList", "payment", JoinType.LEFT_OUTER_JOIN)

		.add(Restrictions.eq("customerId", customerId))
		.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE))
		.add(Restrictions.eq("deleteFlag", AppConstants.DELETEFLAG_ALIVE)).list().get(0);
		
		
	}
	@Override
	public List<Map> getAccounts(Integer customerId) {
		List<Map> accountMap = null;
		String sql = "select ca.customerId as customerId, ca.defaultAccount as defaultAccount, cc.corpCustId as corpCustId,"
				+"cc.corporateName as corporateName, ca.email as email, ca.accountType as accountType, p.paymentId, p.gatewayId from tb_customeraccount as ca LEFT OUTER JOIN tb_corporatecustomer as cc  "  
				+ "ON ca.defaultPayment = cc.corpCustId "
	             + " LEFT OUTER JOIN tb_payment as p ON ca.defaultPayment = p.paymentId "
	             + " WHERE ca.customerId = :customerId AND ca.status = :status AND (ca.deleteFlag = :deleteflag OR ca.deleteFlag is NULL )";
		Query query = getSession().createSQLQuery(sql)
				.setParameter("customerId", customerId)
				.setParameter("status", AppConstants.STATUS_ACTIVE)
				.setParameter("deleteflag", AppConstants.DELETEFLAG_ALIVE);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		accountMap = (List<Map>)query.list();
		
		return accountMap;		
	}
	
	@Override
	public Integer addAccount(Integer customerId, Integer paymentId, String email, String accountype, String pin) {

		/*
		String hql = "select new map(ca.customerId as customerId, ca.corpCustId as corpCustId, ca.corporateName as corporateName, ca.email as email, ca.accountType as accountType, p as Payment from CustomerAccount as ca LEFT OUTER JOIN CorporateCustomer as cc  "  + 
	             "ON cc.defaultPayment = ca.corpCustId  LEFT OUTER JOIN Payment as p ON cc.defaultPayment = p.paymentId AND customerId = :customerId "
	             + " cc.status = :status and cc.deleteFlag = :deleteflag ";
		Query query = getSession().createQuery(hql)
				.setParameter("customerId", customerId)
				.setParameter("status", voyagerConstants.STATUS_ACTIVE)
				.setParameter("deleteflag", voyagerConstants.DELETEFLAG_ALIVE);		
		List<HashMap> accountList = (List<HashMap>)query.list();
	*/

		//Payment payment= customer.getPaymentList().iterator().next();
		CustomerAccount custAcc =  new CustomerAccount();
		custAcc.setCustomerId(customerId);
		custAcc.setEmail(email);
		custAcc.setAccountType(accountype);
		custAcc.setDefaultAccount(0);
		if(AppConstants.ACCOUNT_TYPE_CORPORATE.equals(accountype)){
			
//			if(paymentId == null){
//				Integer corpPaymentId = getCorpCustPaymentIdFromEmail(email, pin);
//				Integer corpCustId = getCorpCustIdFromEmail(email);
//				custAcc.setCorpCustId(corpCustId);
//				custAcc.setDefaultPayment(corpPaymentId);
//			}else
//				custAcc.setDefaultPayment(paymentId);
			Integer corpCustId = verifyEmailPin(email, pin);
			Integer rembursed = getRembursed(corpCustId);
			if(rembursed == 1)
			{
				Integer corpPaymentId = getCorpCustPaymentIdFromEmail(email, pin);
				//Integer corpCustId = getCorpCustIdFromEmail(email);
				custAcc.setCorpCustId(corpCustId);
				custAcc.setDefaultPayment(corpPaymentId);
			}
			else{
				return 0;
			}
		}else{
			custAcc.setDefaultPayment(paymentId);
		}
		getSession().saveOrUpdate(custAcc);
		return AppConstants.CUSTOMER_SIGNUP_SUCCESS;
	}
	
	
	@Override
	public Boolean applyPromo(Integer customerId, String promoCode) {
		
		String hql = "from Promo AS p "  + 
	             "WHERE p.promoCode = :referralCode AND customerId != :customerId AND NOT EXISTS (select * from PromoValidate AS pv "  + 
		             " WHERE pv.customerId = :customerId2 and pv.promoType = :promoType )";
		
		
		return false;
	}
	 
	@Override
	public Integer validateRefCode(String promoCode, Integer customerId, Date expDate, Double discount) {
		
		String hql = "select c.customerId from Customer AS c "  + 
	             "WHERE c.referralCode = :referralCode AND customerId != :customerId AND NOT EXISTS (from PromoValidate AS pv "  + 
		             " WHERE pv.customerId = :customerId2 and pv.promoType = :promoType )";
		Query query = getSession().createQuery(hql)
				.setParameter("referralCode", promoCode.toUpperCase())
				.setParameter("customerId", customerId)
				.setParameter("customerId2", customerId)
				.setParameter("promoType", AppConstants.PROMO_TYPE_REF);
		
		
		String refCustId = (String)query.uniqueResult();
		if(refCustId == null){
			return AppConstants.INVALID_REFERAL_CODE;
		
		}else{
					//return voyagerConstants.VALID_ENTRY;
				PromoValidate pv = new PromoValidate();
				pv.setPromoId(AppConstants.REF_CODE_DEFAULT);
				pv.setCustomerId(customerId);
				pv.setRefStatus(AppConstants.REF_CODE_APPLIED);
				pv.setStatus(AppConstants.STATUS_ACTIVE);
				pv.setValidateDate(new Date());
				pv.setPromoCode(promoCode);
				pv.setPromoType(AppConstants.PROMO_TYPE_REF);
				
/*				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, );
				Date expDate = new Date(cal.getTimeInMillis());
*/				
				pv.setPromoDiscount(discount);
				pv.setExpDate(expDate);
				applyRef(pv);
				
				return pv.getPromoId();
			}
			
	}
		
	@Override
	public Integer validatePromoCode(String promoCode, Integer customerId) {
		
		String hql = "FROM Promo as p "
				+ "where promoCode = :promoCode AND now() between startDate and endDate "
				+ "and status = :status and type = :type ";
		Query query = getSession().createQuery(hql)
				.setParameter("promoCode", promoCode.toUpperCase())
				.setParameter("status", AppConstants.STATUS_ACTIVE)
				.setParameter("type", AppConstants.PROMO_TYPE_CPN);
		
		List<Promo> pList = null;
		Promo promo ;
		pList = query.list();
		if( pList != null && pList.size() >0){
			promo =  pList.get(0);
			hql = "from PromoValidate AS pv "
			 		+ "WHERE pv.customerId = :customerId "
			 		+ "AND pv.promoType = :promoType "
			 		+ "AND p.promoId = :promoId ";
			 
			query = getSession().createQuery(hql)
					.setParameter("customerId", customerId)
					.setParameter("promoType", AppConstants.PROMO_TYPE_CPN)
					.setParameter("promoId", promo.getPromoId());
			List<PromoValidate> pvList = query.list();
			if( pvList  != null && pvList.size() > 0 ){
				return AppConstants.DUPLICATE_PROMO_CODE;
			}else{
				PromoValidate pv = new PromoValidate();
				pv.setPromoId(promo.getPromoId());
				pv.setCustomerId(customerId);
				//pv.setRefStatus(voyagerConstants.REF_CODE_APPLIED);
				pv.setStatus(AppConstants.STATUS_ACTIVE);
				pv.setValidateDate(new Date());
				pv.setPromoCode(promoCode);
				pv.setPromoType(AppConstants.PROMO_TYPE_CPN);
				
				pv.setPromoDiscount(promo.getRewardAmount());
				
				if(promo.getAllExpDate() != null){
					pv.setExpDate(promo.getAllExpDate());
				}else{
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, (promo.getValidDays()!=null?promo.getValidDays():30));
					Date expDate = new Date(cal.getTimeInMillis());
					pv.setExpDate(expDate);
				}
				applyRef(pv);
				return pv.getPromoId();
			}
			
		}else{
			return AppConstants.INVALID_PROMO_CODE;
		}
		
	}
	
	
	
	@Override
	public void applyRef(PromoValidate pv) {
		getSession().save(pv);
		
	}

	@Override
	public Driver signInDriver(Driver driver) {

		List<Driver> dList = getSession().createCriteria(Driver.class)		
				.add(Restrictions.or(Restrictions.eq("email", driver.getEmail()),Restrictions.eq("cseCode", driver.getCseCode()))).add(Restrictions.eq("password", MD5Utils.to_MD5(driver.getPassword()))).list();
		
		if(dList.size() >0 && null != dList.get(0)){
			Driver d = dList.get(0);
			//Driver d = getDriver(dList.get(0).getDriverId())
			if(null != dList.get(0).getDrVehicles()){
				for (DrVehicle drVehicle : dList.get(0).getDrVehicles()) {
					if(AppConstants.STATUS_GLOBAL_ON == drVehicle.getDefaultCar()){
						d.setVehicleId(drVehicle.getVehicleId());
						d.setVehicleType(drVehicle.getvType());
						d.setDefaultVehicle(drVehicle.getMake()!= null?drVehicle.getMake()+" ":"" +
												drVehicle.getModel()!= null?drVehicle.getModel()+" ":"" + 
												drVehicle.getRegistrationId());
					}
				} 
			}
			DriverLogin driverLogin = new DriverLogin();
			driverLogin.setDriverId(d.getDriverId());
			
			Date currDt = new Date();
			driverLogin.setLoginTime(currDt);
			driverLogin.setStatus(AppConstants.STATUS_ACTIVE);
			saveDriverLogin(driverLogin);
						
			return d;
		}else{
			return null;
		}
		
	}
	
	@Override
	public Driver getDriver(Driver driver) {

		List<Driver> dList = getSession().createCriteria(Driver.class)		
				.add(Restrictions.or(Restrictions.eq("email", driver.getEmail()),Restrictions.eq("cseCode", driver.getCseCode()))).list();
		
		if(dList != null){
			return getDriver(driver.getDriverId());
		}else{
			return null;
		}
	}	
	@Override
	public Driver getDriver(Integer driverId) {
	/*	String hql = "from DriverLogin as dl join Driver d ON d.driverId = dl.driverId WHERE dl.driverId = :driverId ";
		Query query = getSession().createQuery(hql)
				.setParameter("driverId",  driverId);
		Driver driver = (Driver)query.list().get(0);
		return driver;*/
		
		Driver driver = (Driver)getSession().createCriteria(Driver.class)		
				.add(Restrictions.eq("driverId", driverId)).list().get(0);
		
		return driver;
	}
	
	@Override
	public void applyPromo(PromoApplied promoApplied) {
		getSession().save(promoApplied);
			
	}
	@Override
	public List<SupportMaster> getSupport(String supportFor) {
		//TODO
		return getSession().createCriteria(SupportMaster.class).add(Restrictions.eq("supportFor", supportFor)).list();
	}

	@Override
	public List<SupportMaster> getSupportQuestions(String supportFor) {
		//TODO
		return getSession().createCriteria(SupportMaster.class).add(Restrictions.eq("supportFor", supportFor)).setProjection(Projections.distinct(Projections.property("supportQuestion"))).addOrder(Order.asc("supportId")).list();
	}
	
	@Override
	public SupportMaster getSupportMaster(Integer supportId) {
		//TODO
		return (SupportMaster)getSession().createCriteria(SupportMaster.class).add(Restrictions.eq("supportId", supportId)).list().get(0);
	}


	@Override
	public Integer updateOTPDriver(Driver driver) {

		String otp = SMSUtil.gerateOTP();
		SMSUtil smsUtil =new SMSUtil();
		if(smsUtil.sendSMS("OTP_TPL", (driver.getCountryCode().toString()+driver.getPhone().toString()), otp, env)){
			String sql = "" 
					+ "INSERT INTO tb_driverotp (driverId, OTPDate,  OTP, status) VALUES (:driverId, :OTPDate, :OTP, :status)"
					+ "ON DUPLICATE KEY UPDATE OTP = :OTP, OTPDate = :OTPDate, status =:status ";
			Query query = getSession().createSQLQuery(sql)
						.setParameter("OTP", otp )
						.setParameter("OTPDate", new Date())
						.setParameter("driverId", driver.getDriverId())
						.setParameter("status", AppConstants.STATUS_ACTIVE);

			int result = query.executeUpdate();
			return result;
		
		}else{
			return Integer.parseInt(AppConstants.GLOBAL_PAGE_ERROR);
		}
	}
		
	@Override
	public Integer verifyOTPDriver(String driverId, String OTP) {
		String sql = " select OTP, OTPDate from tb_driverotp "  + 
	             "WHERE driverId = :driverId and status = :status and OTP = :OTP and OTPDate > :currentDate";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date currentDate = new Date(cal.getTimeInMillis());
		Query query = getSession().createSQLQuery(sql)
								.setParameter("driverId",  driverId)
								.setParameter("status", AppConstants.STATUS_ACTIVE)
								.setParameter("OTP", OTP)
								.setParameter("currentDate", currentDate)
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		
		if( query.list()!=null && query.list().size() > 0){
			HashMap listObj =  (HashMap)query.uniqueResult();
			if (listObj != null){
				System.out.println("OTP --"+listObj.get("OTP")+" and existing + "+listObj.get("OTPDate")+"  -- Date compare "+(currentDate.before((Date)listObj.get("OTPDate"))+"   date-- > "+currentDate.toString()));
			}
			if (listObj != null && (OTP.equals(listObj.get("OTP")) && currentDate.before((Date)listObj.get("OTPDate")))){
//				System.out.println("OTP --"+regRet.getOTP()+" and existing + "+reg.getOTP()+"  -- Date compare "+(currentDate.before(regRet.getOTPDate())+"   date-- > "+currentDate.toString()));
				return AppConstants.VALID_ENTRY;
			}else{
				return AppConstants.INVALID_ENTRY;
			}
		}
		else{
			return AppConstants.INVALID_ENTRY;
		}
		
	}

	@Override
	public Integer updateProfileDriver(Driver driver) {
		
		String updateStr = "";
		
		if(!StringUtils.isEmpty(driver.getPhone())){
			updateStr = " phone = :phone , ";
		}
		if(!StringUtils.isEmpty(driver.getFname())){
			updateStr += " fname = :fname , ";
		}
		if(!StringUtils.isEmpty(driver.getLname())){
			updateStr += " lname = :lname , ";
		}
		if(!StringUtils.isEmpty(driver.getEmail())){
			updateStr += " email = :email , ";
		}
		if(!StringUtils.isEmpty(driver.getPhoto())){
			updateStr += " photo = :photo , ";
		}
		
		if(!StringUtils.isEmpty(updateStr)){
			String hql = "UPDATE Driver set  "+updateStr+" modifiedDate = :modifiedDate "  + 
		             "WHERE driverId = :driverId ";
			Query query = getSession().createQuery(hql)
					.setParameter("modifiedDate", new Date())
						.setParameter("driverId", driver.getDriverId());
			if(!StringUtils.isEmpty(driver.getPhone())){
				query.setParameter("phone", driver.getPhone());
			}
			if(!StringUtils.isEmpty(driver.getFname())){
				query.setParameter("fname",driver.getFname());
			}
			if(!StringUtils.isEmpty(driver.getLname())){
				query.setParameter("lname",driver.getLname());
			}
			if(!StringUtils.isEmpty(driver.getEmail())){
				query.setParameter("email",driver.getEmail());
			}		

			if(!StringUtils.isEmpty(driver.getPhoto())){
				query.setParameter("photo",driver.getPhoto());
			}		
			
			return query.executeUpdate();
			
		}else{
			return 0;
		}
	
	}
	
	@Override
	public List<Booking> getBookingHistory(Integer customerId, Integer start, Integer end, Date startDate, Date endDate) {

		List<Booking> bookingHistory = null;
		String dateFilter ="";
		if(startDate != null && endDate != null ){
			dateFilter += " and cr.tripStartDate between :startDate and :endDate";
		}

		String sql = "select b.createDate as createDate, b.bookingId, b.srcPlace, b.destPlace, cr.rideTotalAmt as rideTotalAmt, "
				+ "concat(d.fname,' ', d.lname) as driverName  from tb_customerbooking as b left outer join tb_customerridedetails as cr on b.bookingId = cr.bookingId "
				+ "join tb_driver d on cr.driverId =  d.driverId "
				+ "where b.customerId = :customerId "+dateFilter+" order by cr.rideStartDate desc" ;
		if(start == 0 && end >0){
			sql+= "LIMIT start, end";
		}
		Query query = getSession().createSQLQuery(sql)
				.setParameter("customerId", customerId);

		if(startDate != null && endDate != null ){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}

		query.setResultTransformer(Transformers.aliasToBean(Booking.class));

		bookingHistory = (List<Booking>)query.list();
		
		return bookingHistory;		
	}
	
	
	@Override
	public List<Booking> getDriverBookingHistory(Integer driverId,
			Integer start, Integer end, Date startDate, Date endDate) {

		List<Booking> bookingHistory = null;
		String dateFilter ="";
		if(startDate != null && endDate != null ){
			dateFilter += " and rideStartDate between :startDate and :endDate";
		}

		String sql = "select b.createDate as createDate, b.bookingId, b.srcPlace, b.destPlace, cr.rideTotalAmt  as rideTotalAmt, "
				+ "concat(d.fname,' ', d.lname) as driverName    from tb_customerbooking as b join tb_customerridedetails as cr on b.bookingId = cr.bookingId "
				+ "join tb_driver d on cr.driverId =  d.driverId "
				+ "where d.driverId = :driverId "+dateFilter+" order by cr.rideStartDate desc" ;
		if(start == 0 && end >0){
			sql+= "LIMIT start, end";
		}
		Query query = getSession().createSQLQuery(sql)
				.setParameter("driverId", driverId);

		if(startDate != null && endDate != null ){
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}

		query.setResultTransformer(Transformers.aliasToBean(Booking.class));

		bookingHistory = (List<Booking>)query.list();
		
		
		return bookingHistory;		
	}

	@Override
	public Booking getBooking(Integer bookingId) {
		return (Booking)getSession().createCriteria(Booking.class).add(Restrictions.eq("bookingId", bookingId)).list().get(0);
	}

	@Override
	public Booking getBookingDetails(Integer bookingId) {
		StringBuilder sql = getBookingMainQuery()
								.append("where b.bookingId = :bookingId") ;
		logger.debug(sql.toString());
		Query query = getSession().createSQLQuery(sql.toString())
				.setParameter("bookingId", bookingId);
		
		query.setResultTransformer(Transformers.aliasToBean(Booking.class));

		Booking booking = (Booking)query.uniqueResult();
				
		return booking;			
		
	}
	
	@Override
	public List<Booking> getTrackBookingList(Integer bookingId) {
		StringBuilder sql = getBookingMainQuery()
								.append("where b.status in (:status1, :status2)") ;
		logger.debug(sql.toString());
		Query query = getSession().createSQLQuery(sql.toString())
				.setParameter("status1", AppConstants.STATUS_STARTED)
				.setParameter("status2", AppConstants.STATUS_CONFIRMED);
		
		query.setResultTransformer(Transformers.aliasToBean(Booking.class));

		List<Booking> bookingList = query.list();
				
		return bookingList;			
		
	}

	private StringBuilder getBookingMainQuery() {
		StringBuilder sb = new StringBuilder( 
		"select b.createDate as createDate, b.bookingId, b.srcPlace, b.destPlace, cr.rideTotalAmt, b.status,   " //b.accountType,
				+ "dr.custRating, dr.driverRating, dv.make, dv.model, dv.registrationId as carNumber, cr.tollCharges, "
				+ "concat(d.fname,' ', d.lname) as driverName, concat(c.fname,' ', c.lname) as customerName, d.driverId, d.phone, "
				+ "cr.baseFare, cr.bookingFee, dr.kmTraveled, cr.kmCharges, dr.waitTime, cr.waitCharges, b.promoId, CASE accountType WHEN 'C' THEN 'Corporate' ELSE 'Personal' END as accountType from tb_customerbooking as b "
				+ "left outer join tb_driverbooking as db on b.bookingId = db.bookingId and db.status in ('BKD', 'STR', 'CON' , 'COM' )"
				+ "left outer join tb_drivertripdetails as dr on b.bookingId =  dr.bookingId "
				+ "left outer join tb_customerridedetails as cr on b.bookingId = cr.bookingId "
				+ "left outer join tb_driver as d on db.driverId =  d.driverId "
				+ "left outer join tb_customer as c on b.customerId = c.customerId "
				+ "left outer join tb_drvehicle as dv on dr.vehicleId =  dv.vehicleId ");
				
		return sb;
	}
	@Override
	public Booking insertCustomerBooking(Booking booking) {
		if(booking.getPromoId()!= null && booking.getPromoId() > 0 ){
//			validatePromoCode(booking.getPromoCode(), booking.getCustomerId())

			PromoApplied pa = new PromoApplied();
			//pa.setPromoValidateId(booking.getPromoId());//TODO
			
			PromoValidate pv = getPromoValidate(booking.getPromoId(),
												booking.getCustomerId());
			if(pv != null){
				pa.setPromoValidateId(pv.getPromoValidateId());
				pa.setBookingId(booking.getBookingId());
				pa.setAppliedDate(new Date());
				applyPromo(pa);
			}else{
				
			}
		}
		if(booking.getPaymentId()== null || booking.getPaymentId() <=0){
			throw new ServiceException("Payment Method is not selected ");
		}
		getSession().persist(booking);
		getSession().flush();
		return booking;
	}

	@Override
	public void updateCustomerBooking(Booking booking) {
		getSession().saveOrUpdate(booking);
	}

	@Override
	public void insertDriverBooking(DriverBooking driverBooking) {
		getSession().saveOrUpdate(driverBooking);
	}
	
	@Override
	public void updateDriverBooking(DriverBooking driverBooking) {
		getSession().merge(driverBooking);
		getSession().flush();
	}
	@Override
	public CustomerRide insertCustomerRide(CustomerRide customerRide) {
		getSession().persist(customerRide);
		getSession().flush();
		return customerRide;
	}
	
	@Override
	public void insertDriverRide(DriverRide driverRide) {
		getSession().saveOrUpdate(driverRide);
	}
	
	@Override
	public void driverReject(DriverBooking driverBooking) {
		getSession().update(driverBooking);
	}

	@Override
	public void saveDrLoc(DrLocation dloc) {
		getSession().saveOrUpdate(dloc);
	}
	
	@Override
	public void deleteDrLoc(DrLocation dloc) {
		getSession().delete(dloc);
	}
	
	@Override
	public List<DrLocation> getCSE(String lat, String lng, String place, String area, Integer radius, String vehicleType) {

		String sql = "SELECT driverId, vehicleType, place, area, lat, lng, ( 3959 * acos( cos( radians(:lat) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(:lng) ) + sin( radians(:lat) ) * sin( radians( lat ) ) ) ) AS distance, gcmRegId FROM tb_drlocation "
		+ " WHERE status = :status   AND lastUpdateDate > :lastUpdate " ;
		if(!StringUtils.isEmpty(vehicleType))
			sql += "AND vehicleType  = :vehicleType ";
		
		sql += "HAVING distance < :radius ORDER BY distance LIMIT 0 , 20 ";	

		//String sql = "select * from tb_drlocation as dl "
		//		+ ""
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		Date currentDate = new Date(cal.getTimeInMillis());

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("status", AppConstants.STATUS_ACTIVE);
		
		query.setParameter("lastUpdate", currentDate);
		query.setParameter("lat", lat);
		query.setParameter("lng", lng);
		query.setParameter("radius", radius);
		if(!StringUtils.isEmpty(vehicleType))
			query.setParameter("vehicleType", vehicleType);
		
		query.setResultTransformer(Transformers.aliasToBean(DrLocation.class));

		List<DrLocation> dList = (List<DrLocation>)query.list();
		
		
		
		/*if(dList.size()>0){
			try {
				List<GoogleDistance> gDist = getExpectedTime( lat, lng,  dList.get(0));
				dList.get(0).setDuration(gDist.get(0).getDuration());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		return dList;			
			
	}

	public Driver getClosestDriver(Integer bookingId) throws Exception{
		return getClosestDriver(getBooking(bookingId));
	}
	
	public Driver getClosestDriver(Booking booking) throws Exception{
		try{
			List<DrLocation>  dList = getCSE(booking.getSourceLatitude(), booking.getSourceLongitude(),
										booking.getSrcPlace(), booking.getSrcArea(), 10, booking.getVehicleType());
			
			logger.debug(dList.size()+"   **********************");
			/*
			DrLocation dl = new DrLocation();
			if(dList.size() >0 ){
				dl =  dList.get(0);
			}*/
			GeoApiContext context = new GeoApiContext().setApiKey(env.getProperty("google.api.key"));
	
			LatLng[] origins = new LatLng[dList.size()];
			LatLng[] destinations = new LatLng[dList.size()];
			
			//Rejected DriverID remove from list
			List<Integer> mapIds = getRejectedDrivers(booking.getBookingId());
			List<DrLocation>  dListNew = new ArrayList<DrLocation>();
			int j =0;
			for (int i = 0; i < dList.size(); i++) {
				DrLocation dl =  dList.get(i);
				if(mapIds.contains(dl.getDriverId())){
					continue;
				}
				
				destinations[j] = new LatLng(Double.valueOf(dl.getLat()), Double.valueOf(dl.getLng()));
				origins[j] = new LatLng(Double.valueOf(booking.getSourceLatitude()), Double.valueOf(booking.getSourceLongitude()));
				dListNew.add(dl);
			//	mapIds.add(dl.getDriverId());
				logger.debug(destinations[j].toString()+"   &&&&&&&&&&&&&&&&");
				j++;
			}
			logger.debug(origins.length+"  ---------------------------------------------------");
			DrLocation drLoc = GoogleServicesWrapper.getClosestDistanceMatrixFromGoogle(context, origins, destinations, dListNew);
			//String[] origins
			if(drLoc == null){
				throw new ServiceException();
			}
			Driver d = getDriver(drLoc.getDriverId());
			d.setVehicleType(drLoc.getVehicleType());
			d.setGcmRegId(drLoc.getGcmRegId());
			/*d.setLat(drLoc.getLat());
			d.setLng(drLoc.getLng());*/
			return d;
		}catch(ServiceException e){
			logger.debug("getClosestDriver  -- No CSE Available");
			throw new ServiceException("No CSE Available"); 
		}
		catch(Exception e){
			
			e.printStackTrace();
			throw new Exception("No CSE Available"); 
			//return null;
		}
	}
	@Override
	public Driver bookCab(Booking booking, boolean nextCSE) {
		
	/*	insertCustomerBooking(booking) ;
		Driver d  = getClosestDriver(booking);
		DriverBooking db = new DriverBooking();
		db.setBookingId(booking.getBookingId());
		db.setDriverId(d.getDriverId());
		
		insertDriverBooking(db) ;
	 */
		if(getPendingInvoices(booking.getCustomerId()).size() >0){
			throw new ServiceException("Customer Cannot book new cab as previous invoice are pendig"); 
		}
		try {
			
			
			booking.setPin(SMSUtil.gerateOTP());
			Booking b = insertCustomerBooking(booking) ;
			DriverBooking db = new DriverBooking();
	
			Driver d;

			//ETA
			d = getClosestDriver(booking);
			if(d == null){
				logger.debug("NO DRIVER ");
				return null;
			}else{
				db.setBookingId(booking.getBookingId());
				db.setDriverId(d.getDriverId());
				//db.setCreateDate(createDate);
				db.setStatus(AppConstants.STATUS_BOOKED);
				
				insertDriverBooking(db);
				
				
				//set driverID in booking object for thread
				b.setDriverId(d.getDriverId());
				//PUSH driver notification
				
				GCMSender gcmSender = new GCMSender(b, d.getGcmRegId(),
						env, this);
			    new Thread(gcmSender).start();
			    
				//gcmSender.sendBookingMessage(b, d.getGcmRegId());
				
				logger.debug(d.getGcmRegId()+"   GCM SEND");
				return d;
			}
		}catch (ServiceException e) {
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Integer cancelBooking(Booking booking) {

		booking = (Booking)getSession().createCriteria(Booking.class).add(Restrictions.eq("bookingId", booking.getBookingId())).uniqueResult();
		
		if(!booking.getStatus().equals(AppConstants.STATUS_STARTED)){
			
			return -1;
		}else if(booking.getStatus().equals(AppConstants.STATUS_CONFIRMED)){
				
			booking.setStatus(AppConstants.STATUS_CANCELLED);
			DriverBooking db = getDriverBooking(booking.getBookingId());
			//if(db.get)
			//TODO
			
			int i = updateBookingStatus(booking, -1);
	
				
		
			return i;
		}else{
			
			return 0;
		}
		
	
		
	}
	
	@Override
	public Integer updateBookingStatus(Booking booking,Integer driverId) {
		updateCustomerBooking(booking.getBookingId(), booking.getStatus());
		
		String dlStatus = AppConstants.STATUS_ACTIVE;
		if(AppConstants.STATUS_CONFIRMED.equals(booking.getStatus())){
			dlStatus = AppConstants.STATUS_RIDING;
		}
		updateDriverLocationStatus(driverId, dlStatus, booking.getBookingId());
		
		return updateDriverBookingStatus(booking, driverId);
	}
	@Override
	public Integer updateDriverBookingStatus(Booking booking, Integer driverId) {
	
		String hqlQuery = "UPDATE DriverBooking set STATUS = :status where bookingId = :bookingId ";
		
		boolean started = false;
		if(driverId > 0){
			hqlQuery  += " AND driverId = :driverId ";
		}
		
		if(!StringUtils.isEmpty(booking.getStatus()) &&  booking.getStatus().equals(AppConstants.STATUS_STARTED)){
			hqlQuery  += "AND bookingId IN (select bookingId from Booking b where b.bookingId = :bookingId2 AND b.pin =:pin)";
			started = true;
		}		
		
		Query query = getSession().createQuery(hqlQuery);
		if(driverId > 0){
			query.setParameter("driverId", driverId);
		}
		
		query.setParameter("status", booking.getStatus());
		query.setParameter("bookingId", booking.getBookingId());
		
		
		if(started){
//			query.setParameter("customerId", booking.getCustomerId());
			//query.setParameter("bookingId", booking.getBookingId());
			query.setParameter("pin", booking.getPin());
			query.setParameter("bookingId2", booking.getBookingId());
		}
		
		int result = query.executeUpdate();
		return result;
	}

	@Override
	public Integer startRide(CustomerRide customerRide, LinkedHashMap<String, LinkedHashMap<String, AttrConfig>>  configMap) {
		
		//driverId & vehicleId from REST Req
		//Booking bookingObjToUpdateSTatus = new Booking();
		//bookingObjToUpdateSTatus.setBookingId(customerRide.getBookingId());
		Booking bookingObj = getBooking(customerRide.getBookingId());		
		bookingObj.setStatus(AppConstants.STATUS_STARTED);
		if(!bookingObj.getPin().equals(customerRide.getPin())){
			throw new InvalidCredentialsException("PIN mismatch");
		}
		
		//bookingObjToUpdateSTatus.setPin(customerRide.getPin());//Set customer PIN
		updateBookingStatus(bookingObj, customerRide.getDriverId());
			
		Date currDt = new Date();
		customerRide.setRideStartDate(currDt);
		customerRide.setCurrency("AUD");//TODO
		try{
			Collection<AttrConfig>  configList = configMap.get("TARIFF").values();
			for (AttrConfig attrConfig : configList) {
				String[] frTime = attrConfig.getAttrValue1().split(":");
				String[] toTime = attrConfig.getAttrValue2().split(":");
				
				if( currDt.getDay() > 5 ){
					if(attrConfig.getAttrName().contains("TARIFF01")){
						continue;
					}else{
						String tarifNm = attrConfig.getAttrName()+"-"+bookingObj.getVehicleType();
						customerRide.setTariffCode(tarifNm);
						customerRide.setBaseFare(new BigDecimal(configMap.get(tarifNm).get("BASE_FARE").getAttrValue1()));
						customerRide.setPerKmFare(new BigDecimal(configMap.get(tarifNm).get("BASE_KM_FARE").getAttrValue1()));
						customerRide.setWaitTimeFare(new BigDecimal(configMap.get(tarifNm).get("BASE_WAIT_CHRG").getAttrValue1()));
//						
						customerRide.setBookingFee(new BigDecimal(configMap.get("FARE").get("BOOKING_FEE").getAttrValue1()));
						//baseWaitTime  = new Long(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_TIME").getAttrValue1());
						break;
					}
					
				}
				
				
				if( (Integer.valueOf(frTime[0]) < Integer.valueOf(toTime[0]) &&
						currDt.getHours() >= Integer.valueOf(frTime[0]) && currDt.getHours() <= Integer.valueOf(toTime[0]))
						||
					(Integer.valueOf(frTime[0]) > Integer.valueOf(toTime[0]) &&	
						currDt.getHours() >= Integer.valueOf(frTime[0]) || currDt.getHours() <= Integer.valueOf(toTime[0]))
						){
					String tarifNm = attrConfig.getAttrName()+"-"+bookingObj.getVehicleType();
					customerRide.setTariffCode(tarifNm);
					customerRide.setBaseFare(new BigDecimal(configMap.get(tarifNm).get("BASE_FARE").getAttrValue1()));
					customerRide.setPerKmFare(new BigDecimal(configMap.get(tarifNm).get("BASE_KM_FARE").getAttrValue1()));
					customerRide.setWaitTimeFare(new BigDecimal(configMap.get(tarifNm).get("BASE_WAIT_CHRG").getAttrValue1()));
//					
					customerRide.setBookingFee(new BigDecimal(configMap.get("FARE").get("BOOKING_FEE").getAttrValue1()));
					//baseWaitTime  = new Long(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_TIME").getAttrValue1());
					break;
				}
				//configMap.get("FARE"+"_"+attrConfig.getAttrName()).get("BASE_FARE").getAttrValue1();
			} 
		}catch(Exception e){
			logger.error(e.getMessage()+"   ----   ");
			customerRide.setBaseFare(new BigDecimal(configMap.get("FARE").get("BASE_FARE").getAttrValue1()));
			customerRide.setPerKmFare(new BigDecimal(configMap.get("FARE").get("BASE_KM_FARE").getAttrValue1()));
			customerRide.setWaitTimeFare(new BigDecimal(configMap.get("FARE").get("BASE_WAIT_CHRG").getAttrValue1()));
			//baseWaitTime  = new Long(configMap.get("FARE").get("BASE_WAIT_TIME").getAttrValue1());
			customerRide.setBookingFee(new BigDecimal(configMap.get("FARE").get("BOOKING_FEE").getAttrValue1()));
			e.printStackTrace();
		}
		customerRide.setPaymentStatus("PEND"); //TOD constants
		customerRide.setCCCharges(new BigDecimal("0")); //TODO TEMPFIX
//		customerRide.setDiscount(discount);
//		customerRide.setOtherCharges(otherCharges);
		
		//customerRide.setBaseFare(new BigDecimal(configMap.get("FARE").get("BASE_FARE").getAttrValue1()));
		
	/*	if(booking.getVehicleType().equals("TXI") || booking.getVehicleType().equals("MTX"))
		{
			
			
		}*/
		//insertDriverRide(driverRide);
		
		//customerRide.setDeletflag(driverRide.getDriverRideId());
		//customerRide;
		
		CustomerRide cr = insertCustomerRide(customerRide);
		return cr.getRideId();

	}
	
	

	@Override
	public PromoValidate getPromoValidate(Integer promoId, Integer customerId) {
	
		String	hql = "from PromoValidate AS pv "
		 		+ "WHERE pv.customerId = :customerId "
		 		+ "AND p.promoId = :promoId "
		 		+ "AND p.expDate > :currDate ";
		 
		Query query = getSession().createQuery(hql)
				.setParameter("customerId", customerId)
				.setParameter("promoId", promoId)
				.setParameter("currDate", new Date());
		PromoValidate pv = (PromoValidate) query.uniqueResult();
		if( pv  != null  ){
			return pv;
		}
		return null;
	}
	@Override
	public PromoApplied getPromoApplied(Integer promoId, Integer bookingId) {
	
		String	hql = "from PromoApplied AS pa "
		 		+ "WHERE pa.bookingId = :bookingId "
		 		+ "AND p.promoId = :promoId "
		 		+ "AND p.appliedDate is not null";
		 
		Query query = getSession().createQuery(hql)
				.setParameter("bookingId", bookingId)
				.setParameter("promoId", promoId);
		PromoApplied pa = (PromoApplied) query.uniqueResult();
		if( pa  != null  ){
			return pa;
		}
		return null;
	}
	
	@Override
	public CustomerRide endJourney(CustomerRide currCustRide, LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap) {

		CustomerRide custRide = (CustomerRide)getSession().get(CustomerRide.class, currCustRide.getRideId());
		
		Booking booking = getBooking(custRide.getBookingId());
		BigDecimal finalRate = new BigDecimal("0");
		DriverRide driverRide  = new DriverRide();
		DriverBooking driverBooking = new DriverBooking();
		DriverEarnings de = new DriverEarnings();
		
		de.setDriverId(custRide.getDriverId());
		
		driverBooking.setBookingId(custRide.getBookingId());
		driverBooking.setDriverId(custRide.getDriverId());

		driverRide.setBookingId(custRide.getBookingId());
		driverRide.setDriverId(custRide.getDriverId());
		
		driverRide.setKmTraveled(currCustRide.getKmTravelled());
		//Date newDate =  
		long totalSecs = currCustRide.getWaitTime();
		long hours = totalSecs / 3600;
		long minutes = (totalSecs % 3600) / 60;
		long seconds = totalSecs % 60;
		
		driverRide.setWaitTime(String.format("%02d:%02d:%02d", hours, minutes, seconds));
		
		//set Dates
		Date currDt = new Date();
		custRide.setRideEndDate(currDt);
		driverBooking.setModifiedDate(currDt);
		booking.setModifiedDate(currDt);
		de.setCreateDate(currDt);
		de.setRideDate(currDt);
		
		//set Status
		booking.setStatus(AppConstants.STATUS_COMPLETE);
		driverBooking.setStatus(AppConstants.STATUS_COMPLETE);
		custRide.setStatus(AppConstants.STATUS_COMPLETE);
		de.setStatus(AppConstants.PAYMENT_PENDING);
		

		/*if(booking.getVehicleType().equals("TXI") || booking.getVehicleType().equals("MTX")){
			//TODO
			
			custRide.setTariffCode(currCustRide.getTariffCode());
			custRide.setRideTotalAmt(currCustRide.getRideTotalAmt());
			custRide.setTollCharges(currCustRide.getTollCharges());
			//custRide.setKm(currCustRide.getTollCharges());
			custRide.setTaxPaid(currCustRide.getTaxPaid());

			driverRide.setKmTraveled(currCustRide.getKmTravelled());

		//	driverRide.setKmTraveled(gd.getDistance());
			
		}else{
		*/
		try{
			GeoApiContext context = new GeoApiContext().setApiKey(env.getProperty("google.api.key"));
			GoogleDistance gd  = GoogleServicesWrapper.getDistanceFromGoogle(context, new LatLng(Double.valueOf(booking.getSourceLatitude()) , Double.valueOf(booking.getSourceLongitude())),
					new LatLng(Double.valueOf(booking.getDestLatitude()) , Double.valueOf(booking.getDestLongitude())));
			
			if(gd.getDistanceUnits().contains("km")){
				driverRide.setKmTraveledGDM(gd.getDistance()) ;
			}else{
				driverRide.setKmTraveledGDM(gd.getDistance()/1000) ;
			}
		}catch(Exception e){
			logger.debug("ERROR in GOOGLE DISTANCE"+e.getMessage());
			
		}
			Long baseWaitTime = 15l, baseWaitUnit = 30l;
			
			
			Date dateNow = new Date();
			
			/* 
			try{
				Collection<AttrConfig>  configList = configMap.get("TARIFF").values();
				for (AttrConfig attrConfig : configList) {
					String[] frTime = attrConfig.getAttrValue1().split(":");
					String[] toTime = attrConfig.getAttrValue2().split(":");
					
					if(Integer.valueOf(frTime[0]) > Integer.valueOf(toTime[0]) && 
							currDt.getHours() < Integer.valueOf(toTime[0])+1 || currDt.getHours() >= Integer.valueOf(frTime[0])
							||
							currDt.getHours() >= Integer.valueOf(frTime[0]) && currDt.getHours() < Integer.valueOf(toTime[0])+1
							){
						gd.setTariffCode(attrConfig.getAttrName());
						//gd.setBaseFare(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_FARE").getAttrValue1()));
						//gd.setKmRate(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_KM_FARE").getAttrValue1()));
						//gd.setWaitCharges(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_CHRG").getAttrValue1()));
	//					gd.setBookingFee(new BigDecimal(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_CHRG").getAttrValue1()));
						baseWaitTime  = new Long(configMap.get(attrConfig.getAttrName()).get("BASE_WAIT_TIME").getAttrValue1());
						baseWaitUnit  = new Long(configMap.get("FARE").get("BASE_WAIT_UNIT").getAttrValue1());
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
				baseWaitUnit  = new Long(configMap.get("FARE").get("BASE_WAIT_UNIT").getAttrValue1());
				e.printStackTrace();
			}
			*/
				
//			gd.setBookingFee(new BigDecimal(configMap.get("FARE").get("BOOKING_FEE").getAttrValue1()));

			baseWaitTime  = new Long(configMap.get(custRide.getTariffCode()).get("BASE_WAIT_TIME").getAttrValue1());
			baseWaitUnit  = new Long(configMap.get(custRide.getTariffCode()).get("BASE_WAIT_UNIT").getAttrValue1());
			
			BigDecimal kmRate = new BigDecimal(0);
			/*if(gd.getDistanceUnits().contains("km")){
				double totalKm = gd.getDistance() - Double.valueOf(configMap.get("FARE").get("BASE_KM").getAttrValue1());
				kmRate = gd.getKmRate()
						.multiply(
								new BigDecimal(totalKm)
								);
			}*/
			Double km = currCustRide.getKmTravelled()/1000;
			driverRide.setKmTraveled(km);
			double baseKm = Double.valueOf(configMap.get("FARE").get("BASE_KM").getAttrValue1());
			if(km > baseKm){
				double totalKm = km - baseKm;
				kmRate = custRide.getPerKmFare().multiply(new BigDecimal(totalKm));
			}
			//Long travelTime = Long.parseLong(custRide.getTravelTime());
			//Long waitTimeForFare = custRide.getWaitTime();// - baseWaitTime;
			//Long waitTimeFareUnits =  waitTimeForFare/baseWaitUnit ;
			BigDecimal waitCharges = custRide.getWaitTimeFare().multiply(new BigDecimal(currCustRide.getWaitTime()).divide(new BigDecimal(baseWaitUnit), 2 , RoundingMode.HALF_UP));
			custRide.setWaitCharges(waitCharges);
			custRide.setKmCharges(kmRate);
			
			de.setBookingFeeTotal(custRide.getBookingFee());
			
			finalRate =   custRide.getBaseFare().add(kmRate).add(waitCharges);
			driverRide.setRideFare(finalRate);
			
			
			de.setTollTotal(new BigDecimal(0));
			custRide.setTollCharges(new BigDecimal(0));
			BigDecimal tollCharges = currCustRide.getTollCharges();
			
			if(tollCharges != null){
				custRide.setTollCharges(tollCharges);
				de.setTollTotal(tollCharges);
				//finalRate = finalRate.add(tollCharges);
			}
			
			
			
			String serviceType = getServiceType(currCustRide.getDriverId());
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+serviceType);
			try{
				BigDecimal voyagerFee = finalRate.multiply(new BigDecimal(configMap.get("FARE").get("UCFEE-"+serviceType.trim()).getAttrValue1())).divide(new BigDecimal(100));//error
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+voyagerFee);
				driverRide.setvoyagerFee(voyagerFee);
				de.setvoyagerFee(voyagerFee);
				de.setDriverEarning(finalRate.subtract(voyagerFee));
				driverRide.setTripEarning(finalRate.subtract(voyagerFee).add(tollCharges));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			//add booking Fee
			
			finalRate = finalRate.add(custRide.getBookingFee());
		
			if(booking.getPromoId() != null){
			Promo p = (Promo)getSession().get(Promo.class, booking.getPromoId());
			if(p.getAllExpDate().before(new Date())){
				if(p.getRewardType().equals("CPN") && p.getRewardType().equals("DISC")){
					custRide.setDiscount(finalRate.multiply(new BigDecimal(p.getRewardAmount()/100)));
				}else if(p.getRewardType().equals("AMOUNT")){
					custRide.setDiscount(new BigDecimal(p.getRewardAmount()));
				}
				//PromoApplied pa =  getPromoApplied(booking.getPromoId(), booking.getBookingId());
				if(custRide.getDiscount() != null){
					finalRate = finalRate.subtract(custRide.getDiscount()); 
				}
			}
			
			}
			
			BigDecimal taxPaid = new BigDecimal(configMap.get("TAX").get("SERVICE").getAttrValue1()).multiply(finalRate).divide(new BigDecimal(100));
			custRide.setTaxPaid(taxPaid);
			finalRate = finalRate.add(taxPaid);
			
			finalRate = finalRate.add(tollCharges);

			
			custRide.setRideTotalAmt(finalRate);
			de.setFareTotal(finalRate);
			custRide.setWaitTime(currCustRide.getWaitTime());
		//}
		
		Double driverReward  = new Double(configMap.get("REWARD").get("DRIVER").getAttrValue1()) * km;
		Double dR = driverReward - driverReward.intValue();
		if(dR >= 0.5)
			driverReward = (double) (driverReward.intValue()+1);
		else
			driverReward = (double) driverReward.intValue();
		driverRide.setDriverRewards(driverReward);
		try{
		String rewardsPoints = sumRewardPoints(currCustRide.getDriverId(),driverReward);
		setRewardsPoints(currCustRide.getDriverId(), rewardsPoints);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		String vehicleId = getVehicleIdFromDrLocation(custRide.getDriverId());
		driverRide.setVehicleId(vehicleId);
	
		System.out.print("Wait time = "+ currCustRide.getWaitTime());
		System.out.print("Kilometer Travelled = "+ currCustRide.getKmTravelled());
		driverRide.setTravelTime(new BigDecimal(currCustRide.getWaitTime()));
		
		String dlStatus = AppConstants.STATUS_ACTIVE;
		updateDriverLocationStatus(driverRide.getDriverId(), dlStatus, booking.getBookingId());
		try{
			endRide(custRide, driverRide);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		updateCustomerBooking(booking);
		updateDriverBooking(driverBooking);
		saveDriverEarning(de);
		
		
		//payment and invoice
		try {
			Integer i = capturePayment(booking.getBookingId(), custRide.getRideId(), booking.getCurrency(), custRide.getRideTotalAmt());
			makeInvoice(booking.getBookingId(), getCustmerProfile(booking.getCustomerId()));
		} catch (Exception e) {
			
			logger.error("Payment failed --- "+e.getMessage());
			try {
				mailSenderPooled.sendEMail("ERROR in COLLECTING PAYMENT booking id --- "+booking.getBookingId(), env.getProperty("voyager.admin.email"), env.getProperty("voyager.support.email"), null, env.getProperty("voyager.admin.email"), "/error", new HashMap());
			} catch (org.springframework.mail.MailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//updateBookingStatus(booking, driverRide.getDriverId());
		//cr.setRideStartDate(bo);

		/**
		 * customerRide object 
		 * get values from mobile km traveled and wait charges or from google
		 * Optimize use ride estimate table in future
		 * calculate total fare
		 * 
		 * 
		 * 
		 */
		
		
		return custRide;
	}
	
	@Override
	public void saveDriverEarning(DriverEarnings de) {

		getSession().saveOrUpdate(de);
		
	}
	private String getVehicleIdFromDrLocation(Integer driverId) {
		String sql = "SELECT vehicleId from tb_drlocation "
				+ " WHERE driverId = :driverId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("driverId", driverId);
		
		
		String vehicleId = query.uniqueResult().toString();
		return vehicleId;
	}
	private void setRewardsPoints(Integer driverId, String rewardsPoints) {
		// TODO Auto-generated method stub
		String hql = "UPDATE Driver set rewardsPoints = :rewardsPoints "  + 
	             "WHERE driverId = :driverId ";
		Query query = getSession().createQuery(hql)
					.setParameter("rewardsPoints",  rewardsPoints)
					.setParameter("driverId", driverId);
		int result = query.executeUpdate();
		
	}
	private String sumRewardPoints(Integer driverId, Double driverReward) {
		String hql = "SELECT SUM(driverRewards) FROM DriverRide "
				+ " WHERE driverId = :driverId";
		Query query = getSession().createQuery(hql);//error
		query.setParameter("driverId", driverId);
		
		//double sumSalary = (Double) query.uniqueResult();
		if(query.uniqueResult()!= null ){
			Double rewPoints = (Double) query.uniqueResult();
			driverReward = driverReward + rewPoints;
			String dReward = driverReward.toString();
			return dReward;
		}else{
			return driverReward.toString();
		}
	}
	private String getServiceType(Integer driverId) {
		String sql = "SELECT serviceType from tb_driver "
				+ " WHERE driverId = :driverId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("driverId", driverId);
		String serviceType = query.uniqueResult().toString();
		return serviceType;
	}
	
	
	@Override
	public void endRide(CustomerRide customerRide, DriverRide driverRide) {
		getSession().merge(customerRide);
		getSession().merge(driverRide);
		getSession().flush();
	}
	
	@Override
	public void addEnquiry(EnquiryForm enq) {
		// TODO Auto-generated method stub
		getSession().saveOrUpdate(enq);
	}
	
	@Override
	public List<DrVehicle> getDrVehicle(Integer driverId) {
		// TODO Auto-generated method stub

		String	hql = "from DrVehicle "
		 		+ "WHERE driverId= :driverId ";
		Query query = getSession().createQuery(hql)
			.setParameter("driverId", driverId);
		return query.list();
	}




	@Override
	public DrVehicle getDrVehicleDetails(Integer vehicleId) {
		// TODO Auto-generated method stub

		String	hql = "from DrVehicle "
		 		+ "WHERE vehicleId = :vehicleId ";
		Query query = getSession().createQuery(hql)
			.setParameter("vehicleId", vehicleId);
		
		return (DrVehicle)query.uniqueResult();
	}
	@Override
	public Integer checkCse(String email, Long phone) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT driverId from tb_driver "
				+ " WHERE email = :email and phone = :phone";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("email", email);
		query.setParameter("phone", phone);
		
		
		Integer driverId = (Integer) query.uniqueResult();
		return driverId;
		//return (Driver) getSession().createCriteria(Driver.class).add(Restrictions.eq("email", email)).add(Restrictions.eq("phone", phone)).list().get(0);
		
	}
	
	public Integer checkEmail(String email) {
		// TODO Auto-generated method stub
		
		String sql = "SELECT driverId from tb_driver "
				+ " WHERE email = :email ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("email", email);
		
		
		Integer driverId = (Integer) query.uniqueResult();
		return driverId;
	}
	@Override
	public void setPassword(Integer driverId, String password) {
		// TODO Auto-generated method stub
		String hql = "UPDATE Driver set password = :password "  + 
	             "WHERE driverId = :driverId ";
		Query query = getSession().createQuery(hql)
					.setParameter("password",  password)
					.setParameter("driverId", driverId);
		int result = query.executeUpdate();
	}
	
	@Override
	public void saveDEmargancyContact(EmergancyContact ec) {
		getSession().saveOrUpdate(ec);
		
	}
	
	@Override
	public List<EmergancyContact> getDEmargancyContact(Integer driverId) {
		return getSession().createCriteria(EmergancyContact.class)
				.add(Restrictions.eq("driverId", driverId))
				.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE))
				.addOrder(Order.asc("contactId")).list();
		
	}
	
	@Override
	public void delDEmergancyContact(Integer driverId, Integer contactId) {
		String hql = "DELETE from EmergancyContact "  + 
	             "WHERE driverId = :driverId and contactId= :contactId";
		
		Query query = getSession().createQuery(hql)
					.setParameter("driverId",  driverId)
						.setParameter("contactId", contactId);
		
		Integer d = query.executeUpdate();
	}
	@Override
	public List<Integer> getRejectedDrivers(Integer bookingId) {
		String hql = "select driverId from DriverBooking "  + 
	             "WHERE bookingId = :bookingId AND status IN (:statusCancel, :statusReject)";
		
		Query query = getSession().createQuery(hql)
					.setParameter("bookingId",  bookingId)
						.setParameter("statusCancel", AppConstants.STATUS_CANCELLED)
						.setParameter("statusReject", AppConstants.STATUS_REJECT);
		
		List<Integer> driverIdList = query.list();
		
		return driverIdList;
	}
	
	@Override
	public Integer capturePayment(Integer bookingId, Integer rideId, String currency, BigDecimal tobePaidAmount) {
		
		String hql = "from Payment p "  + 
	             "WHERE paymentId  = (select paymentId from Booking where bookingId = :bookingId "
	             + "and status = :status )";
		
		Query query = getSession().createQuery(hql)
					.setParameter("bookingId",  bookingId)
						.setParameter("status", AppConstants.STATUS_CONFIRMED);
		
		CustomerRide cr = getCustomerRide(rideId);
		Payment payment = (Payment)query.list().get(0);
		
		BigDecimal amtToBePaid = cr.getRideTotalAmt();
		if(null != tobePaidAmount ){
			amtToBePaid = tobePaidAmount;
		}
		if(payment.getGatewayId().equalsIgnoreCase(AppConstants.GATEWAY_PAYWAY)){
			PayWayAPIUtil payWayAPI = new PayWayAPIUtil();
			payWayAPI.capture(payment, amtToBePaid, currency, bookingId.toString());
		}else if(payment.getGatewayId().equalsIgnoreCase(AppConstants.GATEWAY_PAYPAL)){
			PaypalFuturePaymentUtils payPalUtils = new PaypalFuturePaymentUtils();
			String jsonTocken = payment.getGatewayToken();
			ObjectMapper map = new ObjectMapper();
			JsonNode actualObj = null;
			try {
				actualObj = map.readTree(jsonTocken);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			String authCode = actualObj.get("response").get("code").toString();
			try {
				Integer p = payPalUtils.capture(bookingId.toString(), authCode, amtToBePaid , currency, ""+bookingId);
				//paypalPayment.execute()
				
			} catch (PayPalRESTException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 1;
	}
	@Override
	public void getSendInvoice(Integer rideId) {
		CustomerRide cr = getCustomerRide(rideId);
		
	}

	@Override
	public CustomerRide getCustomerRide(Integer rideId) {
		List<CustomerRide> crList = getSession().createCriteria(Driver.class)		
				.add(Restrictions.eq("rideId", rideId)).add(Restrictions.eq("paymentStatus", "PEND")).add(Restrictions.eq("status", AppConstants.STATUS_CONFIRMED)).list();
		
		if(crList.size() >0){
			return crList.get(0);
		}else{
			return null;
		}

	}
	@Override
	public CustomerRide getCustomerRideFromBooking(Integer bookingId) {
		// TODO Auto-generated method stub
		return null;
	}
	
		@Override
		public void saveRatingByDriver(DriverRide dtd) {
			
				String hql1 = "UPDATE DriverRide set custRating = :custRating, feedback = :feedback  "  + 
			             "WHERE bookingId = :bookingId ";
				Query query1 = getSession().createQuery(hql1)
							.setParameter("custRating", dtd.getCustRating())
							.setParameter("feedback", dtd.getFeedback())
							.setParameter("bookingId", dtd.getBookingId());
				int result0= query1.executeUpdate();
				
			//getSession().saveOrUpdate(dtd);
		}
		
		@Override
		public void saveRatingByCustomer(DriverRide dtd) {
			
				String hql1 = "UPDATE DriverRide set driverRating = :driverRating, feedback = :feedback  "  + 
			             "WHERE bookingId = :bookingId ";
				Query query1 = getSession().createQuery(hql1)
							.setParameter("driverRating", dtd.getDriverRating())
							.setParameter("feedback", dtd.getFeedback())
							.setParameter("bookingId", dtd.getBookingId());
				int result0= query1.executeUpdate();
				
			//getSession().saveOrUpdate(dtd);
		}
		
	@Override
	public Integer changeDefAccount(Integer customerId, String accountType) {
		
			String hql1 = "UPDATE CustomerAccount set defaultAccount = 0 "  + 
		             "WHERE customerId = :customerId and accountType <> :accountType ";
			Query query1 = getSession().createQuery(hql1)
						.setParameter("customerId", customerId)
						.setParameter("accountType", accountType);
			int result0= query1.executeUpdate();
		
		String hql = "UPDATE CustomerAccount set defaultAccount = 1 "  + 
	             "WHERE customerId = :customerId and accountType = :accountType ";
		Query query = getSession().createQuery(hql)
					.setParameter("customerId", customerId)
					.setParameter("accountType", accountType);
		int result = query.executeUpdate();
		return result;
	}
	@Override
	public Integer getRembursed(Integer corpCustId) {
		String sql = "SELECT rembursed from tb_corporatecustomer "
				+ " WHERE corpCustId = :corpCustId ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpCustId", corpCustId);
		
		
		Integer rembursed = Integer.parseInt(query.uniqueResult().toString());
		return rembursed;
	}
	
	@Override
	public Integer verifyEmailPin(String email, String pin) {
		String sql = "SELECT corpCustId from tb_corpemp "
				+ " WHERE email = :email and pin = :pin ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("email", email);
		query.setParameter("pin", pin);
		
		
		Integer corpCustId = Integer.parseInt(query.uniqueResult().toString());
		return corpCustId;
	}
	@Override
	public DriverBooking getDriverBooking(Integer bookingId, Integer status) {
		List<DriverBooking> drList = getSession().createCriteria(DriverBooking.class)		
				.add(Restrictions.eq("bookingId", bookingId)).add(Restrictions.eq("status", status)).list();
		
		if(drList.size() >0){
			return drList.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public DriverBooking getDriverBooking(Integer bookingId) {
		List<DriverBooking> drList = getSession().createCriteria(DriverBooking.class)		
				.add(Restrictions.eq("bookingId", bookingId)).add(Restrictions.eq("status", AppConstants.STATUS_BOOKED)).list();
		
		if(drList.size() >0){
			return drList.get(0);
		}else{
			return null;
		}
	}
	@Override
	public Driver getDriverFromBooking(Integer bookingId) {
		//String sql = "select d.driverId, d.phone, d.photo, d.fname, d.lname, d.countryCode ,dl.gcmRegId,dl.vehicleType,dl.serviceType, dl.vehicleId from tb_driver d join tb_drlocation dl on dl.driverId = d.driverId where d.driverId  = (select driverId from tb_driverbooking db where db.status = :status and db.bookingId = :bookingId )";
		
		
		String sql = "from Driver d where d.driverId  = (select driverId from DriverBooking db where db.status = :status and db.bookingId = :bookingId )";
		Query  query = getSession().createQuery(sql);
		query.setParameter("status", AppConstants.STATUS_BOOKED)
		.setParameter("bookingId", bookingId);		
		Driver d = (Driver)query.uniqueResult();

		String sql2 = "from DrLocation d where d.driverId  = :driverId ";
		Query  query2 = getSession().createQuery(sql2)
			.setParameter("driverId", d.getDriverId());
		
		DrLocation dl = (DrLocation)query2.uniqueResult();
		
		d.setServiceType(dl.getServiceType());
		d.setVehicleId(dl.getVehicleId());
		d.setGcmRegId(dl.getGcmRegId());
		
		//query.setResultTransformer(Transformers.aliasToBean(Driver.class));
/*		  query
			.addScalar("phone", LongType.INSTANCE)
			.addScalar("driverId", IntegerType.INSTANCE)
			.addScalar("photo", StringType.INSTANCE)
			.addScalar("fname", StringType.INSTANCE)
			.addScalar("lname", StringType.INSTANCE)
			.addScalar("countryCode", IntegerType.INSTANCE)
			.addScalar("gcmRegId", StringType.INSTANCE)
			.addScalar("vehicleType", StringType.INSTANCE)
			.addScalar("serviceType", StringType.INSTANCE)
			.addScalar("vehicleId", IntegerType.INSTANCE)
*/			
		
		if(d != null){
			return d;
		}else{
			return null;
		}
	}
	
	
	@Override
	public Map<String,Object> acceptBooking(DriverBooking db) {
		
		Customer c = customerFromBooking(db.getBookingId());
		Map<String,String> paramMap = new HashMap<>();
		
		Driver d = getDriverFromBooking(db.getBookingId());
		DrVehicle v  = getDrVehicleDetails(d.getVehicleId());

		db.setStatus(AppConstants.STATUS_ACCEPT);
		db.setModifiedDate(new Date());
		
		//updateDriverBooking(db);
		//updateCustomerBooking(db.getBookingId(), voyagerConstants.STATUS_CONFIRMED);
		//updateDriverLocationStatus(db.getDriverId(),voyagerConstants.STATUS_RIDING,-1); 
		Booking b = getBooking(db.getBookingId());
		b.setStatus(AppConstants.STATUS_CONFIRMED);
		if(checkIfBookingCancelled(b.getBookingId())){
			throw new ServiceException("Booking cancelled");
		}
		updateBookingStatus(b, db.getDriverId());
		
		paramMap.put("driver", d.getFname()+" "+d.getLname());
		paramMap.put("driverPhone", d.getCountryCode()+d.getPhone().toString());
		paramMap.put("vehicleType",  v.getvType());
		paramMap.put("carNumber",  v.getRegistrationId());
		paramMap.put("pin",  b.getPin());
		//paramMap.put("carNumber",  v.getRegistrationId());
		
	
		SMSUtil sms= new SMSUtil();
		String msg = sms.getMessageTemplate(env.getProperty("SMS.tpl.booking.accept"),  paramMap, env);
		sms.sendSMS(msg, c.getMobile(), env);
		
		b.setPin(paramMap.get("pin"));
		b.setDriverName(paramMap.get("driver"));
		b.setPhone(new BigInteger(paramMap.get("driverPhone")));
		b.setVehicleType(paramMap.get("vehicleType"));
		b.setCarNumber(v.getMake()+" "+v.getModel()+" "+v.getRegistrationId());
		
		GCMSender gcm = new GCMSender(b, c.getGcmRegId(), env, this);
		gcm.sendCustomerMessage(msg, c.getGcmRegId());
		
		Map<String,Object>  newmap = new HashMap<String, Object>(); 
		newmap.put("booking", b);
		newmap.put("customer", c);
		
		return newmap;
	}
	
	public boolean checkIfBookingCancelled(Integer bookingId) {
		Query query = getSession().createQuery("select count(bookingId) from Booking where status = :status")
				.setParameter("status", AppConstants.STATUS_CANCELLED);
		if(query.uniqueResult()!= null && ((Long)query.uniqueResult()) > 0 ){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public void updateDriverLocationStatus(Integer driverId, String statusRiding,Integer bookingId) {

		String hql = "UPDATE DrLocation set status = :status "  ;
		if(driverId > -1){
			hql += "WHERE driverId = :driverId ";
		}else{
			hql += "WHERE driverId in (select driverId from DriverBooking where bookingId =:bookingId )";
		}
		Query query = getSession().createQuery(hql)
					.setParameter("status", statusRiding);
					if(driverId > -1){
						query.setParameter("driverId", driverId);
					}else{
						query.setParameter("bookingId", bookingId);
					}
		int result = query.executeUpdate();
		
	}
	@Override
	public Integer rejectBooking(DriverBooking db) {
		//getSession().createCriteria(DriverBooking.class).add(Restrictions.eq("dr", vehicleId));
		
		db.setStatus(AppConstants.STATUS_REJECT);
		db.setModifiedDate(new Date());
		updateDriverBooking(db);
		return nextAvailableDriverBooking(db.getBookingId());
		
	}
	@Override
	public Integer nextAvailableDriverBooking(Integer bookingId) {

		Booking booking = getBooking(bookingId);
		Driver d;
		try {
			d = getClosestDriver(booking);
			if(d == null){
				return 0;
			}else{
				DriverBooking db = new DriverBooking();
				db.setBookingId(booking.getBookingId());
				db.setDriverId(d.getDriverId());
				//db.setCreateDate(createDate);
				db.setStatus(AppConstants.STATUS_BOOKED);
				insertDriverBooking(db);
				//PUSH driver notification
				GCMSender gcmSender = new GCMSender(booking, d.getGcmRegId(),
								env, this);
			    new Thread(gcmSender).start();
			    
				//gcmSender.sendBookingMessage(booking, d.getGcmRegId());
				
				return 1;
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Customer customerFromBooking(Integer bookingId) {
		
		String sql = "from Customer c where customerId  = "
				+ "(select customerId from Booking cb where cb.bookingId = :bookingId)";
		Query query = getSession().createQuery(sql)
				.setParameter("bookingId", bookingId);		
		
		//query.list();
	
		Customer customer = (Customer)query.list().get(0);
		return customer;
		
	}
	
	@Override
	public DrVehicle saveDrVehicle(DrVehicle dv) {
		String hql = "UPDATE tb_drvehicle set defaultCar = 0 where driverId = :driverId ";
		Query query = getSession().createQuery(hql)
					.setParameter("driverId",  dv.getDriverId());
	
		int result = query.executeUpdate();
		hql = "UPDATE tb_drvehicle set defaultCar = 1 where vehicleId = :vehicleId ";
		query = getSession().createQuery(hql)
					.setParameter("vehicleId",  dv.getVehicleId());
	
		result = query.executeUpdate();
	
		return getDrVehicleDetail(dv.getVehicleId());
	}
	
	@Override
	public DrVehicle getDrVehicleDetail(Integer vehicleId) {
		return (DrVehicle) getSession().createCriteria(DrVehicle.class).add(Restrictions.eq("vehicleId", vehicleId)).uniqueResult();
		
	}
	
	@Override
	public Integer driverRejectIfdNotAccept(Integer driverId, Integer bookingId, String status) {
		String hql = "UPDATE DriverBooking set status = :status where bookingId = :bookingId AND driverId = :driverId AND status != :currStatus";
		Query query = getSession().createQuery(hql)
					.setParameter("status",  AppConstants.STATUS_REJECT)
					.setParameter("bookingId", bookingId)
					.setParameter("driverId", driverId)
					.setParameter("currStatus", AppConstants.STATUS_ACCEPT);
		
	
		Integer result = query.executeUpdate();
		return result;
	}
	@Override
	public void bookingTimeOut(Booking b) {
		Integer i = driverRejectIfdNotAccept(b.getDriverId(), b.getBookingId(), AppConstants.STATUS_REJECT);;
		i = updateCustomerBooking(b.getBookingId(), AppConstants.STATUS_NOT_AVAILABLE);
		/*if(i == 1){
			nextAvailableDriverBooking(b.getBookingId());
		}*/
	}
	
	@Override
	public Integer updateCustomerBooking(Integer bookingId, String status) {
		String hql = "UPDATE Booking set status = :status, modifiedDate = :modifiedDate  where bookingId = :bookingId " ;
		
		
		 if(AppConstants.STATUS_STARTED.equals(status)){
			 	hql += " AND status not in (:s1)";
			}else {
				hql += " AND status = (:s1)";
			}
		
		Query query = getSession().createQuery(hql)
					.setParameter("status",  status)
					.setParameter("bookingId", bookingId)
					.setParameter("modifiedDate", new Date());
		if(AppConstants.STATUS_STARTED.equals(status)){
			query.setParameter("s1", AppConstants.STATUS_CANCELLED);
		}else{
			query.setParameter("s1", AppConstants.STATUS_BOOKED);
		}
		
		/*if(voyagerConstants.STATUS_NOT_AVAILABLE.equals(status)){
			query.setParameter("s1", voyagerConstants.STATUS_BOOKED);
		}*/
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public String getMobile(String email) {
		String sql = "SELECT mobile from tb_registration "
				+ " WHERE email = :email ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("email", email);		
		
		String mobile = (String) query.uniqueResult();
		return mobile;
	}
	@Override
	public void updateDriverLocation(DrLocation dloc) {

		String hql = "UPDATE DrLocation set lat = :lat, lng = :lng, lastUpdateDate = :lastUpdateDate  where driverId = :driverId " ;
		
		Query query = getSession().createQuery(hql)
					.setParameter("lat",  dloc.getLat())
					.setParameter("lng", dloc.getLng())
					.setParameter("lastUpdateDate", new Date())
					.setParameter("driverId", dloc.getDriverId());
		
		Integer result = query.executeUpdate();
	}
	
	@Override
	public void saveDriverLogin(DriverLogin driverLogin) {

		getSession().saveOrUpdate(driverLogin);
	}
	
	@Override
	public void deleteDriverLogin(Integer driverId) {

		String hql = "DELETE from DriverLogin "  + 
	             "WHERE driverId = :driverId";
		
		Query query = getSession().createQuery(hql)
					.setParameter("driverId",  driverId);
		
		Integer d = query.executeUpdate();

	}

	@Override
	public List<Booking> startupCheck(Integer customerId) {
		
		List<Booking> bookingHistory = null;
		String dateFilter ="";

		String sql = "select b.createDate as createDate, b.bookingId, b.srcPlace, b.destPlace, cr.rideTotalAmt  as rideTotalAmt, "
				+ "concat(d.fname,' ', d.lname) as driverName, cr.currency    "
				+ "from tb_customerbooking as b join tb_customerridedetails as cr on b.bookingId = cr.bookingId "
				+ "join tb_drivertripdetails dr on dr.bookingId = cr.bookingId "
				+ "join tb_driver d on dr.driverId =  d.driverId "
				+ "where b.customerId = :customerId  and cr.status = :status and dr.driverRating is null order by cr.rideStartDate desc" ;
		Query query = getSession().createSQLQuery(sql)
				.setParameter("customerId",  customerId)
				.setParameter("status",  AppConstants.STATUS_COMPLETE);
	
		query.setResultTransformer(Transformers.aliasToBean(Booking.class));
		bookingHistory = (List<Booking>)query.list();
		
		return bookingHistory;		
	}
	@Override
	public DrLocation getDrLocation(Integer driverId) {
		// TODO Auto-generated method stub
		return (DrLocation) getSession().createCriteria(DrLocation.class).add(Restrictions.eq("driverId", driverId)).list().get(0);
	}
	
	@Override
	public Booking bookingDetails(Integer bookingId) {
		String sql = "select b.bookingId, dr.custRating, dv.model, dv.carNumber, b.srcPlace, b.destPlace, cr.rideTotalAmt, b.status, "
				 + "concat(d.fname,' ', d.lname) as driverName, cr.tollCharges, cr.baseFare, cr.bookingFee, dr.kmTraveled, cr.kmCharges, cr.waitTime, cr.waitCharges, b.promoId "
				  + "from tb_customerbooking as b, tb_driverbooking as db, tb_customerridedetails as cr, tb_driver as d, tb_drlocation dl, tb_drivertripdetails as dr, tb_drvehicle as dv "
				 + "where b.bookingId = db.bookingId and b.bookingId = cr.bookingId and db.driverId = d.driverId and "
				  + "b.bookingId = dr.bookingId and dl.driverId = db.driverId and dl.vehicleId = dv.vehicleId and "
					+"and b.bookingId = :bookingId";
		logger.debug(sql.toString());
		Query query = getSession().createSQLQuery(sql.toString())
				.setParameter("bookingId", bookingId);
		
		query.setResultTransformer(Transformers.aliasToBean(Booking.class));

		Booking booking = (Booking)query.uniqueResult();
				
		return booking;			
		
	}
	@Override
	public Integer updateOTPForgotPass(Customer c) {
		// TODO Auto-generated method stub
		String hql = "UPDATE Customer set pin = :OTP, pinDate = :OTPDate "  + 
	             "WHERE email = :email ";
		Query query = getSession().createQuery(hql)
					.setParameter("OTP",  c.getPin())
					.setParameter("OTPDate", c.getPinDate())
						.setParameter("email", c.getEmail());
		int result = query.executeUpdate();
		
		//getCustmerProfileFromEmail(reg.getEmail());
		
		return result;
	}

	@Override
	public Customer verifyOTPForgotPass(Customer c) {
		String hql = " from Customer "  + 
	             "WHERE email = :email and status  :status";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date currentDate = new Date(cal.getTimeInMillis());
		Query query = getSession().createQuery(hql)
								.setParameter("email",  c.getEmail())
								.setParameter("status",  c.getStatus());

		Customer customer =  (Customer)query.uniqueResult();
		if (customer  != null){
			System.out.println("OTP --"+customer .getPin()+" and existing + "+customer.getPin()+"  -- Date compare "+(currentDate.before(customer.getPinDate())+"   date-- > "+currentDate.toString()));
		}
		
		if (customer != null && (customer.getPin().equals(c.getPin()) && currentDate.before(customer.getPinDate()))){
			System.out.println("OTP --"+customer.getPin()+" and existing + "+c.getPin()+"  -- Date compare "+(currentDate.before(customer.getPinDate())+"   date-- > "+currentDate.toString()));
			return customer;
		}else{
			return null;
		}
	}
		

		@Override
		public void makeInvoice(Integer bookingId, Customer c){
			
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAylyhXnYJvyZN5itYNMv4IkmW0ydeFVDs");
			GoogleDistance gd = new GoogleDistance();
			GoogleServicesWrapper gsw = new GoogleServicesWrapper();
			
			//Integer bookingId = 2;
			Booking b = new Booking();
			b = bookingIn(bookingId);
			//LatLng origins = new LatLng(Double.parseDouble(b.getSourceLatitude()),Double.parseDouble(b.getSourceLongitude()));
			//LatLng destinations = new LatLng(Double.parseDouble(b.getDestLatitude()),Double.parseDouble(b.getDestLongitude()));
			LatLng origins = new LatLng(18.981474,72.827067);
			LatLng destinations = new LatLng(19.038481,72.858242);
			
			String path = gsw.getDirections(context, origins, destinations);
			System.out.println(path);
			String link = "https://maps.googleapis.com/maps/api/staticmap?size=600x600&maptype=roadmap&path=enc:"+path+"&key=AIzaSyCwiY_bkEJXNAmYcsNkG0Cfkxh7gyvhDnc";
			System.out.println(link);
			HashMap<String, String> map = new HashMap<String, String>();
			 map.put("link", link);
			 map.put("bookingFee", b.getBookingFee().toString());
			 map.put("baseFare", b.getBaseFare().toString());
			 map.put("distanceFare", b.getKmCharges().toString());
			 map.put("timeFare", b.getWaitCharges().toString());
			 map.put("tollCharges", b.getTollCharges().toString());
			 map.put("totalFare", b.getRideTotalAmt().toString());
			 try {
					mailSenderPooled.sendEMail(env.getProperty("voyager.invoice.subject"), env.getProperty("voyager.admin.email"), c.getEmail(), "", env.getProperty("voyager.admin.email"), "/invoice.tpl", map);
				} catch (MailException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (org.springframework.mail.MailException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
		}

		public Booking bookingIn(Integer bookingId) {
			String sql = "select b.bookingId, b.srcPlace, b.destPlace, b.sourceLatitude, b.sourceLongitude, b.destLatitude, b.destLongitude, cr.rideTotalAmt, "
					 + "cr.tollCharges, cr.baseFare, cr.bookingFee, dr.kmTraveled, cr.kmCharges, cr.waitCharges, b.promoId "
					  + "from tb_customerbooking as b, tb_customerridedetails as cr, tb_drivertripdetails as dr "
					 + "where b.bookingId = cr.bookingId and b.bookingId = dr.bookingId and b.bookingId = :bookingId";
			logger.debug(sql.toString());
			
			
			Query query = getSession().createSQLQuery(sql.toString())
					.setParameter("bookingId", bookingId);
			
			query.setResultTransformer(Transformers.aliasToBean(Booking.class));

			Booking booking = (Booking)query.uniqueResult();
					
			return booking;			
			
		}

		public List<Notification> getNotiifications() {
		
			List<Notification> notList = getSession().createCriteria(Notification.class)		
					.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE)).add(Restrictions.le("notifyEndDate", new Date())).list();
			
			return notList;
		}
		
		
		@Override
		public List<DrVehicle> getDrVehicleList(Driver d) {
			List<DrVehicle> dvList = getSession().createCriteria(DrVehicle.class)		
					.add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE)).add(Restrictions.eq("driverId", d.getDriverId())).list();
			
			return dvList;
		}
		
		
		public List<GoogleDistance> getExpectedTime(String lat, String lng, DrLocation dl ) throws Exception{
			try{

				GeoApiContext context = new GeoApiContext().setApiKey(env.getProperty("google.api.key"));
		
				LatLng[] origins = new LatLng[1];
				LatLng[] destinations = new LatLng[1];
				
				destinations[0] = new LatLng(Double.valueOf(dl.getLat()), Double.valueOf(dl.getLng()));
				origins[0] = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

				List<GoogleDistance> drLocList = GoogleServicesWrapper.getDistanceMatrixFromGoogle(context, origins, destinations);
					if(drLocList  == null){
						throw new ServiceException();
					}
				return drLocList;
			}catch(ServiceException e){
				logger.debug("getExpectedTime  -- No CSE Available");
				throw new ServiceException("No CSE Available"); 
			}
			catch(Exception e){
				
				e.printStackTrace();
				throw new Exception("No CSE Available"); 
				//return null;
			}
		}
		
		public List<CustomerRide> getPendingInvoices(Integer customerId) {
			
			List<CustomerRide> pendingPaymentList = getSession().createCriteria(CustomerRide.class)		
					.add(Restrictions.eq("deleteflag", AppConstants.DELETEFLAG_ALIVE ))
							.add(Restrictions.eq("paymentStatus", AppConstants.PAYMENT_PENDING )).add(Restrictions.eq("status", AppConstants.STATUS_COMPLETE)).list();
			
			return pendingPaymentList;
		}


		@Override
		public Booking startupCheckDriver(Integer driverId) {
			
		/*	//List<Booking> bookingHistory = null;
			String dateFilter ="";
			String sql = "from DrLocation d where d.driverId  = :driverId ";
			Query  query = getSession().createQuery(sql)
				.setParameter("driverId", driverId);
			
			DrLocation dl = (DrLocation)query.uniqueResult();
		*/	
		//	if(dl.getStatus().equals("R")){
				String sql2 = "from Booking b where b.driverId  = :driverId and createDate = (select max(createDate) from DriverBooking db where db.driverId = b.driverId and db.status = :status) ";
				Query  query2 = getSession().createQuery(sql2)
					.setParameter("driverId", driverId)
				.setParameter("status", AppConstants.STATUS_REJECTED);
				
				Booking db = (Booking)query2.uniqueResult();
				
				return db;		
				
			/*}else{
				
				return null;
			}
			*/
			
		}

		@Override
		public Integer captureJourneyPayment(Integer bookingId) {
		
			Booking booking = getBooking(bookingId);
			CustomerRide cRide = getCustomerRideFromBooking(bookingId);
			return capturePayment(bookingId, cRide.getRideId(), cRide.getCurrency(), cRide.getRideTotalAmt());
		
		}
}


