package com.doit.liu.server.Mapping;

public class Controller {

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/test")
	public String test() {
		return "test";
	}
}
