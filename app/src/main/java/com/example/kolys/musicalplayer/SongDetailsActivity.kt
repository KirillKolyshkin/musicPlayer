package com.example.kolys.musicalplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView

import java.io.File

class SongDetailsActivity : AppCompatActivity(), View.OnClickListener, ServiceConnection {

    private lateinit var tv_song_name: TextView
    private lateinit var ib_pause: ImageButton
    private lateinit var ib_next: ImageButton
    private lateinit var ib_previous: ImageButton
    private var musicSrv : MediaPlayerService? = null
    private var musicBound = false
    private var flag = true
    private var songId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_details)
        tv_song_name = findViewById(R.id.tv_song_name)
        ib_pause = findViewById(R.id.ib_pause)
        ib_next = findViewById(R.id.ib_next)
        ib_previous = findViewById(R.id.ib_previous)

        val intent = intent
        if (intent != null) {
            tv_song_name.text = intent.getStringExtra("SongName")
            songId = intent.getIntExtra("SongId", 1)
        }

        ib_pause.setOnClickListener(this)
        ib_previous.setOnClickListener(this)
        ib_next.setOnClickListener(this)
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val binder = service as MediaPlayerService.MusicalBinder
        musicSrv = binder.service
        musicBound = true
    }

    override fun onServiceDisconnected(name: ComponentName) {
        musicBound = false
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MediaPlayerService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)

    }

    override fun onStop() {
        super.onStop()
        if (musicBound) {
            unbindService(this)
            musicBound = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.ib_pause -> {
                if (flag) {
                    musicSrv?.play()
                    ib_pause.setImageResource(R.drawable.ic_play)
                } else {
                    musicSrv?.play()
                    ib_pause.setImageResource(R.drawable.ic_pause)
                }
                flag = !flag
            }
            R.id.ib_next -> {
                musicSrv?.getNext()
                if (songId < 5)
                    songId++
                else
                    songId = 1
                tv_song_name.text = "song_$songId"
            }
            R.id.ib_previous -> {
                musicSrv?.getPrevious()
                if (songId > 1)
                    songId--
                else
                    songId = 5
                tv_song_name.text = "song_$songId"
            }
        }
    }
}
