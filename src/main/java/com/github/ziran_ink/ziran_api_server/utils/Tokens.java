package com.github.ziran_ink.ziran_api_server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tokens {
	private static final Map<String, Map<String, Object>> map = new HashMap<>();

	public static String createToken() {
		String token = UUID.randomUUID().toString();
		map.put(token, new HashMap<>());
		return token;
	}

	public static boolean isValidToken(String token) {
		return map.containsKey(token);
	}

	public static void removeToken(String token) {
		map.remove(token);
	}

	public static Map<String, Object> getTokenContext(String token) {
		return map.get(token);
	}

	public static void setTokenProp(String token, String prop, Object value) {
		getTokenContext(token).put(prop, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getTokenProp(String token, String prop) {
		return (T) getTokenContext(token).get(prop);
	}
}
