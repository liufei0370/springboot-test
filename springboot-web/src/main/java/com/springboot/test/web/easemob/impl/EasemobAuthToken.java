package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.AuthTokenAPI;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;

public class EasemobAuthToken implements AuthTokenAPI {

	@Override
	public Object getAuthToken(){
		return EasemobTokenUtil.getAccessToken();
	}
}
