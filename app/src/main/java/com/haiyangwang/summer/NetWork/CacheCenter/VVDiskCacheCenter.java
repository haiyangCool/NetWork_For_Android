package com.haiyangwang.summer.NetWork.CacheCenter;

import android.content.Context;
import android.content.SharedPreferences;
import com.haiyangwang.summer.NetWork.ApplicationContext;

import java.util.Date;

public class VVDiskCacheCenter extends Object {

    private static final String DMDISKCACHEShARENAME = "com.DMDishCache.name";
    private static final String DMDISKCACHEKEYPREFIX = "com.DMDiskCache.pre";

    private static final String DMDISKCACHETIME = "DMDISKCACHETIME";
    private static final String DMDISKCACHEUPDATE = "DMDISKCACHEUPDATE";

    public VVDiskCacheCenter() {}

    public void saveCacheDataWith(String response, String key, int cacheTime) {

        String cacheKey = DMDISKCACHEKEYPREFIX + key;
        String cacheTimeKey = DMDISKCACHETIME + key;
        String cacheUpdateTime = DMDISKCACHEUPDATE + key;

        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(cacheKey,response);
        editor.putInt(cacheTimeKey,cacheTime);
        editor.putLong(cacheUpdateTime,new Date().getTime());
        editor.commit();


    }

    public String fetchCacheWithKey(String key) {

        String cacheKey = DMDISKCACHEKEYPREFIX + key;
        String cacheTimeKey = DMDISKCACHETIME + key;
        String cacheUpdateTime = DMDISKCACHEUPDATE + key;


        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME, Context.MODE_PRIVATE);

        int cacheTime = 0;
        long updateTime = 0;
        if (preferences.contains(cacheTimeKey) && preferences.contains(cacheUpdateTime)) {
            cacheTime = preferences.getInt(cacheTimeKey,0);
            updateTime = preferences.getLong(cacheUpdateTime,0);
        }

        if (preferences.contains(cacheKey)) {

            String cacheResponseStr = preferences.getString(cacheKey,null);

            long timeInterval = new Date().getTime()-updateTime;
            if (timeInterval > cacheTime) {
                return null;
            }
            return  cacheResponseStr;
        }
        return null;

    }

    public void removeCacheByKey(String key) {
        String cacheKey = DMDISKCACHEKEYPREFIX + key;
        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME,0);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.contains(cacheKey)) {
            editor.remove(cacheKey);
            editor.commit();
        }
    }

    public void clearAllCache() {

        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
