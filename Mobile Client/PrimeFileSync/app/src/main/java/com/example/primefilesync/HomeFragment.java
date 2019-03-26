package com.example.primefilesync;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    ListView listView;
    View rootview;
    RequestQueue queue;
    ProgressDialog progress;
    Button uploadB;
    String displayName;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview=inflater.inflate(R.layout.home_page,container, false);
        init();
        initupload();
        return rootview;


    }

    //initialise the upload file button and permissions
    public void initupload() {

        uploadB = (Button) rootview.findViewById(R.id.uploadButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }
        enable_button();
    }

    //set up the button to select file using android ACTION_OPEN_DOCUMENT
    public void enable_button (){

        uploadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set up intent to pass to the upload process
                Intent upIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                upIntent.setType("*/*");
                upIntent.addCategory(Intent.CATEGORY_OPENABLE);
                //gets the intent and sets the request code, will be used in onActivityResult
                startActivityForResult(upIntent, 10);

            }

            });
        }

    //on result from file selection
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // if request code is 100 set up the button
        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            //set read external storage permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);


        }

    }

    //on result from file selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

    //if request code is 10 start the process of the upload
        if (requestCode == 10 && resultCode == RESULT_OK){

            //set up progress dialog to show upload in progress
                progress = new ProgressDialog(getActivity());
                progress.setTitle("Uploading");
                progress.setMessage("Please Wait.....");
                progress.show();

    //start a new thread to start the file upload
            Thread t = new Thread(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {

                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i(TAG, "Uri: " + uri.toString());

                        //get info from the uri
                        Cursor cursor = getActivity().getContentResolver()
                                .query(uri, null, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(
                                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            Log.i(TAG, "Display Name: " + displayName);
                        }else{

                        }


                        //reference the selected
                        File f = new File( Environment.getExternalStorageDirectory() + "/download/"+ displayName);
                        String content_type = getMimeType(displayName);
                        String file_path = f.getAbsolutePath();

                        //start the okHttpClient
                        OkHttpClient client = new OkHttpClient();
                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);

                        //fill requesty body with file contents
                        RequestBody request_body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("file", file_path.substring(file_path.lastIndexOf
                                        ("/") + 1), file_body)
                                .build();

                        //build the request
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("https://rocky-plateau-19773.herokuapp.com/upload/")
                                .post(request_body)
                                .build();

                        try {
                            //attempt request
                            okhttp3.Response response = client.newCall(request).execute();

                            if (!response.isSuccessful()) {
                                throw new IOException("Error  : " + response);

                            }
                            //dissmiss the progress dialog
                            progress.dismiss();


                        } catch (IOException e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }finally {
                            //reopen homefragment to see the new uploaded file in the list
                            Intent intent = new Intent (    HomeFragment.this.getActivity(), HomeFragment.class);
                            HomeFragment homeFragment = new HomeFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();
                        }


                    }
                }
            });
            //start the thread
            t.start();
            }
    }

    //get file type
    public String getMimeType (String path){
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

    }



    //initialise the List View
    public void init(){

        //new requestqueue queue
        queue = Volley.newRequestQueue(this.getContext());
        String url = "https://rocky-plateau-19773.herokuapp.com/files";

        //create arraylists to fill from the json file
        final ArrayList<String> nameAL = new ArrayList<String>();
        final ArrayList<String> typeAL = new ArrayList<String>();
        final ArrayList<Integer> picAL = new ArrayList<Integer>();

            //jsonarrayrequest to retrieve the json for the file list
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    try {
                        //for each file, add filename and filetype and pic to the corresponding arraylists
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
                        //add the files in the arraylists into seperate arrays
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
                        //use the customlistadapter to take the array from above and fill the listview
                        CustomListAdapter listvieweg = new CustomListAdapter(getActivity(), nameArray, typeArray, imageArray);
                        listView = (ListView) rootview.findViewById(R.id.simpleListView);
                        //set up what happens when a file is clicked in listview (in this cause open the edit fragment to get the file options
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent (HomeFragment.this.getActivity(), EditFragment.class);
                                EditFragment editFragment = new EditFragment();
                                Bundle args =new Bundle();
                                args.putString("FileName",listView.getItemAtPosition(position).toString());
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
        //add it to request queue
       queue.add(jsonArrayRequest);

    }


}
