package com.dut.banana.model.dao;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.dut.banana.Account;
import com.dut.banana.Lyric;
import com.dut.banana.bean.Playlist;
import com.dut.banana.lib.Converter;
import com.dut.banana.model.IDataAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trongvn on 16/11/2015.
 */
public class SQLiteDB implements IDataAccess {
    private static SQLiteDB currentInstance = null;
    private String mDBFilePath;
    private SQLiteDatabase db;
    private Cursor c;
    private Activity mActivity;
    private static int ID_COL_ID = 0;
    private static int SONGNAME_COL_ID = 1;
    private static int ARTIST_COL_ID = 2;
    private static int UPLOADER_COL_ID = 3;
    private static int RATE_COL_ID = 5;
    private static int CONTENT_COL_ID = 6;

    private static int NAME_COL_ID = 1;
    private static int SONGIDS_COL_ID = 2;
    private static String TAG = "SQLiteDB";

    public static SQLiteDB getCurrentInstance() {
        return currentInstance;
    }

    private String getAppDir() {
        PackageManager m = mActivity.getPackageManager();
        String s = mActivity.getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        s = p.applicationInfo.dataDir;
        return s;
    }

    public SQLiteDB(String dbFileName, Activity activity) {
        mActivity = activity;
        mDBFilePath = getAppDir() + "/" + dbFileName;

        Log.d(TAG, mDBFilePath);
        currentInstance = this;
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(mDBFilePath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "Database did not exist");
        }
        return checkDB != null;
    }

    void connectDB() {

        // check if database exists
        if (!checkDataBase()) {
            // database did not exist
            Log.d(TAG, "chua co database");

            // create database
            db = SQLiteDatabase.openOrCreateDatabase(mDBFilePath, null);

            // create lyric table
            db.execSQL("CREATE TABLE 'lyric' (" + "\n"
                    + "'id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
                    + "'SongName' NVARCHAR(100) COLLATE NOCASE,"
                    + "'Artist' NVARCHAR(100) COLLATE NOCASE,"
                    + "'Uploader' VARCHAR(10) COLLATE NOCASE,"
                    + "'UploadedDate' VARCHAR(10),"
                    + "'Rate' INTEGER,"
                    + "'Content' NVARCHAR(4000),"
                    + "'NumOfDownloads' INTEGER)");

            // create playlist table
            db.execSQL("CREATE  TABLE 'playlist' (\n" +
                    "'Id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,\n" +
                    "'Name' NVARCHAR(100) NOT NULL COLLATE NOCASE,\n" +
                    "'SongIDs' NVARCHAR(100))");

            // create account table
            db.execSQL("CREATE  TABLE 'account' ('UserName' TEXT PRIMARY KEY  NOT NULL , 'Password' TEXT)");
        } else {
            db = SQLiteDatabase.openOrCreateDatabase(mDBFilePath, null);
        }

        if (null != db)
            Log.d(TAG, "opened database");
    }

    void closeDB() {
        if (null != c)
            c.close();
        db.close();

        Log.d(TAG, "closed database");
    }

    @Override
    public void addLyric(Lyric lyric) {
        connectDB();
        String sql = "INSERT INTO 'lyric' (" + "\n"
                + "SongName,Artist,Uploader,Rate,Content" + "\n"
                + ") VALUES (" + "\n"
                + Converter.toParam(lyric.getSongName()) + ","
                + Converter.toParam(lyric.getArtist()) + ","
                + Converter.toParam(lyric.getUploader()) + ","
                + Converter.toParam(lyric.getRate()) + ","
                + Converter.toParam(lyric.getContent()) + "\n"
                + ")";

        Log.d(TAG, sql);

        db.execSQL(sql);

        Log.d(TAG, "add lyric ok");

        closeDB();
    }

    @Override
    public void updateLyric(Lyric lyric) {
        connectDB();

        String sql = "UPDATE 'lyric' SET" + "\n"
                + "SongName=" + Converter.toParam(lyric.getSongName()) + ","
                + "Artist=" + Converter.toParam(lyric.getArtist()) + ","
                + "Uploader=" + Converter.toParam(lyric.getUploader()) + ","
                + "Rate=" + Converter.toParam(lyric.getRate()) + ","
                + "Content=" + Converter.toParam(lyric.getContent()) + "\n"
                + "WHERE id=" + lyric.getId();

        Log.d(TAG, sql);

        db.execSQL(sql);

        Log.d(TAG, "updated lyric");

        closeDB();
    }

    @Override
    public void removeLyric(int lyricId) {
        connectDB();
        String sql = "DELETE FROM 'lyric' WHERE id=" + lyricId;

        Log.d(TAG, sql);

        db.execSQL(sql);

        closeDB();
    }

    @Override
    public Lyric getLyric(String songName, String artist) {
        connectDB();
        Lyric lyric = new Lyric();
        boolean getted = false;

        String sql = "SELECT * FROM  'lyric'" + "\n"
                + "WHERE SongName=" + Converter.toParam(songName)
                + " AND Artist=" + Converter.toParam(artist);

        Log.d("SQLiteDB: ", sql);

        c = db.rawQuery(sql, null);
        c.moveToFirst();
        if (false == c.isAfterLast()) {
            lyric.setId(c.getInt(ID_COL_ID));
            lyric.setSongName(c.getString(SONGNAME_COL_ID));
            lyric.setArtist(c.getString(ARTIST_COL_ID));
            lyric.setUploader(c.getString(UPLOADER_COL_ID));
            lyric.setRate(new Byte(c.getString(RATE_COL_ID)));
            lyric.setConent(c.getString(CONTENT_COL_ID));

            getted = true;
            Log.d(TAG, "getLyric ok");
        }

        closeDB();
        return getted ? lyric : null;
    }

    public List<Lyric> getAllLyrics() {
        connectDB();

        List<Lyric> listLyrics = new ArrayList<>();
        Lyric l;

        String sql = "SELECT * FROM  'lyric'";

        c = db.rawQuery(sql, null);
        c.moveToFirst();
        while (false == c.isAfterLast()) {
            l = new Lyric();

            l.setId(c.getInt(ID_COL_ID));
            l.setSongName(c.getString(SONGNAME_COL_ID));
            l.setArtist(c.getString(ARTIST_COL_ID));
            l.setUploader(c.getString(UPLOADER_COL_ID));
            l.setRate(new Byte(c.getString(RATE_COL_ID)));
            l.setConent(c.getString(CONTENT_COL_ID));

            listLyrics.add(l);
            c.moveToNext();
        }

        closeDB();
        return listLyrics;
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        connectDB();

        String sql = "INSERT INTO  'playlist' ('Name','SongIDs')\n" +
                "VALUES (" + Converter.toParam(playlist.getName()) + "," +
                Converter.toParam(Converter.listToString(playlist.getSongIds())) + ")";

        Log.d(TAG, sql);

        db.execSQL(sql);

        Log.d(TAG, "added playlist");

        closeDB();
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        connectDB();

        String sql = "UPDATE 'playlist'\n" +
                "SET 'Name'=" + Converter.toParam(playlist.getName()) + "," +
                "'SongIDs'=" + Converter.toParam(Converter.listToString(playlist.getSongIds())) + "\n" +
                "WHERE Id=" + playlist.getId();

        Log.d(TAG, sql);

        db.execSQL(sql);

        Log.d(TAG, "updated playlist");

        closeDB();
    }

    @Override
    public void removePlaylist(int playlistId) {
        connectDB();

        String sql = "DELETE FROM 'playlist' WHERE Id=" + playlistId;

        Log.d(TAG, sql);

        db.execSQL(sql);

        Log.d(TAG, "removed playlist");
        closeDB();
    }

    @Override
    public List<Playlist> getPlaylist(String name) {
        connectDB();

        List<Playlist> listPl = new ArrayList<>();
        Playlist pl;

        String sql = "SELECT * FROM 'playlist'\n" +
                "WHERE Name LIKE " + Converter.toCondition(name);

        Log.d(TAG, sql);

        c = db.rawQuery(sql, null);
        c.moveToFirst();
        while (false == c.isAfterLast()) {
            pl = new Playlist();

            pl.setId(c.getInt(ID_COL_ID));
            pl.setName(c.getString(NAME_COL_ID));
            pl.setSongIds(Converter.stringToListLongs(c.getString(SONGIDS_COL_ID)));

            listPl.add(pl);
            c.moveToNext();
        }

        Log.d(TAG, "getted playlist");
        closeDB();
        return listPl;
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        connectDB();

        List<Playlist> listPl = new ArrayList<>();
        Playlist pl;

        String sql = "SELECT * FROM 'playlist'";
        Log.d(TAG, sql);

        c = db.rawQuery(sql, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            pl = new Playlist();

            pl.setId(c.getInt(ID_COL_ID));
            pl.setName(c.getString(NAME_COL_ID));
            pl.setSongIds(Converter.stringToListLongs(c.getString(SONGIDS_COL_ID)));

            listPl.add(pl);
            c.moveToNext();
        }

        Log.d(TAG, "getted playlist");
        closeDB();
        return listPl;
    }

    @Override
    public Playlist getPlaylist(int id) {
        connectDB();

        String sql = "SELECT * FROM 'playlist' WHERE id = " + id;

        Log.d(TAG, sql);

        c = db.rawQuery(sql, null);
        Playlist playlist = null;
        if (c.moveToFirst()) {
            playlist = new Playlist();
            playlist.setId(id);
            playlist.setName(c.getString(NAME_COL_ID));
            playlist.setSongIds(Converter.stringToListLongs(c.getString(SONGIDS_COL_ID)));
        }

        Log.d(TAG, "getted playlist");
        closeDB();
        return playlist;
    }

    public void addAccount(Account account) {
        connectDB();
        String sql = "INSERT INTO 'main'.'account' (\n" +
                "'UserName','Password'\n" +
                ") VALUES (\n" +
                Converter.toParam(account.getUserName()) + "," +
                Converter.toParam(account.getPassword()) +
                ")";
        Log.d(TAG, sql);
        db.execSQL(sql);
        Log.d(TAG, "added account");
        closeDB();
    }

    @Override
    public void updateAccount(Account account) {
        connectDB();
        String sql = "UPDATE 'account' SET\n" +
                "'UserName' = " + Converter.toParam(account.getUserName()) + ",\n" +
                "'Password' = " + Converter.toParam(account.getPassword());
        Log.d(TAG, sql);
        db.execSQL(sql);
        Log.d(TAG, "updated Account");
        closeDB();
    }

    @Override
    public void deleteAccount() {
        connectDB();
        String sql = "DELETE FROM 'account'";
        Log.d(TAG, sql);
        db.execSQL(sql);
        Log.d(TAG, "deleted account");
        closeDB();
    }

    @Override
    public Account getAccount() {
        connectDB();
        Account acc = null;

        String sql = "SELECT * FROM 'account'";
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            acc = new Account();

            acc.setUserName(c.getString(0));
            acc.setPassword(c.getString(1));
        }

        closeDB();
        return acc;
    }
}
