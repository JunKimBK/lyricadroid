package com.dut.banana.net;

import java.util.List;

import com.dut.banana.BaseLyric;

public class ResponseSearchPackage extends Package {
	private List<BaseLyric> mBaseLyrics;

	public ResponseSearchPackage() {
		super(COMMAND_SEARCH);
	}

	public List<BaseLyric> getBaseLyrics() {
		return mBaseLyrics;
	}

	public void setBaseLyrics(List<BaseLyric> mBaseLyrics) {
		this.mBaseLyrics = mBaseLyrics;
	}

	private static final long serialVersionUID = 1L;

}
