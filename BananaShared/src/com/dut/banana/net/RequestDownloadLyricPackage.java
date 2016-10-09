package com.dut.banana.net;

public class RequestDownloadLyricPackage extends Package {
	private int mLyricId;

	public RequestDownloadLyricPackage() {
		super(COMMAND_DOWNLOAD_LYRIC);
	}

	public int getLyricId() {
		return mLyricId;
	}

	public void setLyricId(int mLyricId) {
		this.mLyricId = mLyricId;
	}

	private static final long serialVersionUID = 1L;

}
