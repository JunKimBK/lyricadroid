package com.dut.banana.model.dao;

import java.util.List;

import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;

public interface IDatabase {
	void addLyric(Lyric _lyric);

	void updateLyric(Lyric _lyric);

	void removeLyric(int _id);

	List<BaseLyric> getListBaseLyrics(String songName, String artist);
	
	List<BaseLyric> getHotPlaylist(int limit);
	
	Lyric getLyric(int lyricId);
	
	boolean createAccount(String userName, String password);
	
	boolean isValidAccount(String userName, String password);
	
	boolean changePassword(String userName, String newPassword);
}