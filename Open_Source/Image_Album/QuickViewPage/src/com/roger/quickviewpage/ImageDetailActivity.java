/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.roger.quickviewpage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roger.quickviewpage.imagecache.ImageCache.ImageCacheParams;
import com.roger.quickviewpage.imagecache.ImageWorker;
import com.roger.quickviewpage.imagecache.Images;

public class ImageDetailActivity extends FragmentActivity {
	private static final String IMAGE_CACHE_DIR = "images";
	public static final String EXTRA_IMAGE = "extra_image";
	private final static String TAG = "ImageDetailActivity";

	private ImagePagerAdapter mAdapter;
	private ImageWorker mImageWorker;
	private ViewPager mPager;

	/** 轮播图点点 **/
	private ViewGroup dotGroup;
	/** 装点点的 **/
	private ImageView[] tips;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		// Fetch screen height and width, to use as our max size when loading
		// images as this
		// activity runs full screen
		final DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		final int height = displaymetrics.heightPixels;// 屏幕高度像素值
		final int width = displaymetrics.widthPixels;// 屏幕宽度像素值

		ImageCacheParams cacheParams = new ImageCacheParams();
		cacheParams.reqHeight = height;// 设置高
		cacheParams.reqWidth = width;// 设置宽
		cacheParams.memoryCacheEnabled = false;
//		cacheParams.loadingResId = R.drawable.ic_launcher;// 加载中默认图
		mImageWorker = ImageWorker.newInstance(this);
		mImageWorker.addParams(TAG, cacheParams);
		// mImageWorker.setLoadingImage(R.drawable.empty_photo);

		// Set up ViewPager and backing adapter
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), Images.imageUrls.length);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		dotGroup = (ViewGroup) findViewById(R.id.dot_group);
		setDot(Images.imageUrls.length);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageSelected(int position) {
				Log.i("Icache", "onPageSelected = "+position);
				for (int i = 0; i < tips.length; i++) {
					if (i == position % tips.length) {
						tips[i].setBackgroundResource(R.drawable.news_diang);
					} else {
						tips[i].setBackgroundResource(R.drawable.news_diang_hover);
					}
				}
			}
		});
		// Set the current item based on the extra passed in to this activity
		final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		mImageWorker.setOnScreen(TAG, true);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		mImageWorker.setOnScreen(TAG, false);
	}

	/**
	 * Called by the ViewPager child fragments to load images via the one
	 * ImageWorker
	 * 
	 * @return
	 */
	public ImageWorker getImageWorker() {
		return mImageWorker;
	}

	/**
	 * The main adapter that backs the ViewPager. A subclass of
	 * FragmentStatePagerAdapter as there could be a large number of items in
	 * the ViewPager and we don't want to retain them all in memory at once but
	 * create/destroy them on the fly.
	 */
	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		private final int mSize;

		public ImagePagerAdapter(FragmentManager fm, int size) {
			super(fm);
			mSize = size;
		}

		@Override
		public int getCount() {
			return mSize;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageDetailFragment.newInstance(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			final ImageDetailFragment fragment = (ImageDetailFragment) object;
			// As the item gets destroyed we try and cancel any existing work.
			fragment.cancelWork();
			super.destroyItem(container, position, object);
		}
	}

	// 设置点点的
	public void setDot(int index) {
		dotGroup.removeAllViews();
		tips = new ImageView[index];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(ImageDetailActivity.this);
			imageView.setLayoutParams(new LayoutParams(5, 5));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.news_diang);
			} else {
				tips[i].setBackgroundResource(R.drawable.news_diang_hover);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			dotGroup.addView(imageView, layoutParams);
		}
	}

}
