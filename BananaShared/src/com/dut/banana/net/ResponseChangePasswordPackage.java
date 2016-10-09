package com.dut.banana.net;

public class ResponseChangePasswordPackage extends ResponseResultPackage {
	private static final long serialVersionUID = 1L;
	
	public ResponseChangePasswordPackage() {
		super(COMMAND_ACCOUNT_CHANGEPASSWORD);
	}
}
