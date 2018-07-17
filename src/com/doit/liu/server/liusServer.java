package com.doit.liu.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.doit.liu.server.handler.HttpHandler;

public class liusServer implements Runnable {

	private int port = 9999;
	private boolean interrupted = false;
	private Logger logger = Logger.getLogger(liusServer.class);

	public liusServer(boolean interrupted) {
		this.interrupted = interrupted;
	}

	@Override
	public void run() {
		ServerSocketChannel ssc = null;
		try {
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			ss.bind(new InetSocketAddress(port));
			logger.info("端口：" + port + "绑定成功!");

			Selector selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			logger.info("liusServer启动成功--------------");

			while (!interrupted) {
				int readyChannel = selector.select();
				if (readyChannel == 0) {// 没有已就绪的通道则不执行下述代码继续循环
					continue;
				}
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();

				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();

					if (key.isAcceptable()) {
						ServerSocketChannel serverChannel = (ServerSocketChannel) key
								.channel();
						SocketChannel clientChannel = serverChannel.accept();
						if (clientChannel != null) {
							logger.info("收到了来自"
									+ ((InetSocketAddress) clientChannel
											.getRemoteAddress())
											.getHostString() + "的请求");
						}
						clientChannel.configureBlocking(false);
						clientChannel.register(selector, SelectionKey.OP_READ);
					} else if (key.isReadable()) {
						SocketChannel clientChannel = (SocketChannel) key
								.channel();
						String requestHeader = recieve(clientChannel);

						if (requestHeader.length() > 0) {
							logger.info("该请求头格式为：\r\n" + requestHeader);
							logger.info("启动子线程对请求进行处理--------");
							// 启动子线程对请求进行处理
							new Thread(new HttpHandler(requestHeader, key))
									.start();
						}
					} else if (key.isWritable()) {
						// 对已输出的channel进行关闭，否则channel将会阻塞
						SocketChannel socketChannel = (SocketChannel) key
								.channel();
						socketChannel.shutdownInput();
						socketChannel.close();
					}
					iterator.remove();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String recieve(SocketChannel socketChannel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes = null;
		int size = 0;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((size = socketChannel.read(buffer)) > 0) {

			buffer.flip();
			bytes = new byte[size];
			buffer.get(bytes);
			baos.write(bytes);
		}
		bytes = baos.toByteArray();

		return new String(bytes);
	}
}
