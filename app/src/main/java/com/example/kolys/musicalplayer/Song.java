package com.example.kolys.musicalplayer;

import android.os.Binder;

import java.net.URL;

public class Song {
    private int songId;

    public Song(int songId, String songName, String pathToSong) {
        this.songId = songId;
        this.songName = songName;
        this.pathToSong = pathToSong;
    }

    private String songName;
    private String pathToSong;

    public String getPathToSong() {
        return pathToSong;
    }

    public void setPathToSong(String pathToSong) {
        this.pathToSong = pathToSong;
    }


    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

}
