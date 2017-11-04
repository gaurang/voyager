package com.cd.voyager.common.util;

public class ValidateUtils {

	public static boolean validatEmail(String email){
	 //String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Boolean b = email.matches(EMAIL_REGEX);
		     // System.out.println("is e-mail: "+email+" :Valid = " + b);
		return b;
	}
	
	public static boolean validatName(String name){
		 String NAME_REGEX = "[A-Z][a-zA-Z]*";
		 Boolean b = name.matches(NAME_REGEX);
		 // System.out.println("is e-name: "+NAME+" :Valid = " + b);
		 return b;
	}
	
	public static boolean validatCellNumber(String  phoneNo){
		 //String CELL_REGEX = "\\d{3}-\\d{7}";
		
		//validate phone numbers of format "1234567890"
        if (phoneNo.matches("^\\[1-9]{1}[0-9]{3,14}$")) return true;
        //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
        //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
        //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
        //return false if nothing matches the input
        else return false;
	}
	
	/*
	 * 
	 * Luhn is an implementation of the Luhn algorithm that checks validity of a credit card number.
	 */
        public static boolean ValidateCC(String ccNumber)
        {
                int sum = 0;
                boolean alternate = false;
                for (int i = ccNumber.length() - 1; i >= 0; i--)
                {
                        int n = Integer.parseInt(ccNumber.substring(i, i + 1));
                        if (alternate)
                        {
                                n *= 2;
                                if (n > 9)
                                {
                                        n = (n % 10) + 1;
                                }
                        }
                        sum += n;
                        alternate = !alternate;
                }
                return (sum % 10 == 0);
        }
	
        
}
