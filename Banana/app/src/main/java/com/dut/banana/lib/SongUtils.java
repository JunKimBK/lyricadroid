package com.dut.banana.lib;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.dut.banana.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 14/11/2015.
 */
public final class SongUtils {
    public static String longToDuration(long milliseconds) {
        milliseconds /= 1000;
        int hours = (int) (milliseconds / 3600);
        int minutes = (int) ((milliseconds % 3600) / 60);
        int seconds = (int) (milliseconds % 60);
        if(hours > 0) {
            return String.format("%2d:%2d:%2d", hours, minutes, seconds);
        } else {
            if(minutes > 0)
                return String.format("%02d:%02d", minutes, seconds);
            else
                return String.format("%d sec", seconds);
        }
    }

    public static List<Song> getAllSongs(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        List<Song> songs = new ArrayList<>();
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                long id = cursor.getLong(idColumn);
                long duration = cursor.getLong(durationColumn);
                String artist = cursor.getString(artistColumn);
                String title = cursor.getString(titleColumn);
                Song song = new Song();
                song.setId(id);
                song.setDuration(duration);
                song.setArtist(artist);
                song.setTitle(title);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        return songs;
    }
}
