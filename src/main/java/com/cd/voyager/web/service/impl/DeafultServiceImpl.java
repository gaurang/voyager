package com.cd.voyager.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cd.voyager.entities.FeedBack;
import com.cd.voyager.entities.Users;
import com.cd.voyager.mobility.data.WebUserDetails;
import com.cd.voyager.web.service.IDefaultService;
import com.cd.voyager.web.service.dao.AbstractDao;


@Repository
@Transactional
public class DeafultServiceImpl extends AbstractDao implements IDefaultService,UserDetailsService  {

	@Override
	public void insertFeedBack(FeedBack feedback) {
		getSession().saveOrUpdate(feedback);

	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		Users user = (Users) getSession().createCriteria(Users.class).add(Restrictions.eq("userName", userName)).uniqueResult();
		if(user == null ){
			throw new UsernameNotFoundException(userName+" Not found");
		}
		//System.out.println("Calle  test");
		return buildUserFromUserEntity(user) ;
	}

	
	 private UserDetails buildUserFromUserEntity(Users userEntity) {
		  // convert model user to spring security user
		  String username = userEntity.getUserName();
		  String password = userEntity.getPassword();
		  boolean enabled = true;
		  boolean accountNonExpired = true;
		  boolean credentialsNonExpired = true;
		  boolean accountNonLocked = true;
		  System.out.println("Called ");

		  UserDetails springUser = new WebUserDetails(username, password, enabled,
		    accountNonExpired, credentialsNonExpired, accountNonLocked,
		    getGrantedAuthorities(userEntity), userEntity);
		  
		//  return  new org.springframework.security.core.userdetails.User(userEntity.getUserName(), userEntity.getPassword(), 
		//		  true, true, true, true, getGrantedAuthorities(userEntity));
		  return springUser;
		 }

	 
	    private List<GrantedAuthority> getGrantedAuthorities(Users userEntity){
	        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	         
	        authorities.add(new SimpleGrantedAuthority("ROLE_"+userEntity.getUserRole().getRoleName()));
	        authorities.add(new SimpleGrantedAuthority(userEntity.getUserRole().getRoleName()));

	        System.out.print("authorities :***********************"+authorities);
	        return authorities;
	    }

}
