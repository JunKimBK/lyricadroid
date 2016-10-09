package com.dut.banana;

/**
 * Present a kara line include start time this line and line content. A lyric
 * contain many kara lines. This class support convert an instance of this class
 * to string(see {@link #toString()}) or and vice versa(see {@link #parse(String)}).
 */
public class KaraLine {
	private KaraTime mStartTime;
	private String mLine;

	public KaraTime getStartTime() {
		return mStartTime;
	}

	public void setStartTime(KaraTime mStartTime) {
		this.mStartTime = mStartTime;
	}

	public String getLine() {
		return mLine;
	}

	public void setLine(String mLine) {
		this.mLine = mLine;
	}

	@Override
	public String toString() {
		return String.format("[%s]%s", mStartTime.toString(), mLine);
	}

	public static KaraLine parse(String st) {
		if (st == null || st.length() < 9)
			return null;
		KaraTime startTime = KaraTime.parse(st.substring(1, 9));
		if (startTime == null)
			return null;
		String line = st.substring(10);
		KaraLine karaLine = new KaraLine();
		karaLine.setStartTime(startTime);
		karaLine.setLine(line);
		return karaLine;
	}
}
