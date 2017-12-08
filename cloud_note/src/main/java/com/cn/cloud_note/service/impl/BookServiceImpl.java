package com.cn.cloud_note.service.impl;

import com.cn.cloud_note.dao.BookDao;
import com.cn.cloud_note.entity.Book;
import com.cn.cloud_note.service.BookService;
import com.cn.cloud_note.util.NoteResult;
import com.cn.cloud_note.util.NoteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookDao bookDao;

	@Override
	public NoteResult renameBook(String bookId, String bookName) {
		//更新表中的笔记本名
		Map<String, Object> params =
				new HashMap<String, Object>();
		params.put("id", bookId);//对应#{id}
		params.put("name", bookName);//对应#{name}
		bookDao.updateName(params);
		//返回NoteResult结果
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("修改笔记本成功");
		return result;
	}

	@Override
	public NoteResult addBook(String userId, String bookName) {
		Book book = new Book();
		book.setCn_user_id(userId);//用户ID
		book.setCn_notebook_name(bookName);//笔记本名
		book.setCn_notebook_type_id("5");//normal
		book.setCn_notebook_desc("");
		Timestamp time = new Timestamp(
				System.currentTimeMillis());
		book.setCn_notebook_createtime(time);//创建时间
		String bookId = NoteUtil.createId();
		book.setCn_notebook_id(bookId);//ID
		bookDao.save(book);//添加笔记本
		//模拟一个异常，测试异常日志记录
		String s = null;
		s.length();
		//返回结果
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("创建笔记本成功");
		result.setData(bookId);//返回笔记本ID
		return result;
	}

	@Override
	public NoteResult loadUserBooks(String userId) {
		NoteResult result = new NoteResult();
		//根据用户ID查询笔记本信息
		List<Book> list =
				bookDao.findByUserId(userId);
		result.setStatus(0);
		result.setMsg("查询笔记本成功");
		result.setData(list);//返回笔记本信息
		return result;
	}
}
