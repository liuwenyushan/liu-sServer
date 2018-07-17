package com.doit.liu.server.Mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UriMap {
	// 存储uri，文件地址的映射
	private static Map<String, String> uriMap = new HashMap<>();

	public static Map<String, String> getUriMap() {

		try {
			Class clazz = Class
					.forName("com.doit.liu.server.Mapping.Controller");
			Controller contrller = (Controller) clazz.newInstance();
			for (Method m : clazz.getDeclaredMethods()) {
				RequestMapping rm = m.getAnnotation(RequestMapping.class);
				if (rm != null) {
					String uri = rm.value();
					String method = rm.method();
					String filePath = (String) m.invoke(contrller);
					uriMap.put(uri, filePath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uriMap;
	}
}
