package com.dut.banana.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dut.banana.R;

/**
 * Copyright by NE 2015.
 * Created by noem on 14/11/2015.
 */
public class PlaylistDialog {
    private Dialog dialog;
    private EditText input;
    private TextView message;
    private View.OnClickListener mOnClickListener;

    public String getPlaylistName() {
        return input.getText().toString();
    }

    public void setPlaylistName(String name) {
        input.setText(name);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setOnOKListener(View.OnClickListener listener) {
        mOnClickListener = listener;
    }

    public void show() {
        dialog.show();
    }

    public PlaylistDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_playlist_addnew);
        dialog.setTitle(R.string.app_name);
        input = (EditText) dialog.findViewById(R.id.playlist_addnew_name);
        message = (TextView) dialog.findViewById(R.id.playlist_addnew_tv_message);
        Button btnOK = (Button) dialog.findViewById(R.id.playlist_addnew_btn_getit);
        Button btnCancel = (Button) dialog.findViewById(R.id.playlist_addnew_btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(v);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
