package com.cn.cloud_note.controller.user;

import javax.annotation.Resource;

import com.cn.cloud_note.service.UserService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/cloud_note/user")
public class UserRegistController {
	@Resource
	private UserService userService;
	
	@RequestMapping("/regist.do")
	@ResponseBody
	public NoteResult execute(
		String name,String password,String nick){
		NoteResult result = 
		userService.registUser(name, nick, password);
		return result;
	}
	
}
