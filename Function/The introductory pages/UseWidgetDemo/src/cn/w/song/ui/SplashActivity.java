package cn.w.song.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_splash);

        int current_version = getVersion(this);
        PreferencesUtils.PREFERENCE_NAME = "SP_LOGIN";
        boolean isExistFirst = PreferencesUtils.checkExist(this, "ISFIRST");
        boolean isExistVersion = PreferencesUtils.checkExist(this, "VERSION");
        if (isExistVersion) {
            int version = PreferencesUtils.getInt(this, "VERSION");
            if (current_version > version && isExistFirst) {
                PreferencesUtils.revomeKey(this, "ISFIRST");
            }
        }

        PreferencesUtils.putInt(this, "VERSION", current_version);

        isExistFirst = PreferencesUtils.checkExist(this, "ISFIRST");
        if (!isExistFirst) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,
                            SlidePageViewDemoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

    /**
     * 获取当前应用内部的的版本号
     * 
     * @return
     */
    private int getVersion(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
