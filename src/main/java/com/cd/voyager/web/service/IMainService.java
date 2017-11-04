package com.cd.voyager.web.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cd.voyager.entities.AttrConfig;
import com.cd.voyager.entities.BankDetails;
import com.cd.voyager.entities.Booking;
import com.cd.voyager.entities.CorpEmp;
import com.cd.voyager.entities.CorporateCustomer;
import com.cd.voyager.entities.Customer;
import com.cd.voyager.entities.CustomerReward;
import com.cd.voyager.entities.DrVehicle;
import com.cd.voyager.entities.Driver;
import com.cd.voyager.entities.DriverDetails;
import com.cd.voyager.entities.DriverEarnings;
import com.cd.voyager.entities.EnquiryForm;
import com.cd.voyager.entities.EquipDriver;
import com.cd.voyager.entities.Equipment;
import com.cd.voyager.entities.Payment;
import com.cd.voyager.entities.SupportMaster;
import com.cd.voyager.entities.Users;

public interface IMainService {
	
 public String getTitle(String pageName) ;
 public String getDesc() ;
 public void update(Driver driver) ;
 public void insertVehicle(DrVehicle vehicle) ;
 public void insertBankDetails(BankDetails bank) ;
 public void insertUser(Users user) ;
 public void insertDriver(Driver driver) ;
 public void insertCustomer(CorporateCustomer corpCust) ;
 public void insertCustomerReward(CustomerReward customerReward);
 public List<Driver> getDriversList() ;
 public List<Equipment> getEquipmentList() ;
 public Equipment getEquipment(String id) ;
 public void deleteEquipment(String id);

 public List<CorporateCustomer> getCustomerList() ;
 public Integer validateCSE(String phone,String fieldName);
 public Driver getDrivers(Integer id) ;
 public CorporateCustomer getCustomer(Integer id) ;
 public void deleteDrivers(Integer id) ;
 public void deleteCustomer(Integer id);

 
 public LinkedHashMap<String,LinkedHashMap<String,AttrConfig>> getGlobalConfig(Integer zoneId);
public Users getUser(String username, String password);
public List<AttrConfig> getAttrConfig();
//public void insertAttribute(List<AttrConfig> attrList);
public BankDetails getBankDetails(Integer driverId);
public void insertEnquiry(EnquiryForm enq);
public void insertSupportMaster(SupportMaster support);
public void insertAttribute(AttrConfig attr);
public List<SupportMaster> getsupport();
public void insertSupport(SupportMaster supp);

public DriverDetails getDriverDetails(Integer driverId);
public void insertDriverDetails(DriverDetails dd);
public void mergeDriver(Driver d);
public List<Booking> getBookingHistory(String startDate, String endDate, Integer corpId);

public List<EnquiryForm> getEnquiry();

public List<DrVehicle> getVehicles(Integer driverId);
public List<DriverEarnings> getDriverEarning(Integer driverId);
public Object getCorpEmpList(Integer corpCustId);
public void insertCorpEmp(List<CorpEmp> empList);
public void insertAsset(Equipment eq);
public List<Customer> getSearchEndUserCustomerList(String openText);
void saveDriverDrVehicleAndBank(Set<DrVehicle> drVehicle, Set<BankDetails> bankDetails);

public void saveUpdateEquipDriver(EquipDriver ed);

public EquipDriver getEquipDriver(Integer dId);
public List<EquipDriver> getEquipDriverList();

public Map<String, String> getDashboard();

public List<String> getActiveCse();
public String getCSECount();

public String getCustCount();
public List<Payment> getPaymentList(Integer corpCustId);
	
public void savePayment(Payment payment);
public void deletePayment(Integer paymentId);
public void deleteCorpEmp(Integer corpEmpId);

public List<Driver> getDriverForAssetDriver();

public List<Equipment> getAssetList();

public Map<String,String> getCorpDashboard(Integer corpCustId);

public String getCorpCustTotal(Integer corpCustId);
public String getCorpCustCount(Integer corpCustId) ;
	
public String getLoayltiPointsCorpCust(Integer corpCustId) ;

public List<Booking> getBookingHistoryCorp(String startDate, String endDate, Integer corpId);

public List<Customer> getEndCustomerList();


}
