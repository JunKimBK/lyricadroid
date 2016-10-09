package com.dut.banana;

import java.io.Serializable;

/**
 * An class present base lyric info include lyric id, song name and artist.
 * Useful when display preview lyric. This class can convert to string and
 * string to instance of this class via
 * {@link com.blogspot.sontx.libex.util.Convert} class
 */
public class BaseLyric implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final byte MAX_RATE = 5;
	private int mId;
	private String mSongName;
	private String mArtist;
	private byte mRate;

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getSongName() {
		return mSongName;
	}

	public void setSongName(String mSongName) {
		this.mSongName = mSongName.replace("''", "'");
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String mArtist) {
		this.mArtist = mArtist.replace("''", "'");
	}

	public byte getRate() {
		return mRate;
	}

	public void setRate(byte mRate) {
		if (mRate <= MAX_RATE && mRate >= 0)
			this.mRate = mRate;
	}
}
