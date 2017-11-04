package com.cd.voyager.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.cd.voyager.common.util.AppConstants;
import com.cd.voyager.entities.CorporateCustomer;
import com.cd.voyager.entities.EnquiryForm;
import com.cd.voyager.entities.FeedBack;
import com.cd.voyager.web.service.IDefaultService;
import com.cd.voyager.web.service.IMainService;

@Controller
public class DefaultController {
	
	private final Logger logger = LoggerFactory.getLogger(DefaultController.class);
	
	@Autowired
	private  IDefaultService defaultServiceImpl;
	
	@Autowired
	private IMainService mainServiceImpl;

	@Autowired
	private ApplicationContext context;

	/**
	 * Entry point main login page
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> model, Principal principal) {

		logger.debug("index() is executed!");
		logger.debug("index "+ (principal!=null ?principal.getName():"NO")); 

		model.put("title", mainServiceImpl.getTitle(""));
		model.put("msg", mainServiceImpl.getDesc());

		return "index";
	}	
	
	/**
	 * TEMP
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/drlogin", method = RequestMethod.GET)
	public ModelAndView drlogin(Map<String, Object> model, Principal principal) {
		logger.debug("here login "+ (getPrincipal()!=null ?getPrincipal():"NO")); 

		logger.debug("login() is executed!");
        if (principal != null) 
        	return new ModelAndView(new RedirectView("cseWeb/dashboard"));
        else
        	return new ModelAndView("drlogin");
	}
		
	/**
	 * Admin dashboard after login
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(Map<String, Object> model, Principal principal) {
		logger.debug("here login "+ (getPrincipal()!=null ?getPrincipal():"NO")); 

		logger.debug("login() is executed!");
        if (principal != null) 
        	return new ModelAndView(new RedirectView("admin/dashboard"));
        else
        	return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public ModelAndView access_Denied(Map<String, Object> model,  Principal principal) {
		
		logger.debug("access denied"+ (getPrincipal()!=null ?getPrincipal():"NO")); 

    	return new ModelAndView("index");
	}

	/**
	 * feedback page 
	 * @return
	 */
	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public ModelAndView feedback() {

		logger.debug("addCorporateCustomer() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("client/feedback");
		
		FeedBack feedback = new  FeedBack();
 		model.addObject("feedback", feedback);
		 
		return model;

	}
	
	/**
	 * save Feedback
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)
	public ModelAndView saveFeedback(@ModelAttribute FeedBack feedback) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("client/actionPage");
	

		//d.setFname();

		defaultServiceImpl.insertFeedBack(feedback);
		model.addObject("actionHeader", context.getMessage("default.thankyou.header", null, Locale.getDefault()));
		model.addObject("actionMessage", context.getMessage("default.thankyou.message", null, Locale.getDefault()));
		
		
		return model ;//

	}
	
	/**
	 * Insert or add feedback form details
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "/actionPage", method = RequestMethod.POST)
	public ModelAndView actionPage(@ModelAttribute FeedBack feedback) {

		logger.debug("driver() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("");
		
		//d.setFname();

		defaultServiceImpl.insertFeedBack(feedback);
		
		return model;//

	}

	private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
	
	/**
	 * Driver Enquiry form
	 * @return
	 */
	@RequestMapping(value = "/enquiryForm")
	public ModelAndView enquiryForm() {

		ModelAndView model = new ModelAndView();
		model.setViewName("enquiryForm");

		return model;

	}

	/**
	 * Submit of enquiry
	 * 
	 * @param firstName
	 * @param lastName
	 * @param city
	 * @param email
	 * @param mobile
	 * @param countryCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveEnquiry", method = RequestMethod.POST )
	public ModelAndView saveEnquiry(@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("city") String city,
			@RequestParam("email") String email, @RequestParam("mobile") Long mobile,@RequestParam("countryCode") Integer countryCode, HttpServletRequest request) {

		String cseType1 = (String) request.getParameter("cseType1");
		String cseType2 = (String) request.getParameter("cseType2");
		String cseType3 = (String) request.getParameter("cseType3");
		String cseType4 = (String) request.getParameter("cseType4");
		String aboutUs = (String) request.getParameter("aboutUs");
		String driveWith = (String) request.getParameter("driveWith");
		ModelAndView model = new ModelAndView();
		// model.setViewName("enquiryForm");
		EnquiryForm enq = new EnquiryForm();
		enq.setAboutUs(aboutUs);
		enq.setCity(city);
		enq.setCseType1(cseType1);
		enq.setCseType2(cseType2);
		enq.setCseType3(cseType3);
		enq.setCseType4(cseType4);
		enq.setEmail(email);
		enq.setFirstName(firstName);
		enq.setLastName(lastName);
		enq.setMobile(mobile);
		enq.setCountryCode(countryCode);
		enq.setDriveWith(driveWith);
		mainServiceImpl.insertEnquiry(enq);
		return new ModelAndView(new RedirectView("enquiryForm"));
	}

	/**
	 * corporate Registration form
	 * @return
	 */
	@RequestMapping(value = "/regCrp" )
	public ModelAndView corporateReg() {

		logger.debug("inside regCrp");
		ModelAndView model = new ModelAndView();
		model.setViewName("corpReg");
		
		return model;

		}
	
	/**
	 * Submit corp Regsitration
	 * 
	 * @param corporateName
	 * @param address
	 * @param street
	 * @param area
	 * @param city
	 * @param state
	 * @param country
	 * @param abnAcnNo
	 * @param pincode
	 * @param phone
	 * @param email
	 * @param logoPath
	 * @return
	 */
	@RequestMapping(value = "/saveCorpReg" , method = RequestMethod.POST)
	public ModelAndView saveCorpReg(@RequestParam("corporateName") String corporateName,
			@RequestParam("address") String address, 
			@RequestParam("street") String street,
			@RequestParam("area") String area, 
			@RequestParam("city") String city,
			@RequestParam("state") String state, 
			@RequestParam("country") String country,
			@RequestParam("abnAcnNo") String abnAcnNo,
			@RequestParam("pincode") String pincode,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("logoPath") MultipartFile logoPath) {

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
		cc.setStatus(AppConstants.STATUS_ACTIVE);
		mainServiceImpl.insertCustomer(cc);
		
		cc.setCorpId("C"+cc.getCorpCustId());
		mainServiceImpl.insertCustomer(cc);
		
		return new ModelAndView(new RedirectView("regCrp"));
	}

	
	/**
	 * Confirmation page
	 * @return
	 */
	@RequestMapping(value = "/confirmation")
	public ModelAndView confirmation() {


		ModelAndView model = new ModelAndView();
		model.setViewName("confirmation");
		
		return model;

		}


	@RequestMapping(value = "/")
	public ModelAndView firstPage() {


		ModelAndView model = new ModelAndView();
		model.setViewName("firstPage");
		
		return model;

		}
	
	public Boolean processFile(MultipartFile file) {

		// final String BASE_DIR_DOWNLOAD = "C:\\Users\\givizer
		// 1\\workspaceNew\\downloads\\";
		// final String BASE_DIR_DOWNLOAD =
		// "/home/sadmin/softwares/apache-tomcat-7.0.67/webapps/downloads/";
		final String BASE_DIR_DOWNLOAD = "C:/Users/Public/Pictures/Sample Pictures/";

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

	

}
