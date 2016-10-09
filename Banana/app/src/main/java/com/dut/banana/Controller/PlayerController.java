package com.dut.banana.Controller;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dut.banana.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by VoTung246 on 17/11/2015.
 */
public class PlayerController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_SONG = 1;
    public static final int REPEAT_ALL = 2;
    private static final int SHOW_PROGRESS = 1;

    private ImageView imvPlay;
    private ImageView imvBack;
    private ImageView imvNext;
    private ImageView imvRepeat;
    private ImageView imvRandom;
    private SeekBar seekBarPlay;
    private TextView tvProcessed;
    private TextView tvTotal;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    private IPlayerController mPlayer;

    boolean isRandom = false;
    private int repeatType = REPEAT_NONE;

    public PlayerController(View presentView) {
        imvPlay = (ImageView) presentView.findViewById(R.id.imageViewPlay);
        imvPlay.setOnClickListener(this);

        imvBack = (ImageView) presentView.findViewById(R.id.imageViewBack);
        imvBack.setOnClickListener(this);

        imvNext = (ImageView) presentView.findViewById(R.id.imageViewNext);
        imvNext.setOnClickListener(this);

        imvRepeat = (ImageView) presentView.findViewById(R.id.imageViewRepeat);
        imvRepeat.setOnClickListener(this);

        imvRandom = (ImageView) presentView.findViewById(R.id.imageViewRandom);
        imvRandom.setOnClickListener(this);

        seekBarPlay = (SeekBar) presentView.findViewById(R.id.seekBarPlaying);
        seekBarPlay.setOnSeekBarChangeListener(this);

        tvProcessed = (TextView) presentView.findViewById(R.id.textViewProcessed);
        tvTotal = (TextView) presentView.findViewById(R.id.textViewTotal);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public void onProgressChanged(SeekBar seekBarPlay, int progressValue, boolean fromuser) {
        if (!fromuser)
            return;

        mPlayer.seekTo(progressValue);
        if (tvProcessed != null)
            tvProcessed.setText(stringForTime(progressValue));
    }

    private void updatePausePlay() {
        if (mPlayer.isPlaying())
            imvPlay.setImageResource(R.drawable.pause_lyrics);
        else
            imvPlay.setImageResource(R.drawable.play_lyrics);
    }

    public void onStartTrackingTouch(SeekBar seekBarPlay) {
        setProgress();
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    public void onStopTrackingTouch(SeekBar seekBarPlay) {

    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null)
            return 0;
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (seekBarPlay != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                seekBarPlay.setProgress((int) pos);
            }
        }

        if (tvTotal != null)
            tvTotal.setText(stringForTime(duration));
        if (tvProcessed != null)
            tvProcessed.setText(stringForTime(position));

        return position;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS:
                    if (mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        int pos = setProgress();
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewPlay:
                doPauseResume();
                break;

            case R.id.imageViewRepeat:
                if (repeatType == REPEAT_NONE) {
                    repeatType = REPEAT_SONG;
                    imvRepeat.setImageResource(R.drawable.repeat_one_lyrics);
                } else if (repeatType == REPEAT_SONG) {
                    repeatType = REPEAT_ALL;
                    imvRepeat.setImageResource(R.drawable.repeat_all_lyrics);
                } else {
                    repeatType = REPEAT_NONE;
                    imvRepeat.setImageResource(R.drawable.no_repeat_lyrics);
                }
                mPlayer.repeat(repeatType);
                break;

            case R.id.imageViewRandom:
                isRandom = !isRandom;
                if (isRandom)
                    imvRandom.setImageResource(R.drawable.random_lyrics);
                else
                    imvRandom.setImageResource(R.drawable.no_random_lyrics);
                mPlayer.random(isRandom);
                break;

            case  R.id.imageViewNext:

                break;

            case R.id.imageViewBack:

                break;
        }
    }
}
