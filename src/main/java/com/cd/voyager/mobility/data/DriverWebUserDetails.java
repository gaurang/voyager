package com.cd.voyager.mobility.data;

import java.util.Collection;

import org.springframework.security.core.userdetails.User;

import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.Users;

public class DriverWebUserDetails extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925365805828949149L;
	
	
	private Driver driverDetails;
	
		
	
	public Driver getDriverDetails() {
		return driverDetails;
	}



	public void setDriverDetails(Driver driverDetails) {
		this.driverDetails = driverDetails;
	}



	public DriverWebUserDetails(String username, String password, boolean enabled,
	         boolean accountNonExpired, boolean credentialsNonExpired,
	         boolean accountNonLocked,
	         Collection authorities, Driver cse) {
		super(username, password,enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.driverDetails = cse;
	}
/*
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return super.getAuthorities();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return super.isEnabled();
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
	
*/	

}
