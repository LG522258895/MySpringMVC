package com.cn.lg.mvcframework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.lg.mvcframework.annotation.MyAutowired;
import com.cn.lg.mvcframework.annotation.MyController;
import com.cn.lg.mvcframework.annotation.MyRequestMapping;
import com.cn.lg.mvcframework.annotation.MyRequestParameter;
import com.cn.lg.mvcframework.annotation.MyService;
import com.cn.lg.mvcframework.utils.StringUtils;

public class MyDisptcherServlet extends HttpServlet{
	
	
	private static final long serialVersionUID = 1L;

	private Properties properties = new Properties();
	
	private List<String> classNames = new ArrayList<String>();
	
	private Map<String, Object> ioc = new HashMap<String, Object>();
	
	private List<HandlerModel> handleMapping = new ArrayList<HandlerModel>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDisptcher(req,resp);
	}

	@SuppressWarnings("unlikely-arg-type")
	private void doDisptcher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		String requestURI = req.getRequestURI();
//		String contextPath = req.getContextPath();
//		requestURI = requestURI.replace(contextPath, "").replaceAll("/+", "");
//		System.out.println(requestURI);
//		Method method = handleMapping.get(requestURI);
//		
//		System.out.println(method);
		HandlerModel handler = getHandler(req);
		if (null == handler) {
			resp.getWriter().write("404 Not Found");
			return;
		}
		
		Class<?>[] paramTypes = handler.method.getParameterTypes();
		
		Object[] paramValues = new Object[paramTypes.length];
		
		Map<String, String[]> params = req.getParameterMap();
		for (Map.Entry<String, String[]> param: params.entrySet()) {
			String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
			
			if (!handler.paramMap.containsKey(param.getKey())) {
				continue;
			}
			
			int index = handler.paramMap.get(param.getKey());
			paramValues[index] = convert(paramTypes[index], value);
		}
		
		int reqIndex = handler.paramMap.get(HttpServletRequest.class.getName());
		paramValues[reqIndex] = req;
		int respIndex = handler.paramMap.get(HttpServletResponse.class.getName());
		paramValues[respIndex] = resp;
		
		try {
			handler.method.invoke(handler.controller, paramValues);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		//加载配置文件 application.properties 代替xml
		doLoadConfig(config.getInitParameter("contextConfigLocation"));
		
		//扫描所有相关的类 得到基础包的路径 递归扫描
		doScanner(properties.getProperty("scanPackage"));
		
		//把扫描的所有的实例 放到IOC中 
		doInstance();
		
		//依赖注入
		doAutowired();
		
		//
		doInitHandleMapping();
		
		System.out.println("=================");
	}

	private void doInitHandleMapping() {
		if (ioc.isEmpty()) {
			return;
		}
		
		for(Entry<String, Object> entry : ioc.entrySet()) {
			Class<? extends Object> clazz = entry.getValue().getClass();
			
			
			if (!clazz.isAnnotationPresent(MyController.class)) {
				continue;
			}
			
			String baseUrl = "";
			if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
				MyRequestMapping myRequestMapping = clazz.getAnnotation(MyRequestMapping.class);
				baseUrl = myRequestMapping.value();
			}
			
			Method[] methods = clazz.getMethods();
			
			for (Method method : methods) {
				if (method.isAnnotationPresent(MyRequestMapping.class)) {
					MyRequestMapping myRequestMapping = method.getAnnotation(MyRequestMapping.class);
					String mappingUrl = "/" + baseUrl + myRequestMapping.value().replaceAll("/+", "/");
					//handleMapping.put(mappingUrl, method);
					
					Pattern pattern = Pattern.compile(mappingUrl);
					handleMapping.add(new HandlerModel(method, entry.getValue(), pattern));
				}
			}
		}
	}

	private void doAutowired() {
		if (ioc.isEmpty()) {
			return;
		}
		for (Entry<String, Object> entry : ioc.entrySet()) {
			Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
			
			for (Field field : declaredFields) {
				if (!field.isAnnotationPresent(MyAutowired.class)) {
					continue;
				}
				
				MyAutowired myAutowired = field.getAnnotation(MyAutowired.class);
				
				String beanName = myAutowired.value().trim();
				
				if ("".equals(beanName)) {
					beanName = field.getType().getName();
				}
				
				field.setAccessible(true);
				
				try {
					field.set(entry.getValue(), ioc.get(beanName));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void doInstance() {
		if (classNames.isEmpty()) {
			return;
		}
		
		for (String className : classNames) {
			try {
				Class<?> clazz = Class.forName(className);
				if (clazz.isAnnotationPresent(MyController.class)) {
					String simpleName = StringUtils.lowerFirst(clazz.getSimpleName());
					ioc.put(simpleName, clazz.newInstance());
				}else if(clazz.isAnnotationPresent(MyService.class)){
					MyService myService = clazz.getAnnotation(MyService.class);
					String beanName = myService.value();
					if (!"".equals(beanName)) {
						ioc.put(beanName, clazz.newInstance());
					}else {
						beanName = StringUtils.lowerFirst(clazz.getSimpleName());
						ioc.put(beanName, clazz.newInstance());
					}
					
					Class<?>[] interfaces = clazz.getInterfaces();
					for (Class<?> inter : interfaces) {
						ioc.put(inter.getName(), clazz.newInstance());
					}
					
				}else {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void doScanner(String packageName) {
		URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				doScanner(packageName + "." + file.getName());
			}else {
				classNames.add(packageName + "." + file.getName().replace(".class", ""));
			}
		}
		
	}

	private void doLoadConfig(String path) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class HandlerModel {
        Method method;
        Object controller;
        Pattern pattern;
        Map<String, Integer> paramMap;
        
        public HandlerModel(Method method, Object controller, Pattern pattern) {
            this.method = method;
            this.controller = controller;
            this.pattern = pattern;
            
            paramMap = new HashMap<String, Integer>();
            putParamMap(method);
        }

		private void putParamMap(Method method) {
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (int i = 0; i < parameterAnnotations.length; i++) {
				for (Annotation annotation : parameterAnnotations[i]) {
					if (annotation instanceof MyRequestParameter) {
						String paramName = ((MyRequestParameter) annotation).value();
						if (!"".equals(paramName)) {
							paramMap.put(paramName, i);
						}
					}
				}
			}
			
			Class<?>[] parameterTypes = method.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> classType = parameterTypes[i];
				if (classType == HttpServletRequest.class || classType == HttpServletResponse.class) {
					paramMap.put(classType.getName(), i);
				}
			}
		}
    }
	
	private Object convert(Class<?> classType,String value) {
		if (Integer.class == classType) {
			return Integer.valueOf(value);
		}
		return value;
	}
	
	private HandlerModel getHandler(HttpServletRequest req) {
		
		if (handleMapping.isEmpty()) {
			return null;
		}
		
		String requestURI = req.getRequestURI();
		String contextPath = req.getContextPath();
		requestURI = requestURI.replace(contextPath, "").replaceAll("/+", "/");
		for (HandlerModel handlerModel : handleMapping) {
			Matcher matcher = handlerModel.pattern.matcher(requestURI);
			if (!matcher.matches()) {
				continue;
			}
			return handlerModel;
		}
		return null;
	}

}
