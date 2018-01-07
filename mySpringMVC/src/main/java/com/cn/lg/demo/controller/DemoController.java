package com.cn.lg.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.lg.demo.service.DemoService;
import com.cn.lg.mvcframework.annotation.MyAutowired;
import com.cn.lg.mvcframework.annotation.MyController;
import com.cn.lg.mvcframework.annotation.MyRequestMapping;
import com.cn.lg.mvcframework.annotation.MyRequestParameter;

@MyController
@MyRequestMapping("web")
public class DemoController {
	
	@MyAutowired
	private DemoService demoService;
	
	@MyRequestMapping("/getName.do")
	public void getName(HttpServletRequest req,HttpServletResponse res,@MyRequestParameter("name")String name) {
		String result = demoService.name(name);
		try {
			res.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@MyRequestMapping("/getName1.do")
	public void getName1(HttpServletRequest req,@MyRequestParameter("name")String name,HttpServletResponse res,@MyRequestParameter("name2")String name2) {
		String result = demoService.name(name);
		String result2 = demoService.name(name2);
		try {
			res.getWriter().write(result);
			res.getWriter().write(result2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
