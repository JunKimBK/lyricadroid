package com.dut.banana.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.banana.R;
import com.dut.banana.Song;
import com.dut.banana.lib.SongUtils;

import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 14/11/2015.
 */
public class SongAdapter extends SupportContextMenuAdapter {
    private List<Song> songs;
    private Context context;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongViewHolder holder = null;

        if(convertView == null) {
            holder = new SongViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_song_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.song_item_tv_title);
            holder.tvArtist = (TextView) convertView.findViewById(R.id.song_item_tv_artist);
            holder.tvDuration = (TextView) convertView.findViewById(R.id.song_item_tv_duration);
            convertView.setTag(holder);
        } else {
            holder = (SongViewHolder) convertView.getTag();
        }

        Song song = songs.get(position);
        holder.id = position;
        holder.tvTitle.setText(song.getTitle());
        holder.tvArtist.setText(song.getArtist());
        holder.tvDuration.setText(SongUtils.longToDuration(song.getDuration()));
        convertView.setOnClickListener(this);
        return convertView;
    }

    private static class SongViewHolder extends ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
        TextView tvDuration;
    }
}
