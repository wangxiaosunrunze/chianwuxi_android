package com.wuxi.app.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchSpinnerAdapter extends ArrayAdapter{

	 Context context;  
	    String[] items = new String[] {};
	    
	public SearchSpinnerAdapter(Context context, int textViewResourceId,
			Object[] objects) {
		super(context, textViewResourceId, objects);
		this.items = (String[]) objects;  
        this.context = context;  
	}

    @Override  
    public View getDropDownView(int position, View convertView,  
            ViewGroup parent) {  
  
        if (convertView == null) {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(  
                    android.R.layout.simple_spinner_item, parent, false);  
        }  
  
        TextView tv = (TextView) convertView  
                .findViewById(android.R.id.text1);  
        tv.setGravity(Gravity.LEFT);
        tv.setText(items[position]);  
        tv.setTextSize(15);  
        return convertView;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        if (convertView == null) {  
            LayoutInflater inflater = LayoutInflater.from(context);  
            convertView = inflater.inflate(  
                    android.R.layout.simple_spinner_item, parent, false);  
        }  
  
        // android.R.id.text1 is default text view in resource of the android.  
        // android.R.layout.simple_spinner_item is default layout in resources of android.  
  
        TextView tv = (TextView) convertView  
                .findViewById(android.R.id.text1);  
        tv.setText(items[position]);
        tv.setGravity(Gravity.LEFT);
        tv.setTextSize(15);  
        return convertView;  
    }
}
