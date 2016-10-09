package com.dut.banana.net;

import android.util.Log;

import com.dut.banana.Account;
import com.dut.banana.BaseLyric;
import com.dut.banana.Lyric;
import com.dut.banana.Song;
import com.dut.banana.lib.Config;
import com.dut.banana.parameter.Parameter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 16/11/2015.
 */
public final class ServerConnection {
    private static ServerConnection ourInstance = new ServerConnection();
    private static final String LOG_TAG = "APP_DEBUG";
    public static ServerConnection getInstance() {
        return ourInstance;
    }

    private ServerConnection() {
    }

    private static final String PARAM_NAME = Parameter.REQUEST;
    private static final String ADDRESS_NAME = Config.SERVER_IP + ":" + Parameter.PORT + "/" + Parameter.SERVER_NAME;

    private String sendRequest(String request) {
        String httpUrl = ADDRESS_NAME + "/" + request;
        String content = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            content = reader.readLine();
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String sendRequest(String servletName, String data) {
        return sendRequest(servletName + "?" + PARAM_NAME + "=" + data);
    }

    private String sendRequest(String servletName, Package pk) {
        return sendRequest(servletName, pk.toString());
    }

    private synchronized Package request(Package requestPackage, String servletName, IWorker onResponse) {
        String response = sendRequest(servletName, requestPackage);

        Package responsePackage = null;
        if (response != null && response.length() > 0)
            responsePackage = Package.parse(response);
        if (onResponse != null) {
            if (responsePackage != null)
                return onResponse.onSuccess(responsePackage);
            else
                onResponse.onFail();
        }
        return null;
    }

    public Lyric downloadLyric(Song song) {
        // send request to server to search for lyric
        RequestSearchPackage requestPackage = new RequestSearchPackage();
        requestPackage.setSongName(song.getTitle());
        requestPackage.setArtist(song.getArtist());

        Package responsePackage = request(requestPackage, Parameter.SERVLET_SEARCH, new IWorker() {
            @Override
            public Package onSuccess(Package param) {
                ResponseSearchPackage responsePackage = (ResponseSearchPackage) param;
                List<BaseLyric> baseLyrics = responsePackage.getBaseLyrics();
                if (baseLyrics.size() == 0)
                    return null;
                BaseLyric defaultLyric = baseLyrics.get(0);

                // send request to server to download lyric
                RequestDownloadLyricPackage requestDownPackage = new RequestDownloadLyricPackage();
                requestDownPackage.setLyricId(defaultLyric.getId());
                return request(requestDownPackage, Parameter.SERVLET_DOWNLOAD_LYRIC, new IWorker() {
                    @Override
                    public Package onSuccess(Package param) {
                        return param;
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }

            @Override
            public void onFail() {
            }
        });

        if (responsePackage == null)
            return null;
        ResponseDownloadLyricPackage lyricPackage = (ResponseDownloadLyricPackage) responsePackage;
        return lyricPackage.getLyrics();
    }

    private boolean simpleRequest(Package requestPackage, String servletName) {
        // now! upload to server
        Log.d(LOG_TAG, "Begin send request to server...");
        ResponseResultPackage responsePackage = (ResponseResultPackage) request(requestPackage, servletName, new IWorker() {
            @Override
            public Package onSuccess(Package param) {
                return param;
            }

            @Override
            public void onFail() {

            }
        });

        boolean ok = responsePackage != null && responsePackage.isIsSuccess();
        String fail = "";
        if (responsePackage != null && responsePackage.getErrorMessage() != null) {
            fail = "because " + responsePackage.getErrorMessage();
        }
        Log.d(LOG_TAG, "Request session done! result is " + (ok ? "OK" : "FAIL " + fail));
        return ok;
    }

    public boolean uploadLyric(Lyric lyric, Account account) {
        // create package to request upload
        RequestUploadPackage requestPackage = new RequestUploadPackage();
        requestPackage.setUserName(account.getUserName());
        requestPackage.setPasswordHash(account.getPassword());// it is plaintext of password! we will hash it before
        requestPackage.setLyric(lyric);
        return simpleRequest(requestPackage, Parameter.SERVLET_UPLOAD);
    }

    public boolean signup(Account account) {
        // create package to request create new account
        RequestCreateAccountPackage requestPackage = new RequestCreateAccountPackage();
        requestPackage.setUserName(account.getUserName());
        requestPackage.setPasswordHash(account.getPassword());
        return simpleRequest(requestPackage, Parameter.SERVLET_CREATE_ACOUNT);
    }

    public boolean changeAccountPassword(Account account, String newPassword) {
        // create package to request create new account
        RequestChangePasswordPackage requestPackage = new RequestChangePasswordPackage();
        requestPackage.setUserName(account.getUserName());
        requestPackage.setPasswordHash(account.getPassword());
        requestPackage.setNewPasswordHash(newPassword);
        return simpleRequest(requestPackage, Parameter.SERVLET_CHANGE_PASSWORD);
    }

    public boolean login(Account account) {
        // create package to request login
        RequestLoginPackage requestPackage = new RequestLoginPackage();
        requestPackage.setUserName(account.getUserName());
        requestPackage.setPasswordHash(account.getPassword());
        return simpleRequest(requestPackage, Parameter.SERVLET_LOGIN);
    }

    public List<BaseLyric> downloadHotPlaylist() {
        // create package to request create new account
        RequestDownloadHotLyricPackage requestPackage = new RequestDownloadHotLyricPackage();
        // now! upload to server
        Log.d(LOG_TAG, "Begin send request to server...");
        ReponseDownloadHotLyricPackage responsePackage = (ReponseDownloadHotLyricPackage) request(requestPackage, Parameter.SERVLET_DOWNLOAD_HOTLYRIC, new IWorker() {
            @Override
            public Package onSuccess(Package param) {
                return param;
            }

            @Override
            public void onFail() {

            }
        });
        if (responsePackage == null)
            return null;
        List<BaseLyric> baseLyrics = responsePackage.getmHotPlaylistLyrics();
        if (baseLyrics == null || baseLyrics.size() == 0)
            return null;
        Log.d(LOG_TAG, "Received response from server");
        return baseLyrics;
    }

    private interface IWorker {
        Package onSuccess(Package param);

        void onFail();
    }
}
