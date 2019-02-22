package com.github.ziran_ink.ziran_api_server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tokens {
	private static final Map<String, Long> map = new HashMap<>();

	public static String createToken() {
		String token = UUID.randomUUID().toString();
		map.put(token, System.currentTimeMillis());
		return token;
	}

	public static boolean isValidToken(String token) {
		return map.containsKey(token);
	}

	public static void removeToken(String token) {
		map.remove(token);
	}
}
