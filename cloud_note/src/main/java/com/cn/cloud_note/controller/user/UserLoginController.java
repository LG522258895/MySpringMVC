package com.cn.cloud_note.controller.user;

import com.cn.cloud_note.service.UserService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/cloud_note/user")
public class UserLoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public NoteResult execute(
            String name,String password){
        NoteResult result =
                userService.checkLogin(name, password);
        return result;
    }
}
