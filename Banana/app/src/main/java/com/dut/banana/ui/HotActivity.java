package com.dut.banana.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dut.banana.BaseLyric;
import com.dut.banana.R;
import com.dut.banana.net.ServerConnection;

import java.util.List;

public class HotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        final ListView listView = (ListView) findViewById(R.id.hot_lv_playlist);
        final TextView textView = (TextView) findViewById(R.id.hot_tv_note);
        Toast.makeText(HotActivity.this, "Updating new hot playlist...", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<BaseLyric> baseLyrics = ServerConnection.getInstance().downloadHotPlaylist();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (baseLyrics != null) {
                            HotAdapter adapter = new HotAdapter(HotActivity.this.getApplicationContext(), baseLyrics);
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            Toast.makeText(HotActivity.this, "Hot playlist is updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            listView.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            Toast.makeText(HotActivity.this, "Something wrong! maybe your network broken down or server has some fault", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }

    private class HotAdapter extends BaseAdapter {
        private List<BaseLyric> lyrics;
        private LayoutInflater inflater;

        public HotAdapter(Context context, List<BaseLyric> lyrics) {
            this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.lyrics = lyrics;
        }

        @Override
        public int getCount() {
            return lyrics.size();
        }

        @Override
        public Object getItem(int position) {
            return lyrics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_hot_item, null);
                holder = new ViewHolder();
                holder.tvDownloaded = (TextView) convertView.findViewById(R.id.hot_item_tv_downloaded);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.hot_item_tv_title);
                holder.tvNumber = (TextView) convertView.findViewById(R.id.hot_item_tv_number);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BaseLyric lyric = lyrics.get(position);
            holder.tvTitle.setText(String.format("%s - %s", lyric.getSongName(), lyric.getArtist()));
            holder.tvNumber.setText((position + 1) + "");
            holder.tvDownloaded.setText(lyric.getRate() + " rate");
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView tvNumber;
        TextView tvTitle;
        TextView tvDownloaded;
    }
}
