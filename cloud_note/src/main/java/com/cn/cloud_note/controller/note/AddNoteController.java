package com.cn.cloud_note.controller.note;

import javax.annotation.Resource;

import com.cn.cloud_note.service.NoteService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cloud_note/note")
public class AddNoteController {
	@Resource
	private NoteService noteService;
	@RequestMapping("/add.do")
	@ResponseBody
	public NoteResult execute(
		String userId,String title,String bookId){
		NoteResult result = noteService.addNote(
				userId, title, bookId);
		return result;
	}
	
}



