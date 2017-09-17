package cn.ian2018.activemanagebyxml;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/9/9/009.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
