package com.dut.banana.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dut.banana.R;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TeamAdapter adapter;

    private List<Member> getMembers() {
        List<Member> members = new ArrayList<>();
        Member noem = new Member("Nô Em", "https://sontx.blogspot.com/", R.drawable.avatar_noem);
        Member trong = new Member("Như Trọng", "https://www.facebook.com/giao.su.7", R.drawable.avatar_trong);
        Member ly = new Member("Văn Lý", "https://www.facebook.com/hapvanco93", R.drawable.avatar_ly);
        Member son = new Member("Đại Sơn", "https://www.facebook.com/sontran.ds", R.drawable.avatar_son);
        Member tung = new Member("Võ Tùng", "https://www.facebook.com/jun.ki.501", R.drawable.avatar_tung);
        members.add(noem);
        members.add(trong);
        members.add(ly);
        members.add(son);
        members.add(tung);
        return members;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ListView lvTeam = (ListView) findViewById(R.id.about_lv_team);
        adapter = new TeamAdapter(this.getApplicationContext(), getMembers());
        lvTeam.setAdapter(adapter);
        lvTeam.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Member member = (Member) adapter.getItem(position);
        String web = member.getWeb();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
        startActivity(browserIntent);
    }

    private class Member {
        private String name;
        private String web;
        private int avatar;

        public Member(String name, String web, int avatar) {
            this.name = name;
            this.web = web;
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public String getWeb() {
            return web;
        }

        public int getAvatar() {
            return avatar;
        }
    }

    private class TeamAdapter extends BaseAdapter {
        private List<Member> members;
        private LayoutInflater inflater;

        public TeamAdapter(Context context, List<Member> members) {
            this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.members = members;
        }

        @Override
        public int getCount() {
            return members.size();
        }

        @Override
        public Object getItem(int position) {
            return members.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_about_team_item, null);
                holder = new ViewHolder();
                holder.ivAvatar = (ImageView) convertView.findViewById(R.id.about_team_item_avatar);
                holder.tvName = (TextView) convertView.findViewById(R.id.about_team_item_name);
                holder.tvWeb = (TextView) convertView.findViewById(R.id.about_team_item_web);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Member member = members.get(position);
            holder.ivAvatar.setImageResource(member.getAvatar());
            holder.tvName.setText(member.getName());
            holder.tvWeb.setText(member.getWeb());
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvWeb;
    }
}
