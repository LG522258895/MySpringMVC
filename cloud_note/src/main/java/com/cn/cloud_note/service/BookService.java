package com.cn.cloud_note.service;


import com.cn.cloud_note.util.NoteResult;

public interface BookService {
	public NoteResult renameBook(
            String bookId, String bookName);
	public NoteResult addBook(
            String userId, String bookName);
	public NoteResult loadUserBooks(String userId);
}
