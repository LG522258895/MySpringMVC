package com.cn.cloud_note.service;


import com.cn.cloud_note.util.NoteResult;

public interface NoteService {
	public NoteResult moveNote(
            String bookId, String noteId);
	public NoteResult recycleNote(String noteId);
	public NoteResult addNote(
            String userId, String title, String bookId);
	public NoteResult updateNote(
            String id, String title, String body);
	public NoteResult loadNote(String noteId);
	public NoteResult loadBookNotes(String bookId);
}
