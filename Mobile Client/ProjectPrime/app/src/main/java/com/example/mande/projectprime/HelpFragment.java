package com.example.mande.projectprime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    View rootView;
    EditText ET_NAME, ET_USER_NAME, ET_USER_PASS;
    String name,user_name,user_pass ;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_help, container, false);

        return rootView;
    }




}
