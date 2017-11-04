package com.cd.voyager.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.cd.voyager.common.util.StringGenUtils;
import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.CorpEmp;
import com.cd.voyager.entities.Payment;
import com.cd.voyager.mobility.data.WebUserDetails;
import com.cd.voyager.mobility.service.IMobileService;
import com.cd.voyager.web.service.IMainService;

@Controller
@Scope("session")
@RequestMapping("/corp")
public class CorpController {

	private final Logger logger = LoggerFactory.getLogger(SystemController.class);
	private final IMainService mainServiceImpl;

	@Autowired
	private final IMobileService mobileServiceImpl;

	// private final MailSender mailSender;

	@Autowired
	private ApplicationContext context;

	@Autowired
	public CorpController(IMainService mainServiceImpl, IMobileService mobileServiceImpl) {
		this.mainServiceImpl = mainServiceImpl;
		this.mobileServiceImpl = mobileServiceImpl;
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
 * Dash board
 * @param request
 * @return
 */
	@RequestMapping(value = "/dashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard(HttpServletRequest request) {

		logger.debug("driver() loaded");

		HttpSession session = request.getSession(true);

		Integer zoneId = Integer.parseInt(context.getMessage("default.zone", null, Locale.getDefault()));

		session.setAttribute("configMap", mainServiceImpl.getGlobalConfig(zoneId));

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/dashboard");
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer corpCustId = userDetails.getUserDetails().getCorpCustId();
		
		model.addObject("ridesDashboard", mainServiceImpl.getCorpDashboard(corpCustId));
		model.addObject("regEmpCount", mainServiceImpl.getCorpCustCount(corpCustId));
		model.addObject("corpCustEmpCount", mainServiceImpl.getCorpCustTotal(corpCustId));
		model.addObject("rewardPoints", mainServiceImpl.getLoayltiPointsCorpCust(corpCustId));
		//model.addObject("activeCse", mainServiceImpl.getActiveCse());

		Calendar cal = Calendar.getInstance();
		String mn = request.getParameter("month")==null?"0":request.getParameter("month");
		Integer month = Integer.parseInt(mn);
		String yr = request.getParameter("year")==null?"0":request.getParameter("year");
		Integer year = Integer.parseInt(yr);
		String wk = request.getParameter("week")==null?"0":request.getParameter("week");
		Integer week = Integer.parseInt(wk);
		
		if(year <= 0 ){
			year = cal.get(Calendar.YEAR);
		}
		if(month <=  0 ){
			month = cal.get(Calendar.MONTH)+1;
		}	
		
		if(week <= 0  ){
			week = cal.get(Calendar.WEEK_OF_MONTH);
		}	
		
	    cal.set(year, month - 1, 01);

	    // "calculate" the start date of the week
	    Calendar first = (Calendar) cal.clone();
	    first.add(Calendar.DAY_OF_WEEK, 
	              first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

	    // and add six days to the end date
	    Calendar last = (Calendar) first.clone();
	    last.add(Calendar.DAY_OF_YEAR, 6);
	    
	    if(week > 1)
	    first.add(Calendar.DAY_OF_YEAR, (7*(week-1)));
	    last.add(Calendar.DAY_OF_YEAR, ((7*(week-1)) +7));
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    
	    List<Booking>  bookingList = mainServiceImpl.getBookingHistoryCorp(df.format(first.getTime()), df.format(last.getTime()), corpCustId);

	    model.addObject("bookingList", bookingList);
	    
		return model;

	}

	
	/**
	 * Get Booking details for Corporate
	 * @param month
	 * @param year
	 * @param week
	 * @return
	 */
	@RequestMapping(value = "/getBookingHistoryCorp")
	public @ResponseBody List<Booking> getBookingHistoryCorp(@RequestParam("month") Integer month, @RequestParam("year") Integer year, @RequestParam("week") Integer week) {

		logger.debug("getDriverAsset() loaded");

		ModelAndView model = new ModelAndView();
		model.setViewName("eq");

		model.addObject("title", mainServiceImpl.getTitle(""));
		model.addObject("msg", mainServiceImpl.getDesc());
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer corpCustId = userDetails.getUserDetails().getCorpCustId();
		
		
	    Calendar cal = Calendar.getInstance();
	    cal.set(year, month - 1, 01);

	    // "calculate" the start date of the week
	    Calendar first = (Calendar) cal.clone();
	    first.add(Calendar.DAY_OF_WEEK, 
	              first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

	    // and add six days to the end date
	    Calendar last = (Calendar) first.clone();
	    last.add(Calendar.DAY_OF_YEAR, 6);
	    
	    if(week > 1)
	    first.add(Calendar.DAY_OF_YEAR, (7*(week-1)));
	    last.add(Calendar.DAY_OF_YEAR, ((7*(week-1)) +7));
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    
	    List<Booking>  bookingList = mainServiceImpl.getBookingHistoryCorp(df.format(first), df.format(last), corpCustId);

	    return bookingList;

	}




	public Boolean processFile(MultipartFile file) {

		// final String BASE_DIR_DOWNLOAD = "C:\\Users\\givizer
		// 1\\workspaceNew\\downloads\\";
		// final String BASE_DIR_DOWNLOAD =
		// "/home/sadmin/softwares/apache-tomcat-7.0.67/webapps/downloads/";
		//final String BASE_DIR_DOWNLOAD = "C:/Users/Public/Pictures/Sample Pictures/";
		final String BASE_DIR_DOWNLOAD = env.getProperty("documents.dir");
		
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


	/*
	 * User form jsp page for registration Corporate
	 */
	
	@RequestMapping(value = "/userForm") // method = RequestMethod.GET
	public ModelAndView userForm() {

		ModelAndView model = new ModelAndView();
		model.setViewName("userForm");

		return model;
	}


/**
 * booking Histroy page
 * @return
 */
	@RequestMapping(value = "/bookingHistory")
	public ModelAndView bookingHistory() {

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/bookingHistory");
		return model;
	}

	/**
	 * List of booking history filter by date
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
		model.setViewName("corporate/listHistory");
		HttpSession session = request.getSession(true);
		session.setAttribute("bookingList", bookingList);
		return model;
	}

	/**
	 * Add payment by Corporate
	 * @return
	 */
	@RequestMapping(value = "/addPayment")
	public ModelAndView addPayment() {
		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/addPayment");
		model.addObject("payment", new Payment());

		return model;

	}
	
	/**
	 * List of existing payment methods
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "/listPayment" )
	public ModelAndView listPayment( Principal principal) {
		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/listPayment");
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addObject ("listPayment",mainServiceImpl.getPaymentList(userDetails.getUserDetails().getCorpCustId()));
		
		return model;

	}
	
	/**
	 * Save CC details for edit of payment details
	 * @param payment
	 * @param expiryMM
	 * @param expiryYY
	 * @return
	 */
	@RequestMapping(value = "/savePayment" ,  method = RequestMethod.POST)
	public ModelAndView savePayment(@ModelAttribute Payment payment, @RequestParam("expiryMM") String expiryMM, @RequestParam("expiryYY") String expiryYY) {
		ModelAndView model = new ModelAndView();
		//model.setViewName("corporate/listPayment");
		payment.setDeleteFlag(AppConstants.DELETEFLAG_ALIVE);
		payment.setStatus(AppConstants.STATUS_ACTIVE);
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//payment.setGatewayId(voyagerConstants.GATEWAY_PAYWAY);
		payment.setCorpCustId(userDetails.getUserDetails().getCorpCustId());
		payment.setExpDate(expiryMM+"/"+expiryYY);
		
		mainServiceImpl.savePayment(payment);
		return new ModelAndView(new RedirectView("listPayment"));

	}

	/**
	 * Delete payment
	 * @param paymentId
	 * @return
	 */
	@RequestMapping(value = "/deletePayment" ,  method = RequestMethod.POST)
	public ModelAndView deletePayment(@RequestParam("paymentId") Integer paymentId) {
		ModelAndView model = new ModelAndView();
		//model.setViewName("corporate/listPayment");
		
		mainServiceImpl.deletePayment(paymentId);
		
		return new ModelAndView(new RedirectView("listPayment"));

	}

	/**
	 * Emp creation for corporate who will be send email for registering as Corporate Customer
	 * @return
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
	 * List of existing employees of thye corporate
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @param corpCustId
	 * @return
	 */
	@RequestMapping(value = "/listEmp", method = RequestMethod.POST)
	public ModelAndView listEmp(@RequestParam("email") String[] email, @RequestParam("firstname") String[] firstname,
			@RequestParam("lastname") String[] lastname, @RequestParam("corpCustId") Integer corpCustId) {
		//System.out.println("ok88888888888888888888888888888888888888888888888888888888888888888888888888883");
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

	/**
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listEmp")
	public ModelAndView listEmp() {

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/listEmp");
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addObject("listEmp", mainServiceImpl.getCorpEmpList(userDetails.getUserDetails().getCorpCustId()));
		return model;

	}

	/**
	 * Delete employee 
	 * @param corpEmpId
	 * @return
	 */
	@RequestMapping(value = "/deleteEmp")
	public ModelAndView deleteEmp(@RequestParam("corpEmpId") Integer corpEmpId) {

		ModelAndView model = new ModelAndView();
		//model.setViewName("corporate/listEmp");
	//	WebUserDetails  userDetails =
	//			 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		mainServiceImpl.deleteCorpEmp(corpEmpId);
		return new ModelAndView(new RedirectView("listEmp"));

	}

	/**
	 * Invoice from Booking Id
	 * @param bookingId
	 * @return
	 */
	@RequestMapping(value = "/invoice")
	public ModelAndView invoice(@RequestParam("bookingId") Integer bookingId) {

		ModelAndView model = new ModelAndView();
		model.setViewName("corporate/invoice");
		model.addObject("booking", mobileServiceImpl.getBookingDetails(bookingId));
	//	WebUserDetails  userDetails =
	//			 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//return new ModelAndView(new RedirectView("listEmp"));
		return model;
	}

	/**
	 * History List of booking
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/history", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView history(HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		WebUserDetails  userDetails =
				 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer corpCustId = userDetails.getUserDetails().getCorpCustId();
		
		model.setViewName("corporate/history");
		Calendar cal = Calendar.getInstance();
		String mn = request.getParameter("month")==null?"0":request.getParameter("month");
		Integer month = Integer.parseInt(mn);
		String yr = request.getParameter("year")==null?"0":request.getParameter("year");
		Integer year = Integer.parseInt(yr);
		String wk = request.getParameter("week")==null?"0":request.getParameter("week");
		Integer week = Integer.parseInt(wk);
		
		if(year <= 0 ){
			year = cal.get(Calendar.YEAR);
		}
		if(month <=  0 ){
			month = cal.get(Calendar.MONTH)+1;
		}	
		
		if(week <= 0  ){
			week = cal.get(Calendar.WEEK_OF_MONTH);
		}	
		
	    cal.set(year, month - 1, 01);

	    // "calculate" the start date of the week
	    Calendar first = (Calendar) cal.clone();
	    first.add(Calendar.DAY_OF_WEEK, 
	              first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

	    // and add six days to the end date
	    Calendar last = (Calendar) first.clone();
	    last.add(Calendar.DAY_OF_YEAR, 6);
	    
	    if(week > 1)
	    first.add(Calendar.DAY_OF_YEAR, (7*(week-1)));
	    last.add(Calendar.DAY_OF_YEAR, ((7*(week-1)) +7));
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    
	    List<Booking>  bookingList = mainServiceImpl.getBookingHistoryCorp(df.format(first.getTime()), df.format(last.getTime()), corpCustId);

	    model.addObject("bookingList", bookingList);
	//	WebUserDetails  userDetails =
	//			 (WebUserDetails )SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//return new ModelAndView(new RedirectView("listEmp"));
		return model;
	}

}