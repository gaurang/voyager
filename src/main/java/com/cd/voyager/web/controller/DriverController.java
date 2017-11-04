package com.cd.voyager.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.web.service.IMainService;

@Controller
@Scope("session")
@RequestMapping("/cseWeb")
public class DriverController {

	

	private final Logger logger = LoggerFactory.getLogger(SystemController.class);
	private final IMainService mainServiceImpl;
	//private final MailSender mailSender;
	
	
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	public DriverController(IMainService mainServiceImpl) {
		this.mainServiceImpl = mainServiceImpl;
	}
	
	@Autowired
	private MailSenderPooled mailSenderPooled;  
	
	
	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard(HttpServletRequest request) {

		logger.debug("CSE() loaded");

		HttpSession session = request.getSession(true);

		Integer zoneId = Integer.parseInt(context.getMessage("default.zone", null, Locale.getDefault()));

		session.setAttribute("configMap", mainServiceImpl.getGlobalConfig(zoneId));

		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard");

		return model;

	}



	
}
