package com.example.contactslisttest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnTouchListener {

    private final class RemoveWindow implements Runnable {
        public void run() {
            removeWindow();
        }
    }
    private static String[] LETTERS = new String[]{"#","A","B","C","D","E","F","G","H","I","J","K","L",
    	"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private RemoveWindow mRemoveWindow = new RemoveWindow();
    Handler mHandler = new Handler();
    private WindowManager mWindowManager;
    private TextView mDialogText;
    private ListView mListView;
    private LinearLayout mLinearLayout;
    private int mLettersLength = LETTERS.length;
    private HashMap<String,Integer> mAlphaIndexMap = new HashMap<String, Integer>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_datalist);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_indicator);
        for (int i = 0; i < LETTERS.length; i++) {
			TextView textView = new TextView(this);
			textView.setText(LETTERS[i]);
			textView.setTextSize(16);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 1.0f));;
			textView.setPadding(4, 0, 2, 0);
			mLinearLayout.addView(textView);
		}
        mLinearLayout.setOnTouchListener(this);
        mListView.setAdapter(new DataAdapter());
        LayoutInflater inflate = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogText = (TextView) inflate.inflate(R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mHandler.post(new Runnable() {
            public void run() {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                mWindowManager.addView(mDialogText, lp);
            }});
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDialogText);
    }
    
    private void removeWindow() {
    	mDialogText.setVisibility(View.INVISIBLE);
    }

    private class DataAdapter extends BaseAdapter {
    	LayoutInflater inflater;
    	String temp = "-1";
    	public DataAdapter(){
    		Arrays.sort(mStrings);
    		this.inflater = LayoutInflater.from(MainActivity.this);
    		for (int i = 0; i < mStrings.length; i++) {
				String alpha = String.valueOf(mStrings[i].charAt(0)).toUpperCase();
				if(isLetter(alpha)){					
					if(!alpha.equals(temp)){					
						mAlphaIndexMap.put(alpha, i);
						temp = alpha;
					}
				} else {
					mAlphaIndexMap.put("#", 0);
				}
			}
    	}

		@Override
		public int getCount() {
			return mStrings.length;
		}

		@Override
		public String getItem(int position) {
			return mStrings[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.list_item, null);
				holder.tvAlphaHeader = (TextView) convertView.findViewById(R.id.tv_alphabar);
				holder.tvData = (TextView) convertView.findViewById(R.id.tv_data);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String alpha = String.valueOf(getItem(position).charAt(0)).toUpperCase();
			if(!isLetter(alpha))
				alpha = "#";
			if(mAlphaIndexMap.get(alpha) == position){		
				holder.tvAlphaHeader.setVisibility(View.VISIBLE);
				holder.tvAlphaHeader.setText(alpha);
			} else {
				holder.tvAlphaHeader.setVisibility(View.GONE);
			}
			holder.tvData.setText(getItem(position));
			return convertView;
		}
    }
    
	private boolean isLetter(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher m = pattern.matcher(str);
		return m.matches();
	}
    
    static class ViewHolder {
    	public TextView tvAlphaHeader;
    	public TextView tvData;
    }
    
    private String[] mStrings = Cheeses.sCheeseStrings;

	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		int height = v.getHeight();
		final int alphaHeight = height / mLettersLength;
		final int fingerY = (int) event.getY();
		int selectIndex = fingerY / alphaHeight;
		if (selectIndex < 0 || selectIndex > mLettersLength - 1) {// ∑¿÷π‘ΩΩÁ
			mLinearLayout.setBackgroundResource(android.R.color.transparent);
			mHandler.removeCallbacks(mRemoveWindow);
    		mHandler.post(mRemoveWindow);
			return true;
		}
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			String letter = LETTERS[selectIndex];
			findLocation(letter);
			break;
		case MotionEvent.ACTION_MOVE:
			letter = LETTERS[selectIndex];
			findLocation(letter);
			break;
		case MotionEvent.ACTION_UP:
			mLinearLayout.setBackgroundResource(android.R.color.transparent);
			mHandler.removeCallbacks(mRemoveWindow);
    		mHandler.post(mRemoveWindow);
			break;
		default:
			break;
		}
		return true;
	}
	
	private void findLocation(String letter){
		if(mAlphaIndexMap.containsKey(letter)){			
			mLinearLayout.setBackgroundResource(android.R.color.darker_gray);
			mDialogText.setVisibility(View.VISIBLE);
			mDialogText.setText(letter);
			int position = mAlphaIndexMap.get(letter);
			mListView.setSelection(position);
		}
	}
	
}