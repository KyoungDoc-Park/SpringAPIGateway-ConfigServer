
package com.kdpark.zuul.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.kdpark.zuul.service.GatewayService;



@Component
public class EtcExceptionResolver implements HandlerExceptionResolver {
	@Autowired GatewayService gatewayService;
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		
		gatewayService.setErrorMsg(request, response, 404, "Not Found");

		ModelAndView model = new ModelAndView();
		model.setViewName("error");
		
		return model;
	}
}