package com.doit.liu.server.handler;

import java.nio.channels.SelectionKey;
import java.util.Map;

import org.apache.log4j.Logger;

import com.doit.liu.server.Context.Context;
import com.doit.liu.server.Context.HttpContext;
import com.doit.liu.server.Mapping.UriMap;

public class HttpHandler implements Runnable {

	private String requestHeader;
	private SelectionKey key;
	private Context context = new HttpContext();

	public Logger logger = Logger.getLogger(HttpHandler.class);

	public HttpHandler(String requestHeader, SelectionKey key) {
		this.requestHeader = requestHeader;
		this.key = key;
	}

	public void run() {
		context.setContext(requestHeader, key);
		String uri = context.getRequest().getUri();
		logger.info("�����uriΪ��" + uri);

		Map<String, String> uriMap = UriMap.getUriMap();

		String fileName;
		if (uriMap.get(uri) != null) {
			// �����ص���ͼ�����ļ�����׺
			fileName = uriMap.get(uri) + ".html";
		} else {
			fileName = "404.html";
		}
		context.getResponse().setHtmlFile(fileName);
		logger.info("���ص��ļ�Ϊ��" + fileName);

		try {
			// ����������������
			new ResponseHandler().write(context);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
