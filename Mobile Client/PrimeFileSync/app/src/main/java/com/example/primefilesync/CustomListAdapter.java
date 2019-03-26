package com.example.primefilesync;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    private final Activity context;

    //list of images
    private final Integer[] imageIDarray;

    //list of file name
    private final String[] nameArray;

    //list of file types
    private final String[] infoArray;


    public CustomListAdapter(Activity context, String[] nameArrayParam, String[] infoArrayParam, Integer[] imageIDArrayParam){

        //pass the recieved  data into the arrays in the class
        super(context, R.layout.listview_row , nameArrayParam);
        this.context=context;
        this.imageIDarray = imageIDArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //gets the refrences to objects in the listview
        TextView nameTextField = (TextView) rowView.findViewById(R.id.FileNameID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.AuthorID);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView3);

        //sets value of objects to the value of the arrays
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };

}
