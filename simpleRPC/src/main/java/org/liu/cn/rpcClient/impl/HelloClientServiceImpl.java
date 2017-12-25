package org.liu.cn.rpcClient.impl;

import java.net.InetSocketAddress;

import org.liu.cn.api.HelloService;
import org.liu.cn.rpcClient.RPCClient;

public class HelloClientServiceImpl implements HelloService{

	@Override
	public String say(String name) {
		HelloService service = RPCClient.getRemoteProxyObj(HelloService.class, new InetSocketAddress("localhost", 8088));
		System.out.println("clent: " + name);
		return service.say(name);
	}

}
