package com.learnitbro.testing.tool.exceptions;

@SuppressWarnings("serial")
public class FailedTestException extends RuntimeException {
	public FailedTestException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

	public FailedTestException(String errorMessage) {
		super(errorMessage);
	}

	public FailedTestException() {
		super();
	}
}