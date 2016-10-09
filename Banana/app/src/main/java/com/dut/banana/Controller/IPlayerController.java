package com.dut.banana.Controller;

/**
 * Created by VoTung246 on 17/11/2015.
 */
public interface IPlayerController {
    void start();

    void pause();

    int getDuration();

    int getCurrentPosition();

    void seekTo(int pos);

    boolean isPlaying();

    void repeat(int type);

    void random(boolean on);
}
