package com.bits.selp.service;

import org.apache.commons.lang3.StringUtils;

public class LoginService {

	public static void validateUserToken(String token) throws IllegalAccessException {
		if(StringUtils.isBlank(token) || !token.contains("Bearer ")) {
			throw new IllegalAccessException("Invalid Authorization");
		}
	}
	
}