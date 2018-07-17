package com.doit.liu.server;


public class Main {

	public static void main(String[] args) {
		/*
		 * Map<String, String> uriMap = UriMap.getUriMap();
		 * System.out.println(uriMap.size());
		 */
		new Thread(new liusServer(false)).start();
	}

}
