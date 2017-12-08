package com.cn.cloud_note.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


public class WebConfig extends WebMvcConfigurerAdapter {
    public FilterRegistrationBean doFilter(){
        return null;
    }
}
