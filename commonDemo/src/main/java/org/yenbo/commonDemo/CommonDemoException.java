package org.yenbo.commonDemo;

import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.exception.ExceptionContext;

public class CommonDemoException extends ContextedRuntimeException {

	private static final long serialVersionUID = -2749940314794523982L;

	public CommonDemoException() {
		super();
	}

	public CommonDemoException(String message) {
		super(message);
	}

	public CommonDemoException(Throwable cause) {
		super(cause);
	}

	public CommonDemoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonDemoException(String message, Throwable cause, ExceptionContext context) {
		super(message, cause, context);
	}
}
