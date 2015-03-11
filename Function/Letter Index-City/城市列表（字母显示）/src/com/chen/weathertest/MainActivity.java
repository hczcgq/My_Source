package com.chen.weathertest;

import java.util.List;
import java.util.Map;

import com.chen.weathertest.BladeView.OnItemClickListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity implements Application.EventHandler {

	private Application mApplication;
	private CityDB mCityDB;
	private PinnedHeaderListView mCityListView;
	private BladeView mLetter;
	private CityAdapter mCityAdapter;
	private List<City> mCities;
	// 首字母集
	private List<String> mSections;
	// 根据首字母存放数据
	private Map<String, List<City>> mMap;
	// 首字母位置集
	private List<Integer> mPositions;
	// 首字母对应的位置
	private Map<String, Integer> mIndexer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Application.mListeners.add(this);
		mApplication = Application.getInstance();
		mCityDB = mApplication.getCityDB();

		mCityListView = (PinnedHeaderListView) findViewById(R.id.citys_list);
		mLetter = (BladeView) findViewById(R.id.citys_bladeview);
		
		mLetter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(String s) {
				if (mIndexer.get(s) != null) {
					mCityListView.setSelection(mIndexer.get(s));
				}
			}
		});
		
		mLetter.setVisibility(View.GONE);
		if (mApplication.isCityListComplite()) {

			mCities = mApplication.getCityList();
			mSections = mApplication.getSections();
			mMap = mApplication.getMap();
			mPositions = mApplication.getPositions();
			mIndexer = mApplication.getIndexer();

			mCityAdapter = new CityAdapter(MainActivity.this, mCities, mMap,
					mSections, mPositions);

			mCityListView.setAdapter(mCityAdapter);
			mCityListView.setOnScrollListener(mCityAdapter);
			mCityListView.setPinnedHeaderView(LayoutInflater.from(
					MainActivity.this).inflate(
					R.layout.biz_plugin_weather_list_group_item, mCityListView,
					false));
			mLetter.setVisibility(View.VISIBLE);
		}

		mCityListView
				.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(MainActivity.this,
								mCityAdapter.getItem(position).getCity(), 0)
								.show();
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Application.mListeners.remove(this);
	}

	@Override
	public void onCityComplite() {
		// 城市列表加载完的回调函数
		mCities = mApplication.getCityList();
		mSections = mApplication.getSections();
		mMap = mApplication.getMap();
		mPositions = mApplication.getPositions();
		mIndexer = mApplication.getIndexer();

		mCityAdapter = new CityAdapter(MainActivity.this, mCities, mMap,
				mSections, mPositions);
		mLetter.setVisibility(View.VISIBLE);
		mCityListView.setAdapter(mCityAdapter);
		mCityListView.setOnScrollListener(mCityAdapter);
		mCityListView.setPinnedHeaderView(LayoutInflater
				.from(MainActivity.this).inflate(
						R.layout.biz_plugin_weather_list_group_item,
						mCityListView, false));
	}

	@Override
	public void onNetChange() {
	}

}
