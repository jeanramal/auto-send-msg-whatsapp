package com.jeanramal.autosendmsgwhatsapp.util;

public class InvalidNumber extends Exception {

	private static final long serialVersionUID = -1590193001519558931L;
	
	public InvalidNumber(String arg0) {
		super(arg0);
	}
	
	public InvalidNumber(Throwable arg0) {
		super(arg0);
	}
	
	public InvalidNumber(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
