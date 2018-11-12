package com.kdpark.zuul.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Service;

import com.netflix.zuul.context.RequestContext;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GatewayService {

	

	/**
	 * 로그작성
	 * 
	 * @param ctx
	 */
	public void writeLog(RequestContext ctx) {
		HttpServletRequest request = ctx.getRequest();

    	Date currEDate = new Date();
    	long endTime = (long) currEDate.getTime();		/* 최종 요청 시간 (단위 : ms) */

    	long startTime = (ctx.get("st") != null)?(long) ctx.get("st"):0;			/* 최초 요청 시간 (단위 : ms) */
    	long excuteTime = endTime-startTime;			/* 처리 시간 */

    	/* 요청 URL */
    	String reqUrl = request.getRequestURI();
		if (request.getQueryString() != null) {
			reqUrl = reqUrl + "?" + request.getQueryString();
		}
        		
		String ipAddr = request.getRemoteAddr();		/* 서비스 IP */
		int httpStatus = ctx.getResponseStatusCode();	/* http status code */
		String nowAsISO = (ctx.get("sd") != null)?(String) ctx.get("sd"):"null";		/* 최초 요청 일시 - pre filter */
		String rUri = (ctx.get("ol") != null)?(String) ctx.get("ol"):reqUrl;			/* 최초 요청 URI */
		
		/*
		요청시간  / http method (GET or POST) / URL+PARAMS / 요청 IP address (서비스 IP) / http code / 응답시간 / 서비스 ID / 사용자ID (선택)
		2018-09-18T07:34:33:294Z POST /auth/getToken 222.121.110.192 202 myx timeTests2
		 */
		
		
		//logging
		StringBuffer logStr = new StringBuffer();
		logStr.append(nowAsISO);
		logStr.append(" ");
		logStr.append(request.getMethod());
		logStr.append(" ");
		logStr.append(rUri);
		logStr.append(" ");
		logStr.append(ipAddr);
		logStr.append(" ");
		logStr.append(httpStatus);
		logStr.append(" ");
		logStr.append(excuteTime);
		logStr.append(" ");

		log.info(logStr.toString());
		
	}
	
	
	/**
     * zuul 오류처리 
     * 
     * @param errCode
     * @param errMsg
     */
    public void setJsonData(RequestContext ctx, int errCode, String errMsg) {
    	StringBuffer map = new StringBuffer();
    	map.append("{");
    	map.append("\"code\"");
    	map.append(":");
    	map.append("\"");
    	map.append(String.valueOf(errCode));
    	map.append("\"");
    	map.append(",");
    	map.append("\"errMsg\"");
    	map.append(":");
    	map.append("\"");
    	map.append(errMsg);
    	map.append("\"");    	
    	map.append("}");
   	    	
    	ctx.setResponseStatusCode(errCode);
    	ctx.setResponseBody(map.toString());
    	ctx.getResponse().setContentType("application/json;charset=utf-8");
    	ctx.setSendZuulResponse(false);   	

    	writeLog(ctx);
    }
    
    
    /**
	 * 사용자 토큰 체크
	 * 
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public int checkApiUserAuth(String userToken) {
		String chkVal = "N";
		int returnVal = HttpStatus.SC_INTERNAL_SERVER_ERROR;

		
		try {
			//TODO 사용자 validation check부분
			//chkVal = "select from db or others...";
			
			if ("null".equals(chkVal)) {
				returnVal = HttpStatus.SC_BAD_REQUEST;
			} else {
				returnVal = HttpStatus.SC_OK;
			}
		} catch(Exception e) {
			returnVal = HttpStatus.SC_BAD_REQUEST;
			
		}

		return returnVal;
	}
	
	
	 /**
     * 직접요청에 대한 오류 로그
     * 
     * @param request
     * @param response
     * @param errCode
     * @param errMsg
     */
    public void setErrorMsg(HttpServletRequest request, HttpServletResponse response, int errCode, String errMsg) {
    	StringBuffer map = new StringBuffer();
    	map.append("{");
    	map.append("\"code\"");
    	map.append(":");
    	map.append("\"");
    	map.append(String.valueOf(errCode));
    	map.append("\"");
    	map.append(",");
    	map.append("\"errMsg\"");
    	map.append(":");
    	map.append("\"");
    	map.append(errMsg);
    	map.append("\"");    	
    	map.append("}");
   	
    	PrintWriter out;
		try {
			out = response.getWriter();
			response.setStatus(errCode);
			response.setContentType("application/json");
	    	response.setCharacterEncoding("UTF-8");

	    	writeErrorLog(request, response);
	    	
	    	out.print(map.toString());
	    	out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	/**
	 * @param request
	 * @param response
	 */
	public void writeErrorLog(HttpServletRequest request, HttpServletResponse response) {
    	Date currSDate = new Date();
    	long startTime = (long) currSDate.getTime();
    	long excuteTime = 0;			/* 처리 시간 */

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String nowAsISO = df.format(currSDate);
    	
    	/* 요청 URL */
    	String reqUrl = request.getRequestURI();
		if (request.getQueryString() != null) {
			reqUrl = reqUrl + "?" + request.getQueryString();
		}
		
		String ipAddr = request.getRemoteAddr();		/* 서비스 IP */
		int httpStatus = response.getStatus();	/* http status code */

		String rUri = (request.getParameter("ol") != null)?request.getParameter("ol"):reqUrl;			/* 최초 요청 URI */

		/*
		요청시간  / http method (GET or POST) / URL+PARAMS / 요청 IP address (서비스 IP) / http code / 응답시간 / 서비스 ID / 사용자ID (선택)
		2018-09-18T07:34:33:294Z POST /auth/getToken 222.121.110.192 202 myx timeTests2
		 */
		StringBuffer logStr = new StringBuffer();
		logStr.append(nowAsISO);
		logStr.append(" ");
		logStr.append(request.getMethod());
		logStr.append(" ");
		logStr.append(rUri);
		logStr.append(" ");
		logStr.append(ipAddr);
		logStr.append(" ");
		logStr.append(httpStatus);
		logStr.append(" ");
		logStr.append(excuteTime);		
		

		log.info(logStr.toString());
	}
   
}
