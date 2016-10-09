package com.dut.banana.net;

import com.dut.banana.Lyric;

public class RequestUploadPackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private Lyric mLyric;
	
	public RequestUploadPackage() {
		setCommand(COMMAND_ACCOUNT_UPLOAD);
	}

	public Lyric getLyric() {
		return mLyric;
	}

	public void setLyric(Lyric mLyric) {
		this.mLyric = mLyric;
	}
}
