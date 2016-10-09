package com.dut.banana.net;

public class RequestChangePasswordPackage extends RequestLoginPackage {
	private static final long serialVersionUID = 1L;
	private String mNewPasswordHash;

	public RequestChangePasswordPackage() {
		setCommand(COMMAND_ACCOUNT_CHANGEPASSWORD);
	}

	public String getNewPasswordHash() {
		return mNewPasswordHash;
	}

	public void setNewPasswordHash(String mNewPasswordHash) {
		this.mNewPasswordHash = mNewPasswordHash;
	}
}
