package com.example.kolys.musicalplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.FrameLayout

import com.example.kolys.musicalplayer.RecyclerView.SongsAdapter

import java.util.ArrayList

class RecuclerActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener, ServiceConnection, MyCallBack {

    lateinit var recyclerView: RecyclerView
    private val songs = ArrayList<Song>()
    private var musicSrv: MediaPlayerService? = null
    private var playIntent: Intent? = null
    private var musicBound = false
    private var settingsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeClass.getTheme(this)
        setTheme(ThemeClass.getTheme(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recucler)
        fillData()
        recyclerView = findViewById(R.id.recycler_view)
        val manager = LinearLayoutManager(this)
        val onClick = object : MyCallBack {
            override fun onItemClick(position: Int) {
                musicSrv?.let {
                    it.stop()
                    it.setSong(position)
                    it.playSong()
                }
                val intent = Intent(this@RecuclerActivity, SongDetailsActivity::class.java)
                intent.putExtra("SongName", songs[position].songName)
                intent.putExtra("SongId", songs[position].songId)
                startActivity(intent)
            }
        }
        val adapter = SongsAdapter(songs, onClick)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        prefs.registerOnSharedPreferenceChangeListener(this)
        val prefBtn = findViewById<Button>(R.id.btn_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_settings, SettingsFragment.newInstance())
                .commit()
        val container = findViewById<FrameLayout>(R.id.container)
        val settings = findViewById<FrameLayout>(R.id.fl_settings)
        prefBtn.setOnClickListener {
            if (settingsShown) {
                settings.visibility = View.GONE
                container.visibility = View.VISIBLE
            } else {
                container.visibility = View.GONE
                settings.visibility = View.VISIBLE
            }
            settingsShown = !settingsShown
        }
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as MediaPlayerService.MusicalBinder
        musicSrv = binder.service
        musicSrv?.initMusicPlayer(songs)
        musicSrv?.stop()
        musicBound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
        musicBound = false
    }

    override fun onStart() {
        super.onStart()
        if (playIntent == null) {
            playIntent = Intent(this, MediaPlayerService::class.java)
            bindService(playIntent, this, Context.BIND_AUTO_CREATE)
            startService(playIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (musicBound) {
            unbindService(this)
            musicBound = false
        }
    }

    override fun onDestroy() {
        stopService(playIntent)
        musicSrv = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun fillData() {
        songs.add(Song(1, "song_1"))
        songs.add(Song(2, "song_2"))
        songs.add(Song(3, "song_3"))
        songs.add(Song(4, "song_4"))
        songs.add(Song(5, "song_5"))
    }

    override fun onItemClick(position: Int) {
        musicSrv?.stop()
        musicSrv?.setSong(position)
        musicSrv?.playSong()
        val intent = Intent(this, SongDetailsActivity::class.java)
        intent.putExtra("SongName", songs[position].songName)
        intent.putExtra("SongId", songs[position].songId)
        startActivity(intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) = recreate()

}