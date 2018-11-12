package com.kdpark.zuul.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.kdpark.zuul.service.GatewayService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class GWPreFilter extends ZuulFilter {

	
	@Autowired GatewayService gatewayService;
	
	
	@Override
	public String filterType() {
		return "pre";
	}
	
	@Override
	public int filterOrder() {
		return -1;
	}
	
	@Override
    public boolean shouldFilter() {
        //return true;
		return RequestContext.getCurrentContext().getResponseStatusCode() != HttpServletResponse.SC_OK;
    }

    @Override
    public Object run() throws ZuulException {
    	RequestContext ctx = RequestContext.getCurrentContext();
    	HttpServletRequest request = ctx.getRequest();
    	
    	int chkAuth = HttpStatus.SC_BAD_REQUEST;
    	
    	Date currSDate = new Date();
    	long startTime = (long) currSDate.getTime();

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);
		String nowAsISO = df.format(currSDate);
    	
		/* 요청시간을 추가하여 전달 */
		ctx.put("sd", nowAsISO);					// 요청일시
        ctx.put("st", startTime);					// 요청시간
        ctx.put("ol", request.getRequestURI());		// 요청URI
     		
			
    	if (chkAuth != HttpStatus.SC_OK) {
    		gatewayService.setJsonData(ctx, chkAuth, "access denied");
            return null;
    	}        
           
    	/* 사용자 인증 체크 */ 
        Object objUserToken = request.getHeader("Authorization");
	
        if (objUserToken == null) {
        	gatewayService.setJsonData(ctx, HttpStatus.SC_BAD_REQUEST, "no token infomation");
            return false;
        }
        String[] arrUserToken = objUserToken.toString().split(" ");

		// 사용자 토큰 전달 - 로그용
		ctx.put("userToken", arrUserToken[1]);

		chkAuth = gatewayService.checkApiUserAuth(arrUserToken[1]);
    	if (chkAuth != HttpStatus.SC_OK) {
    		chkAuth = 999;
    		gatewayService.setJsonData(ctx, chkAuth, "access denied");
            return null;
    	}
    

        return null;
    }
}
