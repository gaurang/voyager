package com.cd.voyager.exception;

public class CorporateSignupWIthoutPayment extends RootException {

	 /* 
	 * @author Gaurang
	 * @version 1.0
	 * @since 1.0
	 * 
	 */

	private static final long serialVersionUID = -1424775182024890621L;

	public CorporateSignupWIthoutPayment() {
		super();
	}

	public CorporateSignupWIthoutPayment(String message) {
		super(message);
	}

	public CorporateSignupWIthoutPayment(Throwable cause) {
		super(cause);
	}

	public CorporateSignupWIthoutPayment(String message, Throwable cause) {
		super(message, cause);
	}

}
