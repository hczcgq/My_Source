package com.mrwujay.cascade.activity;

import com.mrwujay.cascade.R;
import com.mrwujay.cascade.R.id;
import com.mrwujay.cascade.R.layout;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    private WheelView mWheelView;

    private Button mBtnConfirm;

    private String[] numbers = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "10" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpViews() {
        mWheelView = (WheelView) findViewById(R.id.id_province);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        mWheelView.setViewAdapter(new ArrayWheelAdapter<String>(
                MainActivity.this, numbers));
        // 设置可见条目数量
        mWheelView.setVisibleItems(7);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_confirm:
            showSelectedResult();
            break;
        default:
            break;
        }
    }

    private void showSelectedResult() {
        Toast.makeText(MainActivity.this,
                "当前选中:" + numbers[mWheelView.getCurrentItem()],
                Toast.LENGTH_SHORT).show();
    }
}
