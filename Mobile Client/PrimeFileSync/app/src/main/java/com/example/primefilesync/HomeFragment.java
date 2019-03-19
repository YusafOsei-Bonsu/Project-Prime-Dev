package com.example.primefilesync;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import java.net.*;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeFragment extends Fragment {

    ListView listView;
    View rootview;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*for list view (url = server url) - starts by connecting to server and ends by disconnecting
        try {
            URL url = new URL("mongodb://projectPrime:projectPrime@projectprime-shard-00-00-wreg9.mongodb.net:27017,projectprime-shard-00-01-wreg9.mongodb.net:27017,projectprime-shard-00-02-wreg9.mongodb.net:27017/test?ssl=true&replicaSet=ProjectPrime-shard-0&authSource=admin&retryWrites=true");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {

                //what to do from the server URL (here fill the list maybe)



                //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                //readStream(in);
            //} catch (IOException ex) {
             //   urlConnection.disconnect();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException ex2) {
            System.out.print("Cant connect to server");
        }

           */

        rootview=inflater.inflate(R.layout.home_page,container, false);
        init();
        return rootview;





    }



    public void init(){
        System.out.println("test1");
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = "http:/localhost/files";

            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //System.out.println("response: "+response.toString());
                    System.out.println("test2");
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("error 1");
                }
            });

       queue.add(jsonArrayRequest);
        //make sure can connect to server,

        System.out.println("test3");
        String[] nameArray = {"test1","test2","test3","test4","test5","test6","test7","test8","test9","test10"};
        String[] authorArray = {"test11","test22","test33","test44","test55","test66","test77","test88","test99","test1010"};
        Integer[] imageArray = {R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery};


        CustomListAdapter listvieweg = new CustomListAdapter(this.getActivity() , nameArray, authorArray, imageArray);
        listView = (ListView) rootview.findViewById(R.id.simpleListView);
        listView.setAdapter(listvieweg);

    }


}
