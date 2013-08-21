package com.wuxi.app.fragment.homepage.mygoverinteractpeople;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.PopWindowManager;
import com.wuxi.app.R;
import com.wuxi.app.engine.ForumCommentService;
import com.wuxi.app.fragment.BaseItemContentFragment;
import com.wuxi.app.util.Constants;
import com.wuxi.app.util.GIPRadioButtonStyleChange;
import com.wuxi.domain.ForumWrapper.Forum;
import com.wuxi.domain.HotReviewWrapper.HotReview;
import com.wuxi.exception.NetException;

/**
 * 政民互动 热点话题 话题详情界面布局碎片
 * 
 * @author 智佳 罗森
 * 
 */
public class HotReviewContentFragment extends BaseItemContentFragment implements
		OnCheckedChangeListener {

	private RadioGroup radioGroup = null;
	private RadioButton content_info_radiobtn = null;
	private RadioButton content_comment_radiobtn = null;

	private View popview = null;

	// 声明弹出窗体变量
	private PopupWindow popWindow = null;

	private PopWindowManager popWindowManager = null;

	private Button comment_btn = null;

	private int[] radiobtnids = { R.id.forum_content_info_radiobtn,
			R.id.forum_content_comment_radiobtn };

	private HotReview hotReview;

	@Override
	protected int getContentLayoutId() {
		return R.layout.forum_content_layout;
	}

	@Override
	protected String getContentTitleText() {
		return "热点话题";
	}

	/**
	 * @param hotReview
	 *            the hotReview to set
	 */
	public void setHotReview(HotReview hotReview) {
		this.hotReview = hotReview;
	}

	@Override
	public void initUI() {
		super.initUI();

		initLayout();

		HotReviewInfoFragment hotReviewInfoFragment = new HotReviewInfoFragment();
		hotReviewInfoFragment.setHotReview(getHotReview());
		onTransaction(hotReviewInfoFragment);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		GIPRadioButtonStyleChange radioButtonStyleChange = new GIPRadioButtonStyleChange(
				R.drawable.gip_button_selected_bk, 0, Color.WHITE,
				R.color.gip_second_frame_button_brown);
		radioButtonStyleChange.refreshRadioButtonStyle(view, radiobtnids,
				checkedId);

		switch (checkedId) {
		case R.id.forum_content_info_radiobtn:
			initUI();
			break;

		case R.id.forum_content_comment_radiobtn:
			HotReviewReplyFragment hotReviewReplyFragment = new HotReviewReplyFragment();
			hotReviewReplyFragment.setHotReview(getHotReview());
			onTransaction(hotReviewReplyFragment);
			break;
		}
	}

	/**
	 * 初始化布局控件
	 */
	private void initLayout() {
		radioGroup = (RadioGroup) view
				.findViewById(R.id.forum_content_radiogroup);
		radioGroup.setOnCheckedChangeListener(this);

		content_info_radiobtn = (RadioButton) view
				.findViewById(R.id.forum_content_info_radiobtn);
		content_info_radiobtn.setText("话题内容");

		content_comment_radiobtn = (RadioButton) view
				.findViewById(R.id.forum_content_comment_radiobtn);
		content_comment_radiobtn.setText("网友留言与回复");

		comment_btn = (Button) view
				.findViewById(R.id.forum_content_comment_btn);
		comment_btn.setText("参与讨论");
		comment_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindow = makePopWindow(context);
				int[] xy = new int[2];
				comment_btn.getLocationOnScreen(xy);
				popWindow.showAtLocation(comment_btn, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0,
						comment_btn.getHeight() * 2 + 20);
			}
		});
	}

	/**
	 * 创建弹出窗体
	 * 
	 * @param con
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private PopupWindow makePopWindow(Context con) {
		PopupWindow popupWindow = null;

		popview = LayoutInflater.from(con).inflate(
				R.layout.forum_content_popwindow_layout, null);

		Button submitBtn = (Button) popview.findViewById(R.id.forum_submit_btn);
		submitBtn.setText("发送信息");

		final EditText submitContent = (EditText) popview
				.findViewById(R.id.forum_popwindow_content_edit);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(context, "暂未开通该功能...", Toast.LENGTH_SHORT)
						.show();
			}
		});

		popWindowManager = PopWindowManager.getInstance();

		popWindowManager.addPopWindow(popupWindow);

		popupWindow = new PopupWindow(con);

		popupWindow.setContentView(popview);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.naviga_leftitem_back));
		popupWindow.setWidth(LayoutParams.FILL_PARENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		popupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popupWindow.setTouchable(true); // 设置PopupWindow可触摸
		popupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸

		return popupWindow;
	}

	/**
	 * 跳转界面
	 * 
	 * @param fragment
	 */
	protected void onTransaction(BaseFragment fragment) {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.forum_content_fragment, fragment);
		ft.commit();
	}

	/**
	 * 获取话题数据
	 * 
	 * @return
	 */
	private HotReview getHotReview() {
		hotReview = (HotReview) getArguments().get("hotReview");
		return hotReview;
	}
}
