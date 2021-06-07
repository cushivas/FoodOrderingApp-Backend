package com.upgrad.FoodOrderingApp.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SaveOrderException extends Exception {
	private final String code;

	private final String errorMessage;

	public SaveOrderException(String code, String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}

	public String getCode() {
		return code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	@Override
	public void printStackTrace(PrintStream arg0) {
		super.printStackTrace(arg0);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
	}

}
