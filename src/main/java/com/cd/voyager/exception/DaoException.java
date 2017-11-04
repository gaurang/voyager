/**
 * Copyright ® 2007 Vividlime Co. Ltd. 
 * All rights reserved. 
 */

package com.cd.voyager.exception;

/**
 * exception extends from RootException
 * all data access classes should throw this exception or its sub-class;
 * 
 * 
 * @author Eskalate
 * @version 1.0
 * @since 1.0
 */
public class DaoException extends RootException {

	private static final long serialVersionUID = -6702816577391337435L;

	public DaoException() {
		super();
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

}
