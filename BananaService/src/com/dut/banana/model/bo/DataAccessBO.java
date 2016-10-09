package com.dut.banana.model.bo;

import java.util.List;

import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.model.dao.SQLiteDB;

public class DataAccessBO implements IDataAccess {
	private static final String M_DATA_FILE_PATH = "/mnt/01D0430720DCAEB0/OneDrive/Repos/banana-project/BananaService/banana.sqlite";
	private static final int M_SIZE_OF_HOTPLAYLIST = 5;
	SQLiteDB sqliteDB = new SQLiteDB(M_DATA_FILE_PATH);

	@Override
	public void addLyric(Lyric lyric) {
		sqliteDB.addLyric(lyric);
	}

	@Override
	public void updateLyric(Lyric lyric) {
		sqliteDB.updateLyric(lyric);
	}

	@Override
	public void removeLyric(int lyricId) {
		sqliteDB.removeLyric(lyricId);
	}

	@Override
	public List<BaseLyric> search(String songName, String artist) {
		System.out.println(String.format("Search for %s - %s", songName, artist));
		List<BaseLyric> baseLyrics = sqliteDB.getListBaseLyrics(songName, artist);
		System.out.println("Found " + baseLyrics.size() + " lyrics");
		return baseLyrics;
	}

	@Override
	public Lyric downloadLyric(int lyricId) {
		System.out.println("Request download lyric id = " + lyricId);
		Lyric lyric = sqliteDB.getLyric(lyricId);
		if(lyric != null) {
			System.out.println("Found lyric");
		} else {
			System.out.println("No found!");
		}
		return lyric;
	}

	@Override
	public List<BaseLyric> downloadHotPlaylist() {
		return sqliteDB.getHotPlaylist(M_SIZE_OF_HOTPLAYLIST);
	}

	@Override
	public boolean createAccount(String userName, String password) {
		return sqliteDB.createAccount(userName, password);
	}

	@Override
	public boolean changePassword(String userName, String newPassword) {
		return sqliteDB.changePassword(userName, newPassword);
	}

	@Override
	public boolean isValidAccount(String userName, String password) {
		return sqliteDB.isValidAccount(userName, password);
	}

}