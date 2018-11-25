package com.example.kolys.musicalplayer

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager

import java.util.ArrayList

class MediaPlayerService : Service(), MediaPlayer.OnPreparedListener {
    private lateinit var player : MediaPlayer
    private lateinit var songs: ArrayList<Song>
    private var songPosn: Int = 0
    private val musicBind = MusicalBinder()

    override fun onBind(intent: Intent): IBinder? {
        return musicBind
    }

    override fun onPrepared(mp: MediaPlayer) = mp.start()

    fun initMusicPlayer(songs: ArrayList<Song>) {
        songPosn = 0
        player = MediaPlayer()
        this.songs = songs
        player.setWakeMode(this,
                PowerManager.PARTIAL_WAKE_LOCK)
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnPreparedListener(this)
        playSong()
    }

    fun playSong() {
        val currSong = songs[songPosn].songId.toLong()
        val trackUri = Uri.parse("android.resource://com.example.kolys.musicalplayer/raw/song_$currSong")
        player = MediaPlayer.create(this, trackUri)
        player.start()
        player.setOnCompletionListener { getNext() }
    }

    fun setSong(songIndex: Int) {
        songPosn = songIndex
    }

    fun getNext() {
        if (songPosn < songs.size - 1)
            songPosn++
        else
            songPosn = 0
        stop()
        playSong()
    }

    fun getPrevious() {
        if (songPosn == 0)
            songPosn = songs.size - 1
        else
            songPosn--
        stop()
        playSong()
    }

    fun play() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.start()
        }
    }

    fun stop() = player.release()

    inner class MusicalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

}