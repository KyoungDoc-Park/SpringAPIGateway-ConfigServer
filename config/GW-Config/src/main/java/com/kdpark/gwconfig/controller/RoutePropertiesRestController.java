package com.kdpark.gwconfig.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdpark.gwconfig.response.ReturnEntity;
import com.kdpark.gwconfig.service.RoutePropertiesService;




@RestController
public class RoutePropertiesRestController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired RoutePropertiesService routePropertiesService;
	
	/**
	 * API 등록/수정/삭제
	 * 
	 * @param type
	 * @param params
	 * @return
	 */
	@PostMapping(value="/routeproperties/{type}", consumes=MediaType.APPLICATION_JSON_VALUE, produces= MediaType.APPLICATION_JSON_VALUE)
	public Object apiPropertiesProc(@PathVariable("type") String type, @RequestBody HashMap<String,String> params) {
		ReturnEntity ent = null;

		ent = routePropertiesService.routePropertiesManagement(type, params);
		
		if (ent != null) {
			if (ent.getResult().get("code") == "200") {
				/* 정책업데이트 */
				routePropertiesService.gatewayPolicyUpdate();
			}
		}

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("code", ent.getResult().get("code"));
		map.put("errMsg", ent.getResult().get("errMsg"));
		
		return map;
	}
	
}