package com.cn.cloud_note.dao;

import com.cn.cloud_note.entity.Share;

import java.util.List;


public interface ShareDao {
	public int updateShare(Share share);
	public Share findById(String shareId);
	public List<Share> findLikeTitle(String title);
	public Share findByNoteId(String noteId);
	public void save(Share share);
}
