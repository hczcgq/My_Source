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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.roger.quickviewpage.imagecache.Images;
import com.roger.quickviewpage.imagecache.ImageCache.ImageCacheParams;
import com.roger.quickviewpage.imagecache.ImageWorker;
import com.roger.quickviewpage.imagecache.ImageWorker.OnImageLoadedListener;
import com.roger.quickviewpage.imagecache.Utils;

/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight
 * forward GridView implementation with the key addition being the ImageWorker
 * class w/ImageCache to load children asynchronously, keeping the UI nice and
 * smooth and caching thumbnails for quick retrieval. The cache is retained over
 * configuration changes like orientation change so the images are populated
 * quickly as the user rotates the device.
 */
public class ImageGridFragment extends Fragment implements
		AdapterView.OnItemClickListener {
	private static final String TAG = "ImageGridFragment";

	private ImageAdapter mAdapter;
	private ImageWorker mImageWorker;

	/**
	 * Empty constructor as per the Fragment documentation
	 */
	public ImageGridFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ImageAdapter(getActivity(), Images.imageUrls);

		ImageCacheParams cacheParams = new ImageCacheParams();
		cacheParams.reqWidth = getResources().getDisplayMetrics().widthPixels / 5;
		cacheParams.reqHeight = (int) (cacheParams.reqWidth * 1.5);
		cacheParams.loadingResId = R.drawable.empty_photo;
		// cacheParams.clearDiskCacheOnStart = true;
		cacheParams.memCacheSize = (1024 * 1024 * Utils
				.getMemoryClass(getActivity())) / 2;
		// cacheParams.clearDiskCacheOnStart = true;
		mImageWorker = ImageWorker.newInstance(getActivity());
		mImageWorker.addParams(TAG, cacheParams);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_grid_fragment,
				container, false);
		final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);

		return v;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
		i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}

	/**
	 * The main adapter that backs the GridView. This is fairly standard except
	 * the number of columns in the GridView is used to create a fake top row of
	 * empty views as we use a transparent ActionBar and don't want the real top
	 * row of images to start off covered by it.
	 */
	private class ImageAdapter extends BaseAdapter {

		private final Activity mContext;
		private GridView.LayoutParams mImageViewLayoutParams;
		String[] imageUrls;

		public ImageAdapter(Activity context, String[] imageUrls) {
			super();
			mContext = context;
			this.imageUrls = imageUrls;
			int width = (int) ((context.getResources().getDisplayMetrics().widthPixels / 3) * 1.5);
			mImageViewLayoutParams = new GridView.LayoutParams(
					LayoutParams.FILL_PARENT, width);
		}

		@Override
		public int getCount() {
			// Size of adapter + number of columns for top empty row
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return imageUrls[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup container) {

			// Now handle the main ImageView thumbnails

			ImageView imageView;
			if (convertView == null) { // if it's not recycled, instantiate and
										// initialize
				imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(mImageViewLayoutParams);
			} else { // Otherwise re-use the converted view
				imageView = (ImageView) convertView;
			}

			Log.e("ad", "position ==  " + position);
			mImageWorker.loadBitmap(imageUrls[position], imageView,null);

			// Finally load the image asynchronously into the ImageView, this
			// also takes care of
			// setting a placeholder image while the background thread runs

			return imageView;
		}

	}
}
