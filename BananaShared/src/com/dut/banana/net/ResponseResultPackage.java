package com.dut.banana.net;

public abstract class ResponseResultPackage extends Package {
	private static final long serialVersionUID = 1L;
	private boolean mIsSuccess;
	private String mErrorMessage;

	protected ResponseResultPackage(int command) {
		super(command);
	}

	public boolean isIsSuccess() {
		return mIsSuccess;
	}

	public void setIsSuccess(boolean mIsSuccess) {
		this.mIsSuccess = mIsSuccess;
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}

	public void setErrorMessage(String mErrorMessage) {
		this.mErrorMessage = mErrorMessage;
	}
}
