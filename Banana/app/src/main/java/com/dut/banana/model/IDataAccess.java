package com.dut.banana.model;

import com.dut.banana.Account;
import com.dut.banana.Lyric;
import com.dut.banana.bean.Playlist;

import java.util.List;

/**
 * Created by trongvn on 16/11/2015.
 */
public interface IDataAccess {
    void addLyric(Lyric lyric);
    void updateLyric(Lyric lyric);
    void removeLyric(int lyricId);
    Lyric getLyric(String songName, String artist);

    void addPlaylist(Playlist playlist);
    void updatePlaylist(Playlist playlist);
    void removePlaylist(int playlistId);
    List<Playlist> getPlaylist(String name);

    List<Playlist> getAllPlaylists();
    Playlist getPlaylist(int id);

    void addAccount(Account account);

    void updateAccount(Account account);

    void deleteAccount();

    Account getAccount();
}
