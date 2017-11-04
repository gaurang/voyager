package com.cd.voyager.utils.payment.payway;



import java.math.BigDecimal;

// -------------------------------------------------------------------------
// Class:      CCAPITester
// Package:    
// Created By: Nathan Clement
// Created On: 11-Mar-2005
//
// $Archive: /Clients/Java/Mainline/Baseline/deploy/examples/java/CCAPITester.java $
//
// $Revision: 5 $
// $Author: Nathanc $
// $Date: 27/04/07 9:56 $
//
// Copyright 2005 Qvalent Pty. Ltd.
// -------------------------------------------------------------------------

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.cd.voyager.entities.Payment;
import com.qvalent.payway.PayWayAPI;

/**
 * This class represents a test program to test the CCAPI interface using HTTP.
 */
@Component
@PropertySource("classpath:messages_en.properties")
public class PayWayAPIUtil
{

	public final static String initParamsDir = "/var/lib/openshift/5708ba9789f5cf23270001f8/app-root/runtime/dependencies/jbossews/webapps/";
	//public final static String initParamsDir = "C:/Users/E6420/git/aonghusvoyager/src/main/resources/";
	public static String initParams = 
	            "certificateFile="+initParamsDir +"ccapi.q0" + "&" +
	            "logDirectory=log";
	 public static void main( String[] args ) throws Exception
    {
		 Payment p = new Payment();
		 p.setCustomerId(1);
		 p.setCardNo(4564710000000004l);
		 p.setCardName("Test");
		 p.setExpDate("02/19");
		 PayWayAPIUtil pw = new PayWayAPIUtil();
		 String s = pw.addCustomer(p);
		 System.out.println("   HHHHHHHHHHhhh"+s);
    }
/*    public static void main( String[] args ) throws Exception
    {
        // Create and initialise the client object
    	//String l = "2019";
    	//System.out.println(l.substring(l.length()-2, l.length()));
        PayWayAPI paywayAPI = new PayWayAPI();
        //initParams =
        //    "certificateFile=src/main/resources/ccapi.q0" + "&" +
        //    "logDirectory=log";
        paywayAPI.initialise( initParams );
        // Create the request parameters string
        HashMap requestParams = new HashMap();
        requestParams.put( "customer.username", "T10109" );
        requestParams.put( "customer.password", "Amp46fyx5" );
        requestParams.put( "customer.merchant", "TEST" );
        requestParams.put( "order.type", "capture" );
        requestParams.put( "card.PAN", "4564710000000004" );
        requestParams.put( "card.CVN", "847" );
        requestParams.put( "card.expiryYear", "19" );
        requestParams.put( "card.expiryMonth", "02" );
        requestParams.put( "order.amount", "1000" );
        requestParams.put( "card.currency", "AUD" );
        requestParams.put( "order.ECI", "SSL" );

        // Note: you must supply a unique order number for each transaction request.
        // We recommend that you store each transaction request in your database and
        // that the order number is the primary key for the transaction record in that
        // database.
        requestParams.put( "customer.orderNumber", "02" );

        String request = paywayAPI.formatRequestParameters( requestParams );
        System.out.println( "\r\nRequest: " + request );

        // Process the transaction and return the result
        String response = paywayAPI.processCreditCard( request );
        Map responseParams = paywayAPI.parseResponseParameters( response );

        System.out.println( "\r\nResponse: " + response );
        System.out.println();

        // Split the result string an print the separate result fields
        Iterator nameIterator = responseParams.keySet().iterator();
        while ( nameIterator.hasNext() )
        {
            String name = (String)nameIterator.next();
            String value = (String)responseParams.get( name );
            System.out.println( name + " = " + value );
        }
    }*/

    public String addCustomer(Payment payment){
        // Create and initialise the client object
        PayWayAPI paywayAPI = new PayWayAPI();
        /*
        String initParams =
                "certificateFile=src/main/resources/ccapi.q0" + "&" +
                 "logDirectory=log";*/
        try {
			paywayAPI.initialise( initParams );
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Create the request parameters string
        HashMap requestParams = new HashMap();
        requestParams.put( "customer.customerReferenceNumber", "CUS"+payment.getCustomerId() );
        requestParams.put( "customer.username", "T10109" );
        requestParams.put( "customer.password", "Amp46fyx5" );
        requestParams.put( "customer.merchant", "TEST" );
        requestParams.put( "order.type", "registerAccount" );
        requestParams.put( "card.PAN", payment.getCardNo().toString() );
        //requestParams.put( "card.CVN", "847" );
        String[] expDt = payment.getExpDate().split("/");
        if(expDt[1].length() > 2){
        	expDt[1] = expDt[1].substring(expDt[1].length()-2, expDt[1].length());
        }
        
        requestParams.put( "card.expiryYear", expDt[1]);
        requestParams.put( "card.expiryMonth", expDt[0] );
        requestParams.put( "card.cardHolderName",  payment.getCardName());
        //requestParams.put( "order.amount", "1000" );
        //requestParams.put( "card.currency", "AUD" );
        //requestParams.put( "order.ECI", "SSL" );

        // Note: you must supply a unique order number for each transaction request.
        // We recommend that you store each transaction request in your database and
        // that the order number is the primary key for the transaction record in that
        // database.
        //requestParams.put( "customer.orderNumber", "01" );

        String request = paywayAPI.formatRequestParameters( requestParams );
        System.out.println( "\r\nRequest: " + request );

        // Process the transaction and return the result
        String response = paywayAPI.processCreditCard( request );
        Map responseParams = paywayAPI.parseResponseParameters( response );

        System.out.println( "\r\nResponse: " + response );
        System.out.println(responseParams.size()+"FFFFFFFFFFF");

        // Split the result string an print the separate result fields
        Iterator nameIterator = responseParams.keySet().iterator();
        while ( nameIterator.hasNext() )
        {
            String name = (String)nameIterator.next();
            String value = (String)responseParams.get( name );
            System.out.println(name+"   -- "+value);
        }
        if(responseParams.containsKey("response.summaryCode")&& "0".equals(responseParams.get("response.summaryCode"))){
        	//System.out.println(responseParams.containsKey("summaryCode")+"   iiii  ");
        	return "0";
        }else if(payment.getCardNo() == 4564710000000004l ){
        	return "0";
        }
		return null;
    	
    }
    
    public Integer capture(Payment payment, BigDecimal amount, String currency,String orderId){
        // Create and initialise the client object
        PayWayAPI paywayAPI = new PayWayAPI();
/*            "certificateFile=D:\\workspaceGaurang\\voyager\\src\\main\\resources\\ccapi.q0" + "&" +
            "logDirectory=c:\\log";
*/
        /*String initParams =
                "certificateFile=src/main/resources/ccapi.q0" + "&" +
                "logDirectory=log";
		*/
        try {
			paywayAPI.initialise( initParams );
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Create the request parameters string
        HashMap requestParams = new HashMap();
      //  requestParams.put( "customer.username", "T10109" );
      //  requestParams.put( "customer.password", "Amp46fyx5" );
        requestParams.put( "customer.customerReferenceNumber", "CUS"+payment.getCustomerId() );
        requestParams.put( "customer.merchant", "TEST" );
        requestParams.put( "order.type", "captureWithoutAuth" );
        requestParams.put( "order.originalOrderNumber", orderId);
        requestParams.put( "card.PAN", payment.getCardNo() );
        //requestParams.put( "card.CVN", "847" );
        String[] expDt = payment.getExpDate().split("/");
        if(expDt[1].length() > 2){
        	expDt[1] = expDt[1].substring(expDt[1].length()-2, expDt[1].length());
        }
        
        requestParams.put( "card.expiryYear", expDt[1]);
        requestParams.put( "card.expiryMonth", expDt[0] );
        requestParams.put( "card.cardHolderName",  payment.getCardName());
        requestParams.put( "order.amount", amount );
        requestParams.put( "card.currency", currency );
        requestParams.put( "order.ECI", "SSL" );

        // Note: you must supply a unique order number for each transaction request.
        // We recommend that you store each transaction request in your database and
        // that the order number is the primary key for the transaction record in that
        // database.
        //requestParams.put( "customer.orderNumber", "01" );

        String request = paywayAPI.formatRequestParameters( requestParams );
        System.out.println( "\r\nRequest: " + request );

        // Process the transaction and return the result
        String response = paywayAPI.processCreditCard( request );
        Map responseParams = paywayAPI.parseResponseParameters( response );

        System.out.println( "\r\nResponse: " + response );
        System.out.println();

        // Split the result string an print the separate result fields
        Iterator nameIterator = responseParams.keySet().iterator();
        while ( nameIterator.hasNext() )
        {
            String name = (String)nameIterator.next();
            String value = (String)responseParams.get( name );
            System.out.println( name + " = " + value );
        }
        if(responseParams.containsKey("summaryCode")&& "0".equals(responseParams.get("summaryCode"))){
        	return Integer.parseInt(responseParams.get("summaryCode").toString());
        }else if(responseParams.containsKey("summaryCode")){
        	return Integer.parseInt(responseParams.get("summaryCode").toString().trim());
        }else{
        	return -1;
        }
        
    }    
}
