package com.dut.banana.ui.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dut.banana.R;
import com.dut.banana.Song;
import com.dut.banana.bean.Playlist;
import com.dut.banana.ui.adapter.PlaylistAdapter;
import com.dut.banana.ui.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 15/11/2015.
 */
public final class SongHelper {
    private PlaylistAdapter playlistAdapter;
    private SongAdapter songAdapter;
    private ListView presentView;
    private ListView playlistView;
    private Activity activity;
    private List<Song> allSongs;

    public List<Song> getSongs(Playlist playlist) {
        if (playlist == null)
            return allSongs;
        List<Song> songs = new ArrayList<>();
        long[] songIds = playlist.getSongIds();
        for (int i = 0; i < songIds.length; i++) {
            for (Song song : allSongs) {
                if (song.getId() == songIds[i]) {
                    songs.add(song);
                    break;
                }
            }
        }
        return songs;
    }

    public Song getSong(int position) {
        return (Song) songAdapter.getItem(position);
    }

    public void setSongAdapter(SongAdapter songAdapter) {
        this.songAdapter = songAdapter;
    }

    public SongHelper(Activity activity, SongAdapter adapter, ListView presentView, PlaylistAdapter playlistAdapter, ListView playlistView, List<Song> allSongs) {
        this.activity = activity;
        this.songAdapter = adapter;
        this.presentView = presentView;
        this.playlistAdapter = playlistAdapter;
        this.playlistView = playlistView;
        this.allSongs = allSongs;
    }

    public void addToPlaylist(final Song song) {
        final List<Playlist> playlists = playlistAdapter.getData();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        if (playlists.size() > 0) {
            //builder.setMessage("Add " + song.getTitle() + " to playlist below: ");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    activity.getApplicationContext(),
                    android.R.layout.select_dialog_singlechoice);
            for (int i = 0; i < playlists.size(); i++) {
                arrayAdapter.add(playlists.get(i).getName());
            }

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Playlist playlist = playlists.get(which);
                    Toast.makeText(activity, "Added " + song.getTitle() + " to " + playlist.getName(), Toast.LENGTH_SHORT).show();
                    long[] currentSongIds = playlist.getSongIds();
                    long[] songIds = new long[currentSongIds.length + 1];
                    System.arraycopy(currentSongIds, 0, songIds, 0, currentSongIds.length);
                    songIds[songIds.length - 1] = song.getId();
                    playlist.setSongIds(songIds);
                    playlistView.invalidateViews();

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setMessage("You have not any playlist!\n Do you want to add new playlist?");
            builder.setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PlaylistHelper playlistHelper = PlaylistHelper.getCurrentInstance();
                    playlistHelper.addPlaylist();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.show();
    }
}
