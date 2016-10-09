package com.dut.banana.ui.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.dut.banana.R;
import com.dut.banana.bean.Playlist;
import com.dut.banana.model.IDataAccess;
import com.dut.banana.ui.PlaylistDialog;
import com.dut.banana.ui.adapter.PlaylistAdapter;

/**
 * Copyright by NE 2015.
 * Created by noem on 15/11/2015.
 */
public final class PlaylistHelper {
    private static PlaylistHelper currentInstance;
    private PlaylistAdapter playlistAdapter;
    private ListView presentView;
    private Activity activity;
    private IDataAccess database;

    public static PlaylistHelper getCurrentInstance() {
        return currentInstance;
    }

    public PlaylistHelper(Activity activity, IDataAccess database, PlaylistAdapter adapter, ListView presentView) {
        this.activity = activity;
        this.playlistAdapter = adapter;
        this.presentView = presentView;
        currentInstance = this;
        this.database = database;
    }

    public void addPlaylist() {
        final PlaylistDialog dialog = new PlaylistDialog(activity);
        dialog.setOnOKListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dialog.getPlaylistName();
                if (name == null || name.length() == 0) {
                    Toast.makeText(activity, "You must enter a name for new playlist fist!", Toast.LENGTH_SHORT).show();
                } else {
                    // create new playlist with name which was given to database and
                    // update to list view
                    Playlist playlist = new Playlist();
                    playlist.setName(name);
                    playlist.setSongIds(new long[0]);
                    playlistAdapter.add(playlist);
                    // now! add this playlist to database
                    database.addPlaylist(playlist);
                }
            }
        });
        dialog.show();
    }

    public void renamePlaylist(final int position) {
        final PlaylistDialog dialog = new PlaylistDialog(activity);
        final Playlist playlist = (Playlist) playlistAdapter.getItem(position - 1);
        dialog.setPlaylistName(playlist.getName());
        dialog.setMessage("");
        dialog.setOnOKListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dialog.getPlaylistName();
                if (name == null || name.length() == 0) {
                    Toast.makeText(activity, "What's playlist name?!", Toast.LENGTH_SHORT).show();
                } else {
                    playlist.setName(name);
                    presentView.invalidateViews();
                    // now! update this playlist to database
                    database.updatePlaylist(playlist);
                }
            }
        });
        dialog.show();
    }

    public void deletePlaylist(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final Playlist playlist = (Playlist) playlistAdapter.getItem(position - 1);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Delete " + playlist.getName() + " ?");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Do it now!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playlistAdapter.remove(playlist);
                // now! delete this playlist from database
                database.removePlaylist(playlist.getId());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
