package com.cd.voyager.exception;

public class InvalidCredentialsException extends RootException{


	private static String errorMessage;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7038339407898628100L;


	public InvalidCredentialsException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}


	public InvalidCredentialsException(Throwable cause) {
		super(cause);
	}


	@Override
	public String getMessage() {
		return this.errorMessage;
	}
	
	

}
