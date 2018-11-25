package com.example.kolys.musicalplayer.RecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.kolys.musicalplayer.MyCallBack
import com.example.kolys.musicalplayer.Song
import com.example.kolys.musicalplayer.R

class SongsAdapter(private val songs: List<Song>, private val activity: MyCallBack) : RecyclerView.Adapter<SongsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTV: TextView = itemView.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_song_item, parent, false)

        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val song = songs[position]
        holder.nameTV.text = song.songName
        holder.itemView.setOnClickListener { activity.onItemClick(position) }
    }

    override fun getItemCount(): Int  = songs.size

}