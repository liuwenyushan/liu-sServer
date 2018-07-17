package com.doit.liu.server.Context;

import java.nio.channels.SelectionKey;

import com.doit.liu.server.message.HttpRequest;
import com.doit.liu.server.message.HttpResponse;
import com.doit.liu.server.message.Request;
import com.doit.liu.server.message.Response;

public class HttpContext extends Context {

	private Request request;
	private Response response;

	public void setContext(String requestHeader, SelectionKey key) {
		// ��ʼ��request
		request = new HttpRequest(requestHeader);
		// ��ʼ��response
		response = new HttpResponse(key);

		setRequest();
		setResponse();
	}

	private void setRequest() {
		super.request = this.request;
	}

	private void setResponse() {
		super.response = this.response;
	}

}
