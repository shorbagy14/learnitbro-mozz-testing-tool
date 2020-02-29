package com.learnitbro.testing.tool.exceptions;

@SuppressWarnings("serial")
public class CopyFileException extends RuntimeException {
	public CopyFileException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

	public CopyFileException(String errorMessage) {
		super(errorMessage);
	}

	public CopyFileException() {
		super();
	}
}