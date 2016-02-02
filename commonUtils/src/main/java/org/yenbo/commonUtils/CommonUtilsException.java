package org.yenbo.commonUtils;

import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.exception.ExceptionContext;

public class CommonUtilsException extends ContextedRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8650311563885256088L;

	public CommonUtilsException() {
		super();
	}

	public CommonUtilsException(String message) {
		super(message);
	}

	public CommonUtilsException(Throwable cause) {
		super(cause);
	}

	public CommonUtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonUtilsException(String message, Throwable cause, ExceptionContext context) {
		super(message, cause, context);
	}
}
