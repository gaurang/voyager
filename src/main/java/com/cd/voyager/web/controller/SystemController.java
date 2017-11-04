package com.cd.voyager.web.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.common.util.MD5Utils;
import com.cd.voyager.common.util.StringGenUtils;
import com.cd.voyager.email.MailSender;
import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.BankDetails;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.CorpEmp;
import com.cd.voyager.entities.CorporateCustomer;
import com.cd.voyager.entities.CustomerReward;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverDetails;
import com.cd.voyager.entities.DriverEarnings;
import com.cd.voyager.entities.EnquiryForm;
import com.cd.voyager.entities.EquipDriver;
import com.cd.voyager.entities.Equipment;
import com.cd.voyager.entities.SupportMaster;
import com.cd.voyager.entities.Users;
import com.cd.voyager.mobility.data.UserDetailSession;
import com.cd.voyager.web.formWrappers.CustomerForm;
import com.cd.voyager.web.formWrappers.DriverForm;
import com.cd.voyager.web.service.IMainService;

@Controller
@Scope("session")
@RequestMapping("/admin")
public class SystemController {

	private final Logger logger = LoggerFactory.getLogger(SystemController.class);
	private final IMainService mainServiceImpl;
	// private final MailSender mailSender;

	@Autowired
	private ApplicationContext context;

	@Autowired
	public SystemController(IMainService mainServiceImpl) {
		this.mainServiceImpl = mainServiceImpl;
	}

	@Autowired
	private Environment env;

	@Autowired
	private MailSenderPooled mailSenderPooled;

	/*
	 * @RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	 * public ModelAndView hello(@PathVariable("name") String name) {
	 * 
	 * logger.debug("hello() is executed - $name {}", name);
	 * 
	 * ModelAndView model = new ModelAndView(); model.setViewName("index");
	 * 
	 * model.addObject("title", mainServiceImpl.getTitle(name));
	 * model.addObject("msg", mainServiceImpl.getDesc());
	 * 
	 * return model;
	 * 
	 * }
	 * 
	 */

	/**
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView login(Map<String, Object> model, Principal principal) {
		logger.debug("login() is executed!");
		if (principal != null)
			return new ModelAndView(new RedirectView("dashboard"));
		else
			return new ModelAndView(new RedirectView("../index"));
	}

	/**
	 * dashboard
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard(HttpServletRequest request) {

		logger.debug("driver() loaded");

		HttpSession session = request.getSession(true);

		Integer zoneId = Integer.parseInt(context.getMessage("default.zone", null, Locale.getDefault()));

		session.setAttribute("configMap", mainServiceImpl.getGlobalConfig(zoneId));

		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard");

		model.addObject("ridesDashboard", mainServiceImpl.getDashboard());
		model.addObject("cseCount", mainServiceImpl.getCSECount());
		model.addObject("custCount", mainServiceImpl.getCustCount());
		model.addObject("activeCse", mainServiceImpl.getActiveCse());

		
		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addDriver", method = RequestMethod.GET)
	public ModelAndView addDriver() {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("addDriver");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		DriverForm driverForm = new DriverForm();
		model.addObject("driverForm", driverForm);

		return model;

	}

	/**
	 * 
	 * @param df
	 * @param request
	 * @param imageFile
	 * @param licenseFile
	 * @param driverAuthFile
	 * @param insuranceFile
	 * @param registrationFilePath
	 * @param insuranceFilePath
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/saveAddDriver", method = RequestMethod.POST)
	public ModelAndView saveAddDriver(@ModelAttribute DriverForm df, HttpServletRequest request,
			@RequestParam("imageFile") MultipartFile imageFile, @RequestParam("licenseFile") MultipartFile licenseFile,
			@RequestParam("driverAuthFile") MultipartFile driverAuthFile,
			@RequestParam("insuranceFile") MultipartFile insuranceFile,
			@RequestParam("registrationFilePath") MultipartFile registrationFilePath,
			@RequestParam("insuranceFilePath") MultipartFile insuranceFilePath) throws IOException {

		logger.debug("driver() loaded");

		/*
		 * model.setViewName("listDrivers");
		 * 
		 * model.addObject("title", mainServiceImpl.getTitle(""));
		 * model.addObject("msg", mainServiceImpl.getDesc()); //d.setFname();
		 */
		Driver d = df.getDriver();
		DriverDetails dd = df.getDriverDetails();
		DrVehicle v = df.getDrVehicle();
		BankDetails bd = df.getBank();

		// Users user = new Users();
		// user.setUserName(d.getId()+"-"+d.getServiceType());
		// user.setPassword(StringUtils.randomString(6));
		// user.setUserType());

		/*
		 * HashSet<DrVehicle> vehicleSet = new HashSet<DrVehicle>();
		 * vehicleSet.add(v); d.setDrVehicles(vehicleSet);
		 * 
		 * 
		 * HashSet<BankDetails> bankDetailsSet = new HashSet<BankDetails>();
		 * bankDetailsSet.add(bd); d.setBankDetails(bankDetailsSet);
		 */
		Driver existingDriverObj = new Driver();
		boolean newEntry = true;
		if (d.getDriverId() != null) {
			newEntry = false;
			existingDriverObj = mainServiceImpl.getDrivers(d.getDriverId());

		}
		
		if (imageFile != null && imageFile.getOriginalFilename() != null && processFile(imageFile)) {
			d.setPhoto(imageFile.getOriginalFilename());
		} else {
			if(!newEntry)
			d.setPhoto(existingDriverObj.getPhoto());
		}
		if (licenseFile != null && licenseFile.getOriginalFilename() != null && processFile(licenseFile)) {
			dd.setLicensePath(licenseFile.getOriginalFilename());
		} else {
			if(!newEntry)
			dd.setLicensePath(existingDriverObj.getDriverDetails().getLicensePath());
		}
		if (insuranceFile != null && insuranceFile.getOriginalFilename() != null && processFile(insuranceFile)) {
			dd.setDriverInsurancePath(insuranceFile.getOriginalFilename());
		} else {
			if(!newEntry)
			dd.setDriverInsurancePath(existingDriverObj.getDriverDetails().getDriverInsurancePath());
		}
		if (driverAuthFile != null && driverAuthFile.getOriginalFilename() != null && processFile(driverAuthFile)) {
			dd.setDriverAuthPath(driverAuthFile.getOriginalFilename());
		} else {
			if(!newEntry)
			dd.setDriverAuthPath(existingDriverObj.getDriverDetails().getDriverAuthPath());
		}
		if (registrationFilePath != null && registrationFilePath.getOriginalFilename() != null
				&& processFile(registrationFilePath)) {
			v.setRegistrationDocPath(registrationFilePath.getOriginalFilename());
		} else {
			if(!newEntry)
			v.setRegistrationDocPath(existingDriverObj.getDrVehicles().iterator().next().getRegistrationDocPath());

			// v.setRegistrationDocPath(existingDriverObj.getDrVehicles().);

		}
		if (insuranceFilePath != null && insuranceFilePath.getOriginalFilename() != null
				&& processFile(insuranceFilePath)) {
			v.setVehinsurancePath(insuranceFilePath.getOriginalFilename());
		} else {
			if(!newEntry)
			v.setVehinsurancePath(existingDriverObj.getDrVehicles().iterator().next().getVehinsurancePath());
			/*for (DrVehicle drVehicle : vehList) {
				drVehicle.getVehinsurancePath();
			}*/
			// v.setVehinsurancePath(existingDriverObj.getDrVehicles().getVehinsurancePath);
		}

		d.setReferralCode(
				StringGenUtils.gerateRefCode(d.getFname(), d.getLname(), AppConstants.REFERAL_DRIVER_FORMAT));

		// d.setCseCode(d.getServiceType());

		// mainServiceImpl.insertDrivers(d);
		if (newEntry) {
			d.setCseCode(d.getServiceType());

			char[] CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@".toCharArray();
			String password = StringGenUtils.randomString(CHARSET, 8);
			d.setPassword(MD5Utils.to_MD5(password));
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("fname", d.getFname());
			map.put("emailid", d.getEmail());
			map.put("password", password);
			try {
				mailSenderPooled.sendEMail("Welcome to voyager", "mihir.shah2910@gmail.com", d.getEmail(), "",
						"mihir.shah2910@gmail.com", "/newDriver.tpl", map);
			} catch (MailException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// mainServiceImpl.insertDriver(d);

			mainServiceImpl.insertDriver(d);
			// d.setDriverId(d.getDriverId());

			d.setCseCode(d.getServiceType() + d.getDriverId());
			dd.setDriverId(d.getDriverId());
			v.setDriverId(d.getDriverId());
			bd.setDriverId(d.getDriverId());
			d.setDriverDetails(dd);
			HashSet<DrVehicle> vehicleSet = new HashSet<DrVehicle>();
			vehicleSet.add(v);
			d.setDrVehicles(vehicleSet);
			HashSet<BankDetails> bankDetailsSet = new HashSet<BankDetails>();
			bankDetailsSet.add(bd);
			d.setBankDetails(bankDetailsSet);
			mainServiceImpl.mergeDriver(d);

		} else {
			// mainServiceImpl.insertDriver(d);
			// String cse = d.getServiceType()+d.getDriverId();
			// d.setCseCode(cse);
			// dd.setDriverId(d.getDriverId());
			// v.setDriverId(d.getDriverId());
			// bd.setDriverId(d.getDriverId());
			d.setDriverDetails(dd);
			v.setDriverId(existingDriverObj.getDriverId());
			dd.setDriverId(existingDriverObj.getDriverId());
			bd.setDriverId(existingDriverObj.getDriverId());
			d.setDriverId(existingDriverObj.getDriverId());
			HashSet<DrVehicle> vehicleSet = new HashSet<DrVehicle>();
			vehicleSet.add(v);
//			d.setDrVehicles(vehicleSet);
			HashSet<BankDetails> bankDetailsSet = new HashSet<BankDetails>();
			bankDetailsSet.add(bd);
//			d.setBankDetails(bankDetailsSet);
		
			BeanUtils.copyProperties(d, existingDriverObj, "cseCode", "phone","driverId", "email");
			existingDriverObj.setBankDetails(bankDetailsSet);
			existingDriverObj.setDrVehicles(vehicleSet);
			mainServiceImpl.mergeDriver(existingDriverObj);
			mainServiceImpl.saveDriverDrVehicleAndBank(vehicleSet, bankDetailsSet);
			
		}
		/*
		 * List result = mainServiceImpl.getDrId(d.getPhone()); Integer
		 * dId=(Integer) result.get(0); v.setDriverId(dId); HashSet<DrVehicle>
		 * vehicleSet = new HashSet<DrVehicle>(); vehicleSet.add(v);
		 * d.setDrVehicles(vehicleSet); mainServiceImpl.insertDrivers(d);
		 */

		/*
		 * Set<BankDetails> bankDetails = null; bankDetails.add(bd);
		 * d.setBankDetails(bankDetails);
		 */
		// bankDet.add(bd);
		/*
		 * Integer drId=d.getDriverId(); d.setDriverId(drId);
		 * dd.setDriverId(drId); v.setDriverId(drId); bd.setDriverId(drId);
		 * d.setDriverDetails(dd);
		 */
		// mainServiceImpl.insertDrivers(d);

		/*
		 * DriverLogin driverLogin = new DriverLogin(); String type =
		 * voyagerConstants.DRIVERTYPE_TAXI;
		 * driverLogin.setDriverUsername(d.getServiceType()+d.getId());
		 * //driverLogin.setDriverId(d.getId()); driverLogin.setPassword("");
		 */

		// d.setDriverLogin(driverLogin);
		// mainServiceImpl.insertVehicle(v);
		// mainServiceImpl.insertBankDetails(bd);
		// mainServiceImpl.insertUser(user);

		return new ModelAndView(new RedirectView("listDrivers"));

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addCustomer", method = RequestMethod.GET)
	public ModelAndView addCustomer() {

		logger.debug("addCorporateCustomer() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("addCustomer");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());

		// model.addObject("drivers", new Drivers());
		// model.addObject("vehicle", new Vehicle());
		// model.addObject("bank", new BankDetails());
		/*
		 * HashMap<String,Object> ObjectMap = new HashMap<String,Object>();
		 * 
		 * ObjectMap.put("customer", new Customer());
		 * ObjectMap.put("customerProfile", new CustomerProfile());
		 */

		CustomerForm customerForm = new CustomerForm();
		model.addObject("customerForm", customerForm);

		return model;

	}

	/**
	 * 
	 * @param customerForm
	 * @param logoPath
	 * @return
	 */
	@RequestMapping(value = "/saveAddCustomer", method = RequestMethod.POST)
	public ModelAndView saveAddCustomer(@ModelAttribute CustomerForm customerForm,
			@RequestParam("logoPath") MultipartFile logoPath) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		// model.setViewName("listDrivers");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		// d.setFname();

		CorporateCustomer corpCust = customerForm.getCorpCust();
		// Users user = customerForm.getUsers();
		// corpCust.setStatus();
		// corpCust.setReferralCode(StringGenUtils.gerateRefCode(user.getFname(),
		// user.getLname()));
		// CustomerReward cp = customerForm.getCustomerReward();

		if (processFile(logoPath)) {
			corpCust.setLogoPath(logoPath.getOriginalFilename());
		}

		if (corpCust.getCorpCustId() == null) {
			mainServiceImpl.insertCustomer(corpCust);
		}
		
		corpCust.setCorpId("C" + corpCust.getCorpCustId());
		
		if (StringUtils.isEmpty(corpCust.getReferralCode())) {
			String referralCode = StringGenUtils.gerateRefCode(corpCust.getCorporateName(), corpCust.getCorpId(),
					AppConstants.REFERAL_CORPORATE_FORMAT);
			corpCust.setReferralCode(referralCode);
		}
		
		// cp.setCustomerId(corpCust.getCorpCustId());
		mainServiceImpl.insertCustomer(corpCust);
		// mainServiceImpl.insertCustomerReward(cp);

		return new ModelAndView(new RedirectView("listCustomers"));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listDrivers")
	public ModelAndView listDrivers() {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("listDrivers");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());

		model.addObject("listDrivers", mainServiceImpl.getDriversList());

		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listEndCustomers")
	public ModelAndView listEndCustomers() {

		logger.debug("listEndCustomers() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("listEndCustomers");

		model.addObject("listEndCustomers", mainServiceImpl.getEndCustomerList());

		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listCustomers")
	public ModelAndView listCustomers() {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("listCustomers");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		model.addObject("listCustomers", mainServiceImpl.getCustomerList());

		return model;

	}

	/**
	 * 
	 * @param driverId
	 * @return
	 */
	@RequestMapping(value = "/getDriver")
	public ModelAndView getDriver(@RequestParam Integer driverId) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("addDriver");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());

		DriverForm driverForm = new DriverForm();
		Driver dr = mainServiceImpl.getDrivers(driverId);
		driverForm.setDriver(dr);
		driverForm.setDriverDetails(dr.getDriverDetails());

		if (dr.getDrVehicles().iterator().hasNext()) {
			driverForm.setDrVehicle(driverForm.getDriver().getDrVehicles().iterator().next());
		} else {
			driverForm.setDrVehicle(new DrVehicle());
		}

		if (driverForm.getDriver().getBankDetails().iterator().hasNext()) {
			driverForm.setBank(driverForm.getDriver().getBankDetails().iterator().next());
		} else {
			driverForm.setBank(new BankDetails());
		}

		// driverForm.setVehicle(mainServiceImpl.getVehicle());
		// driverForm.setVehicle(mainServiceImpl.getBankDetails());

		model.addObject("driverForm", driverForm);

		return model;

	}

	/**
	 * 
	 * @param corpCustId
	 * @return
	 */
	@RequestMapping(value = "/getCustomer")
	public ModelAndView getCustomer(@RequestParam("id") Integer corpCustId) {

		logger.debug("getCustomer() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("addCustomer");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());

		CustomerForm customerForm = new CustomerForm();
		customerForm.setCorpCust(mainServiceImpl.getCustomer(corpCustId));

		// driverForm.setVehicle(mainServiceImpl.getVehicle());
		// driverForm.setVehicle(mainServiceImpl.getBankDetails());

		model.addObject("customerForm", customerForm);

		return model;

	}

	/**
	 * 
	 * @param licenseNo
	 * @return
	 */
	@RequestMapping(value = "/validateCSE")
	public @ResponseBody String validateCSE(@RequestParam("licenseNo") String licenseNo) {

		Integer i = mainServiceImpl.validateCSE("licenseNo", licenseNo);
		return i + "";
	}

	/**
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/checkId")
	public @ResponseBody String checkId(@RequestParam("phone") String phone) {

		Integer i = mainServiceImpl.validateCSE("phone", phone);
		return i + "";
	}

	/**
	 * 
	 * @param driverId
	 * @return
	 */
	@RequestMapping(value = "/deleteCSE")
	public ModelAndView deleteCSE(@RequestParam("driverId") String driverId) {
		mainServiceImpl.deleteDrivers(Integer.parseInt(driverId));
		return new ModelAndView(new RedirectView("listDrivers"));
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteCustomer")
	public ModelAndView deleteCustomer(@RequestParam("id") String id) {
		mainServiceImpl.deleteCustomer(Integer.parseInt(id));
		return new ModelAndView(new RedirectView("listCustomers"));

	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public Boolean processFile(MultipartFile file) {

		// final String BASE_DIR_DOWNLOAD = "C:\\Users\\givizer
		// 1\\workspaceNew\\downloads\\";
		// final String BASE_DIR_DOWNLOAD =
		// "/home/sadmin/softwares/apache-tomcat-7.0.67/webapps/downloads/";
		String BASE_DIR_DOWNLOAD = env.getProperty("documents.dir");
		if(env.getProperty("local").equals("1")){
			BASE_DIR_DOWNLOAD = env.getProperty("local.documents.dir");
		}
		
		//final String BASE_DIR_DOWNLOAD = ;
		
		String fileName = "", msg = "";
		try {
			fileName = file.getOriginalFilename();
			byte[] bytes = file.getBytes();
			BufferedOutputStream buffStream = new BufferedOutputStream(
					new FileOutputStream(new File(BASE_DIR_DOWNLOAD + fileName)));
			buffStream.write(bytes);
			buffStream.close();
			msg = "You have successfully uploaded " + fileName + "<br/>";
			logger.debug(msg + "******************88");
		} catch (Exception e) {
			// return "You failed to upload " + fileName + ": " + e.getMessage()
			// +"<br/>";
			logger.debug(msg + "******************EE" + e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password) {

		ModelAndView model = new ModelAndView();
		Users user = new Users();
		user = mainServiceImpl.getUser(username, password);
		HttpSession session = request.getSession(true);
		session.setAttribute("user", user);
		if (user != null)
			return new ModelAndView(new RedirectView("dashboard"));
		else
			return new ModelAndView(new RedirectView("uc"));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/dlogin")
	public ModelAndView dlogin() {

		ModelAndView model = new ModelAndView();
		model.setViewName("dlogin");

		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ddetails")
	public ModelAndView ddetails() {

		ModelAndView model = new ModelAndView();
		model.setViewName("ddetails");

		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userForm") // method = RequestMethod.GET
	public ModelAndView userForm() {

		ModelAndView model = new ModelAndView();
		model.setViewName("userForm");

		return model;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/attributeForm") // , method = RequestMethod.GET)
	public ModelAndView attributeForm(HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		model.setViewName("attributeForm");
		List<AttrConfig> attrList = new ArrayList<AttrConfig>();
		AttrConfig attr = new AttrConfig();
		attrList = mainServiceImpl.getAttrConfig();
		HttpSession session = request.getSession(true);
		session.setAttribute("attrList", attrList);

		return model;

	}

	/**
	 * 
	 * @param attrName
	 * @param attrValue1
	 * @param attrValue2
	 * @param unit
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/attributeForm1", method = RequestMethod.POST)
	public ModelAndView attributeForm1(@RequestParam("attrName") String[] attrName,
			@RequestParam("attrValue1") String[] attrValue1, @RequestParam("attrValue2") String[] attrValue2,
			@RequestParam("unit") String[] unit, HttpServletRequest request) {

		for (int i = 0; i < attrName.length; i++) {

			if (!StringUtils.isEmpty(attrName[i])) {
				AttrConfig attr = new AttrConfig();
				attr.setAttrName(attrName[i]);
				attr.setAttrValue1(attrValue1[i]);
				attr.setAttrValue2(attrValue2[i]);
				attr.setUnit(unit[i]);
				mainServiceImpl.insertAttribute(attr);
			}

		}
		return new ModelAndView(new RedirectView("attributeForm"));
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/accountDetails") // , method = RequestMethod.GET)
	public ModelAndView accountDetails(HttpServletRequest request) {
		// String accNo = "123";
		Integer driverId = 1;
		BankDetails bank = new BankDetails();
		// List<BankDetails> bank = new ArrayList<BankDetails>();
		bank = mainServiceImpl.getBankDetails(driverId);
		ModelAndView model = new ModelAndView();
		model.setViewName("accountDetails");
		HttpSession session = request.getSession(true);
		session.setAttribute("bank", bank);
		return model;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listEnquiry") // ,method = RequestMethod.POST)
	public ModelAndView listEnquiry(HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		model.setViewName("listEnquiry");
		List<EnquiryForm> enqList = new ArrayList<EnquiryForm>();
		enqList = mainServiceImpl.getEnquiry();
		HttpSession session = request.getSession(true);
		session.setAttribute("enqList", enqList);
		return model;

	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/supportTable")
	public ModelAndView supportTable() {

		ModelAndView model = new ModelAndView();
		model.setViewName("supportTable");
		return model;
	}

	/**
	 * 
	 * @param supportType
	 * @param supportQuestion
	 * @param description
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/supportTable1", method = RequestMethod.POST)
	public ModelAndView supportTable1(@RequestParam("supportType") String[] supportType,
			@RequestParam("supportQuestion") String[] supportQuestion,
			@RequestParam("description") String[] description, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		// List<SupportMaster> suppList = new ArrayList<SupportMaster>();
		// model.setViewName("listSupport");
		for (int i = 0; i < supportType.length; i++) {

			if (!StringUtils.isEmpty(supportType[i])) {
				SupportMaster supp = new SupportMaster();
				supp.setSupportType(supportType[i]);
				supp.setSupportQuestion(supportQuestion[i]);
				supp.setDescription(description[i]);
				mainServiceImpl.insertSupportMaster(supp);
				// attrList.add(supp);
			}

		}
		// mainServiceImpl.insertAttribute(suppList);
		return new ModelAndView(new RedirectView("listSupport"));
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listSupport") // , method = RequestMethod.GET)
	public ModelAndView listSupport(HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		model.setViewName("listSupport");
		List<SupportMaster> suppList = new ArrayList<SupportMaster>();
		SupportMaster supp = new SupportMaster();
		suppList = mainServiceImpl.getsupport();
		HttpSession session = request.getSession(true);
		session.setAttribute("suppList", suppList);
		return model;

	}

	/**
	 * 
	 * @param supportId
	 * @param supportType
	 * @param supportQuestion
	 * @param description
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/nextSupport", method = RequestMethod.POST)
	public ModelAndView nextSupport(@RequestParam("supportId") Integer[] supportId,
			@RequestParam("supportType") String[] supportType,
			@RequestParam("supportQuestion") String[] supportQuestion,
			@RequestParam("description") String[] description, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		// List<SupportMaster> suppList = new ArrayList<SupportMaster>();
		// model.setViewName("listSupport");
		for (int i = 0; i < supportType.length; i++) {

			if (!StringUtils.isEmpty(supportType[i])) {
				SupportMaster supp = new SupportMaster();
				supp.setSupportId(supportId[i]);
				supp.setSupportType(supportType[i]);
				supp.setSupportQuestion(supportQuestion[i]);
				supp.setDescription(description[i]);
				mainServiceImpl.insertSupport(supp);
				// attrList.add(supp);
			}

		}
		// mainServiceImpl.insertAttribute(suppList);
		return new ModelAndView(new RedirectView("listSupport"));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bookingHistory")
	public ModelAndView bookingHistory() {

		ModelAndView model = new ModelAndView();
		model.setViewName("bookingHistory");
		return model;
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listHistory")
	public ModelAndView listHistory(@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, HttpServletRequest request) {

		List<Booking> bookingList = new ArrayList<Booking>();
		ModelAndView model = new ModelAndView();
		Integer corpId = 1; // TODO add corpId after login
		bookingList = mainServiceImpl.getBookingHistory(startDate, endDate, corpId);
		model.setViewName("listHistory");
		HttpSession session = request.getSession(true);
		session.setAttribute("bookingList", bookingList);
		return model;
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addVehicle")
	public ModelAndView addVehicle() {

		ModelAndView model = new ModelAndView();
		model.setViewName("addVehicle");

		return model;

	}

	/**
	 * 
	 * @param vehInsuranceNo
	 * @param vehinsuranceRenewDt
	 * @param make
	 * @param model1
	 * @param vType
	 * @param carNumber
	 * @param ownerId
	 * @param vehAttributes
	 * @param seatingCapacity
	 * @param insuranceFilePath
	 * @return
	 */
	@RequestMapping(value = "/saveVehicle", method = RequestMethod.POST)
	public ModelAndView saveVehicle(@RequestParam("vehInsuranceNo") String vehInsuranceNo,
			@RequestParam("vehinsuranceRenewDt") String vehinsuranceRenewDt, @RequestParam("make") String make,
			@RequestParam("model") String model1, @RequestParam("vType") String vType,
			@RequestParam("carNumber") String carNumber, @RequestParam("ownerId") String ownerId,
			@RequestParam("vehAttributes") String vehAttributes,
			@RequestParam("seatingCapacity") Integer seatingCapacity,
			@RequestParam("insuranceFilePath") MultipartFile insuranceFilePath) {

		Integer driverId = 1; // TODO get driverId from login
		DrVehicle v = new DrVehicle();
		v.setDriverId(driverId);
		v.setMake(make);
		v.setModel(model1);
		v.setvType(vType);
		v.setCarNumber(carNumber);
		v.setOwnerId(ownerId);
		v.setVehAttributes(vehAttributes);
		v.setSeatingCapacity(seatingCapacity);
		v.setVehInsuranceNo(vehInsuranceNo);
		v.setVehinsuranceRenewDt(vehinsuranceRenewDt);
		if (processFile(insuranceFilePath)) {
			v.setVehinsurancePath(insuranceFilePath.getOriginalFilename());
		}
		mainServiceImpl.insertVehicle(v);

		return new ModelAndView(new RedirectView("listVehicle"));
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listVehicle")
	public ModelAndView listVehicle(HttpServletRequest request) {

		Integer driverId = 1; // TODO get driveId on login
		ModelAndView model = new ModelAndView();
		model.setViewName("listVehicle");
		List<DrVehicle> listVehicles = new ArrayList<DrVehicle>();
		listVehicles = mainServiceImpl.getVehicles(driverId);
		HttpSession session = request.getSession(true);
		session.setAttribute("listVehicles", listVehicles);
		return model;

	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/paymentState")
	public ModelAndView paymentState(HttpServletRequest request) {

		ModelAndView model = new ModelAndView();

		Integer driverId = 1; // TODO get driveId on login
		model.setViewName("paymentState");
		List<DriverEarnings> listDriverEarning = new ArrayList<DriverEarnings>();
		listDriverEarning = mainServiceImpl.getDriverEarning(driverId);
		HttpSession session = request.getSession(true);
		session.setAttribute("listVehicles", listDriverEarning);
		return model;

	}

	/*
	 * @RequestMapping(value = "/sendEmail") public ModelAndView paymentState()
	 * throws IOException{ ModelAndView model = new ModelAndView();
	 * HashMap<String, String> map = new HashMap<String, String>();
	 * map.put("fname", "Mihir"); map.put("emailid",
	 * "mihir.shah2910@gmail.com"); map.put("password", "abcd1234"); try {
	 * mailSenderPooled.sendEMail("Welcome to voyager",
	 * "mehtakinjal2610@gmail.com", "mihir.shah2910@gmail.com",
	 * "shahsj@gmail.com", "mihir.shah2910@gmail.com",
	 * "C:\\Users\\Nidhi\\git\\aonghusvoyager1\\src\\main\\resources\\template\\newDriver.tpl",
	 * map); } catch (MailException | MessagingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * logger.debug("Success"); return null; }
	 */

	@RequestMapping(value = "/emp")
	public ModelAndView emp() {

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/emp");

		// return model;

		CorpEmp corpEmp = new CorpEmp();
		model.addObject("emp", corpEmp);

		return model;

	}

	/**
	 * 
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param corpCustId
	 * @return
	 */
	@RequestMapping(value = "/listEmp", method = RequestMethod.POST)
	public ModelAndView listEmp(@RequestParam("email") String[] email, @RequestParam("firstname") String[] firstname,
			@RequestParam("lastname") String[] lastname, @RequestParam("custCoptId") Integer corpCustId) {
		System.out.println("ok88888888888888888888888888888888888888888888888888888888888888888888888888883");
		ModelAndView model = new ModelAndView();
		// model.setViewName("emp");
		// d.setFname();
		char[] characterSet = "1234567890".toCharArray();
		String myCurrentDir = System.getProperty("user.dir") + File.separator + System.getProperty("sun.java.command")
				.substring(0, System.getProperty("sun.java.command").lastIndexOf(".")).replace(".", File.separator);
		System.out.println(myCurrentDir);

		// map.put("fname", "Mihir");
		//String corpCustId = "C1"; // TODO get corpCustId after login
		List<CorpEmp> empList = new ArrayList<CorpEmp>();
		for (int i = 0; i < email.length; i++) {

			if (!StringUtils.isEmpty(email[i])) {
				CorpEmp corpEmp = new CorpEmp();
				String pin = StringGenUtils.randomString(characterSet, 6);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("pin", pin);

				try {
					mailSenderPooled.sendEMail("Corporate", "mihir.shah2910@gmail.com", email[i], "",
							"mihir.shah2910@gmail.com", "/addCorpEmp.tpl", map);
					// mailSenderPooled.sendEMail("Corporate",
					// "mihir.shah2910@gmail.com", "mihir.shah2910@gmail.com",
					// "", "mihir.shah2910@gmail.com", "/invoice.html", new
					// HashMap<String, String>() );
				} catch (MailException | IOException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// map.clear();
				corpEmp.setEmail(email[i]);
				corpEmp.setFname(firstname[i]);
				corpEmp.setLname(lastname[i]);
				corpEmp.setCorpCustId(corpCustId);
				corpEmp.setPin(pin);
				empList.add(corpEmp);
			}

		}

		mainServiceImpl.insertCorpEmp(empList);

		return new ModelAndView(new RedirectView("listEmp"));

	}

/*	@RequestMapping(value = "/listEmp")
	public ModelAndView listEmp() {

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/listEmp");

		model.addObject("listEmp", mainServiceImpl.getCorpEmpList());
		return model;

	}
*/
	@RequestMapping(value = "/insertAsset")
	public ModelAndView insertAsset() {

		ModelAndView model = new ModelAndView();
		model.setViewName("insertAsset");

		return model;

	}
	
	/**
	 * 
	 * @param assetId
	 * @return
	 */
	@RequestMapping(value = "/getAsset")
	public ModelAndView getAsset( @RequestParam("assetId") String assetId) {

		ModelAndView model = new ModelAndView();
		model.setViewName("insertAsset");

		model.addObject("equipment", mainServiceImpl.getEquipment(assetId));
		
		return model;

	}
	
	/**
	 * 
	 * @param serialNumber
	 * @param modelName
	 * @param description
	 * @param assetType
	 * @param prodCode
	 * @param assetId
	 * @return
	 */
	@RequestMapping(value = "/saveAsset", method = RequestMethod.POST)
	public ModelAndView saveAsset(@RequestParam("serialNumber") String serialNumber,
			@RequestParam("modelName") String modelName, @RequestParam("description") String description,
			@RequestParam("assetType") String assetType, @RequestParam("prodCode") String prodCode, @RequestParam("assetId") Integer assetId) {

		Integer user = 1; // TODO get UserId from login
		Equipment eq = new Equipment();
		if(assetId != null  && assetId > 0 ){
			eq.setAssetId(assetId);
		}
		
		Date currDt = new Date();
		eq.setCreateDate(currDt);
		eq.setModifiedDate(currDt);

		eq.setCreateBy(user);
		eq.setModifiedBy(user);

		eq.setSerialNumber(serialNumber);
		eq.setModelName(modelName);
		eq.setDescription(description);
		eq.setAssetType(assetType);
		eq.setProdCode(prodCode);
		eq.setStatus(AppConstants.ASSET_ACTIVE);
		eq.setDeleteFlag(AppConstants.DELETEFLAG_ALIVE+"");

		mainServiceImpl.insertAsset(eq);

		/*
		 * UserDetailSession u = (UserDetailSession)
		 * SecurityContextHolder.getContext().getAuthentication().getDetails();
		 * System.out.println("User Id.................."+u.getUsername());
		 */

		return new ModelAndView(new RedirectView("listAsset"));
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/manageAsset")
	public ModelAndView manageAsset() {

		ModelAndView model = new ModelAndView();
		model.setViewName("manageAsset");

		return model;

	}
/*
	@RequestMapping(value = "/corpReg")
	public ModelAndView corpReg() {

		ModelAndView model = new ModelAndView();
		model.setViewName("corpReg");

		return model;

	}


	@RequestMapping(value = "/saveCorpReg")
	public ModelAndView saveCorpReg(@RequestParam("corporateName") String corporateName,
			@RequestParam("address") String address, @RequestParam("street") String street,
			@RequestParam("area") String area, @RequestParam("city") String city, @RequestParam("state") String state,
			@RequestParam("country") String country, @RequestParam("abnAcnNo") String abnAcnNo,
			@RequestParam("pincode") String pincode, @RequestParam("phone") String phone,
			@RequestParam("email") String email, @RequestParam("logoPath") MultipartFile logoPath) {

		CorporateCustomer cc = new CorporateCustomer();
		cc.setCorporateName(corporateName);
		cc.setAddress(address);
		cc.setStreet(street);
		cc.setArea(area);
		cc.setCity(city);
		cc.setState(state);
		cc.setCountry(country);
		cc.setAbnAcnNo(abnAcnNo);
		cc.setPincode(pincode);
		cc.setPhone(phone);
		cc.setEmail(email);
		if (processFile(logoPath)) {
			cc.setLogoPath(logoPath.getOriginalFilename());
		}
		cc.setStatus(voyagerConstants.STATUS_ACTIVE);
		mainServiceImpl.insertCustomer(cc);

		cc.setCorpId("C" + cc.getCorpCustId());
		mainServiceImpl.insertCustomer(cc);

		return new ModelAndView(new RedirectView("corpReg"));
	}
*/
	
	/**
	 * 
	 * @param openText
	 * @return
	 */
	@RequestMapping(value = "/searchCustomer")
	public ModelAndView searchCustomer(@RequestParam("openText") String openText) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("rideCustomer");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		model.addObject("customers", mainServiceImpl.getSearchEndUserCustomerList(openText));

		return model;

	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listAsset")
	public ModelAndView listAsset() {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("listAsset");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		model.addObject("listAsset", mainServiceImpl.getEquipmentList());

		return model;

	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/manageAssets")
	public ModelAndView manageAssets() {

		logger.debug("manageAssets() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("manageAsset");

		model.addObject("driverList", mainServiceImpl.getDriverForAssetDriver());
		model.addObject("assetList", mainServiceImpl.getAssetList());
		
		return model;

	}

	/**
	 * 
	 * @param driverId
	 * @return
	 */
	@RequestMapping(value = "/getDriverAsset")
	public @ResponseBody EquipDriver getDriverAsset(@RequestParam("driverId") Integer driverId) {

		logger.debug("getDriverAsset() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("eq");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		EquipDriver eq = mainServiceImpl.getEquipDriver(driverId);

		return eq;

	}

	/**
	 * 
	 * @param assetId
	 * @param driverId
	 * @return
	 */
	@RequestMapping(value = "/saveAssetManage")
	public ModelAndView saveAssetManage(@RequestParam("assetId") String assetId, @RequestParam("driverId") Integer driverId) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("manageAsset");
		EquipDriver ed =  new EquipDriver();
		ed.setAssetId(assetId);
		ed.setDriverId(driverId);
		ed.setDeleteFlag(AppConstants.DELETEFLAG_ALIVE+"");
		
//		model.addObject("listAsset", mainServiceImpl.getEquipmentList());
		mainServiceImpl.saveUpdateEquipDriver(ed);
		
		return new ModelAndView(new RedirectView("listAsset"));
	}

}