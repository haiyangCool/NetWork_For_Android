package com.haiyangwang.summer.NetWork;

import android.app.Application;
import android.util.Log;
/* 全局Context*/
public class ApplicationContext {

    private static final String TAG = "ApplicationContext";
    public static final Application INSTANCE;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("静态初始化Application必须在主线程.");
        } catch (final Exception e) {
            Log.i(TAG," AppGlobals.获取全局Application失败" + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.i(TAG,"在Activity 线程获取失败." + e.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
    }
}
