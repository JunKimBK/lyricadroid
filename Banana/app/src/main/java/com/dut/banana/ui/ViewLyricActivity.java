package com.dut.banana.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.banana.Account;
import com.dut.banana.Lyric;
import com.dut.banana.R;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.net.ServerConnection;
import com.dut.banana.ui.helper.AccountHelper;

public class ViewLyricActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText lyricView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lyric);
        lyricView = (EditText) findViewById(R.id.view_lyric_et_content);

        Lyric lyric = (Lyric) SharedObject.getInstance().get(Config.SHARED_CURRENT_LYRIC);
        if (lyric != null)
            lyricView.setText(lyric.getContent());

        TextView tvUploader = (TextView) findViewById(R.id.view_lyric_tv_uploader);
        String uploader = lyric.getUploader();
        tvUploader.setText(String.format("Upload by %s", uploader != null ? uploader : "NE"));

        ImageButton btnSave = (ImageButton) findViewById(R.id.view_lyric_ib_save);
        ImageButton btnUpload = (ImageButton) findViewById(R.id.view_lyric_ib_upload);
        btnSave.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String content = lyricView.getText().toString();
        if (content.length() == 0) {
            Toast.makeText(ViewLyricActivity.this, "Nothing here!", Toast.LENGTH_SHORT).show();
            return;
        }
        final Lyric lyric = (Lyric) SharedObject.getInstance().get(Config.SHARED_CURRENT_LYRIC);
        lyric.setConent(content);
        IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
        if (database == null)
            return;

        switch (v.getId()) {
            case R.id.view_lyric_ib_save:
                database.updateLyric(lyric);
                Toast.makeText(ViewLyricActivity.this, "Saved new lyric content!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_lyric_ib_upload:
                Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
                if (account == null) {
                    AccountHelper accountHelper = (AccountHelper) SharedObject.getInstance().get(Config.SHARED_ACCOUNT_HELPER);
                    accountHelper.requestAccount(this, new AccountHelper.IWorker() {
                        @Override
                        public void onAccountEnter(Account account) {
                            upload(lyric);
                        }

                        @Override
                        public void onAccountRefuse() {
                        }
                    });
                } else {
                    upload(lyric);
                }
                break;
        }
    }

    private void upload(final Lyric lyric) {
        // save lyric to database before upload it
        IDataAccess database = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
        database.updateLyric(lyric);
        final Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
        // now! upload to server
        Toast.makeText(ViewLyricActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean ok = ServerConnection.getInstance().uploadLyric(lyric, account);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = ok ? "Upload done!" : "Something wrong when upload lyric(maybe your account is invalid!)";
                        Toast.makeText(ViewLyricActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}
