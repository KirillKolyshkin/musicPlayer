package com.example.kolys.musicalplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

public class SongDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_song_name;
    ImageButton ib_pause;
    ImageButton ib_next;
    ImageButton ib_previous;
    int songId;
    private MediaPlayerService musicSrv = MediaPlayerService.getInstance();
    private Intent playIntent;
    boolean musicBound = false;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_details);
        tv_song_name = findViewById(R.id.tv_song_name);
        ib_pause = findViewById(R.id.ib_pause);
        ib_next = findViewById(R.id.ib_next);
        ib_previous = findViewById(R.id.ib_previous);

        Intent intent = getIntent();
        if (intent != null) {
            tv_song_name.setText(intent.getStringExtra("SongName"));
            songId = intent.getIntExtra("SongId", 1);
        }
        //File newFile = new File();

        ib_pause.setOnClickListener(this);
        ib_previous.setOnClickListener(this);
        ib_next.setOnClickListener(this);
    }

    ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MusicalBinder binder = (MediaPlayerService.MusicalBinder) service;
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }

        @Override
        public void onNullBinding(ComponentName name) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaPlayerService.class);
        bindService(intent, musicConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicBound) {
            unbindService(musicConnection);
            musicBound = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_pause:
                if (flag) {
                    musicSrv.pauseMusic();
                    ib_pause.setImageResource(R.drawable.ic_play);
                } else {
                    musicSrv.unPauseMusic();
                    ib_pause.setImageResource(R.drawable.ic_pause);
                }
                flag = !flag;
                break;
            case R.id.ib_next:
                musicSrv.getNext();
                if (songId < 5)
                    songId++;
                else
                    songId = 1;
                tv_song_name.setText("song_" + songId);
                break;
            case R.id.ib_previous:
                musicSrv.getPrevious();
                if (songId > 1)
                    songId--;
                else
                    songId = 5;
                tv_song_name.setText("song_" + songId);
                break;
        }
    }
}



