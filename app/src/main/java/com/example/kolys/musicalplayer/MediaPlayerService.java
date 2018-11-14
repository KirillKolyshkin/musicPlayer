package com.example.kolys.musicalplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.ArrayList;

public class MediaPlayerService extends Service implements
        MediaPlayer.OnPreparedListener {

    private static MediaPlayerService mediaPlayerService;
    private MediaPlayer player = new MediaPlayer();
    private ArrayList<Song> songs;
    private int songPosn;
    private final IBinder musicBind = new MusicalBinder();

    public static MediaPlayerService getInstance() {
        if (mediaPlayerService == null)
            mediaPlayerService = new MediaPlayerService();
        return mediaPlayerService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void initMusicPlayer(ArrayList<Song> songs) {
        songPosn = 0;
        player = new MediaPlayer();
        this.songs = songs;
        player.setWakeMode(this,
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        playSong();
    }

    public void playSong() {
        long currSong =  songs.get(songPosn).getSongId();
        Uri trackUri = Uri.parse("android.resource://com.example.kolys.musicalplayer/raw/song_" + currSong);
        player = MediaPlayer.create(this, trackUri);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                getNext();
            }
        });
    }

    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    public void getNext() {
        if (songPosn < songs.size() - 1)
            songPosn++;
        else
            songPosn = 0;
        stop();
        playSong();
    }

    public void getPrevious() {
        if (songPosn == 0)
            songPosn = songs.size() - 1;
        else
            songPosn--;
        stop();
        playSong();
    }

    public void play() {
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
    }

    public void stop() {
        player.release();
    }

    public class MusicalBinder extends Binder {
        MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

}