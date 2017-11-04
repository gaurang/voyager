package com.cd.voyager.mobility.data;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.cd.voyager.entities.Users;

public class WebUserDetails extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1925365805828949149L;
	
	
	private Users userDetails;
	
		
	
	public Users getUserDetails() {
		return userDetails;
	}



	public void setUserDetails(Users userDetails) {
		this.userDetails = userDetails;
	}



	public WebUserDetails(String username, String password, boolean enabled,
	         boolean accountNonExpired, boolean credentialsNonExpired,
	         boolean accountNonLocked,
	         Collection authorities, Users user) {
		super(username, password,enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userDetails = user;
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
