package com.cn.cloud_note.dao;


import com.cn.cloud_note.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
	public void save(User user);
	public User findByName(@Param("name")String cn_user_name);
}
