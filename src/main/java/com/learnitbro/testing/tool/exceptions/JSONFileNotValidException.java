package com.learnitbro.testing.tool.exceptions;

@SuppressWarnings("serial")
public class JSONFileNotValidException extends RuntimeException {
	public JSONFileNotValidException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

	public JSONFileNotValidException(String errorMessage) {
		super(errorMessage);
	}

	public JSONFileNotValidException() {
		super();
	}
}