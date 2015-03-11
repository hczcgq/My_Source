package com.cjj.viewpager_cycle_banner;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {

	/**
	 * 存放轮播图片的list
	 */
	private List<ImageView> listviews;  
	
	/**
	 * 存放图片的数组
	 */
	private int[] pics;  
	
	

	public ViewPagerAdapter(List<ImageView> listviews, int[] pics) {
		super();
		this.listviews = listviews;
		this.pics = pics;
	}

	public ViewPagerAdapter() {
		// TODO Auto-generated constructor stub
	}

	   @Override  
       public void destroyItem(ViewGroup container, int position, Object object) {  
           ImageView view = listviews.get(position);  
           container.removeView(view);  
           view.setImageBitmap(null);  
       }  
 
       @Override  
       public Object instantiateItem(ViewGroup container, int i) {  
           if (i == 0) {  
               listviews.get(i).setImageResource(pics[3]);  
           } else if (i == (listviews.size() - 1)) {  
               listviews.get(i).setImageResource(pics[0]);  
           } else {  
               listviews.get(i).setImageResource(pics[i - 1]);  
           }  
           container.addView(listviews.get(i));  
           return listviews.get(i);  
       }  
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listviews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
