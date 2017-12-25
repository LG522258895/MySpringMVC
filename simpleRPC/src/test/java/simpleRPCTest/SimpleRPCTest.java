package simpleRPCTest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.liu.cn.api.HelloService;
import org.liu.cn.rpcClient.impl.HelloClientServiceImpl;
import org.liu.cn.rpcServer.Server;
import org.liu.cn.rpcServer.impl.HelloServerServiceImpl;
import org.liu.cn.rpcServer.impl.ServiceCenter;

public class SimpleRPCTest {

	@Before
	public void startServer() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Server serviceServer = new ServiceCenter(8088);
					serviceServer.register(HelloService.class, HelloServerServiceImpl.class);
					serviceServer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Test
	public void startClient() {
		HelloService helloService = new HelloClientServiceImpl();
		String name = helloService.say("test");
		System.out.println(name);
	}
}
