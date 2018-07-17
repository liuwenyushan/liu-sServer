package com.doit.liu.server.Context;

import java.nio.channels.SelectionKey;

import com.doit.liu.server.message.Request;
import com.doit.liu.server.message.Response;

public abstract class Context {
	protected Request request;
	protected Response response;

	public abstract void setContext(String requestHeader, SelectionKey key);

	public Request getRequest() {
		return request;
	}

	public Response getResponse() {
		return response;
	}
}
