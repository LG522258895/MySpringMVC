package com.cn.cloud_note.controller.book;

import javax.annotation.Resource;

import com.cn.cloud_note.service.BookService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cloud_note/book")
public class AddBookController {
	@Resource
	private BookService bookService;
	
	@RequestMapping("/add.do")
	@ResponseBody
	public NoteResult execute(
		String userId,String bookName){
		NoteResult result = 
			bookService.addBook(userId, bookName);
		return result;
	}
	
	
}



