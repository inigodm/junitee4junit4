package com.inigo.testing.exceptions;

public class UnitTestingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnitTestingException(Exception e) {
		super(e);
	}

	public UnitTestingException(String string) {
	   super(string);
	}

}
