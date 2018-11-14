package com.example.kolys.musicalplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;

import com.example.kolys.musicalplayer.RecyclerView.SongsAdapter;

import java.util.ArrayList;

public class RecuclerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, ServiceConnection, MyCallBack {


    public RecyclerView recyclerView;
    private ArrayList<Song> songs = new ArrayList<>();
    private MediaPlayerService musicSrv = MediaPlayerService.getInstance();
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean settingsShown = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        ThemeClass.getTheme(this);
        setTheme(ThemeClass.getTheme(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recucler);
        fillData();
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        MyCallBack onClick = new MyCallBack() {
            @Override
            public void onItemClick(int position) {
                musicSrv.stop();
                musicSrv.setSong(position);
                musicSrv.playSong();
                Intent intent = new Intent(RecuclerActivity.this, SongDetailsActivity.class);
                intent.putExtra("SongName", songs.get(position).getSongName());
                intent.putExtra("SongId", songs.get(position).getSongId());
                startActivity(intent);
            }
        };
        SongsAdapter adapter = new SongsAdapter(songs, onClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(this);
        Button prefBtn = findViewById(R.id.btn_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_settings, Settings.newInstance())
                .commit();
        final FrameLayout container = findViewById(R.id.container);
        final FrameLayout settings = findViewById(R.id.fl_settings);
        prefBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (settingsShown) {
                    settings.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                } else {
                    container.setVisibility(View.GONE);
                    settings.setVisibility(View.VISIBLE);
                }
                settingsShown = !settingsShown;
            }
        });
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MediaPlayerService.MusicalBinder binder = (MediaPlayerService.MusicalBinder) service;
        musicSrv = binder.getService();
        musicSrv.initMusicPlayer(songs);
        musicSrv.stop();
        musicBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicBound = false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, this, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (musicBound) {
            unbindService(this);
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
        songs.add(new Song(1, "song_1"));
        songs.add(new Song(2, "song_2"));
        songs.add(new Song(3, "song_3"));
        songs.add(new Song(4, "song_4"));
        songs.add(new Song(5, "song_5"));
    }

    @Override
    public void onItemClick(int position) {
        musicSrv.stop();
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