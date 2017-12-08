package com.cn.cloud_note.controller.note;

import javax.annotation.Resource;

import com.cn.cloud_note.service.NoteService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/cloud_note/note")
public class LoadNotesController {
	@Resource
	private NoteService noteService;
	
	@RequestMapping("/loadnotes.do")
	@ResponseBody
	public NoteResult execute(String bookId){
		NoteResult result = 
			noteService.loadBookNotes(bookId);
		return result;
	}
	
}



