package com.example.primefilesync;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView listView;
    View rootview;
    RequestQueue queue;
    private static final int READ_REQUEST_CODE = 42;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview=inflater.inflate(R.layout.home_page,container, false);
        initupload();
        init();
        return rootview;


    }

    private void initupload() {


        Button uploadB = (Button) rootview.findViewById(R.id.uploadButton);
        uploadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                upIntent.setType("*/*");
                upIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(upIntent, READ_REQUEST_CODE);
            }
        });




    }


    public void init(){

        queue = Volley.newRequestQueue(this.getContext());
        String url = "http://10.40.10.83:3003/files";

        final ArrayList<String> nameAL = new ArrayList<String>();
        final ArrayList<String> typeAL = new ArrayList<String>();
        final ArrayList<Integer> picAL = new ArrayList<Integer>();
        String[] nameArray = new String[0];
        String[] typeArray = new String[0];
        Integer[] imageArray = new Integer[0];

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject file = response.getJSONObject(i);
                            String fileName = file.getString("filename");
                            nameAL.add(fileName);
                            String fileType = file.getString("contentType");
                            typeAL.add(fileType);
                            picAL.add(R.drawable.ic_menu_gallery);
                        }
                    }catch(JSONException e){

                    }finally{
                        String[] nameArray = new String[nameAL.size()];
                        for(int i=0; i<nameAL.size();i++){
                            nameArray[i]=nameAL.get(i);

                        }

                        String[] typeArray = new String[typeAL.size()];
                        for(int j=0; j<nameAL.size();j++){
                            typeArray[j]=typeAL.get(j);

                        }

                        Integer[] imageArray = new Integer[picAL.size()];
                        for(int k=0; k<picAL.size();k++){
                            imageArray[k]=picAL.get(k);
                        }
                        CustomListAdapter listvieweg = new CustomListAdapter(getActivity(), nameArray, typeArray, imageArray);
                        listView = (ListView) rootview.findViewById(R.id.simpleListView);
                        //test
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent (HomeFragment.this.getActivity(), EditFragment.class);
                                EditFragment editFragment = new EditFragment();
                                Bundle args =new Bundle();
                                args.putString("FileName",listView.getItemAtPosition(position).toString());
                                //test
                                //args.putString("contentType",listView.getItemAtPosition(position+1).toString());
                                editFragment.setArguments(args);
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, editFragment).addToBackStack(null).commit();
                            }
                        });
                        listView.setAdapter(listvieweg);

                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

       queue.add(jsonArrayRequest);
        //make sure can connect to server,

    }


}
