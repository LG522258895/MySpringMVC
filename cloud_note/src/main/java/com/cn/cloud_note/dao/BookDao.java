package com.cn.cloud_note.dao;

import com.cn.cloud_note.entity.Book;

import java.util.List;
import java.util.Map;


public interface BookDao {
	public int updateName(
            Map<String, Object> params);
	public void save(Book book);
	public List<Book> findByUserId(String userId);
}
