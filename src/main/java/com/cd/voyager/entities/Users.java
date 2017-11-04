package com.cd.voyager.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonProperty;


@Entity
@Table(name="tb_users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userid;
	
	private String userName;
	
	private String password;
	
	private String pin;
	
	private String userType;
	
	//private Integer roleId;
	
	private String status;

	private String createBy;
	private String createDate;
	
	private String modifiedBy;
	private String modifiedDate;


	private String fname;
	
	private String lname;
	
	private Integer corpCustId;
	
	//private String mname;
	
	@JsonProperty("roleId")
	@OneToOne(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name="roleId")
	private UserRole userRole;

	@OneToMany(cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name="userId")
	@JsonProperty("userActivityList")
	private Set<UserActivity> userActivitiyList;

	public Set<UserActivity> getUserActivitiyList() {
		return userActivitiyList;
	}
	public void setUserActivitiyList(Set<UserActivity> userActivitiyList) {
		this.userActivitiyList = userActivitiyList;
	}
	public UserRole getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/*public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}*/
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public Integer getCorpCustId() {
		return corpCustId;
	}
	public void setCorpCustId(Integer corpCustId) {
		this.corpCustId = corpCustId;
	}
	
	
	
	
}
