package org.liu.cn.rpcServer.impl;

import org.liu.cn.api.HelloService;

public class HelloServerServiceImpl implements HelloService{

	@Override
	public String say(String name) {
		System.out.println("server: " + name);
		return name;
	}

}
