package com.doit.liu.server.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class HttpRequest implements Request {
	// 参数
	private Map<String, Object> attribute = new HashMap<>();
	// 请求头
	private Map<String, Object> headers = new HashMap<>();
	// 请求方法
	private String method;
	// uri
	private String uri;
	// 协议版本
	private String protocol;

	Logger logger = Logger.getLogger(HttpRequest.class);

	public HttpRequest(String httpHeader) {
		init(httpHeader);
	}

	private void init(String httpHeader) {
		String[] headers = httpHeader.split("\r\n");
		String[] firstHeader = headers[0].split(" ");
		/*
		 * initMethod(headers[0]); initURI(headers[0]);
		 * initProtocol(headers[0]);
		 */
		if (firstHeader.length < 3) {
			logger.error("请求头错误！");
		} else {
			this.method = firstHeader[0];
			this.uri = firstHeader[1];
			this.protocol = firstHeader[2];
		}
		initRequestHeader(headers);
	}

	// 设置request参数
	private void initAttribute(String attr) {
		String[] attrs = attr.split("&");
		for (String string : attrs) {
			String key = string.substring(0, string.indexOf("="));
			String value = string.substring(string.indexOf("=" + 1));
			attribute.put(key, value);
		}
	}

	// 初始化请求头
	private void initRequestHeader(String[] strs) {
		for (int i = 1; i < strs.length; i++) {
			String key = strs[i].substring(0, strs[i].indexOf(":"));
			String value = strs[i].substring(strs[i].indexOf(":") + 1);
			headers.put(key, value);
		}
	}

	@Override
	public Map<String, Object> getAttribute() {
		return attribute;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public Map<String, Object> getHeaders() {
		return headers;
	}

	@Override
	public Set<String> getHeaderNames() {
		return headers.keySet();
	}

	@Override
	public Object getHeader(String key) {
		return headers.get(key);
	}

}
