package com.dut.banana.net;

import java.io.Serializable;

import com.blogspot.sontx.libex.util.Convert;

/**
 * Present a communication package use between client and server. This class can
 * convert to string and string to instance of this class via
 * {@link com.blogspot.sontx.libex.util.Convert} class. See {@link #toString()}
 * for convert to string(to send) and see {@link #parse(String)} for convert to
 * package object(process after received).
 */
public abstract class Package implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Each command can be included in header of request/response **/

	/**
	 * Search a lyric from server. Result can have 0, 1, 2... lyrics, each lyric
	 * which is returned will a {@link com.dut.banana.BaseLyric} object.
	 */
	public static final int COMMAND_SEARCH = 1;
	/**
	 * Download a specific lyric by give an lyric id. Result can have 0(lyric id
	 * not exists) or 1(OK), result is a {@link com.dut.banana.Lyric} object.
	 */
	public static final int COMMAND_DOWNLOAD_LYRIC = 2;
	/**
	 * Download the hot playlist which created by count of download Result is
	 * list of {@link com.dut.banana.BaseLyric} objects
	 */
	public static final int COMMAND_DOWNLOAD_HOTPLAYLIST = 3;
	/**
	 * Create an account, a guest will give a user name(do not exists yet) and
	 * password(has been hash by md5) for server, then the server will create an
	 * account and send result for client. The result can 1 for success or 0 for
	 * fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_CREATE = 4;
	/**
	 * Login to server if client is a user, server need user name and password
	 * hash for check login. The result can 1 for success or 0 for fail(then a
	 * message will contain error info)
	 */
	public static final int COMMAND_ACCOUNT_LOGIN = 5;
	/**
	 * User change password, server need new password hash. Note: require login
	 * before use this function. The result can 1 for success or 0 for fail(then
	 * a message will contain error info)
	 */
	public static final int COMMAND_ACCOUNT_CHANGEPASSWORD = 6;
	/**
	 * A user contribute a lyric to server, user need provide an
	 * {@link com.dut.banana.Lyric} object has converted to string. The result
	 * can 1 for success or 0 for fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_UPLOAD = 7;
	/**
	 * A user report a lyric is spam, user need provide a lyric id and short
	 * description about this lyric(why's it spam?). The result can 1 for
	 * success or 0 for fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_REPORT = 8;
	/**
	 * A user rate for a lyric, user need provide an rate number between 0 and
	 * {@link {@link com.dut.banana.BaseLyric.#MAX_RATE}. The result can 1 for
	 * success or 0 for fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_RATE = 9;
	/**
	 * A user delete a lyric, user need provide a lyric id. The result can 1 for
	 * success or 0 for fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_MGR_DELETE = 10;
	/**
	 * A user edit a lyric, user need provide a
	 * {@link {@link com.dut.banana.Lyric}. The result can 1 for success or 0
	 * for fail(then a message will contain error info).
	 */
	public static final int COMMAND_ACCOUNT_MGR_EDIT = 11;
	public static final int COMMAND_BOSS_LOGIN = 12;
	public static final int COMMAND_BOSS_CHANGEPASSWORD = 13;
	public static final int COMMAND_BOSS_MGR_LYRIC_DELETE = 14;
	public static final int COMMAND_BOSS_MGR_LYRIC_BROWSEREPORTS = 15;
	public static final int COMMAND_BOSS_MGR_ACCOUNT_DELETE = 16;

	private int mCommand;

	public int getCommand() {
		return mCommand;
	}

	protected Package(int command) {
		mCommand = command;
	}

	protected void setCommand(int command) {
		mCommand = command;
	}

	@Override
	public String toString() {
		return Convert.objectToString(this);
	}

	public static Package parse(String st) {
		return (Package) Convert.stringToObject(st);
	}
}
