package com.wuxi.app.fragment.search;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wuxi.app.R;
import com.wuxi.app.fragment.BaseItemContentFragment;
import com.wuxi.domain.SearchResultWrapper.SearchResult;

/**
 * 搜索内容 fragment
 * 
 * @author 杨宸 智佳
 * */

public class SearchResultDetailFragment extends BaseItemContentFragment {
	private ProgressBar searchDetail_pb;// webView加载进度条
	private SearchResult resultContent;
	private WebView searchDetail_wb;// 加载数据的webView

	@Override
	public void initBtn() {

		super.initBtn();
		super.download_btn.setVisibility(ImageView.INVISIBLE);// 隐藏下载图标

		searchDetail_pb = (ProgressBar) view.findViewById(R.id.search_detail_pb);

		searchDetail_wb = (WebView) view.findViewById(R.id.search_detail_wb);// 加载数据的webView

		resultContent = (SearchResult) getArguments().get("result");

		if (resultContent != null) {
			showResultData();
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void showResultData() {
		String wapUrl = resultContent.getLink();
		searchDetail_wb.getSettings().setJavaScriptEnabled(true);
		searchDetail_wb.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {

				if (progress == 100) {
					searchDetail_pb.setVisibility(ProgressBar.GONE);// 移除进度条

				}

			}
		});
		searchDetail_wb.getSettings().setUseWideViewPort(true);
		searchDetail_wb.getSettings().setBuiltInZoomControls(true);
		searchDetail_wb.getSettings().setLoadWithOverviewMode(true);
		searchDetail_wb.loadUrl(wapUrl);
	}

	@Override
	protected int getContentLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.search_detail_layout;
	}

	@Override
	protected String getContentTitleText() {
		// TODO Auto-generated method stub
		return "全站搜索";
	}

}