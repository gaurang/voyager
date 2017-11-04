package com.cd.voyager.secure;

import java.text.ParseException;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.nimbusds.jwt.PlainJWT;

public class JWTAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = 1L;
    private final Object principal;
    private Object details;
    
 
    Collection  authorities;
    PlainJWT parser;
    
    
    public JWTAuthenticationToken(String jwtToken) throws ParseException {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
        parser = PlainJWT.parse(jwtToken);
        this.principal=parser.getJWTClaimsSet().getSubject();
         
        this.setDetailsAuthorities();
 
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
    private void setDetailsAuthorities() {
        String username = principal.toString();
        /*UserDetailSession adapter = 
        details=adapter;
        authorities=(Collection) adapter.getAuthorities();
         */
    }
 
    @Override
    public Collection getAuthorities() {
        return authorities;
    }
}