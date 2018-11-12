package com.kdpark.zuul.filter;

import org.springframework.beans.factory.annotation.Autowired;

import com.kdpark.zuul.service.GatewayService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class GWPostFilter extends ZuulFilter {
	
	@Autowired GatewayService gatewayService;

	@Override
	public String filterType() {
		return "post";
	}
 
	@Override
	public int filterOrder() {
		return 1;
	}
 
	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public Object run() {
	    RequestContext ctx = RequestContext.getCurrentContext();
	    
	    if (ctx.getResponseStatusCode() == 200) {
	    	// 200 OK일 경우 logging 진행
	    	gatewayService.writeLog(ctx);
        } else {
        	// 실피경우  에러 다이렉트  리턴
	    	gatewayService.setJsonData(ctx, ctx.getResponseStatusCode(), "error");
	    }
		return null;
	}
}
