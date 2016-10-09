package com.dut.banana.net;


import java.util.List;
import com.dut.banana.BaseLyric;

public class ReponseDownloadHotLyricPackage extends Package {

	private static final long serialVersionUID = 1L;	
	private List<BaseLyric> mHotPlaylistLyrics;
	public ReponseDownloadHotLyricPackage() {
		super(COMMAND_DOWNLOAD_HOTPLAYLIST);
		// TODO Auto-generated constructor stub
	}
	public List<BaseLyric> getmHotPlaylistLyrics() {
		return mHotPlaylistLyrics;
	}
	public void setmHotPlaylistLyrics(List<BaseLyric> mHotPlaylistLyrics) {
		this.mHotPlaylistLyrics = mHotPlaylistLyrics;
	}


}
