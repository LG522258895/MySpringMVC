package com.cn.cloud_note.service;


import com.cn.cloud_note.util.NoteResult;

public interface ShareService {
	public NoteResult favorShare(
            String shareId, String userId);
	public NoteResult loadShare(String shareId);
	public NoteResult searchShare(String keyword);
	public NoteResult shareNote(String noteId);
}
