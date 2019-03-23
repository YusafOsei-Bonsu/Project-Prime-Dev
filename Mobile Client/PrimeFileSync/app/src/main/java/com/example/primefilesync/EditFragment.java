package com.example.primefilesync;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EditFragment extends Fragment {

    RequestQueue queue;
    DownloadManager downloadManager;

    public EditFragment() {
        // Required empty public constructor
    }


    TextView typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //test
        final View view = inflater.inflate(R.layout.edit_page, container, false);
        final TextView fileName = (TextView) view.findViewById(R.id.editFileView);
        String key = getArguments().getString("FileName");
        String key2 = getArguments().getString("contentType");
        System.out.println(key);
        fileName.setText(key);
        System.out.println("the file should be: "+fileName.getText());


        Button button = (Button) view.findViewById(R.id.RemoveButton);
        button.setOnClickListener(new View.OnClickListener (){
            public void onClick(View v) {
                try {

                    URL url = new URL("http://10.40.10.83:3003/files/" + fileName.getText());
                    new RemoveFileTask().execute(url);

                }catch(Exception e){
                    e.printStackTrace();

                }finally {
                    Intent intent = new Intent (EditFragment.this.getActivity(), HomeFragment.class);
                    HomeFragment homeFragment = new HomeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

                }
            }
        });

        //Download button

        Button downloadbtn = (Button) view.findViewById(R.id.Downloadbutton);
            downloadbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadManager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("http://10.40.10.83:3003/files/" + fileName.getText());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });



        return view;

    }




}
