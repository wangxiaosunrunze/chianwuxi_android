package com.wuxi.app.fragment.homepage.goverpublicmsg;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wuxi.app.fragment.BaseSlideFragment;
import com.wuxi.app.fragment.commonfragment.FifterContentListFragment;
import com.wuxi.app.util.Constants.FragmentName;
import com.wuxi.domain.Content;

public class GoverMsgFifterContentListFragment extends FifterContentListFragment{

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Content content = (Content) adapterView.getItemAtPosition(position);
		BaseSlideFragment baseSlideFragment = this.baseSlideFragment;
		if (super.parentItem != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("url", content.getWapUrl());
			bundle.putSerializable("fragmentTitle", parentItem.getName());
			baseSlideFragment.slideLinstener.replaceFragment(null, -1, FragmentName.GOVERMSG_WEBCONTENT_FARGMENT, bundle);

		}else if(super.channel!=null){
			Bundle bundle = new Bundle();
			bundle.putSerializable("url", content.getWapUrl());
			bundle.putSerializable("fragmentTitle", channel.getChannelName());
			baseSlideFragment.slideLinstener.replaceFragment(null, -1, FragmentName.GOVERMSG_WEBCONTENT_FARGMENT, bundle);
		}
	}

}