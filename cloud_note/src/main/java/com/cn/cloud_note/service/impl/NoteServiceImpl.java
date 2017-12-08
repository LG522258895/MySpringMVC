package com.cn.cloud_note.service.impl;

import com.cn.cloud_note.dao.NoteDao;
import com.cn.cloud_note.dao.ShareDao;
import com.cn.cloud_note.entity.Note;
import com.cn.cloud_note.entity.Share;
import com.cn.cloud_note.service.NoteService;
import com.cn.cloud_note.util.NoteResult;
import com.cn.cloud_note.util.NoteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteDao noteDao;

	@Autowired
	private ShareDao shareDao;

	@Override
	public NoteResult moveNote(String bookId, String noteId) {
		//更新笔记本ID
		Map<String, Object> params =
				new HashMap<String, Object>();
		params.put("noteId", noteId);
		params.put("bookId", bookId);
		noteDao.updateBookId(params);
		//返回结果
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("转移笔记成功");
		return result;
	}

	@Override
	public NoteResult recycleNote(String noteId) {
		Map<String,Object> params =
				new HashMap<String, Object>();
		params.put("noteId", noteId);//笔记ID
		params.put("status", "2");//删除状态
		noteDao.updateStatus(params);//更新笔记状态
		//TODO 删除分享表cn_share分享的笔记记录
		//返回NoteResult
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("删除笔记成功");
		return result;
	}

	@Override
	public NoteResult addNote(String userId, String title, String bookId) {
		Note note = new Note();
		note.setCn_note_title(title);//标题
		note.setCn_user_id(userId);//用户ID
		note.setCn_notebook_id(bookId);//笔记本ID
		note.setCn_note_status_id("1");//normal
		note.setCn_note_type_id("1");//normal
		note.setCn_note_body("");//空串
		long time = System.currentTimeMillis();
		note.setCn_note_create_time(time);//创建时间
		note.setCn_note_last_modify_time(time);//最后修改时间
		String noteId = NoteUtil.createId();
		note.setCn_note_id(noteId);//笔记ID
		noteDao.save(note);//插入cn_note表
		//返回结果
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("创建笔记成功");
		result.setData(noteId);//返回笔记ID
		return result;
	}

	@Override
	public NoteResult updateNote(String id, String title, String body) {
		NoteResult result = new NoteResult();
		//更新笔记cn_note表操作
		Note note = new Note();
		note.setCn_note_id(id);//笔记ID
		note.setCn_note_title(title);//标题
		note.setCn_note_body(body);//内容
		long time = System.currentTimeMillis();
		note.setCn_note_last_modify_time(time);//最后更新时间
		noteDao.update(note);
		//更新分享cn_share表title,body
		Share share = new Share();
		share.setCn_note_id(id);
		share.setCn_share_title(title);
		share.setCn_share_body(body);
		shareDao.updateShare(share);
		result.setStatus(0);
		result.setMsg("保存笔记完成");
		return result;
	}

	@Override
	public NoteResult loadNote(String noteId) {
		NoteResult result = new NoteResult();
		Note note = noteDao.findById(noteId);
		result.setStatus(0);
		result.setMsg("加载笔记内容成功");
		result.setData(note);
		return result;
	}

	@Override
	public NoteResult loadBookNotes(String bookId) {
		NoteResult result = new NoteResult();
		List<Note> list =
				noteDao.findByBookId(bookId);
		result.setStatus(0);
		result.setMsg("查询笔记列表成功");
		result.setData(list);
		return result;
	}
}
