package com.doit.liu.server.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;

import org.apache.log4j.Logger;

import com.doit.liu.server.Context.Context;
import com.doit.liu.server.message.Request;
import com.doit.liu.server.message.Response;

public class ResponseHandler {
	private Request request;
	private Response response;
	private String protocol;
	private int statuCode;
	private String statuCodeStr;
	private ByteBuffer buffer;
	private String serverName;
	private String contentType;
	private SocketChannel channel;
	private Selector selector;
	private SelectionKey key;
	private Logger logger = Logger.getLogger(ResponseHandler.class);
	private BufferedReader reader;
	private String htmlFile;

	public void write(Context context) throws ClosedChannelException,
			FileNotFoundException {
		request = context.getRequest();
		response = context.getResponse();
		buffer = ByteBuffer.allocate(1024 * 1024);
		protocol = request.getProtocol();
		statuCode = response.getStatuCode();
		serverName = Response.SERVER_NAME;
		contentType = response.getContentType();
		key = response.getKey();
		selector = key.selector();
		channel = (SocketChannel) key.channel();
		htmlFile = response.getHtmlFile();

		String html = setHtml(context);

		StringBuilder sb = new StringBuilder();

		// ״̬��
		sb.append(protocol + " " + statuCode + " " + statuCodeStr + "\r\n");
		// ��Ӧͷ
		sb.append("Server:" + serverName + "\r\n");
		sb.append("Content-Type:" + contentType + "\r\n");
		sb.append("Date:" + new Date() + "\r\n");
		if (reader != null) {
			sb.append("Content-Length:" + html.getBytes().length + "\r\n");
		}
		sb.append("\r\n");
		sb.append(html);

		buffer.put(sb.toString().getBytes());
		buffer.flip();
		logger.info("������Ӧ\r\n" + sb.toString());
		try {
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String setHtml(Context context) throws FileNotFoundException {
		StringBuilder html = null;
		if (htmlFile != null && htmlFile.length() > 0) {
			html = new StringBuilder();

			try {
				reader = new BufferedReader(new FileReader(new File(htmlFile)));
				String htmlStr;
				htmlStr = reader.readLine();
				while (htmlStr != null) {
					html.append(htmlStr + "\r\n");
					htmlStr = reader.readLine();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return html.toString();
	}
}
