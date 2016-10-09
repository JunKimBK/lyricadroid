package com.dut.banana.net;

public class RequestRatePackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private int mLyricId;
	private byte mRate;
	private String mComment;

	public RequestRatePackage() {
		setCommand(COMMAND_ACCOUNT_RATE);
	}

	public int getLyricId() {
		return mLyricId;
	}

	public void setLyricId(int mLyricId) {
		this.mLyricId = mLyricId;
	}

	public byte getRate() {
		return mRate;
	}

	public void setRate(byte mRate) {
		this.mRate = mRate;
	}

	public String getComment() {
		return mComment;
	}

	public void setComment(String mComment) {
		this.mComment = mComment;
	}
}
