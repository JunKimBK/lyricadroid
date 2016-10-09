package com.dut.banana.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dut.banana.Song;
import com.dut.banana.ui.MainActivity;

import java.util.List;
import java.util.Random;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {
    public static final String MEDIA_PLAYER_PREPARED = "MEDIA_PLAYER_PREPARED";
    private static final int NOTIFY_ID = 1;
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;
    private List<Song> songs;
    private int position;
    private boolean shuffle = false;
    private Random rand;

    public void initMusicPlayer() {
        //initialize position
        position = 0;
        //create player
        player = new MediaPlayer();

        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(List<Song> theSongs) {
        songs = theSongs;
    }

    public void playSong() {
        player.reset();
        //get song
        Song playSong = songs.get(position);
        //get id
        long currSong = playSong.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    public void playSong(int songIndex) {
        position = songIndex;
        playSong();
    }

    public void playNext() {
        if (shuffle) {
            int randPosition;
            do {
                randPosition = rand.nextInt(songs.size());
            } while (position == randPosition);
            position = randPosition;
        } else {
            position++;
            if (position >= songs.size())
                position = 0;
        }
        playSong();
    }

    public void playPrev() {
        position--;
        if (position < 0)
            position = songs.size() - 1;
        playSong();
    }

    public void pause() {
        if (player != null && player.isPlaying())
            player.pause();
    }

    public void seek(int pos) {
        player.seekTo(pos);
    }

    public void setShuffle() {
        shuffle = !shuffle;
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        if (player == null)
            return false;
        return player.isPlaying();
    }

    @Override
    public void onCreate() {
        //create the service
        super.onCreate();

        rand = new Random();

        initMusicPlayer();

//        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
//                AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            // could not get audio focus.
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        player = null;
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        Song playSong = songs.get(position);
        builder.setContentIntent(pendInt)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker(playSong.getTitle())
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(playSong.getTitle());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);

        Intent onPreparedIntent = new Intent(MEDIA_PLAYER_PREPARED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(onPreparedIntent);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (player == null)
                    initMusicPlayer();
                else if (!player.isPlaying())
                    player.start();
                player.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (player.isPlaying())
                    player.stop();
                player.release();
                player = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (player.isPlaying())
                    player.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (player.isPlaying())
                    player.setVolume(0.1f, 0.1f);
                break;
        }

    }

    public class MusicBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}
