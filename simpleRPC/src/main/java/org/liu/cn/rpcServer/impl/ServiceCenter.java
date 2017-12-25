package org.liu.cn.rpcServer.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.liu.cn.rpcServer.Server;

public class ServiceCenter implements Server {

	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

	private static boolean isRunning = false;

	private static int port;
	
	public ServiceCenter(int port) {
        this.port = port;
    }

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public void start() throws IOException {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(port));
		System.out.println("start server");
		try {
			while (true) {
				// 1.�����ͻ��˵�TCP���ӣ��ӵ�TCP���Ӻ����װ��task�����̳߳�ִ��
				executor.execute(new ServiceTask(server.accept()));
			}
		} finally {
			server.close();
		}
	}

	@Override
	public void register(Class serviceInterface, Class impl) {
		System.out.println("��ʼע�����"); 
		serviceRegistry.put(serviceInterface.getName(), impl);
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void stop() {
		isRunning = false;
        executor.shutdown();
        System.out.println("������ѹر�");
	}

	private static class ServiceTask implements Runnable {
		Socket clent = null;

		public ServiceTask(Socket client) {
			this.clent = client;
		}

		@Override
		public void run() {
			ObjectInputStream input = null;
			ObjectOutputStream output = null;
			try {
				// 2.���ͻ��˷��͵����������л��ɶ��󣬷�����÷���ʵ���ߣ���ȡִ�н��
				input = new ObjectInputStream(clent.getInputStream());
				String serviceName = input.readUTF();
				String methodName = input.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
				Object[] arguments = (Object[]) input.readObject();
				Class serviceClass = serviceRegistry.get(serviceName);
				if (serviceClass == null) {
					throw new ClassNotFoundException(serviceName + " not found");
				}
				Method method = serviceClass.getMethod(methodName, parameterTypes);
				Object result = method.invoke(serviceClass.newInstance(), arguments);

				// 3.��ִ�н�������л���ͨ��socket���͸��ͻ���
				output = new ObjectOutputStream(clent.getOutputStream());
				output.writeObject(result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (clent != null) {
					try {
						clent.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

}
