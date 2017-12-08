package com.cn.cloud_note.service;


import com.cn.cloud_note.util.NoteResult;

public interface UserService {
	
	public NoteResult registUser(
            String name, String nick, String password);
	
	public NoteResult checkLogin(
            String name, String password);
	
}
