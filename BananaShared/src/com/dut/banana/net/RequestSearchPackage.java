package com.dut.banana.net;

public class RequestSearchPackage extends Package {
	private String mSongName;
	private String mArtist;

	private static final long serialVersionUID = 1L;

	public RequestSearchPackage() {
		super(COMMAND_SEARCH);
	}

	public String getSongName() {
		return mSongName;
	}

	public void setSongName(String mSongName) {
		this.mSongName = mSongName;
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String mArtist) {
		this.mArtist = mArtist;
	}

}
