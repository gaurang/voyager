package com.cd.voyager.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.cd.voyager.entities.Booking;
import com.cd.voyager.mobility.service.IMobileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@Configurable
@PropertySource("classpath:messages_en.properties")
public class GCMSender implements Runnable {

    private String gcmRegId;
    
    private Object obj;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Environment env;

	private IMobileService mobileServiceImpl;


    public GCMSender(Object obj, String gcmRegId, Environment env,IMobileService mobileServiceImpl) {
    	this.obj = obj;
    	this.gcmRegId = gcmRegId;
    	this.env =  env;
    	this.mobileServiceImpl = mobileServiceImpl;
		// TODO Auto-generated constructor stub
	}
   // The SENDER_ID here is the "Browser Key" that was generated when I
   // created the API keys for my Google APIs project.
    private static final String SENDER_ID = "AIzaSyCwiY_bkEJXNAmYcsNkG0Cfkxh7gyvhDnc";
    

	public void sendBookingMessage(Object object, String regId){
		
		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);
		ObjectMapper om = new ObjectMapper();
		 
		// This Message object will hold the data that is being transmitted
		// to the Android client devices.  For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		 
		// If multiple messages are sent using the same .collapseKey()
		// the android target device, if it was offline during earlier message
		// transmissions, will only receive the latest message for that key when
		// it goes back on-line.
		
		try {
		Booking bookingObj = (Booking) this.obj;	
		System.out.println(om.writeValueAsString(bookingObj)+"   hhhh ");
		Message message = new Message.Builder()
		.collapseKey("GCM_Message")
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", om.writeValueAsString(bookingObj))
		.build();
		 System.out.println(message.getData().toString());
		    // use this for multicast messages.  The second parameter
		    // of sender.send() will need to be an array of register ids.
		    Result result = sender.send(message, regId, 1);
		     
		    if (result.getMessageId() != null) {
		        String canonicalRegId = result.getCanonicalRegistrationId();
		        if (canonicalRegId != null) {
		             
		        }
		    } else {
		        String error = result.getErrorCodeName();
		        System.out.println("Broadcast failure: " + error);
		    }
		     
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

public void sendCustomerMessage(Object object, String regId){
		
		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);
		//ObjectMapper om = new ObjectMapper();
		 
		// This Message object will hold the data that is being transmitted
		// to the Android client devices.  For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		 
		// If multiple messages are sent using the same .collapseKey()
		// the android target device, if it was offline during earlier message
		// transmissions, will only receive the latest message for that key when
		// it goes back on-line.
		
		try {
		Booking bookingObj = (Booking) this.obj;	
		//System.out.println(om.writeValueAsString(bookingObj)+"   hhhh ");
		Message message = new Message.Builder()
		.collapseKey("GCM_Message")
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", object.toString())
		.build();
		 System.out.println(message.getData().toString());
		    // use this for multicast messages.  The second parameter
		    // of sender.send() will need to be an array of register ids.
		    Result result = sender.send(message, regId, 1);
		     
		    if (result.getMessageId() != null) {
		        String canonicalRegId = result.getCanonicalRegistrationId();
		        if (canonicalRegId != null) {
		             
		        }
		    } else {
		        String error = result.getErrorCodeName();
		        System.out.println("Broadcast failure: " + error);
		    }
		     
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendBookingMessage(this.obj, this.gcmRegId);
		Booking book = (Booking) this.obj;
		
		try {
			Thread.sleep(15000l);
			mobileServiceImpl.bookingTimeOut(book);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.debug("ERROR IN GCM THREAD  "+e.getMessage());
			e.printStackTrace();
			
		}
		
		
	}
	
	
	public static void main(String[] args) {
		
		//GCMSender s = new GCMSender(obj, gcmRegId, env, mobileServiceImpl)
		String regId  = "fUEo71W_eQU:APA91bHz8EuSV6Ox3zgWoAurOQmy9dAnlh2a9lCKyHZVR16oW10qDGMFV-OETgqPd_8AblMHJ3C_652FSWDa-MGtMZuLRCjDvBNTHcUsEgqPhrEbSDDxoQdTNnkbkO7IGZ4mt9M3rSYedfs";
		Sender sender = new Sender(SENDER_ID);
		try{
		Message message = new Message.Builder()
		.collapseKey("GCM_Message")
		.timeToLive(30)
		.delayWhileIdle(true)
		.addData("message", "Test")
		.build();
		 System.out.println(message.getData().toString());
		    // use this for multicast messages.  The second parameter
		    // of sender.send() will need to be an array of register ids.
		    Result result = sender.send(message, regId, 1);
		     
		    if (result.getMessageId() != null) {
		        String canonicalRegId = result.getCanonicalRegistrationId();
		        if (canonicalRegId != null) {
		             
		        }
		    } else {
		        String error = result.getErrorCodeName();
		        System.out.println("Broadcast failure: " + error);
		    }
		     
		} catch (Exception e) {
		    e.printStackTrace();
		}

		
	}
}