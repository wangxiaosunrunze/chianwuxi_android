package com.wuxi.app.fragment.homepage.informationcenter;

import com.wuxi.app.fragment.commonfragment.ContentDetailFragment;

/**
 * 
 * @author wanglu 泰得利通
 * 魅力锡城内容详细页
 *
 */
public class InfoCenterContentDetailFragment extends ContentDetailFragment {

	@Override
	protected String getContentTitleText() {
		
		if(channel!=null){
			return channel.getChannelName();
		}else if(menuItem!=null){
			return menuItem.getName();
		}else{
			return "资讯中心";
		}
		
	}

	
	

}