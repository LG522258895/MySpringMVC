package com.cn.cloud_note.dao;

import com.cn.cloud_note.entity.Note;

import java.util.List;
import java.util.Map;


public interface NoteDao {
	/**
	 * 根据笔记ID更新笔记本ID
	 * @param params noteId:笔记ID;bookId:笔记本ID
	 * @return 受影响记录行数
	 */
	public int updateBookId(
            Map<String, Object> params);
	public int updateStatus(
            Map<String, Object> params);
	public void save(Note note);
	public int update(Note note);
	public Note findById(String id);
	public List<Note> findByBookId(String bookId);
}
