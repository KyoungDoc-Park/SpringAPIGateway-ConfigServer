package com.kdpark.zuul.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kdpark.zuul.exception.EtcException;

/**
 * configuration서버에 없는 URL은 Exception 처리 
 * @author kdpark
 *
 */
@Controller
public class GatewayContoller {
	
	@RequestMapping(method=RequestMethod.GET)
	public void callGETMethod(HttpServletRequest request, HttpServletResponse response) {
		throw new EtcException("NotFound", request, response);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void callPOSTMethod(HttpServletRequest request, HttpServletResponse response) {
		throw new EtcException("NotFound", request, response);
	}
}
