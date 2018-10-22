package com.vps.restapi.utils;

import java.util.UUID;

import org.springframework.http.HttpHeaders;

public class CommonUtils {
	public static final String generateUuid() {
		return UUID.randomUUID().toString();

	}

	public static HttpHeaders redirectUrl() {
		HttpHeaders headers = new HttpHeaders();
		String redirectUrl = "http://localhost:4200/";
		headers.add("Location", redirectUrl);
		return headers;
	}

}
