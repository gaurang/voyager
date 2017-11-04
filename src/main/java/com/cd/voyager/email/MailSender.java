package com.cd.voyager.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.mail.MessagingException;

public interface MailSender {
	
	public void sendEMail(String subject, String from, String to, String cc, String replyTo, String template, HashMap<String,String>  propertyMap) throws FileNotFoundException, IOException, MessagingException;
	
	//public Integer sendMail(String subject, String from, String to, String cc, String replyTo, String template), new HashMap() {{put("test1", "test1"); put("test", "test");}});
	
}
