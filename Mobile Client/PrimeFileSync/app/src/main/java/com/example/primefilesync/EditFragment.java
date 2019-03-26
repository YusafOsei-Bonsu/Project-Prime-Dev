package com.example.primefilesync;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class EditFragment extends Fragment {

    DownloadManager downloadManager;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set up the file options page
        final View view = inflater.inflate(R.layout.edit_page, container, false);
        final TextView fileName = (TextView) view.findViewById(R.id.editFileView);
        String key = getArguments().getString("FileName");
        fileName.setText(key);


        Button button = (Button) view.findViewById(R.id.RemoveButton);
        //set on click listener for remove button
        button.setOnClickListener(new View.OnClickListener (){
            public void onClick(View v) {
                try {
                    //execute the removefile asynchronous task, this is to avoid UI freezing when executed on main thread
                    URL url = new URL("https://rocky-plateau-19773.herokuapp.com/files/" + fileName.getText() + "?_method=DELETE");
                    new RemoveFileTask().execute(url);

                }catch(Exception e){
                    e.printStackTrace();

                }finally {

                    try {
                        //give the page one second before reopening the Homefragment, to give server time to process request
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //open home fragment after remove is done
                    Intent intent = new Intent (EditFragment.this.getActivity(), HomeFragment.class);
                    HomeFragment homeFragment = new HomeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

                }
            }
        });

        // set up Download button

        Button downloadbtn = (Button) view.findViewById(R.id.Downloadbutton);
            downloadbtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    //set up download manager and pass uri of the file intended to download
                    downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("https://rocky-plateau-19773.herokuapp.com/download/" + fileName.getText());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    //set mime download type as the mime type of the file requested
                    request.setMimeType(getMimeType(String.valueOf(fileName.getText())));
                    //set visibility of the download notification
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });



        return view;

    }

    //gets the mime from a url
    public String getMimeType (String path){
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

    }


}
