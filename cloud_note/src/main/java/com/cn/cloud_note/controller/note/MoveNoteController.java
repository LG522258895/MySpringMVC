package com.cn.cloud_note.controller.note;

import javax.annotation.Resource;

import com.cn.cloud_note.service.NoteService;
import com.cn.cloud_note.util.NoteResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cloud_note/note")
public class MoveNoteController {
	@Resource
	private NoteService noteService;
	
	@RequestMapping("/move.do")
	@ResponseBody
	public NoteResult execute(
		String bookId,String noteId){
		NoteResult result = 
			noteService.moveNote(bookId, noteId);
		return result;
	}

	@RequestMapping("/alertDelete.do")
	@ResponseBody
	public String alertDelete(){
		return "alert/alert_delete_note";
	}
	
}
