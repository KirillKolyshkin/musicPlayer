package com.example.kolys.musicalplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ServiceCompat;
import android.app.Service;
import android.util.Log;

import java.util.ArrayList;

public class MediaPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static MediaPlayerService mediaPlayerService;
    private MediaPlayer player = new MediaPlayer();
    private ArrayList<Song> songs;
    private int songPosn;
    private final IBinder musicBind = new MusicalBinder();
    private Context context;

    public static MediaPlayerService getInstance() {
        if (mediaPlayerService == null)
            mediaPlayerService = new MediaPlayerService();
        return mediaPlayerService;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        songPosn = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void initMusicPlayer() {
        player.setWakeMode(context,
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songs) {
        this.songs = songs;
    }


    public void playSong() {
        player.reset();
        Song playSong = songs.get(songPosn);
        long currSong = playSong.getSongId();
        Uri trackUri = Uri.parse("android.resource://com.example.kolys.musicalplayer/raw/song_" + currSong);

        try {
            player.setDataSource(context, trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.start();
    }

    public void pauseMusic() {
        player.pause();
    }

    public void unPauseMusic() {
        player.start();
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public void getNext() {
        if (songPosn < songs.size() - 1)
            songPosn++;
        else
            songPosn = 0;
        playSong();
    }

    public void getPrevious() {
        if (songPosn == 0)
            songPosn = songs.size() - 1;
        else
            songPosn--;
        playSong();
    }


    public class MusicalBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}