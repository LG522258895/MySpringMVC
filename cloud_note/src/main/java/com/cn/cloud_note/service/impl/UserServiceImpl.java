package com.cn.cloud_note.service.impl;

import com.cn.cloud_note.dao.UserDao;
import com.cn.cloud_note.entity.User;
import com.cn.cloud_note.service.UserService;
import com.cn.cloud_note.util.NoteResult;
import com.cn.cloud_note.util.NoteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public NoteResult registUser(String name, String nick, String password) {
		NoteResult result = new NoteResult();
		//检测用户名是否存在
		User has_user = userDao.findByName(name);
		if(has_user!=null){//已存在
			result.setStatus(1);
			result.setMsg("用户名已存在");
			return result;
		}
		//执行注册逻辑
		User user = new User();
		//设置用户ID
		String userId = NoteUtil.createId();
		user.setCn_user_id(userId);
		user.setCn_user_name(name);//设置用户名
		user.setCn_user_nick(nick);//设置昵称
		//密码加密,设置密码
		String md5_password = NoteUtil.md5(password);
		user.setCn_user_password(md5_password);
		userDao.save(user);//添加用户

		result.setStatus(0);
		result.setMsg("注册成功");
		return result;
	}

	@Override
	public NoteResult checkLogin(String name, String password) {
		NoteResult result = new NoteResult();
		User user = userDao.findByName(name);
		//检测用户名
		if (user == null) {
			result.setStatus(1);
			result.setMsg("用户名错误");
//			result.setData(null);
			return result;
		}
		//检测密码
		//将用户输入的明文加密
		String md5Password = NoteUtil.md5(password);
		//比对数据库中密码
		if (!user.getCn_user_password().equals(md5Password)) {
			result.setStatus(2);
			result.setMsg("密码错误");
			return result;
		}
		//用户名和密码正确
		result.setStatus(0);
		result.setMsg("登录成功");
		//将用户身份ID返回
		result.setData(user.getCn_user_id());
		return result;
	}
}
