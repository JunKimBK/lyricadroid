package com.dut.banana.net;

import com.dut.banana.Lyric;

public class ResponseDownloadLyricPackage extends Package {
	private Lyric mLyrics;

	public ResponseDownloadLyricPackage() {
		super(COMMAND_DOWNLOAD_LYRIC);
	}

	public Lyric getLyrics() {
		return mLyrics;
	}

	public void setLyrics(Lyric mLyrics) {
		this.mLyrics = mLyrics;
	}

	private static final long serialVersionUID = 1L;

}
