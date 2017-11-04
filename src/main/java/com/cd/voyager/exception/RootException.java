/**
 * Copyright ® 2007 Vividlime Co. Ltd. 
 * All rights reserved. 
 */

package com.cd.voyager.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * root exception extends from RuntimeExcetpion
 * all other exceptions should extends from this exception or its sub-class
 * 
 * @author Eskalate
 * @version 1.0
 * @since 1.0
 * 
 */
public class RootException extends RuntimeException {

	private static final long serialVersionUID = -8270460880155405808L;

	private Throwable cause;

	/**
	 * Constructs a new exception with null as its detail message.
	 *
	 */
	public RootException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message the detail message.
	 *
	 */
	public RootException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param  t the cause. A null value is permitted, and indicates that
	 * the cause is nonexistent or unknown.
	 *
	 */
	public RootException(Throwable t) {
		super(t.getMessage());
		try {
			initCause(t);
		} catch (Throwable e) {
		}
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message the detail message.
	 * @param  t the cause. A null value is permitted, and indicates that
	 * the cause is nonexistent or unknown.
	 */
	public RootException(String message, Throwable t) {
		super(message);
		try {
			initCause(t);
		} catch (Throwable e) {
		}
	}

	/**
	 * Initializes the cause of this exception to the specified value.
	 * @param t the cause.
	 * @return a reference to this instance.
	 * @exception   IllegalArgumentException
	 * @exception   IllegalStateException
	 */
	public Throwable initCause(Throwable t) throws IllegalArgumentException,
			IllegalStateException {
		cause = t;
		return this;
	}

	/**
	 * Returns the cause of this exception.
	 *
	 * @return   a cause for this exception if any, null otherwise
	 *
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * Method printStackTrace prints the stack trace to the <i>ps</i> PrintStream.
	 *
	 * @param ps PrintStream used for the output.
	 */
	public void printStackTrace(PrintStream ps) {
		if (null != cause) {
			cause.printStackTrace(ps);
		}
		super.printStackTrace(ps);
	}

	/**
	 * Method printStackTrace prints the stack trace to the <i>pw</i> PrintWriter.
	 *
	 * @param pw PrintWriter used for the output.
	 */
	public void printStackTrace(PrintWriter pw) {
		if (null != cause) {
			cause.printStackTrace(pw);
		}
		super.printStackTrace(pw);
	}

	/**
	 * Method printStackTrace prints the stack trace to the standard error stream.
	 */
	public void printStackTrace() {
		printStackTrace(System.err);
	}

}
