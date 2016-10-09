package com.dut.banana.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;
import com.dut.banana.model.lib.Converter;

public class SQLiteDB implements IDatabase {
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	private String mDataFilePath;

	public SQLiteDB(String dataFilePath) {
		mDataFilePath = dataFilePath;
	}

	private void connectDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + mDataFilePath);
			stm = con.createStatement();
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	private void closeDB() {
		try {
			if (null != rs)
				rs.close();
			stm.close();
			con.close();
			System.out.println("Closed database successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addLyric(Lyric lyric) {
		connectDB();
		String sql = "INSERT INTO 'lyric' (" + "\n"
				+ "SongName,Artist,Uploader,UploadedDate,Rate,Content,NumOfDownloads" + "\n"
				+ ") VALUES (" + "\n"
				+ Converter.toParam(lyric.getSongName()) + ","
				+ Converter.toParam(lyric.getArtist()) + ","
				+ Converter.toParam(lyric.getUploader()) + ","
				+ Converter.toParam(Converter.dateToString(new Date())) + ","
				+ Converter.toParam(lyric.getRate()) + ","
				+ Converter.toParam(lyric.getContent()) + ","
				+ Converter.toParam(0) + "\n"
				+ ")";
		System.out.println(sql);
		try {
			stm.execute(sql);
			System.out.println("Added lyric successful");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	@Override
	public void updateLyric(Lyric lyric) {
		connectDB();
		String sql = "UPDATE 'lyric' SET" + "\n"
				+ "SongName=" + Converter.toParam(lyric.getSongName()) + ","
				+ "Artist=" + Converter.toParam(lyric.getArtist()) + ","
				+ "Uploader=" + Converter.toParam(lyric.getUploader()) + ","
				+ "UploadedDate=" + Converter.toParam(Converter.dateToString(lyric.getUploadedDate())) + ","
				+ "Rate=" + Converter.toParam(lyric.getRate()) + ","
				+ "Content=" + Converter.toParam(lyric.getContent()) + ","
				+ "NumOfDownloads=" + Converter.toParam(lyric.getNumOfDonwloads()) + "\n"
				+ "WHERE id=" + lyric.getId();
		System.out.println(sql);
		try {
			stm.execute(sql);
			System.out.println("Updated lyric successful");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	@Override
	public void removeLyric(int _id) {
		connectDB();
		String sql = "DELETE FROM 'lyric' WHERE id=" + _id;
		try {
			stm.execute(sql);
			System.out.println("Deleted lyric successful");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
	}

	@Override
	public List<BaseLyric> getListBaseLyrics(String songName, String artist) {
		connectDB();
		
		List<BaseLyric> listBaseLyrics = new ArrayList<>();
		BaseLyric baseLyric;
		
		String sql = "SELECT id, SongName, Artist, Rate FROM 'lyric'" + "\n"
				+ "WHERE " + "\n"
				+ "SongName LIKE " + Converter.toCondition(songName) + " AND "
				+ "Artist LIKE " + Converter.toCondition(artist);
		
		try {
			rs = stm.executeQuery(sql);
			
			while (rs.next()) {
				baseLyric = new BaseLyric();
				
				baseLyric.setId(rs.getInt("id"));
				baseLyric.setSongName(rs.getString("SongName"));
				baseLyric.setArtist(rs.getString("Artist"));
				baseLyric.setRate(new Byte(rs.getString("Rate")));
				
				listBaseLyrics.add(baseLyric);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		return listBaseLyrics;
	}
	
	@Override
	public List<BaseLyric> getHotPlaylist(int limit) {
		connectDB();
		
		List<BaseLyric> hotPlaylist = new ArrayList<>();
		BaseLyric baseLyric;
		String sql = "SELECT id, SongName, Artist, Rate, NumOfDownloads FROM 'lyric'" + "\n"
				+ "ORDER BY NumOfDownloads DESC LIMIT " + limit;
		
		try {
			rs = stm.executeQuery(sql);
			
			while (rs.next()) {
				baseLyric = new BaseLyric();
				
				baseLyric.setId(rs.getInt("id"));
				baseLyric.setSongName(rs.getString("SongName"));
				baseLyric.setArtist(rs.getString("Artist"));
				baseLyric.setRate(new Byte(rs.getString("Rate")));
				
				hotPlaylist.add(baseLyric);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		return hotPlaylist;
	}

	@Override
	public Lyric getLyric(int lyricId) {
		connectDB();
		Lyric lyric = null;
		String sqlIncrease;
		
		String sqlGet = "SELECT * FROM 'lyric'" + "\n"
				+ "WHERE id=" + lyricId;
		
		try {
			rs = stm.executeQuery(sqlGet);
			
			if (rs.next()) {
				lyric = new Lyric();
				lyric.setId(rs.getInt("id"));
				lyric.setSongName(rs.getString("SongName"));
				lyric.setArtist(rs.getString("Artist"));
				lyric.setUploader(rs.getString("Uploader"));
				lyric.setUploadedDate(Converter.stringToDate(rs.getString("UploadedDate")));
				lyric.setRate(new Byte(rs.getString("Rate")));
				lyric.setConent(rs.getString("Content"));
				lyric.setNumOfDonwloads(rs.getInt("NumOfDownloads") + 1);
			}
			
			// increase num of downloads
			sqlIncrease = "UPDATE 'lyric'" + "\n"
					+ "SET 'NumOfDownloads' =" + lyric.getNumOfDonwloads() + "\n"
					+ "WHERE id = " + lyricId;
			stm.executeUpdate(sqlIncrease);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		return lyric;
	}

	public List<String> getAllTables() {
		connectDB();
		List<String> listTables = new ArrayList<>();
		try {
			rs = stm.executeQuery("SELECT name FROM sqlite_temp_master WHERE type='table'");
			while (rs.next()) {
				listTables.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();	
		}
		return listTables;
	}
	
	@Override
	public boolean createAccount(String userName, String password) {
		if (checkAccountExist(userName))
			return false;
		
		connectDB();
		String sql = "INSERT INTO 'account' ('UserName','Password')"
				+ "VALUES (" + Converter.toParam(userName) + "," + Converter.toParam(password) + ")";
		System.out.println(sql);
		boolean createSuccessful = true;
		try {
			stm.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			createSuccessful = false;
		} finally {
			closeDB();
		}
		return createSuccessful;
	}

	@Override
	public boolean isValidAccount(String userName, String password) {
		connectDB();
		String sql = "SELECT UserName FROM 'account'" + "\n"
				+ "WHERE UserName=" + "'" + userName + "'" + " AND " 
				+ "Password=" + "'" + password + "'";
		System.out.println(sql);
		boolean checkOK = false;
		try {
			rs = stm.executeQuery(sql);
			if (rs.next())
				checkOK = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return checkOK;
	}

	@Override
	public boolean changePassword(String userName, String newPassword) {
		connectDB();
		boolean changeOK = true;
		String sql = "UPDATE 'account'" + "\n"
				+ "SET Password = '" + newPassword + "'\n"
				+ "WHERE UserName = '" + userName + "'";
		System.out.println(sql);
		try {
			stm.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			changeOK = false;
		} finally {
			closeDB();
		}
		return changeOK;
	}

	private boolean checkAccountExist(String userName) {
		connectDB();
		String sql = "SELECT UserName FROM 'account' WHERE UserName=" + "'" + userName + "'";
		boolean accountExist = false;
		try {
			rs = stm.executeQuery(sql);
			
			if (rs.next())
				accountExist = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return accountExist;
	}
}