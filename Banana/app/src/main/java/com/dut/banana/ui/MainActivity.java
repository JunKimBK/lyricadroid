package com.dut.banana.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dut.banana.Lyric;
import com.dut.banana.LyricFactor;
import com.dut.banana.R;
import com.dut.banana.Song;
import com.dut.banana.bean.Playlist;
import com.dut.banana.lib.Config;
import com.dut.banana.lib.SharedObject;
import com.dut.banana.lib.SongUtils;
import com.dut.banana.media.MediaPlayerService;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.model.dao.SQLiteDB;
import com.dut.banana.net.ServerConnection;
import com.dut.banana.showlyric.LyricView;
import com.dut.banana.ui.adapter.PlaylistAdapter;
import com.dut.banana.ui.adapter.SongAdapter;
import com.dut.banana.ui.helper.AccountHelper;
import com.dut.banana.ui.helper.PlaylistHelper;
import com.dut.banana.ui.helper.SongHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl,
        View.OnTouchListener, AdapterView.OnItemClickListener {
    private static final String LOG_TAG = "APP_DEBUG";
    private static final int PLAYLIST_VIEW_ID = 0;
    private static final int SONG_VIEW_ID = 1;
    private static final int LYRIC_VIEW_ID = 2;
    private static final int MIN_DISTANCE = 80;
    private float downX, downY, upX, upY;
    private MusicController controller;
    private MediaPlayerService playerService;
    private boolean musicBound = false;
    private List<Song> songs;
    private Intent playIntent;
    private boolean paused = false;
    private boolean playbackPaused = false;
    private ViewFlipper viewFlipper;
    private LyricView lyricView;
    private Thread lyricLoader = null;
    private Animation slide_in_left, slide_out_right;// swipe right
    private Animation slide_out_left, slide_in_right;// swipe left
    private PlaylistAdapter playlistAdapter;
    private SongAdapter songAdapter;
    private PlaylistHelper playlistHelper;
    private SongHelper songHelper;
    private IDataAccess database;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MusicBinder binder = (MediaPlayerService.MusicBinder) service;
            //get service
            playerService = binder.getService();
            //pass list
            playerService.setList(songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private BroadcastReceiver onPrepareReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            // When music player has been prepared, show controller
            controller.show(0);
            if (viewFlipper.getDisplayedChild() != LYRIC_VIEW_ID) {
                setMoveLeftEffect();
                viewFlipper.setDisplayedChild(LYRIC_VIEW_ID);
            }
        }
    };

    private void setController() {
        if (controller != null)
            return;
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.main_container));
        controller.setEnabled(true);
        controller.show(0);
    }

    private SongAdapter initListSongs(List<Song> songs) {
        songAdapter = new SongAdapter(this.getApplicationContext(), songs);
        ListView listView = (ListView) findViewById(R.id.main_lv_songs);
        listView.setAdapter(songAdapter);
        listView.setOnItemClickListener(this);
        songAdapter.setPresentView(listView);
        return songAdapter;
    }

    private void initListPlaylists() {
        playlistAdapter = new PlaylistAdapter(this.getApplicationContext(), database.getAllPlaylists());
        final ListView listView = (ListView) findViewById(R.id.main_lv_playlist);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.layout_playlist_item_addnew, null));
        listView.setAdapter(playlistAdapter);
        listView.setOnItemClickListener(this);
        playlistAdapter.setPresentView(listView);
    }

    private void initHelpers() {
        final ListView playlistView = (ListView) findViewById(R.id.main_lv_playlist);
        playlistHelper = new PlaylistHelper(this, database, playlistAdapter, playlistView);

        final ListView songsView = (ListView) findViewById(R.id.main_lv_songs);
        songHelper = new SongHelper(this, songAdapter, songsView, playlistAdapter, playlistView, songs);

        AccountHelper accountHelper = new AccountHelper();
        accountHelper.cacheAccount();
        SharedObject.getInstance().set(Config.SHARED_ACCOUNT_HELPER, accountHelper);
    }

    private void playNext() {
        playerService.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void playPrev() {
        playerService.playPrev();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    private void loadLyricAsync(final int pos) {
        if (lyricLoader != null && lyricLoader.isAlive())
            lyricLoader.interrupt();
        final Song song = songHelper.getSong(pos);
        TextView tvTitle = (TextView) findViewById(R.id.main_lyric_title);
        tvTitle.setText(String.format("%s - %s", song.getTitle(), song.getArtist()));
        lyricLoader = new Thread(new Runnable() {
            @Override
            public void run() {
                // search in local folder, if not exists then search in server
                // after two steps we have lyric for this song then display
                // in lyric view
                SharedObject.getInstance().set(Config.SHARED_CURRENT_SONG, song);
                final Lyric localLyric = database.getLyric(song.getTitle(), song.getArtist());
                if (localLyric == null) {
                    Log.d(LOG_TAG, "downloading lyric");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lyricView.setLoadingTipText("Downloading lyric...");
                        }
                    });
                    Log.d(LOG_TAG, "downloaded lyric");
                    final Lyric serverLyric = ServerConnection.getInstance().downloadLyric(song);
                    Log.d(LOG_TAG, "checking download content");
                    SharedObject.getInstance().set(Config.SHARED_CURRENT_LYRIC, serverLyric);
                    if (serverLyric != null) {
                        Log.d(LOG_TAG, "Downloaded lyric from server! now, save it to local database");
                        database.addLyric(serverLyric);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (serverLyric != null)
                                lyricView.setLyric(LyricFactor.getKaraLines(serverLyric.getContent()));
                            else
                                lyricView.setLoadingTipText("Sorry! not found!");
                        }
                    });
                    Log.d(LOG_TAG, "checked download content");
                } else {
                    Log.d(LOG_TAG, "Lyric exists in local! use it");
                    SharedObject.getInstance().set(Config.SHARED_CURRENT_LYRIC, localLyric);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lyricView.setLyric(LyricFactor.getKaraLines(localLyric.getContent()));
                        }
                    });
                }
            }
        });
        lyricLoader.start();
    }

    private void registerContextMenu() {
        ListView playlistListView = (ListView) findViewById(R.id.main_lv_playlist);
        registerForContextMenu(playlistListView);

        ListView songsListView = (ListView) findViewById(R.id.main_lv_songs);
        registerForContextMenu(songsListView);

        registerForContextMenu(lyricView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new SQLiteDB("db", this);
        SharedObject.getInstance().set(Config.SHARED_DATABASE_MGR, database);

        songs = SongUtils.getAllSongs(this);


        viewFlipper = (ViewFlipper) findViewById(R.id.main_vf);
        lyricView = (LyricView) findViewById(R.id.main_lyric_view);
        ListView songList = (ListView) findViewById(R.id.main_lv_songs);
        ListView playlistList = (ListView) findViewById(R.id.main_lv_playlist);

        initListSongs(songs);
        initListPlaylists();
        setController();
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onPrepareReceiver,
                new IntentFilter(MediaPlayerService.MEDIA_PLAYER_PREPARED));

        playlistList.setOnTouchListener(this);
        songList.setOnTouchListener(this);
        lyricView.setOnTouchListener(this);

        viewFlipper.setDisplayedChild(SONG_VIEW_ID);

        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        registerContextMenu();

        initHelpers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        controller.hide();
    }

    @Override
    public void start() {
        playerService.playSong();
    }

    @Override
    public void pause() {
        playerService.pause();
        playbackPaused = true;
    }

    @Override
    public int getDuration() {
        if (playerService != null && musicBound && playerService.isPlaying())
            return playerService.getDuration();
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (playerService != null && musicBound && playerService.isPlaying()) {
            int pos = playerService.getCurrentPosition();
            lyricView.setNowDuration(pos);
            return pos;
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        playerService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if (playerService != null && musicBound)
            return playerService.isPlaying();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        if (playerService == null)
            return 0;
        return (playerService.getCurrentPosition() * 100) / playerService.getDuration();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setMoveRightEffect() {
        viewFlipper.setInAnimation(slide_in_right);
        viewFlipper.setOutAnimation(slide_out_left);
    }

    private void setMoveLeftEffect() {
        viewFlipper.setInAnimation(slide_in_left);
        viewFlipper.setOutAnimation(slide_out_right);
    }

    private void onRightSwipe() {
//        if (viewFlipper.getDisplayedChild() < 2) {
//            setMoveRightEffect();
//            viewFlipper.setDisplayedChild(viewFlipper.getDisplayedChild() + 1);
//        }
    }

    private void onLeftSwipe() {
//        if (viewFlipper.getDisplayedChild() > 0) {
//            setMoveLeftEffect();
//            viewFlipper.setDisplayedChild(viewFlipper.getDisplayedChild() - 1);
//        }
    }

    private void onDownSwipe() {

    }

    private void onUpSwipe() {

    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX > 0) {
                            this.onRightSwipe();
                            return true;
                        }
                        if (deltaX < 0) {
                            this.onLeftSwipe();
                            return true;
                        }
                    } else {
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else {
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            this.onDownSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            this.onUpSwipe();
                            return true;
                        }
                    } else {
                        return false; // We don't consume the event
                    }
                }

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.main_lv_playlist:
                getMenuInflater().inflate(R.menu.playlist_context_menu, menu);
                break;
            case R.id.main_lv_songs:
                getMenuInflater().inflate(R.menu.song_context_menu, menu);
                break;
            case R.id.main_lyric_view:
                getMenuInflater().inflate(R.menu.lyric_context_menu, menu);
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // playlist and song
        if (info != null) {
            int position = info.position;
            View parent = (View) info.targetView.getParent();
            switch (parent.getId()) {
                case R.id.main_lv_playlist:
                    switch (item.getItemId()) {
                        case R.id.playlist_context_menu_rename:
                            playlistHelper.renamePlaylist(position);
                            break;
                        case R.id.playlist_context_menu_delete:
                            playlistHelper.deletePlaylist(position);
                            break;
                    }
                    break;
                case R.id.main_lv_songs:
                    switch (item.getItemId()) {
                        case R.id.song_context_menu_add_playlist:
                            songHelper.addToPlaylist(songs.get(position));
                            break;
                    }
                    break;
            }
        } else {
            // here is lyric
            switch (item.getItemId()) {
                case R.id.lyric_context_menu_view:
                    Lyric lyric = (Lyric) SharedObject.getInstance().get(Config.SHARED_CURRENT_LYRIC);
                    if (lyric == null)
                        Toast.makeText(MainActivity.this, "No lyric for this song", Toast.LENGTH_SHORT).show();
                    else
                        startActivity(new Intent(this, ViewLyricActivity.class));
                    break;
                case R.id.lyric_context_menu_contribute:
                    if (SharedObject.getInstance().get(Config.SHARED_CURRENT_SONG) != null) {
                        pause();
                        if (SharedObject.getInstance().get(Config.SHARED_CURRENT_LYRIC) == null)
                            startActivity(new Intent(this, RawLyricActivity.class));
                        else
                            startActivity(new Intent(this, LyricEditorActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "You must play a song before", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentId = viewFlipper.getDisplayedChild();

        switch (item.getItemId()) {
            case R.id.main_menu_playlist:
                if (currentId != PLAYLIST_VIEW_ID) {
                    setMoveLeftEffect();
                    viewFlipper.setDisplayedChild(PLAYLIST_VIEW_ID);
                }
                break;
            case R.id.main_menu_song:
                if (currentId != SONG_VIEW_ID) {
                    if (currentId > SONG_VIEW_ID)
                        setMoveLeftEffect();
                    else
                        setMoveRightEffect();
                    viewFlipper.setDisplayedChild(SONG_VIEW_ID);
                }
                break;
            case R.id.main_menu_lyric:
                if (currentId != LYRIC_VIEW_ID) {
                    setMoveRightEffect();
                    viewFlipper.setDisplayedChild(LYRIC_VIEW_ID);
                }
                break;
            case R.id.main_menu_hot:
                startActivity(new Intent(this, HotActivity.class));
                break;
            case R.id.main_menu_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.main_menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.main_lv_playlist:
                if (position == 0) {
                    playlistHelper.addPlaylist();
                } else {
                    position--;
                    List<Song> songs = songHelper.getSongs((Playlist) playlistAdapter.getItem(position));
                    SongAdapter songAdapter = initListSongs(songs);
                    songHelper.setSongAdapter(songAdapter);
                    setMoveRightEffect();
                    viewFlipper.setDisplayedChild(SONG_VIEW_ID);
                }
                break;
            case R.id.main_lv_songs:
                playerService.playSong(position);
                loadLyricAsync(position);
                if (playbackPaused) {
                    setController();
                    playbackPaused = false;
                }
                break;
        }
    }
}
