package org.yenbo.jetty.exception;

import org.apache.commons.lang3.exception.ExceptionContext;

public class AccessTokenExistedException extends OAuth2DemoException {

	private static final long serialVersionUID = 7084133186897116775L;

	public AccessTokenExistedException() {
		super();
	}

	public AccessTokenExistedException(String message) {
		super(message);
	}

	public AccessTokenExistedException(Throwable cause) {
		super(cause);
	}

	public AccessTokenExistedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessTokenExistedException(String message, Throwable cause, ExceptionContext context) {
		super(message, cause, context);
	}
}
