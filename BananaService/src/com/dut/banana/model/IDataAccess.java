package com.dut.banana.model;

import java.util.List;

import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;

public interface IDataAccess {
	public void addLyric(Lyric lyric);

	public void updateLyric(Lyric lyric);

	public void removeLyric(int lyricId);

	public List<BaseLyric> search(String songName, String artist);

	public Lyric downloadLyric(int lyricId);

	public List<BaseLyric> downloadHotPlaylist();
	
	public boolean createAccount(String userName, String password);
	
	public boolean changePassword(String userName, String newPassword);
	
	public boolean isValidAccount(String userName, String password);
}