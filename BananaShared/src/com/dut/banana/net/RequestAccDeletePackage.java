package com.dut.banana.net;

public class RequestAccDeletePackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private int mLyricId;

	public RequestAccDeletePackage() {
		setCommand(COMMAND_ACCOUNT_MGR_DELETE);
	}

	public int getLyricId() {
		return mLyricId;
	}

	public void setLyricId(int mLyricId) {
		this.mLyricId = mLyricId;
	}
}
