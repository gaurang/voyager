package com.cd.voyager.secure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
     protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException ) throws IOException, ServletException {
        String contentType = request.getContentType();
        logger.info(contentType);
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
    }
 
}