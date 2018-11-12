package com.kdpark.gwconfig.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kdpark.gwconfig.vo.RoutePropertiesVO;



@Mapper
public interface RoutePropertiesMapper {
	
	/**
	 * 등록
	 * 
	 * @param routePropertiesVO
	 */
	void insertRouteProperties(RoutePropertiesVO routePropertiesVO);
	
	
	/**
	 * 수정
	 * 
	 * @param routePropertiesVO
	 */
	void updateRouteProperties(RoutePropertiesVO routePropertiesVO);
	
	/**
	 * 삭제
	 * 
	 * @param routePropertiesVO
	 */
	void deleteRouteProperties(RoutePropertiesVO routePropertiesVO);

	
	/**
	 * 등록 여부 확인
	 * 
	 * @param routePropertiesVO
	 * @return
	 */
	int selectRoutePropertiesCnt(RoutePropertiesVO routePropertiesVO);
	
	
	
}