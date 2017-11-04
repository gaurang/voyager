/**
 * Copyright ® 2007 Eskalate Co. Ltd. 
 * All rights reserved. 
 * $id$
 */

package com.cd.voyager.exception;

/**
 * Extends from RootException
 * all services should throw this exception or its sub-class
 * 
 * @author zhangliang
 * @version 1.0
 * @since 1.0
 * 
 */
public class ServiceException extends RootException {

	private static final long serialVersionUID = -1424775182024890621L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
