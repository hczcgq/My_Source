package cn.w.song.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.w.song.widget.scroll.SlidePageView;
import cn.w.song.widget.scroll.SlidePageView.OnPageViewChangedListener;

/**
 * SlidePageView支持各显示单元的自行定制，不仅可以支持等宽的显示单元设计，也支持不规则宽度的显示单元设计。
 * SlidePageView支持移动滑动和甩手两种滑动方式。
 * 了解详情看http://blog.csdn.net/swadair/article/details/7529159
 * 注意确保本项目导入了w.song.android.widget-1.0.3.jar组件包
 * w.song.android.widget-1.0.3.jar下载地址http://download.csdn.net/detail/swadair/4271503
 * @author w.song
 * @version 1.0.1
 * @date 2012-5-2
 */
public class SlidePageViewDemoActivity extends Activity {
	private ImageView[] points = new ImageView[4];
	private LinearLayout mLinearLayout;
	private int current = 0;// 默认在第一页
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidepageviewdemo_ui);//仿微信引导页
//		setContentView(R.layout.slidepageviewdemo_ui_b);//自定义等宽显示单元demo
//		setContentView(R.layout.slidepageviewdemo_ui_c);//自定义不规则宽度显示单元demo
		SlidePageView spv = (SlidePageView) findViewById(R.id.slidepageviewtest_ui_SlidePageView_test);
		//spv.setCurrPagePosition(0);//设置当前页位置
		spv.setOnPageViewChangedListener(new OnPageViewChangedListener() {

			@Override
			public void OnPageViewChanged(int currPagePosition,
					View currPageView) {
				System.out.println("currPagePosition=" + currPagePosition);
				points[currPagePosition].setImageResource(R.drawable.point_select);
		        points[current].setImageResource(R.drawable.point_normal);
		        current = currPagePosition;
			}
		});
		
		PreferencesUtils.putBoolean(this, "ISFIRST", true);
		
		mLinearLayout = (LinearLayout) this.findViewById(R.id.linearlayout);
		initPoints();
	}
	
	public void OnStart(View v) {
	    Intent intent=new Intent(this,LoginActivity.class);
	    startActivity(intent);
	    finish();
	}
	
	 // 初始化下面的小圆点
    private void initPoints() {
        for (int i = 0; i < 4; i++) {
            points[i] = (ImageView) mLinearLayout.getChildAt(i);
            points[i].setImageResource(R.drawable.point_normal);

        }
        current = 0;// 默认在第一页
        points[current].setImageResource(R.drawable.point_select);// 此刻处于第一页，把第一页的小圆圈设置为unenabled
    }
}
