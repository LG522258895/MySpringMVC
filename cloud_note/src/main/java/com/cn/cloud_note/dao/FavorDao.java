package com.cn.cloud_note.dao;


import com.cn.cloud_note.entity.Favor;

public interface FavorDao {
	public int findFavor(Favor favor);
	public void save(Favor favor);
}
