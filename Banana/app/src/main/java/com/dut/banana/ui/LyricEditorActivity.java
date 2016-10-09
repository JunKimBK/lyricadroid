package com.dut.banana.ui;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.banana.Account;
import com.dut.banana.KaraLine;
import com.dut.banana.KaraTime;
import com.dut.banana.Lyric;
import com.dut.banana.LyricFactor;
import com.dut.banana.R;
import com.dut.banana.Song;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.net.ServerConnection;
import com.dut.banana.ui.helper.AccountHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class LyricEditorActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemClickListener {
    private static final int SHOW_PROGRESS = 1;
    private SeekBar progressBar;
    private ImageButton btnPlay;
    private TextView tvDuration;
    private Song song;
    private MediaPlayer player = null;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private final Handler mHandler = new ProgressHandler(this);
    private LineAdapter adapter;
    private ListView listView;

    private List<KaraLine> loadLyric(Lyric lyric) {
        return LyricFactor.getKaraLines(lyric.getContent());
    }

    private void loadLyric() {
        Lyric lyric = (Lyric) SharedObject.getInstance().get(Config.SHARED_CURRENT_LYRIC);
        List<KaraLine> lines;

        if (lyric == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String content = bundle.getString(Config.INPUT_LYRIC_EDITOR);

            StringTokenizer tokenizer = new StringTokenizer(content, "\n");

            lines = new ArrayList<>();
            while (tokenizer.hasMoreElements()) {
                KaraLine line = new KaraLine();
                line.setLine(tokenizer.nextToken());
                KaraTime time = new KaraTime();
                time.setDuration(-1);
                line.setStartTime(time);
                lines.add(line);
            }
        } else {
            lines = loadLyric(lyric);
        }

        adapter = new LineAdapter(this.getApplicationContext(), lines);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_editor);
        song = (Song) SharedObject.getInstance().get(Config.SHARED_CURRENT_SONG);
        if (song == null) {
            Toast.makeText(LyricEditorActivity.this, "Which is song what you want to contribute lyric?", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadLyric();

        listView = (ListView) findViewById(R.id.lyric_editor_lv_lines);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        btnPlay = (ImageButton) findViewById(R.id.lyric_editor_ib_play);
        ImageButton btnNext = (ImageButton) findViewById(R.id.lyric_editor_ib_next);

        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        progressBar = (SeekBar) findViewById(R.id.lyric_editor_sb_progress);
        progressBar.setOnSeekBarChangeListener(this);
        tvDuration = (TextView) findViewById(R.id.lyric_editor_tv_duration);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    private void playSongAsync() {
        if (player != null) {
            player.start();
            return;
        }
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    private void seekTo(int pos) {
        player.seekTo(pos);
        tvDuration.setText(stringForTime(pos));
    }

    private String stringForTime(int timeMs) {
        if (timeMs < 0)
            return "NaN";
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

    private int updateProgress() {
        if (player == null)
            return -1;
        int position = player.getCurrentPosition();
        int duration = player.getDuration();

        if (duration > 0) {
            // use long to avoid overflow
            //long pos = 1000L * position / duration;
            progressBar.setProgress(position);
            tvDuration.setText(stringForTime(position));
        }
        return position;
    }

    private void updatePlayButton() {
        int ic = (player != null && player.isPlaying()) ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        btnPlay.setImageResource(ic);
    }

    private Lyric getLyric() {
        Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
        Song song = (Song) SharedObject.getInstance().get(Config.SHARED_CURRENT_SONG);
        Lyric lyric = new Lyric();
        if (account != null)
            lyric.setUploader(account.getUserName());
        else
            lyric.setUploader("NE");
        lyric.setConent(adapter.getLyricContent());
        lyric.setNumOfDonwloads(0);
        lyric.setUploadedDate(new Date());
        lyric.setId(-1);
        lyric.setSongName(song.getTitle());
        lyric.setArtist(song.getArtist());
        lyric.setRate((byte) 0);
        return lyric;
    }

    private void addLyric() {
        if (adapter.completed()) {
            IDataAccess dataAccess = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
            dataAccess.addLyric(getLyric());
            Toast.makeText(LyricEditorActivity.this, "Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LyricEditorActivity.this, "Complete this lyric before add!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadLyric(final Account account) {
        if (adapter.completed()) {
            IDataAccess dataAccess = (IDataAccess) SharedObject.getInstance().get(Config.SHARED_DATABASE_MGR);
            dataAccess.addLyric(getLyric());
            Toast.makeText(LyricEditorActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean ok = ServerConnection.getInstance().uploadLyric(getLyric(), account);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = ok ? "Upload done!" : "Something wrong when upload lyric(maybe your account is invalid!)";
                            Toast.makeText(LyricEditorActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();

        } else {
            Toast.makeText(LyricEditorActivity.this, "Complete this lyric before upload!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        if (player != null && player.isPlaying()) {
            mHandler.removeMessages(SHOW_PROGRESS);
            player.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            mHandler.removeMessages(SHOW_PROGRESS);
            player.release();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyric_editor_ib_play:
                if (player != null && player.isPlaying())
                    player.pause();
                else
                    playSongAsync();
                updatePlayButton();
                break;
            case R.id.lyric_editor_ib_next:
                if (player != null && player.isPlaying()) {
                    int currentPosition = player.getCurrentPosition();
                    adapter.nextLine(currentPosition);
                    listView.invalidateViews();
                }
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        mp.release();
        player = null;
        Toast.makeText(LyricEditorActivity.this, "Complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        int duration = player.getDuration();
        progressBar.setMax(duration);
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        Toast.makeText(LyricEditorActivity.this, "Playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser || player == null)
            return;
        seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeMessages(SHOW_PROGRESS);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        updateProgress();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        //updatePlayButton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (player != null && player.isPlaying()) {
            KaraLine line = (KaraLine) adapter.getItem(position);
            KaraTime time = new KaraTime();
            time.setDuration(player.getCurrentPosition());
            line.setStartTime(time);
            adapter.setCurrentLine(position);
            listView.invalidateViews();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lyric_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lyric_editor_menu_add:
                addLyric();
                break;
            case R.id.lyric_editor_menu_upload:
                Account account = (Account) SharedObject.getInstance().get(Config.SHARED_ACCOUNT);
                if (account != null) {
                    uploadLyric(account);
                } else {
                    AccountHelper accountHelper = (AccountHelper) SharedObject.getInstance().get(Config.SHARED_ACCOUNT_HELPER);
                    accountHelper.requestAccount(this, new AccountHelper.IWorker() {
                        @Override
                        public void onAccountEnter(Account account) {
                            uploadLyric(account);
                        }

                        @Override
                        public void onAccountRefuse() {
                        }
                    });
                }
                break;
        }
        return true;
    }

    private static class ProgressHandler extends Handler {
        private WeakReference<LyricEditorActivity> target;

        public ProgressHandler(LyricEditorActivity target) {
            this.target = new WeakReference<LyricEditorActivity>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            int pos;
            LyricEditorActivity target = this.target.get();
            if (target == null)
                return;
            switch (msg.what) {
                case SHOW_PROGRESS:
                    pos = target.updateProgress();
                    if (pos < 0 || (target.player != null && target.player.isPlaying())) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }

    private class LineAdapter extends BaseAdapter {
        private List<KaraLine> lines;
        private LayoutInflater inflater;
        private int currentLine = -1;

        public LineAdapter(Context context, List<KaraLine> lines) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.lines = lines;
        }

        public boolean completed() {
            for (KaraLine line : lines) {
                if (line.getStartTime().getDuration() < 0)
                    return false;
            }
            return true;
        }

        public String getLyricContent() {
            StringBuilder builder = new StringBuilder();
            for (KaraLine line : lines) {
                builder.append(line.toString() + "\n");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }

        public int getCurrentLine() {
            return currentLine;
        }

        public void setCurrentLine(int currentLine) {
            this.currentLine = currentLine;
        }

        public void nextLine(long duration) {
            if (currentLine < lines.size() - 1) {
                KaraLine line = lines.get(++currentLine);
                line.getStartTime().setDuration(duration);
            }
        }

        @Override
        public int getCount() {
            return lines.size();
        }

        @Override
        public Object getItem(int position) {
            return lines.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_lyric_editor_item, null);
                holder = new ViewHolder();
                holder.tvDuaration = (TextView) convertView.findViewById(R.id.lyric_editor_item_tv_duration);
                holder.tvLine = (TextView) convertView.findViewById(R.id.lyric_editor_item_tv_line);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            KaraLine line = lines.get(position);
            int color = position == currentLine ? R.color.colorAccent : R.color.colorNormal;
            holder.tvDuaration.setText(stringForTime((int) line.getStartTime().getDuration()));
            holder.tvDuaration.setTextColor(getResources().getColor(color));
            holder.tvLine.setText(line.getLine());
            return convertView;
        }

        private class ViewHolder {
            TextView tvDuaration;
            TextView tvLine;
        }
    }
}