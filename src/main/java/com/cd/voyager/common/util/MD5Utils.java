/**
 * Copyright ® 2007 Vividlime Co. Ltd. 
 * All rights reserved. 
 */
package com.cd.voyager.common.util;

import java.security.MessageDigest;

/**
 *  Encode the string according to MD5
 * 
 * @author Eskalate
 * @version 1.0
 * @since 1.0
 *
 */
public class MD5Utils {

	/**
	 * encode the string according to MD5
	 * 
	 * @param s 
	 *       the string want to encode
	 * @return
	 *       the encoded String
	 */
	public final static String to_MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
    
    



     
  


}
