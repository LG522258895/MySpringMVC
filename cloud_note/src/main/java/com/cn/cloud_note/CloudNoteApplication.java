package com.cn.cloud_note;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@MapperScan("com.cn.cloud_note.dao")
public class CloudNoteApplication {

	@RequestMapping("/cloud_note")
	public String getHolle(){
		return "log_in";
	}

	public static void main(String[] args) {
		SpringApplication.run(CloudNoteApplication.class, args);
	}
}
