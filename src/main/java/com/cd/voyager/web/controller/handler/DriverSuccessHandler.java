package com.cd.voyager.web.controller.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component(value="driverSuccessHandler")
public class DriverSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

@Override
public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {

	String sessionId=request.getSession().getId();
	// Please write response containing this session id
	
	}

}
