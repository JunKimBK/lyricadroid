package com.dut.banana.showlyric;

import com.dut.banana.KaraLine;

import java.util.List;

/**
 * Created by VoTung246 on 8/11/2015.
 */
public interface ILyricBuilder {
    List<KaraLine> getKaraLines(String lyricContent);
}
