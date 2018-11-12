package com.kdpark.zuul.filter;

import com.netflix.zuul.ZuulFilter;

public class GWRouteFilter extends ZuulFilter {
	
	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 999;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		return null;
	}
}
