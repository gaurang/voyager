package com.cd.voyager.testObject;

import com.cd.voyager.common.util.GCMSender;
import com.cd.voyager.entities.Booking;

public class Greeting {

	    private String content;

	    public Greeting(String content) {
	        this.content = content;
	    }

	    public String getContent() {
	        return content;
	    }

	    
	    public static void main(String[] args) {
	    	Booking booking = new Booking();
	    	booking.setBookingId(1);
	    	booking.setDriverName("Ajith");
	    	//GCMSender gcmSender = new GCMSender(booking, "");
			
	    	//gcmSender.sendBookingMessage(booking, "fyEARR1mSwc:APA91bFRYqQOu4BboKXG_avJrAW0lH3zs4E5vY5GGLYyCfHtqKAitjm307RYIZexmQl7CKAB2gZTwxvifNL_R8N_TxyOjOaP0FvSWtBOGUF4rFhCGerx-OvlpE8z36yefVdrUZLGBlZK");

		}
	}
