package org.yenbo.jetty.exception;

import org.apache.commons.lang3.exception.ExceptionContext;

public class RefreshTokenExistedException extends OAuth2DemoException {

	private static final long serialVersionUID = -9211434608691169600L;

	public RefreshTokenExistedException() {
		super();
	}

	public RefreshTokenExistedException(String message) {
		super(message);
	}

	public RefreshTokenExistedException(Throwable cause) {
		super(cause);
	}

	public RefreshTokenExistedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefreshTokenExistedException(String message, Throwable cause, ExceptionContext context) {
		super(message, cause, context);
	}
}
