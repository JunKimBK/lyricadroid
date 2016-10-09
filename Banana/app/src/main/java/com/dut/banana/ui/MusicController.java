package com.dut.banana.ui;

import android.app.Activity;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.Toast;

/**
 * Copyright by NE 2015.
 * Created by noem on 13/11/2015.
 */
public class MusicController extends MediaController {

    public MusicController(Activity context) {
        super(context);
    }

    private int doublePress = 0;
    private Handler waitHandler = new Handler();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            doublePress++;
            if (doublePress == 1) {
                Toast.makeText(MusicController.this.getContext(), "Press back again to exit!", Toast.LENGTH_SHORT).show();
                waitHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doublePress = 0;
                    }
                }, 2000);
            } else if (doublePress > 2) {
                ((Activity) getContext()).onBackPressed();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void show(int timeout) {
        super.show(0);
    }

    @Override
    public void hide() {
        show(0);
    }
}
