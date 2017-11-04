package com.cd.voyager.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component(value="driverAuthEntry")
public class DriverAuthenticationEntryPoint implements AuthenticationEntryPoint{

public void commence(HttpServletRequest arg0, HttpServletResponse response, 
		AuthenticationException arg2) throws IOException, ServletException

	{
	
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	
	}
}
