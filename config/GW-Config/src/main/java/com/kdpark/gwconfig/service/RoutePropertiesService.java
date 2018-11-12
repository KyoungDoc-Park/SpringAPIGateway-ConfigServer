package com.kdpark.gwconfig.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kdpark.gwconfig.controller.RoutePropertiesRestController;
import com.kdpark.gwconfig.mapper.RoutePropertiesMapper;
import com.kdpark.gwconfig.response.ReturnEntity;
import com.kdpark.gwconfig.vo.RoutePropertiesVO;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Service
public class RoutePropertiesService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired RoutePropertiesMapper routePropertiesMapper;
	
	@Value("${gateway.role-update.url}") String gatewayUrl;

	/**
	 * gateway role update
	 */
	public void gatewayPolicyUpdate() {
		Response roleResponse = RestAssured.given().relaxedHTTPSValidation().and().with().when().post(gatewayUrl);
		roleResponse.jsonPath().prettyPrint();
	}
	

	/**
	 * API 등록/수정/삭제 처리
	 * 
	 * @param type
	 * @param params
	 * @return
	 */
	public ReturnEntity routePropertiesManagement(String type, HashMap<String,String> params) {
		ReturnEntity ent = new ReturnEntity();
		
		/* 기 등록여부 확인 */
		int chkVal = selectRoutePropertiesCnt(params);
		
		/* 등록 */
		if ("regist".equals(type)) {
			if (chkVal >= 2) {
				ent.setData("500", "이미 등록된 API 입니다.");
			} else {
				try {
					insertRouteProperties(params);
					ent.setData("200", "등록되었습니다.");
				} catch (Exception e) {
					ent.setData("500", "등록 시 오류가 발생하였습니다.");
				}
			}
		/* 수정 */
		} else if ("modify".equals(type)) {
			if (chkVal == 2) {
				try {
					updateRouteProperties(params);
					ent.setData("200", "수정되었습니다.");
				} catch (Exception e) {
					ent.setData("500", "수정 시 오류가 발생하였습니다.");
				}
			} else {
				ent.setData("500", "데이터가 존재하지 않습니다.");
			}
		/* 삭제 */	
		} else if ("delete".equals(type)) {
			if (chkVal == 2) {
				try {
					deleteRouteProperties(params);
					ent.setData("200", "삭제되었습니다.");
				} catch (Exception e) {
					ent.setData("500", "삭제 시 오류가 발생하였습니다.");
				}
			} else {
				ent.setData("500", "데이터가 존재하지 않습니다.");
			}
		} else {
			ent.setData("403", "잘못된 접근입니다.");
		}
		return ent;
	}
	
	
	/**
	 * 등록
	 * 
	 * @param params
	 * @return
	 */
	public void insertRouteProperties(HashMap<String,String> params) throws Exception {
		String groupKey = params.get("route_name");
		String groupPathValue = params.get("route_path");
		String groupUrlValue = params.get("route_dest");
		
		log.info("aaa :" + groupKey + "_/" + groupPathValue + "_/" + groupUrlValue);
		
		/* properties path */
		RoutePropertiesVO routePropertiesVO = new RoutePropertiesVO();
		routePropertiesVO.setGroupKey(groupKey);
		routePropertiesVO.setGroupType("path");
		routePropertiesVO.setGroupValue(groupPathValue);
		routePropertiesMapper.insertRouteProperties(routePropertiesVO);

		/* properties url */
		RoutePropertiesVO routePropertiesUrlVO = new RoutePropertiesVO();
		routePropertiesUrlVO.setGroupKey(groupKey);
		routePropertiesUrlVO.setGroupType("url");
		routePropertiesUrlVO.setGroupValue(groupUrlValue);
		routePropertiesMapper.insertRouteProperties(routePropertiesUrlVO);	
	}
	
	
	/**
	 * 수정
	 * 
	 * @param params
	 * @return
	 */
	public void updateRouteProperties(HashMap<String,String> params) throws Exception {
		String groupKey = params.get("route_name");
		String groupPathValue = params.get("route_path");
		String groupUrlValue = params.get("route_dest");
		
		/* properties path */
		RoutePropertiesVO routePropertiesPathVO = new RoutePropertiesVO();
		routePropertiesPathVO.setGroupKey(groupKey);
		routePropertiesPathVO.setGroupType("path");
		routePropertiesPathVO.setGroupValue(groupPathValue);
		routePropertiesMapper.updateRouteProperties(routePropertiesPathVO);

		/* properties url */
		RoutePropertiesVO routePropertiesUrlVO = new RoutePropertiesVO();
		routePropertiesUrlVO.setGroupKey(groupKey);
		routePropertiesUrlVO.setGroupType("url");
		routePropertiesUrlVO.setGroupValue(groupUrlValue);
		routePropertiesMapper.updateRouteProperties(routePropertiesUrlVO);	
	}
	
	
	/**
	 * 삭제
	 * 
	 * @param params
	 * @throws Exception
	 */
	public void deleteRouteProperties(HashMap<String,String> params) throws Exception {
		String groupKey = params.get("route_name");
		
		RoutePropertiesVO routePropertiesVO = new RoutePropertiesVO();
		routePropertiesVO.setGroupKey(groupKey);
		routePropertiesMapper.deleteRouteProperties(routePropertiesVO);	
	}
	
	/**
	 * 등록여부 확인
	 * 
	 * @param params
	 * @return
	 */
	public int selectRoutePropertiesCnt(HashMap<String,String> params) {
		String groupKey = params.get("route_name");
		
		RoutePropertiesVO routePropertiesVO = new RoutePropertiesVO();
		routePropertiesVO.setGroupKey(groupKey);
		int cnt = routePropertiesMapper.selectRoutePropertiesCnt(routePropertiesVO);
		
		return cnt;		
	}
	
	
}