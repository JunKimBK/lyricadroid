package com.dut.banana.net;

public class RequestReportPackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private int mLyricId;
	private String mComment;

	public RequestReportPackage() {
		setCommand(COMMAND_ACCOUNT_REPORT);
	}

	public int getLyricId() {
		return mLyricId;
	}

	public void setLyricId(int mLyricId) {
		this.mLyricId = mLyricId;
	}

	public String getComment() {
		return mComment;
	}

	public void setComment(String mComment) {
		this.mComment = mComment;
	}
}
