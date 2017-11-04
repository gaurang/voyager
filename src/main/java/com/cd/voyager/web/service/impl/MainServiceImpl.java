package com.cd.voyager.web.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.ejb.Stateless;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cd.voyager.common.util.AppConstants;
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
import com.cd.voyager.utils.payment.payway.PayWayAPIUtil;
import com.cd.voyager.web.service.IMainService;
import com.cd.voyager.web.service.dao.AbstractDao;

@Repository
@Transactional
@Stateless
public class MainServiceImpl extends AbstractDao implements IMainService{

	private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);	


 

	public String getDesc() {

		logger.debug("getDesc() is executed!");

		return "Gradle + Spring MVC Hello World Example";

	}

	public String getTitle(String name) {

		logger.debug("getTitle() is executed! $name : {}", name);

		if(StringUtils.isEmpty(name)){
			return "Hello World";
		}else{
			return "Hello " + name;
		}
		
	}
	
	/*public void insertDrivers(Drivers driver) {
	
		setUpEntityManagerFactory();
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	
	    entityManager.getTransaction().begin();
	    entityManager.persist(driver);
	    
	    entityManager.getTransaction().commit();
	    entityManager.close();


	}
	public List<Drivers> getDrivers() {
		
		setUpEntityManagerFactory();
	    EntityManager entityManager = entityManagerFactory.createEntityManager();
	
	    entityManager.getTransaction().begin();
	    Query q = entityManager.createQuery("from Drivers");
	    List<Drivers> driversList =  q.getResultList();
	    
	    entityManager.getTransaction().commit();
	    entityManager.close();

	    return driversList;
	}
*/
    
    public void update(Driver driver) {
    	
    		getSession().saveOrUpdate(driver);

	}

    public void insertVehicle(DrVehicle vehicle) {
    	
		getSession().saveOrUpdate(vehicle);

    }

    public void insertBankDetails(BankDetails bank) {
    	
		getSession().saveOrUpdate(bank);

    }

    public void insertUser(Users user) {
    	
		getSession().saveOrUpdate(user);

    }
   
    public void insertDriver(Driver driver) {
  
		getSession().saveOrUpdate(driver);
		
    }
    //@Transactional
    public void insertCustomer(CorporateCustomer corpCust) {
    	
		getSession().saveOrUpdate(corpCust);
    }
    
    public void insertCustomerReward(CustomerReward customerReward) {
    	
		getSession().saveOrUpdate(customerReward);
    }
    
    
    public List<Driver> getDriversList() {
    	
		return getSession().createCriteria(Driver.class).add(Restrictions.eq("deleteFlag", AppConstants.DELETEFLAG_ALIVE))
				.list();
    }
    

    public List<CorporateCustomer> getCustomerList() {
    	
		return getSession().createCriteria(CorporateCustomer.class).add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE)).list();
		
		//add(Restrictions.ne("deleteflag", voyagerConstants.DELETEFLAG_ALIVE))
    }

        
    public Integer validateCSE(String phone,String fieldName) {
    	
		if(getSession().createCriteria(Driver.class).add(Restrictions.eq(fieldName, phone)).list().size() >0)
		{
			return 1;
		}else{
			return 0;
		}
		
    }

    public Driver getDrivers(Integer driverId) {
    	
		return (Driver)getSession().createCriteria(Driver.class).add(Restrictions.eq("driverId", driverId)).list().get(0);
    }

    public CorporateCustomer getCustomer(Integer id) {
    	
		return (CorporateCustomer)getSession().createCriteria(CorporateCustomer.class).add(Restrictions.eq("id", id)).list().get(0);
    }
    
    public void deleteDrivers(Integer driverId) {
    	
		Driver d = (Driver)getSession().createCriteria(Driver.class).add(Restrictions.eq("driverId", driverId)).list().get(0);
		d.setDeleteFlag(AppConstants.DELETEFLAG_DELETED);
		getSession().update(d);
    }

    
    public void deleteCustomer(Integer id) {
    	
    	CorporateCustomer   c = (CorporateCustomer)getSession().createCriteria(CorporateCustomer.class).add(Restrictions.eq("id", id)).list().get(0);
		c.setStatus(AppConstants.STATUS_INACTIVE);
		getSession().update(c);

    }

   //@Transactional
	@Override
	public LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> getGlobalConfig(
			Integer zoneId) {
		
	    String HQL_QUERY = "from AttrConfig where zoneId = :zoneId and status = :status order by level, masterAttr, sort";
	    Query query = getSession().createQuery(HQL_QUERY).setParameter("zoneId",zoneId).setParameter("status", AppConstants.STATUS_ACTIVE);
	    
//	    query.setResultTransformer(transformer);
	    List<AttrConfig> rows = (List<AttrConfig>)query.list();    
	    System.out.println("Selected row count : " + rows.size());
	    LinkedHashMap<String, LinkedHashMap<String, AttrConfig>> configMap = new LinkedHashMap<>();
//	    logger.debug(configMap.size()+" __________________--");
	    
	    
	    LinkedHashMap<String, AttrConfig> attrConfigLevel2 = new LinkedHashMap<String, AttrConfig>() ;
	    String masterAttr ="";
	    boolean level2 = false;
	    String type = "";
	    
	    for(AttrConfig attrConfig : rows) {
//	    	logger.debug(attrConfigLevel2.size()+"  YYYYYYYYYYYYYYY __________________--"+attrConfigLevel2.get(attrConfig.getAttrName())+"  "+attrConfig.getAttrName()+" " +masterAttr);
	    	String masterAttrThis = attrConfig.getMasterAttr();
	    	if(!masterAttrThis.equals(masterAttr)){
	    		if(attrConfigLevel2 != null && attrConfigLevel2.size() >0 ){
	    			if(level2){
	    				if(masterAttr.contains(AppConstants.CONFIG_DELEMETER)){
	    					configMap.get(type).get(masterAttr.split("-")[0]).setSubAttributes(attrConfigLevel2);
	    				}else{
	    					configMap.get(type).get(masterAttr).setSubAttributes(attrConfigLevel2);
	    				}
	    				configMap.put(masterAttr , attrConfigLevel2);
	    			}else{
	    				configMap.put(masterAttr , attrConfigLevel2);
	    			}
	    			attrConfigLevel2 = new LinkedHashMap<String, AttrConfig>();
	    			
	    		}
	    		//logger.debug(attrConfigLevel2.size()+"  222 __________________--");
	    		//logger.debug(attrConfigLevel2.size()+"  1111 __________ZZZZZZZZZZZZZ___--"+attrConfigLevel2.get(attrConfig.getAttrName())+"  "+attrConfig.getAttrName()+" " +masterAttr);
	    	}else if(attrConfig.getLevel().equals("2")){
				type = attrConfig.getType();
				level2 = true;
			}
	    	attrConfigLevel2.put(attrConfig.getAttrName(), attrConfig);
//	    	logger.debug(attrConfigLevel2.size()+"  1111 __________________--"+attrConfigLevel2.get(attrConfig.getAttrName())+"  "+attrConfig.getAttrName()+" " +masterAttr);
	    	masterAttr = masterAttrThis;
	    	
	    }
		if(attrConfigLevel2 != null && attrConfigLevel2.size() >0 ){
			if(level2){
				if(masterAttr.contains(AppConstants.CONFIG_DELEMETER)){
					configMap.get(type).get(masterAttr.split("-")[0]).setSubAttributes(attrConfigLevel2);
				}else{
					configMap.get(type).get(masterAttr).setSubAttributes(attrConfigLevel2);
				}
				configMap.put(masterAttr , attrConfigLevel2);
			}else{
				configMap.put(masterAttr , attrConfigLevel2);
			}
		}
//		logger.debug(configMap.toString()+" __________________--");
		return configMap;
	}

	public Users getUser(String username, String password) {
		// TODO Auto-generated method stub
		Users user= new Users();
		getSession().createCriteria(Users.class).add(Restrictions.eq("userName",username)).add(Restrictions.eq("password", password)).add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE));
		return (user);
	}
	
	/*public void insertAttribute(AttrConfig attr) {
		getSession().saveOrUpdate(attr);
	}*/
	

public void insertSupportMaster(SupportMaster sup){
	getSession().saveOrUpdate(sup);
}

/*public void insertSupportMaster(List<SupportMaster> support){
	
	for (SupportMaster sup : support){

		insertSupportMaster(sup);
	}
}*/

@Override
public BankDetails getBankDetails(Integer driverId) {
	
	BankDetails bank= new BankDetails();
	bank=(BankDetails) getSession().createCriteria(BankDetails.class).add(Restrictions.eq("driverId", driverId)).list().get(0);
	return bank;
}

@Override
public List<AttrConfig> getAttrConfig() {
	
	List<AttrConfig> attrList = new ArrayList<AttrConfig>();
	attrList=(List<AttrConfig>)getSession().createCriteria(AttrConfig.class).list();
	return attrList;

}
@Override
public void insertAttribute(AttrConfig attr) {
	
	String hql = "UPDATE AttrConfig set  attrValue1 = :attrValue1 , attrValue2 = :attrValue2 , unit = :unit "  + 
             "WHERE attrName = :attrName ";
	Query query = getSession().createQuery(hql)
				.setParameter("attrValue1", attr.getAttrValue1())
				.setParameter("attrValue2", attr.getAttrValue2())
				.setParameter("unit", attr.getUnit())
				.setParameter("attrName", attr.getAttrName());
	query.executeUpdate();
}

@Override
public void insertEnquiry(EnquiryForm enq) {
	// TODO Auto-generated method stub
	getSession().saveOrUpdate(enq);
}

@Override
public List<SupportMaster> getsupport() {
	List<SupportMaster> suppList = new ArrayList<SupportMaster>();
	suppList=(List<SupportMaster>)getSession().createCriteria(SupportMaster.class).list();
	return suppList;
}

@Override
public void insertSupport(SupportMaster supp) {
	// TODO Auto-generated method stub
	String hql = "UPDATE SupportMaster set  supportType = :supportType , supportQuestion = :supportQuestion , description = :description "  + 
            "WHERE supportId = :supportId ";
	Query query = getSession().createQuery(hql)
				.setParameter("supportType", supp.getSupportType())
				.setParameter("supportQuestion", supp.getSupportQuestion())
				.setParameter("description", supp.getDescription())
				.setParameter("supportId", supp.getSupportId());
				
	query.executeUpdate();
}

@Override
public List<Booking> getBookingHistory(String startDate, String endDate, Integer corpId) {
	
	List<Booking> bookingHistory = null;
	String dateFilter ="";
	if(startDate != null && endDate != null ){
		dateFilter += " and cr.rideStartDate between :startDate and :endDate";
	}
	
	String sql = "select cb.bookingId, concat(c.fname,' ', c.lname) as customerName, cc.email as email, cb.srcPlace, cb.destPlace, cr.rideStartDate as rideDate, cr.bookingFee as bookingFee, cr.rideTotalAmt as rideTotalAmt "
			+ "from tb_customerbooking as cb, tb_corporatecustomer as cc, tb_customerridedetails as cr, tb_customer as c "
			+ "where cb.bookingId = cr.bookingId and "
			+ "      cb.corpCustId = cc.corpCustId and "
			+ "		 cb.customerId = c.customerId and"
			+ "      cc.corpId  = :corpId "
			+ dateFilter +" order by cr.rideStartDate desc";
	
	Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpId", corpId);
		
	if(startDate != null && endDate != null ){
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
	}
	
	query.setResultTransformer(Transformers.aliasToBean(Booking.class));

	bookingHistory = (List<Booking>)query.list();
	
	return bookingHistory;
}

@Override
public List<Booking> getBookingHistoryCorp(String startDate, String endDate, Integer corpId) {
	
	List<Booking> bookingHistory = null;
	String dateFilter ="";
	if(startDate != null && endDate != null ){
		dateFilter += " and cr.rideStartDate between :startDate and :endDate";
	}
	
	String sql = "select ce.corpEmpId empId, ce.email, cb.bookingId, concat(c.fname,' ', c.lname) as customerName, cb.srcPlace, cb.destPlace, cr.rideStartDate as rideDate, cr.bookingFee as bookingFee, cr.rideTotalAmt as rideTotalAmt "
			+ "from tb_customerbooking as cb, tb_corporatecustomer as cc, tb_customerridedetails as cr, tb_customer as c, tb_corpemp ce, tb_customeraccount ca "
			+ "where cb.bookingId = cr.bookingId and "
			+ "      cb.corpCustId = cc.corpCustId and "
			+ "		 cb.customerId = c.customerId and"
			+ "      cc.corpCustId  = ce.corpCustId and"
			+"       ca.customerId = c.customerId and ca.accountType = 'C' and " //--ca.email = ce.email and
			+ "      cc.corpCustId  = :corpCustId "
			+ dateFilter +" order by cr.rideStartDate desc";
	
	Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpCustId", corpId);
		
	if(startDate != null && endDate != null ){
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
	}
	
	query.setResultTransformer(Transformers.aliasToBean(Booking.class));

	bookingHistory = (List<Booking>)query.list();
	
	return bookingHistory;
}

@Override
public DriverDetails getDriverDetails(Integer driverId) {
	// TODO Auto-generated method stub
	return (DriverDetails)getSession().createCriteria(DriverDetails.class).add(Restrictions.eq("driverId", driverId)).list().get(0);
}

@Override
public void insertDriverDetails(DriverDetails dd) {
	// TODO Auto-generated method stub
	getSession().saveOrUpdate(dd);
}

@Override
public void mergeDriver(Driver d) {
	// TODO Auto-generated method stub
	getSession().merge(d);
	getSession().flush();
}

@Override
public void saveDriverDrVehicleAndBank(Set<DrVehicle> drVehicles, Set<BankDetails> bankDetails) {
	// TODO Auto-generated method stub
	for (Iterator iterator = bankDetails.iterator(); iterator.hasNext();) {
		BankDetails bd = (BankDetails) iterator.next();
		getSession().merge(bd);
	}
	for (Iterator iterator = drVehicles.iterator(); iterator.hasNext();) {
		DrVehicle bd = (DrVehicle) iterator.next();
		getSession().merge(bd);
	}

	getSession().flush();
}

public List<EnquiryForm> getEnquiry() {
	
	List<EnquiryForm> enqList = new ArrayList<EnquiryForm>();
	enqList=(List<EnquiryForm>)getSession().createCriteria(EnquiryForm.class).list();
	return enqList;

}

@Override
public List<DrVehicle> getVehicles(Integer driverId) {
	// TODO Auto-generated method stub
	return (List<DrVehicle>)getSession().createCriteria(DrVehicle.class).add(Restrictions.eq("driverId", driverId)).list();
}

@Override
public List<DriverEarnings> getDriverEarning(Integer driverId) {
	// TODO Auto-generated method stub
	
	Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
    
    //Date currentTime = localCalendar.getTime();
    int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
    int currentDay = localCalendar.get(Calendar.DATE);
    int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
    int currentYear = localCalendar.get(Calendar.YEAR);

	List<DriverEarnings> listDriverEarning = null;
	
	String sql = "select ";
	
	/*String sql = "select cb.bookingId, concat(c.fname,' ', c.lname) as customerName, cc.email as email, cb.srcPlace, cb.destPlace, cr.rideStartDate as rideDate, cr.bookingFee as bookingFee, cr.rideTotalAmt as rideTotalAmt "
			+ "from tb_customerbooking as cb, tb_corporatecustomer as cc, tb_customerridedetails as cr, tb_customer as c "
			+ "where cb.bookingId = cr.bookingId and "
			+ "      cb.corpCustId = cc.corpCustId and "
			+ "		 cb.customerId = c.customerId and"
			+ "      cc.corpId  = :corpId "
			+ " order by cr.rideStartDate desc";*/
	
	Query query = getSession().createSQLQuery(sql);
//		query.setParameter("corpId", corpId);
		
	/*if(startDate != null && endDate != null ){
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
	}*/
	
	query.setResultTransformer(Transformers.aliasToBean(DriverEarnings.class));

	listDriverEarning = (List<DriverEarnings>)query.list();
	
	return listDriverEarning;

	}

	public void insertCorpEmp(CorpEmp corpEmp) {
	
		getSession().saveOrUpdate(corpEmp);
	}

	public void insertCorpEmp(List<CorpEmp> corpEmpList) {
	
		for (CorpEmp corpEmp : corpEmpList) {
			insertCorpEmp(corpEmp);
		}
	
	}


	public List<CorpEmp> getCorpEmpList(Integer corpCustId) {
	
		return getSession().createCriteria(CorpEmp.class).add(Restrictions.eq("corpCustId", corpCustId)).add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE)).list();
	}

	@Override
	public void insertAsset(Equipment eq) {
    	
		getSession().saveOrUpdate(eq);

    }

	@Override
	public List<Customer> getSearchEndUserCustomerList(String openText) {
		// TODO Auto-generated method stub
		return (List<Customer>)getSession().createCriteria(Customer.class).list().get(0);	
	}

	
    public void deleteEquipment(String id) {
    	
    	Equipment   e = (Equipment)getSession().createCriteria(Equipment.class).add(Restrictions.eq("assetId", id)).list().get(0);
		e.setStatus(AppConstants.STATUS_INACTIVE);
		getSession().update(e);

    }
    
    public void updateEquipments(Equipment e) {
    	
		getSession().saveOrUpdate(e);

    }
    
	public List<Equipment> getEquipmentList() {
	    	
			return getSession().createCriteria(Equipment.class).add(Restrictions.eq("deleteFlag", AppConstants.DELETEFLAG_ALIVE+""))
					.list();
    }
	
	public Equipment getEquipment(String assetId) {
		
		return (Equipment)getSession().createCriteria(Equipment.class).add(Restrictions.eq("assetId", assetId))
				.list().get(0);
	}

    public void saveUpdateEquipDriver(EquipDriver e) {
    	
		getSession().saveOrUpdate(e);

    }
    
	public List<EquipDriver> getEquipDriverList() {
	    	
			return getSession().createCriteria(EquipDriver.class).add(Restrictions.eq("deleteFlag", AppConstants.DELETEFLAG_ALIVE+""))
					.list();
    }

	public EquipDriver getEquipDriver(Integer driverId) {
    	
		return (EquipDriver)getSession().createCriteria(EquipDriver.class).add(Restrictions.eq("driverId", driverId))
				.list().get(0);
	}

	
	public Map<String,String> getDashboard() {
		
		String dateFilter ="";
		dateFilter += " and  DATE(cr.rideStartDate) = CURDATE()";
		
		String sql = "select count(*)  as count, cr.rideTotalAmt as rideTotalAmt "
				+ "from tb_customerridedetails as cr "
				+ "where cr.status = 'COM' "
				+ dateFilter +" ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String,String> data= (Map<String,String>)query.list().get(0);
		
		return data;
	}
	public String getCustCount() {
		
		
		String sql = "select count(*) count "
				+ "from tb_customer as dr "
				+ "where dr.status = 'A' ";
		
		Query query = getSession().createSQLQuery(sql);
		String  data= ((BigInteger)query.list().get(0)).toString();
		
		return data;
	}

	public String getCSECount() {
		
		
		String sql = "select count(*) count "
				+ "from tb_driver as dr "
				+ "where dr.deleteFlag = 0 ";
		
		Query query = getSession().createSQLQuery(sql);

		String  data = ((BigInteger)query.list().get(0)).toString();
		
		
		return data;
	}
	
	
	public List<String> getActiveCse() {
		
		List<String>  data = new ArrayList<String>();
		String sql = "select count(*) count "
				+ "from tb_drlocation as dr "
				+ "where dr.status = 'R' and serviceType = 'TX' ";
		
		Query query = getSession().createSQLQuery(sql);
		data.add(((BigInteger)query.list().get(0)).toString());
		
		sql = "select count(*) count "
				+ "from tb_drlocation as dr "
				+ "where dr.status = 'R' and serviceType = 'PT' ";
		
		query = getSession().createSQLQuery(sql);
		
		
		data.add(((BigInteger)query.list().get(0)).toString());
		
		sql = "select count(*) count "
				+ "from tb_driver as dr "
				+ "where dr.deleteflag = 0 and serviceType = 'TX' ";
		
		query = getSession().createSQLQuery(sql);
		data.add(((BigInteger)query.list().get(0)).toString());
		sql = "select count(*) count "
				+ "from tb_driver as dr "
				+ "where dr.deleteflag = 0 and serviceType = 'PT' ";
		
		query = getSession().createSQLQuery(sql);
		data.add(((BigInteger)query.list().get(0)).toString());
		
		return data;
	}

	@Override
	public List<Payment> getPaymentList(Integer corpCustId) {
		return getSession().createCriteria(Payment.class).add(Restrictions.eq("status", AppConstants.STATUS_ACTIVE)).add(Restrictions.eq("corpCustId", corpCustId)).list();
	}
	@Override
	public void savePayment(Payment payment) {
		// TODO Auto-generated method stub
		getSession().saveOrUpdate(payment);
		PayWayAPIUtil payway = new PayWayAPIUtil();
		String success = payway.addCustomer(payment);
		
		if(!success.equals("0") ){
			logger.error("adding PAYWAY Failed");
		}
	}
	@Override
	public void deletePayment(Integer paymentId) {
    	Payment   p = (Payment)getSession().createCriteria(Payment.class).add(Restrictions.eq("paymentId", paymentId)).list().get(0);
		p.setDeleteFlag(AppConstants.DELETEFLAG_DELETED);
		p.setStatus(AppConstants.STATUS_INACTIVE);
		getSession().update(p);
		
	}

	@Override
    public void deleteCorpEmp(Integer id) {
    	
    	CorpEmp   c = (CorpEmp)getSession().createCriteria(CorpEmp.class).add(Restrictions.eq("corpEmpId", id)).list().get(0);
		c.setStatus(AppConstants.STATUS_INACTIVE);
		getSession().update(c);

    }

	
	public List<Driver> getDriverForAssetDriver(){

		String sql = "select dr.driverId, dr.fname, dr.lname, dr.cseCode  "
				+ "from tb_driver as dr "
				+ "where dr.deleteflag = 0 and driverId not in (select driverId from tb_eqp_driver where deleteflag = 0)";
		
		Query query = getSession().createSQLQuery(sql);
		
		query.setResultTransformer(Transformers.aliasToBean(Driver.class));
		return (List<Driver>)query.list();
	}
	public List<Equipment> getAssetList(){

		String sql = "select e.assetId, e.serialNumber  "
				+ "from tb_equipment as e "
				+ "where e.deleteflag = 0 and assetId not in (select assetId from tb_eqp_driver where deleteflag = 0)";
		
		Query query = getSession().createSQLQuery(sql);
		
		query.setResultTransformer(Transformers.aliasToBean(Equipment.class));
		return (List<Equipment>)query.list();
	}
	public String getCorpCustCount(Integer corpCustId) {
		
		String sql = "select count(*) count "
				+ "from tb_customer c left join tb_customeraccount ca on c.customerId = ca.customerId "
				+ "where c.status = 'A'  and corpCustId = :corpCustId ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpCustId", corpCustId);
		
		String  data= ((BigInteger)query.list().get(0)).toString();
		
		return data;
	}
	public String getCorpCustTotal(Integer corpCustId) {
		
		String sql = "select count(*) count  "
				+ " from tb_corpemp ce "
				+ "where ce.status = 'A'  and corpCustId = :corpCustId ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpCustId", corpCustId);
		
		String  data= ((BigInteger)query.list().get(0)).toString();
		
		return data;
	}

	public Map<String,String> getCorpDashboard(Integer corpCustId) {
		
		String dateFilter ="";
		dateFilter += " and  DATE(cr.rideStartDate) = CURDATE()";
		
		String sql = "select count(*)  as count, cr.rideTotalAmt as rideTotalAmt "
				+ "from tb_customerridedetails as cr left join tb_customerbooking cb "
				+ "on cb.bookingId = cr.bookingId "
				+ "where cr.status = 'COM' and corpCustId = :corpCustId "
				+ dateFilter +" ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("corpCustId", corpCustId);
		Map<String,String> data= (Map<String,String>)query.list().get(0);
		
		return data;
	}
	public String getLoayltiPointsCorpCust(Integer corpCustId) {
		
		
		String sql = "select IFNULL(rewardPoints, 0) as rewardPoints "
				+ "from tb_corporatecustomer as cc "
				+ "where cc.corpCustId = :corpCustId ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("corpCustId", corpCustId);
		
		String  data= ((BigDecimal)query.list().get(0)).toString();
		
		return data;
	}
		
	@Override
	public List<Customer> getEndCustomerList() {
		
		String sql = "select customerId, fname, lname, referralCode, rewardPoints, mobile, mname, email "
				+ "from tb_customer as c "
				+ "where c.deleteFlag = 0 and c.status = 'A'";
		
		Query query = getSession().createSQLQuery(sql);
		
		query.setResultTransformer(Transformers.aliasToBean(Customer.class));
		return (List<Customer>)query.list();

	}	
}

