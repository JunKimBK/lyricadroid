package com.dut.banana;

import java.util.Date;

/**
 * Present lyric detail, this class add info for {@link BaseLyric} class.
 */
public class Lyric extends BaseLyric {
	private static final long serialVersionUID = 1L;

	private String mUploader;
	private Date mUploadedDate;
	private String mContent;
	private int mNumOfDonwloads;

	public String getUploader() {
		return mUploader;
	}

	public void setUploader(String mUploader) {
		if (null == mUploader)
			return;
		this.mUploader = mUploader.replace("''", "'");
	}

	public Date getUploadedDate() {
		return mUploadedDate;
	}

	public void setUploadedDate(Date mUploadedDate) {
		this.mUploadedDate = mUploadedDate;
	}

	public String getContent() {
		return mContent;
	}

	public void setConent(String content) {
		if (null == content)
			return;
		mContent = content.replace("''", "'");
	}

	public int getNumOfDonwloads() {
		return mNumOfDonwloads;
	}

	public void setNumOfDonwloads(int mNumOfDonwloads) {
		this.mNumOfDonwloads = mNumOfDonwloads;
	}
}
