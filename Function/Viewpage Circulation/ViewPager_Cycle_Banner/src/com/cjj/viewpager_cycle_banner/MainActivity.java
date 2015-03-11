package com.cjj.viewpager_cycle_banner;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Build;

/** 
* @ClassName: MainActivity 
* @author cjj 
* @date 2014年11月20日 下午1:32:15 
* @Description:ViewPager实现轮播图片的循环播放  
*/
public class MainActivity extends Activity implements OnPageChangeListener {

	/**
	 * 定义ViewPager对象
	 */
	private ViewPager viewPager;
	
	/**
	 * 定义ViewPager的适配器
	 */
	private ViewPagerAdapter adapter;
	
	/**
	 * 存放轮播图片的list
	 */
	private List<ImageView> listviews;  
	
	/**
	 * 存放图片的数组
	 */
	private int[] pics = { R.drawable.new_img1, R.drawable.new_img2, R.drawable.new_img3,R.drawable.new_img4 };
//	private int[] pics = { R.drawable.new_img1, R.drawable.new_img2};  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewpager);
		listviews = new ArrayList<ImageView>();
		 // 添加viewpager多出的两个view  
		 int length = pics.length + 2;  
		 for (int i = 0; i < length; i++) {
			ImageView imageView = new ImageView(this);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  
                    ViewGroup.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			listviews.add(imageView);  
		}
		 
		adapter = new ViewPagerAdapter(listviews,pics);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		 // 设置viewpager在第二个视图显示  
        viewPager.setCurrentItem(1);  
		
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
		 int pageIndex = arg0;  
		 
		 if (arg0 == 0) {
			// 当视图在第一个时，将页面号设置为图片的最后一张。  
            pageIndex = pics.length;  
		}else if (arg0 == pics.length + 1) {
			// 当视图在最后一个是,将页面号设置为图片的第一张。  
            pageIndex = 1;
		}
		 
		 if (arg0 != pageIndex) {
			viewPager.setCurrentItem(pageIndex, false);  
	           return;  
		}
	}


}
