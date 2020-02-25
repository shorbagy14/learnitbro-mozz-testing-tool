package com.learnitbro.testing.tool.exceptions;

@SuppressWarnings("serial")
public class ReadFileException extends RuntimeException {
	public ReadFileException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

	public ReadFileException(String errorMessage) {
		super(errorMessage);
	}

	public ReadFileException() {
		super();
	}
}