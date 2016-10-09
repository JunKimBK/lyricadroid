package com.dut.banana.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dut.banana.R;
import com.dut.banana.bean.Playlist;

import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 14/11/2015.
 */
public class PlaylistAdapter extends SupportContextMenuAdapter {
    private List<Playlist> playlists;
    private int nowPlaylistId = 0;
    private LayoutInflater inflater;

    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.playlists = playlists;
    }

    public List<Playlist> getData() {
        return playlists;
    }

    public void setNowPlaylist(int id) {
        nowPlaylistId = id;
    }

    public void add(Playlist object) {
        playlists.add(0, object);
        nowPlaylistId++;
        super.notifyDataSetChanged();
    }

    public void remove(Playlist object) {
        int index = -1;
        for (Playlist playlist : playlists) {
            index++;
            if (playlist.equals(object))
                break;
        }
        if (index == nowPlaylistId)
            nowPlaylistId = -1;
        playlists.remove(object);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int position) {
        return playlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlaylistViewHolder holder = null;
        if (convertView == null) {
            holder = new PlaylistViewHolder();
            convertView = inflater.inflate(R.layout.layout_playlist_item, null);
            holder.vHeader = convertView.findViewById(R.id.playlist_item_ll_header);
            holder.tvName = (TextView) convertView.findViewById(R.id.playlist_item_tv_name);
            holder.tvCount = (TextView) convertView.findViewById(R.id.playlist_item_tv_count);
            convertView.setTag(holder);
        } else {
            holder = (PlaylistViewHolder) convertView.getTag();
        }

        Playlist playlist = playlists.get(position);
        int color = position == nowPlaylistId ? R.color.colorNowPlaylist : R.color.colorNormalPlaylist;
        holder.id = position + 1;
        holder.vHeader.setBackgroundResource(color);
        holder.tvName.setText(playlist.getName());
        holder.tvCount.setText(playlist.getSongIds().length + " songs");
        convertView.setOnClickListener(this);
        return convertView;
    }

    private static class PlaylistViewHolder extends ViewHolder {
        View vHeader;
        TextView tvName;
        TextView tvCount;
    }
}
