package com.cd.voyager.common.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages_en.properties")
public class SMSUtil {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Environment env;

	 public boolean sendSMS(String tpl, String mobile, String param, Environment env ) {
		 	String message = tpl.replace("[OTP]", param);
		 	return sendSMS(message, mobile, env); 
	    }
	 
	 public String getMessageTemplate(String msg, Map<String, String> paramMap, Environment env ) {

		 String message = replacePlaceHolders(msg, paramMap);
		 return message;
	//	 return sendSMS(message, mobile, env);
	 }

	 public boolean sendSMS(String msg, String mobile, Map<String, String> paramMap, Environment env ) {

			 String message = replacePlaceHolders(msg, paramMap);
			 return sendSMS(message, mobile, env);
			 
    }
	 public boolean sendSMS(String msg, String mobile, Environment env ){
		 
		 this.env = env;
		 try{
				
			 String username = "sonaljs";//env.getProperty("SMS.prop.username")
			 String password = "6C1984FE-C2BC-2778-821B-F25809E44338";//env.getProperty("SMS.prop.passKey")
			 String originator = "voyager";//env.getProperty("SMS.prop.senderId")
			 String requestUrl  =  "https://api-mapper.clicksend.com/http/v2/send.php?"+   //env.getProperty("SMS.prop.url")
			  "username=" + URLEncoder.encode(username, "UTF-8") +
			  "&key=" + URLEncoder.encode(password, "UTF-8") +
			  "&to=" + URLEncoder.encode(mobile, "UTF-8") +
			  "&messagetype=SMS:TEXT" +
			  "&message=" + URLEncoder.encode(msg, "UTF-8");
			  //"&originator=" + URLEncoder.encode(originator, "UTF-8");
				 URL url = new URL(requestUrl);
				 System.out.println(requestUrl);
				/* HttpURLConnection uc = (HttpURLConnection)url.openConnection();
				 System.out.println(uc.getResponseMessage());
				 uc.disconnect();*/
		 } catch(Exception ex) {
			 logger.error("ERROR in SMS sedning - "+ex.getMessage());
			 ex.printStackTrace();
		 }
		 return true;

		 
	 }
	 public static String gerateOTP() {
	        Integer randomPIN= (int)(Math.random()*9000)+1000;
	        String PINString = String.valueOf(randomPIN);
	       // String PINString = "1111";
	        return 	PINString;
	    }
	 
	 
	 public String replacePlaceHolders(String input, Map<String,String> propertyMap){
			Pattern pattern = Pattern.compile("\\[(.+?)\\]");
			Matcher matcher = pattern.matcher(input);
			StringBuilder builder = new StringBuilder();
			int i = 0;
			while (matcher.find()) {
			    String replacement = propertyMap.get(matcher.group(1));
			    builder.append(input.substring(i, matcher.start()));
			    if (replacement == null)
			        builder.append(matcher.group(0));
			    else
			        builder.append(replacement);
			    i = matcher.end();
			}
			builder.append(input.substring(i, input.length()));
			input = builder.toString();
			return input;
	 }
	 
	 public String replacePlaceHolders(String input, String value){
			Pattern pattern = Pattern.compile("\\[(.+?)\\]");
			Matcher matcher = pattern.matcher(input);
			StringBuilder builder = new StringBuilder();
			int i = 0;
			builder.append(input.substring(i, input.length()));
			input = builder.toString();
			return input;
	 }
}
