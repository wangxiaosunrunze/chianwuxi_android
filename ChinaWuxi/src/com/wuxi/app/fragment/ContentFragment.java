package com.wuxi.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.R;

public class ContentFragment extends BaseFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return  inflater.inflate(R.layout.activity_eoefragment,null);
	}

}