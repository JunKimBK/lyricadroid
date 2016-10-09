package com.dut.banana.bean;

/**
 * Copyright by NE 2015.
 * Created by noem on 14/11/2015.
 */
public class Playlist {
    private int id;
    private String name;
    private long[] songIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace("''", "'");
    }

    public long[] getSongIds() {
        return songIds;
    }

    public void setSongIds(long[] songIds) {
        this.songIds = songIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
