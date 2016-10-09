package com.dut.banana.net;

import com.dut.banana.Lyric;

public class RequestAccEditPackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private Lyric mLyric;

	public RequestAccEditPackage() {
		setCommand(COMMAND_ACCOUNT_MGR_EDIT);
	}

	public Lyric getLyric() {
		return mLyric;
	}

	public void setLyric(Lyric mLyric) {
		this.mLyric = mLyric;
	}
}
