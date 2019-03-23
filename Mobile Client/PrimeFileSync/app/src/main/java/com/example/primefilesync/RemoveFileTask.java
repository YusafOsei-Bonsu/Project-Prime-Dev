package com.example.primefilesync;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.net.HttpURLConnection;
import java.net.URL;

public class RemoveFileTask extends AsyncTask<URL, Integer, Long> {


    @Override
    protected Long doInBackground(URL... urls) {


        try {
            System.out.println("attempting delete");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            for(int i=0; i<urls.length;i++) {
                HttpURLConnection httpCon = (HttpURLConnection) urls[i].openConnection();
                httpCon.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestMethod("DELETE");
                httpCon.setConnectTimeout(1000);
                httpCon.setReadTimeout(1000);
                httpCon.getInputStream().close();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }

}
