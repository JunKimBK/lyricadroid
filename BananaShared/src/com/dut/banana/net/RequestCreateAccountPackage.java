package com.dut.banana.net;

public class RequestCreateAccountPackage extends Package {
	private static final long serialVersionUID = 1L;
	private String mUserName;
	private String mPasswordHash;

	public RequestCreateAccountPackage() {
		super(COMMAND_ACCOUNT_CREATE);
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getPasswordHash() {
		return mPasswordHash;
	}

	public void setPasswordHash(String mPasswordHash) {
		this.mPasswordHash = mPasswordHash;
	}
}
