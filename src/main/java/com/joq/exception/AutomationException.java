package com.joq.exception;

@SuppressWarnings("serial")
public class AutomationException extends Exception {
	/**
	 * User defined exception constructor with message
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-04-2020
	 * @modified 03-05-2023
	 * @param message
	 */
	public AutomationException(String message) {
		super(message);
	}

	/**
	 * User defined exception constructor with message and cause of exception
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-04-2020
	 * @modified 03-05-2023
	 * @param message
	 * @param cause
	 * @throws AutomationException
	 */
	public AutomationException(String message, Throwable cause) throws AutomationException {
		super(message, cause);
	}

	/**
	 * User defined exception constructor with cause of exception
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-04-2020
	 * @modified 03-05-2023
	 * @param cause
	 * @throws AutomationException
	 */
	public AutomationException(Throwable cause) throws AutomationException {
		super(cause);
	}
}
