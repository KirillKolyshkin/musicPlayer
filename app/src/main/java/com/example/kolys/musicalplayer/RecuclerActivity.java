package com.example.kolys.musicalplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.kolys.musicalplayer.RecyclerView.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

public class RecuclerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{


    public RecyclerView recyclerView;
    private ArrayList<Song> songs = new ArrayList<>();
    private MediaPlayerService musicSrv = MediaPlayerService.getInstance();
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeClass.getTheme(this);
        setTheme(ThemeClass.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recucler);
        fillData();
        musicSrv.setContext(this);
        MyAdapter adapter = new MyAdapter(songs, this);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        musicSrv.setList(songs);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
        Button prefBtn = (Button) findViewById(R.id.btn_settings);
        prefBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent settingsActivity = new Intent(getBaseContext(),
                        Settings.class);
                startActivity(settingsActivity);
            }
        });
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerService.MusicalBinder binder = (MediaPlayerService.MusicalBinder) service;
            musicSrv = binder.getService();
            musicSrv.setList(songs);
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
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
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
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fillData() {
        //songs = new ArrayList<>();
        songs.add(new Song(1, "song_1", "raw/song_1.mp3"));
        songs.add(new Song(2, "song_2", "MusicalPlayer//app//src//main//res//raw//song_2"));
        songs.add(new Song(3, "song_3", "MusicalPlayer//app//src//main//res//raw//song_3"));
        songs.add(new Song(4, "song_4", "MusicalPlayer//app//src//main//res//raw//song_4"));
        songs.add(new Song(5, "song_5", "MusicalPlayer//app//src//main//res//raw//song_5"));
    }

    public void onItemClick(int position) {
        musicSrv.setSong(position);
        musicSrv.playSong();
        Intent intent = new Intent(this, SongDetailsActivity.class);
        intent.putExtra("SongName", songs.get(position).getSongName());
        intent.putExtra("SongId", songs.get(position).getSongId());
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        recreate();
    }
}


