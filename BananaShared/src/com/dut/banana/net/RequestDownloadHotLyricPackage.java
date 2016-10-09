package com.dut.banana.net;

public class RequestDownloadHotLyricPackage extends Package {
    private int mSizeOfHotPlaylist;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestDownloadHotLyricPackage() {
		super(COMMAND_DOWNLOAD_HOTPLAYLIST);
		// TODO Auto-generated constructor stub
	}

	public int getmSizeOfHotPlaylist() {
		return mSizeOfHotPlaylist;
	}

	public void setmSizeOfHotPlaylist(int mSizeOfHotPlaylist) {
		this.mSizeOfHotPlaylist = mSizeOfHotPlaylist;
	}

}
