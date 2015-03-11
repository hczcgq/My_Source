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
 * SlidePageView֧�ָ���ʾ��Ԫ�����ж��ƣ���������֧�ֵȿ����ʾ��Ԫ��ƣ�Ҳ֧�ֲ������ȵ���ʾ��Ԫ��ơ�
 * SlidePageView֧���ƶ�������˦�����ֻ�����ʽ��
 * �˽����鿴http://blog.csdn.net/swadair/article/details/7529159
 * ע��ȷ������Ŀ������w.song.android.widget-1.0.3.jar�����
 * w.song.android.widget-1.0.3.jar���ص�ַhttp://download.csdn.net/detail/swadair/4271503
 * @author w.song
 * @version 1.0.1
 * @date 2012-5-2
 */
public class SlidePageViewDemoActivity extends Activity {
	private ImageView[] points = new ImageView[4];
	private LinearLayout mLinearLayout;
	private int current = 0;// Ĭ���ڵ�һҳ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidepageviewdemo_ui);//��΢������ҳ
//		setContentView(R.layout.slidepageviewdemo_ui_b);//�Զ���ȿ���ʾ��Ԫdemo
//		setContentView(R.layout.slidepageviewdemo_ui_c);//�Զ��岻��������ʾ��Ԫdemo
		SlidePageView spv = (SlidePageView) findViewById(R.id.slidepageviewtest_ui_SlidePageView_test);
		//spv.setCurrPagePosition(0);//���õ�ǰҳλ��
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
	
	 // ��ʼ�������СԲ��
    private void initPoints() {
        for (int i = 0; i < 4; i++) {
            points[i] = (ImageView) mLinearLayout.getChildAt(i);
            points[i].setImageResource(R.drawable.point_normal);

        }
        current = 0;// Ĭ���ڵ�һҳ
        points[current].setImageResource(R.drawable.point_select);// �˿̴��ڵ�һҳ���ѵ�һҳ��СԲȦ����Ϊunenabled
    }
}
