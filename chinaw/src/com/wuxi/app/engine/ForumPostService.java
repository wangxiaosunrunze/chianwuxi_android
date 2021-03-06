/**
 * 
 */
package com.wuxi.app.engine;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wuxi.app.util.Constants;
import com.wuxi.exception.NetException;

/**
 * 提交帖子业务类
 * 
 * @author 智佳 罗森
 * 
 */
public class ForumPostService extends Service {

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public ForumPostService(Context context) {
		super(context);
	}

	/**
	 * 提交帖子
	 * 
	 * @param access_token
	 * @param theme
	 * @param content
	 * @return
	 * @throws NetException
	 * @throws JSONException
	 */
	public boolean submitPosts(String access_token, String theme, String content)
			throws NetException, JSONException {
		// 检查网络连接
		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.FOORUM_POST_URL + "?title=" + theme
				+ "&content=" + content + "&access_token=" + access_token;

		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);

		if (resultStr != null) {
			JSONObject jsonObject = new JSONObject(resultStr);
			return jsonObject.getBoolean("success");
		} else {
			return false;
		}

	}

}
