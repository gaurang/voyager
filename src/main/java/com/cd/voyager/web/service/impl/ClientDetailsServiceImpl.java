package com.cd.voyager.web.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cd.voyager.email.MailSenderPooled;
import com.cd.voyager.entities.CorporateCustomer;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.mobility.data.CustomerWebUserDetails;
import com.cd.voyager.mobility.data.DriverWebUserDetails;
import com.cd.voyager.web.controller.SystemController;
import com.cd.voyager.web.service.dao.AbstractDao;

@Repository
@Transactional
public class ClientDetailsServiceImpl extends AbstractDao implements UserDetailsService  {

	private final Logger logger = LoggerFactory.getLogger(SystemController.class);

	//private final MailSender mailSender;
	
	@Autowired
	private ApplicationContext context;
	
	
	@Autowired
	private MailSenderPooled mailSenderPooled;  
	

		@Override
		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
			Customer cust = (Customer)getSession().createCriteria(Customer.class).add(Restrictions.eq("email", email)).uniqueResult();
			return buildUserFromUserEntity(cust) ;
		}

		
		 private UserDetails buildUserFromUserEntity(Customer cust) {
			  // convert model user to spring security user
			  String username = cust.getEmail();
			  String password = cust.getPassword();
			  boolean enabled = true;
			  boolean accountNonExpired = true;
			  boolean credentialsNonExpired = true;
			  boolean accountNonLocked = true;
			  System.out.println("Called ");

			  UserDetails springUser = new CustomerWebUserDetails(username, password, enabled,
			    accountNonExpired, credentialsNonExpired, accountNonLocked,
			    getGrantedAuthorities(cust),cust);
			  
			//  return  new org.springframework.security.core.userdetails.User(userEntity.getUserName(), userEntity.getPassword(), 
			//		  true, true, true, true, getGrantedAuthorities(userEntity));
			  return springUser;
			 }

		 
		    private List<GrantedAuthority> getGrantedAuthorities(Customer userEntity){
		        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		         
		        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
		        authorities.add(new SimpleGrantedAuthority("DRIVER"));

		        System.out.print("authorities :***********************"+authorities);
		        return authorities;
		    }


}
