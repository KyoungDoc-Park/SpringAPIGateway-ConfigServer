package com.kdpark.zuul.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EtcException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public EtcException(String msg, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated constructor stub
	}

	public void MyException(String msg) {
	}
 }