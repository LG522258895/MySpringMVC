package org.liu.cn.rpcServer;

import java.io.IOException;

public interface Server {
	
	public int getPort();
	
	public void start() throws IOException;
	
	public void register(Class serviceInterface, Class impl);
	
	public boolean isRunning();
	
	public void stop();
}
