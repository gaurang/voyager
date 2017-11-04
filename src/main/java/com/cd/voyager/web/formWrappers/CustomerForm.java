package com.cd.voyager.web.formWrappers;

import com.cd.voyager.entities.CorporateCustomer;
import com.cd.voyager.entities.CustomerReward;
import com.cd.voyager.entities.Users;

public class CustomerForm {

	private CorporateCustomer corpCust = new CorporateCustomer();

	private CustomerReward customerReward = new CustomerReward();
	
	//private Users users = new Users();

	public CorporateCustomer getCorpCust() {
		return corpCust;
	}

	public void setCorpCust(CorporateCustomer corpCust) {
		this.corpCust = corpCust;
	}

	public CustomerReward getCustomerReward() {
		return customerReward;
	}

	public void setCustomerProfile(CustomerReward customerReward) {
		this.customerReward = customerReward;
	}

	/*public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}*/

	// private PaymentDetails paymentDetails = new PaymentDetails();

}
