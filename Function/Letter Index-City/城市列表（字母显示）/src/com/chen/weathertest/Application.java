package com.chen.weathertest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

public class Application extends android.app.Application {
    public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();

    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private static final int CITY_LIST_SCUESS = 0;

    private static final String FORMAT = "^[a-z,A-Z].*$";

    private static Application mApplication;

    private CityDB mCityDB;

    private Map<String, Integer> mWeatherIcon;// 天气图标

    private Map<String, Integer> mWidgetWeatherIcon;// 插件天气图标

    private List<City> mCityList;

    // 首字母集
    private List<String> mSections;

    // 根据首字母存放数据
    private Map<String, List<City>> mMap;

    // 首字母位置集
    private List<Integer> mPositions;

    // 首字母对应的位置
    private Map<String, Integer> mIndexer;

    private boolean isCityListComplite;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case CITY_LIST_SCUESS:
                isCityListComplite = true;
                if (mListeners.size() > 0)// 通知接口完成加载
                    for (EventHandler handler : mListeners) {
                        handler.onCityComplite();
                    }
                break;
            default:
                break;
            }
        }
    };

    public static synchronized Application getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        initData();
    }

    private void initData() {
        mApplication = this;
        initCityList();

    }

    public synchronized CityDB getCityDB() {
        if (mCityDB == null)
            mCityDB = openCityDB();
        return mCityDB;
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.chen.weathertest" + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        if (!db.exists()) {
            // L.i("db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }

    public List<City> getCityList() {
        return mCityList;
    }

    public List<String> getSections() {
        return mSections;
    }

    public Map<String, List<City>> getMap() {
        return mMap;
    }

    public List<Integer> getPositions() {
        return mPositions;
    }

    public Map<String, Integer> getIndexer() {
        return mIndexer;
    }

    public boolean isCityListComplite() {
        return isCityListComplite;
    }

    public Map<String, Integer> getWeatherIconMap() {
        return mWeatherIcon;
    }

    private void initCityList() {
        mCityList = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                isCityListComplite = false;
                prepareCityList();
                mHandler.sendEmptyMessage(CITY_LIST_SCUESS);
            }
        }).start();
    }

    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
        for (City city : mCityList) {
            String firstName = city.getFirstPY();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
        return true;
    }

    BroadcastReceiver netChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(NET_CHANGE_ACTION)) {
                if (mListeners.size() > 0)// 通知接口完成加载
                    for (EventHandler handler : mListeners) {
                        handler.onNetChange();
                    }
            }
        }

    };

    public static abstract interface EventHandler {
        public abstract void onCityComplite();

        public abstract void onNetChange();
    }
}
