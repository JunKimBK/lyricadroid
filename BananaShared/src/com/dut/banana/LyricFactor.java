package com.dut.banana;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class provide utilities to work in a lyric.
 *
 */
public final class LyricFactor {
	public static List<KaraLine> getKaraLines(String lyricContent) {
		StringTokenizer tokenizer = new StringTokenizer(lyricContent, "\n");
		ArrayList<KaraLine> karaLines = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			KaraLine karaLine = KaraLine.parse(tokenizer.nextToken());
			if (karaLine == null)
				return null;
			karaLines.add(karaLine);
		}
		return karaLines;
	}

	public static String getLyricContent(List<KaraLine> karaLines) {
		StringBuilder builder = new StringBuilder();
		for (KaraLine karaLine : karaLines) {
			builder.append(karaLine.toString());
			builder.append("\n");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
