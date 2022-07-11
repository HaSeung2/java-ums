package dao;

import java.util.HashMap;

public class Session {
	//Session.datas.put("login_id","apple");
	private static HashMap<String, String> datas = new HashMap<String, String>();

	
	//Session.put("login_id","apple");
	public static void put(String key, String value) {
		datas.put(key, value);
	}
	public static String get(String key) {
		return datas.get(key);
	}
}
