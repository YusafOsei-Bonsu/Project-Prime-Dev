package com.example.primefilesync;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.net.HttpURLConnection;
import java.net.URL;

public class RemoveFileTask extends AsyncTask<URL, Integer, Long> {

    //asynchronous task to ensure the UI doesnt freeze up during connection

    @Override
    protected Long doInBackground(URL... urls) {


        try {
            //change threadpolicy to permit all to allow delete method
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //for all urls provided, perform the delete, using HttpUrlConnection
            for(int i=0; i<urls.length;i++) {
                //setting up the connection
                HttpURLConnection httpCon = (HttpURLConnection) urls[i].openConnection();
                httpCon.setRequestProperty(
                        "Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestMethod("DELETE");
                //timeouts incase the connection doesnt end
                httpCon.setConnectTimeout(1000);
                httpCon.setReadTimeout(1000);
                //perform connection
                httpCon.getInputStream().close();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;

    }

}
