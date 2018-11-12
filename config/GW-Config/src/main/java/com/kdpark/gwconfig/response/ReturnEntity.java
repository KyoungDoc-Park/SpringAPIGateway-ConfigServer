package com.kdpark.gwconfig.response;

import java.util.HashMap;

public class ReturnEntity {
	HashMap<String,String> result;
	
	public ReturnEntity() {
		result = new HashMap<String,String>();
	}
	
	public void setData(String codeVal, String msgVal) {
		result.put("code", codeVal);
		result.put("errMsg", msgVal);
	}

	public HashMap<String, String> getResult() {
		return result;
	}
	public void setResult(HashMap<String, String> result) {
		this.result = result;
	}

}