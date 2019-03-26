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

    RequestQueue queue;
    DownloadManager downloadManager;
    String displayName;

    public EditFragment() {
        // Required empty public constructor
    }


    TextView typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set up the file options page
        final View view = inflater.inflate(R.layout.edit_page, container, false);
        final TextView fileName = (TextView) view.findViewById(R.id.editFileView);
        String key = getArguments().getString("FileName");
        String key2 = getArguments().getString("contentType");
        fileName.setText(key);


        Button button = (Button) view.findViewById(R.id.RemoveButton);
        //set on click listener for remove button
        button.setOnClickListener(new View.OnClickListener (){
            public void onClick(View v) {
                try {

                    URL url = new URL("http://10.40.6.85:3003/files/" + fileName.getText());
                    new RemoveFileTask().execute(url);

                }catch(Exception e){
                    e.printStackTrace();

                }finally {
                    //test
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //
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
                    downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("http://10.40.6.85:3003/files/" + fileName.getText());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setMimeType(getMimeType(String.valueOf(fileName.getText())));
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });



        return view;

    }

    public String getMimeType (String path){
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

    }




}
