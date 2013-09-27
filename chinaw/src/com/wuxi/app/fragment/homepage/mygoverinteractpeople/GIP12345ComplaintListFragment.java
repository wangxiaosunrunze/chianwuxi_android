/**
 * 
 */
package com.wuxi.app.fragment.homepage.mygoverinteractpeople;

import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.MainTabActivity;
import com.wuxi.app.R;
import com.wuxi.app.activity.homepage.mygoverinteractpeople.GIP12345AllMailContentActivity;
import com.wuxi.app.adapter.ComplaintLetterListAdapter;
import com.wuxi.app.engine.LetterService;
import com.wuxi.app.util.Constants;
import com.wuxi.app.util.LogUtil;
import com.wuxi.domain.LetterWrapper;
import com.wuxi.domain.LetterWrapper.Letter;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

/**
 * 政民互动 12345来信办理平台 建议咨询投诉列表 界面碎片
 * 
 * @author 智佳 罗森
 * 
 */
public class GIP12345ComplaintListFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener, OnScrollListener {

	protected static final String TAG = "GIP12345ComplaintListFragment";

	private View view = null;
	private Context context = null;

	private ListView mListView;
	private ProgressBar list_pb;
	private LetterWrapper letterWrapper;
	private List<Letter> letters;

	private ComplaintLetterListAdapter adapter;

	private static final int DATA__LOAD_SUCESS = 0;
	private static final int DATA_LOAD_ERROR = 1;

	private int visibleLastIndex;
	private int visibleItemCount;// 当前显示的总条数
	private final static int PAGE_NUM = 10;

	private boolean isFirstLoad = true;// 是不是首次加载数据
	private boolean isSwitch = false;// 切换
	private boolean isLoading = false;

	private View loadMoreView;// 加载更多视图
	private Button loadMoreButton;
	private ProgressBar pb_loadmoore;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String tip = "";

			if (msg.obj != null) {
				tip = msg.obj.toString();
			}
			switch (msg.what) {
			case DATA__LOAD_SUCESS:
				showLettersList();
				break;
			case DATA_LOAD_ERROR:
				list_pb.setVisibility(View.VISIBLE);
				Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.complaint_list_layout, null);
		context = getActivity();

		// 初始化布局
		initLayout();

		// 第一次加载数据
		loadFirstData(0, PAGE_NUM);

		return view;
	}

	/**
	 * 初始化布局控件
	 */
	private void initLayout() {
		mListView = (ListView) view
				.findViewById(R.id.gip_12345_complaint_listView);
		mListView.setOnItemClickListener(this);

		list_pb = (ProgressBar) view
				.findViewById(R.id.gip_12345_complaint_listView_pb);

		loadMoreView = View.inflate(context, R.layout.list_loadmore_layout,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);
		pb_loadmoore = (ProgressBar) loadMoreView
				.findViewById(R.id.pb_loadmoore);

		mListView.addFooterView(loadMoreView);// 为listView添加底部视图
		mListView.setOnScrollListener(this);// 增加滑动监听
		loadMoreButton.setOnClickListener(this);
	}

	/**
	 * @方法： loadFirstData
	 * @描述： 第一次加载数据
	 * @param start
	 * @param end
	 */
	private void loadFirstData(int start, int end) {
		loadData(start, end);
	}

	/**
	 * 加载数据
	 */
	private void loadData(final int startIndex, final int endIndex) {
		if (isFirstLoad || isSwitch) {
			list_pb.setVisibility(View.VISIBLE);
		} else {
			pb_loadmoore.setVisibility(ProgressBar.VISIBLE);
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				isLoading = true;// 正在加载数据
				Message message = handler.obtainMessage();
				LetterService letterService = new LetterService(context);
				try {
					String url = Constants.Urls.SUGGESTLETTER_URL+"?start="+startIndex+"&end="+endIndex;
					letterWrapper = letterService.getLetterLitstWrapper(url);
					if (null != letterWrapper) {
						handler.sendEmptyMessage(DATA__LOAD_SUCESS);

					} else {
						message.obj = "error";
						handler.sendEmptyMessage(DATA_LOAD_ERROR);
					}

				} catch (NetException e) {
					LogUtil.i(TAG, "出错");
					e.printStackTrace();
					message.obj = e.getMessage();
					handler.sendEmptyMessage(DATA_LOAD_ERROR);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NODataException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 显示列表
	 */
	public void showLettersList() {
		letters = letterWrapper.getData();
		if (letters == null || letters.size() == 0) {
			Toast.makeText(context, "对不起，暂无建议咨询投诉信息", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (isFirstLoad) {
				adapter = new ComplaintLetterListAdapter(letters, context);
				isFirstLoad = false;
				mListView.setAdapter(adapter);
				list_pb.setVisibility(View.GONE);
				isLoading = false;
			} else {
				if (isSwitch) {
					adapter.setLetters(letters);
					list_pb.setVisibility(View.GONE);
				} else {
					for (Letter letter : letters) {
						adapter.addItem(letter);
					}
				}

				adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				mListView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
				isLoading = false;
			}
		}

		if (letterWrapper.isNext()) {
			pb_loadmoore.setVisibility(ProgressBar.GONE);
			loadMoreButton.setText("点击加载更多");
		} else {
			mListView.removeFooterView(loadMoreView);
		}
	}
	
	/**
	 * @方法： loadMoreData
	 * @描述： 加载更多数据
	 * @param view
	 */
	private void loadMoreData(View view) {
		if (isLoading) {
			return;
		} else {
			loadData(visibleLastIndex + 1, visibleLastIndex + 1 + PAGE_NUM);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1,
			int position, long arg3) {
		Letter letter = (Letter) adapterView.getItemAtPosition(position);

		Intent intent = new Intent(getActivity(),
				GIP12345AllMailContentActivity.class);
		intent.putExtra("letter", letter);
		MainTabActivity.instance.addView(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loadMoreButton:
			if (letterWrapper != null && letterWrapper.isNext()) {// 还有下一条记录

				isSwitch = false;
				loadMoreButton.setText("loading.....");
				loadMoreData(v);
			}
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;// 最后一条索引号
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
	}

}
