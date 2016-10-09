package com.dut.banana;

public final class KaraTime {
	private long duration;

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	// 00:01.06
	public static KaraTime parse(String st) {
		if (st == null || st.length() != 8)
			return null;
		byte min = Byte.parseByte(st.substring(0, 2));
		byte sec = Byte.parseByte(st.substring(3, 5));
		byte msec = Byte.parseByte(st.substring(6));
		KaraTime karaTime = new KaraTime();
		karaTime.duration = min * 60 * 1000 + sec * 1000 + msec;
		return karaTime;
	}
}
