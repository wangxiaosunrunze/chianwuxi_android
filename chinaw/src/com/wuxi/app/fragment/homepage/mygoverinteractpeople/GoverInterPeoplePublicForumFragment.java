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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wuxi.app.BaseFragment;
import com.wuxi.app.MainTabActivity;
import com.wuxi.app.R;
import com.wuxi.app.activity.homepage.mygoverinteractpeople.ForumContentActivity;
import com.wuxi.app.activity.homepage.mygoverinteractpeople.ForumPostActivity;
import com.wuxi.app.adapter.PublicForumListAdapter;
import com.wuxi.app.engine.ForumService;
import com.wuxi.app.util.Constants;
import com.wuxi.app.util.LogUtil;
import com.wuxi.domain.ForumWrapper;
import com.wuxi.domain.ForumWrapper.Forum;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

/**
 * 政民互动 之 公众论坛模块
 * 
 * @author 杨宸 智佳
 * */
public class GoverInterPeoplePublicForumFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener, OnScrollListener {

	private static final String TAG = "GoverInterPeoplePublicForumFragment";

	private Context context;
	private View view;

	// 贴子列表
	private ListView mListView;
	// 数据刷新进度条
	private ProgressBar list_pb;
	// 我要发帖按钮
	private ImageButton postButton;

	// 论坛包装类对象
	private ForumWrapper forumWrapper;

	// 论坛列表
	private List<Forum> forums;

	private PublicForumListAdapter adapter;

	// 数据加载成功标志
	private static final int DATA__LOAD_SUCESS = 0;
	// 数据加载失败标志
	private static final int DATA_LOAD_ERROR = 1;

	// 获取帖子的起始坐标
	private int startIndex = 0;
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
				showForums();
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
		view = inflater.inflate(R.layout.goverinterpeople_publicforum_layout,
				null);
		context = getActivity();

		initLayout();

		// 第一次加载数据
		loadFirstData(startIndex, PAGE_NUM);

		return view;

	};

	/**
	 * @方法： initLayout
	 * @描述： 初始化布局控件
	 */
	private void initLayout() {
		mListView = (ListView) view.findViewById(R.id.gip_forum_listview);
		mListView.setOnItemClickListener(this);

		list_pb = (ProgressBar) view.findViewById(R.id.gip_forum_listview_pb);

		postButton = (ImageButton) view.findViewById(R.id.gip_forum_imagebtn);
		postButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(),
						ForumPostActivity.class);

				MainTabActivity.instance.addView(intent);
			}
		});

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
				ForumService forumService = new ForumService(context);
				try {
					forumWrapper = forumService
							.getForumWrapper(Constants.Urls.FORUM_LIST_URL,
									startIndex, endIndex);
					if (forumWrapper != null) {
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
	 * 显示论坛列表
	 */
	private void showForums() {
		forums = forumWrapper.getForums();

		if (forums == null || forums.size() == 0) {
			Toast.makeText(context, "对不起，暂无论坛信息", Toast.LENGTH_SHORT).show();
		} else {
			if (isFirstLoad) {
				adapter = new PublicForumListAdapter(context, forums);
				isFirstLoad = false;
				mListView.setAdapter(adapter);
				list_pb.setVisibility(View.GONE);
				isLoading = false;
			} else {
				if (isSwitch) {
					adapter.setForums(forums);
					list_pb.setVisibility(View.GONE);
				} else {
					for (Forum forum : forums) {
						adapter.addItem(forum);
					}
				}

				adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
				mListView.setSelection(visibleLastIndex - visibleItemCount + 1); // 设置选中项
				isLoading = false;
			}
		}

		if (forumWrapper.isNext()) {
			pb_loadmoore.setVisibility(ProgressBar.GONE);
			loadMoreButton.setText("点击加载更多");
		} else {
			if (adapter != null) {
				mListView.removeFooterView(loadMoreView);
			}
			
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
		Forum forum = (Forum) adapterView.getItemAtPosition(position);

		Intent intent = new Intent(getActivity(), ForumContentActivity.class);
		intent.putExtra("forum", forum);

		MainTabActivity.instance.addView(intent);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loadMoreButton:
			if (forumWrapper != null && forumWrapper.isNext()) {// 还有下一条记录
				isSwitch = false;
				loadMoreButton.setText("loading.....");
				loadMoreData(v);
			}
			break;
		}
	}
	
	

}
