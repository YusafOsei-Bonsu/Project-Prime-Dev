package com.example.primefilesync;

import android.content.Intent;
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

                    System.out.println("attempting delete");
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL url = new URL("http://10.40.20.172:3003/files/" + fileName.getText());
                    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                   // httpCon.setDoInput(true);
                    httpCon.setRequestProperty(
                            "Content-Type", "application/x-www-form-urlencoded");
                    httpCon.setRequestMethod("DELETE");
                    httpCon.setConnectTimeout(1000);
                    httpCon.setReadTimeout(1000);
                    httpCon.getInputStream().close();

                }catch(Exception e){
                    e.printStackTrace();

                }finally {
                    Intent intent = new Intent (EditFragment.this.getActivity(), HomeFragment.class);
                    HomeFragment homeFragment = new HomeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

                }
            }
        });


        return view;

    }




}
