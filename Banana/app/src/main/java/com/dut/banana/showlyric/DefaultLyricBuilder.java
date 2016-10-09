package com.dut.banana.showlyric;

import android.util.Log;

import com.dut.banana.KaraLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VoTung246 on 8/11/2015.
 */
public class DefaultLyricBuilder implements ILyricBuilder{

    public List<KaraLine> getKaraLines(String lyricContent){
        if(lyricContent == null ||lyricContent.length()==0){
            return null;
        }
        StringReader stringReader = new StringReader(lyricContent);
        BufferedReader bufferedReader = new BufferedReader(stringReader);
        String line = null;
        List<KaraLine> lines = new ArrayList<KaraLine>();
        try {
            do {
                line = bufferedReader.readLine();
                if (line!= null && line.length()>0){
                    KaraLine lyricLines = KaraLine.parse(line);
                    if (lyricLines != null){
                            lines.add(lyricLines);
                    }
                }
            }while (line !=null);
        }catch (Exception e){
            e.getMessage();
            return  null;
        }
        finally {
            try {
                bufferedReader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            stringReader.close();
        }
        return lines;
    }
}
