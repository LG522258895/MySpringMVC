package com.cn.lg.demo.service.impl;

import com.cn.lg.demo.service.DemoService;
import com.cn.lg.mvcframework.annotation.MyService;

@MyService
public class DemoServiceImpl implements DemoService{

	@Override
	public String name(String name) {
		return "My name is " + name;
	}

}
