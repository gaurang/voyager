package com.cd.voyager.common.util;

import java.security.SecureRandom;
import java.util.Random;

public class StringGenUtils {

	private static final char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
			.toCharArray();

	public static String randomString(char[] characterSet, int length) {
		Random random = new SecureRandom();
		char[] result = new char[length];
		for (int i = 0; i < result.length; i++) {
			// picks a random index out of character set > random character
			int randomCharIndex = random.nextInt(characterSet.length);
			result[i] = characterSet[randomCharIndex];
		}
		return new String(result);
	}

	public static String randomString(int length) {
		return randomString(CHARSET_AZ_09, length);
	}

	public static boolean isEmpty(String value, String paramName,
			String errMessage) {
		if (org.springframework.util.StringUtils.isEmpty(value)) {
			// header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
			errMessage += "Invalid Email";
			return false;
		} else {
			return true;
		}

	}

	public static boolean isEmpty(String value, String paramName,
			String errMessage, boolean err) {
		if (org.springframework.util.StringUtils.isEmpty(value)) {
			// header.setStatusCode(voyagerConstants.ERROR_INVALID_INPUT);
			errMessage += errMessage.replace("{{element}}", paramName);
			err = (err == true ? false : err);
			return false;
		} else {
			return true;
		}

	}

	public static String replaceProperty(String message, String paramName, String paramValue) {
			return message.replace("{{"+paramName+"}}", paramName);
	}
	
	public static String replaceProperty(String message, String paramValue) {
		return replaceProperty(message,"element",paramValue);
	}
	
	 public static String geratePIN() {
	        Integer randomPIN= (int)(Math.random()*9000)+1000;
	        String PINString = String.valueOf(randomPIN);
	        return 	PINString;
	 }
	 
	 public static String gerateRefCode(String fname, String lname) {
		 	String refCode = "";
		 	Integer endIdx = 2;
		 	if(fname.length() >= 3){
		 		refCode += fname.substring(0, endIdx);
		 	}else{
		 		refCode += fname;
		 	}
		 	
			if(lname.length() >= 3){
		 		refCode += lname.substring(0, endIdx);
		 	}else{
		 		refCode += lname;
		 	}
		 
		 	Integer number= (int)(Math.random()*900)+100;
	        return 	"UC"+refCode+number;
	 }

	 public static String gerateRefCode(String fname, String lname, String type) {
		 	String refCode = "";
		 	Integer endIdx = 2;
		 	if(fname.length() >= 3){
		 		refCode += fname.substring(0, endIdx);
		 	}else{
		 		refCode += fname;
		 	}
		 	
			if(lname.length() >= 3){
		 		refCode += lname.substring(0, endIdx);
		 	}else{
		 		refCode += lname;
		 	}
		 
		 	Integer number= (int)(Math.random()*900)+100;
	        return 	type+refCode+number;
	 }

}
