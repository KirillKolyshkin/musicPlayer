package com.example.kolys.musicalplayer.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kolys.musicalplayer.MyCallBack;
import com.example.kolys.musicalplayer.Song;
import com.example.kolys.musicalplayer.R;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    private List<Song> songs;
    private MyCallBack activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tv_name);
        }
    }

    public SongsAdapter(List<Song> songs, MyCallBack activity) {
        this.songs = songs;
        this.activity = activity;
    }

    @Override
    public SongsAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_song_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Song song = songs.get(position);
        holder.nameTV.setText(song.getSongName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

}