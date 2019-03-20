package com.example.primefilesync;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EditFragment extends Fragment {

    public EditFragment() {
        // Required empty public constructor
    }


    TextView typeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //test
        View view = inflater.inflate(R.layout.edit_page, container, false);
        TextView fileName = (TextView) view.findViewById(R.id.editFileView);
        String key = getArguments().getString("FileName");
        String key2 = getArguments().getString("contentType");
        System.out.println(key);
        fileName.setText(key);
        System.out.println("the file should be: "+fileName.getText());

        return view;



    }


}
