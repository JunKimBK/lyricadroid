package com.dut.banana.showlyric;

import com.dut.banana.KaraLine;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by VoTung246 on 8/11/2015.
 */
public interface ILyricView {
    /**
     * set the lyric lines to display
     */
    void setLyric(List<KaraLine> lyricLines);

    void setListener(LyricViewListener l);

    public static interface LyricViewListener{
    }
}
