package com.haiyangwang.summer.NetWork.CacheCenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.haiyangwang.summer.NetWork.ApplicationContext;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VVDiskCacheCenter extends Object {
    private static final String TAG = "VVDiskCacheCenter";

    private static final String DMDISKCACHEShARENAME = "com.DMDishCache.name";
    private static final String DMDISKCACHEKEYPREFIX = "com.DMDiskCache.pre";

    private static final String DMDISKCACHEUPDATE = "DMDISKCACHEUPDATE";
    private static final String DMDISKCACHETIME = "DMDISKCACHETIME";

    public VVDiskCacheCenter() {}

    public void saveCacheDataWith(String response, String key, int cacheTime) {

        String cacheKey = DMDISKCACHEKEYPREFIX + key;

        Map<String ,Object> cacheObj = new HashMap<>();
        cacheObj.put(cacheKey,response);
        cacheObj.put(DMDISKCACHEUPDATE,new Date().getTime());
        cacheObj.put(DMDISKCACHETIME,cacheTime);

        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        try{
            JSONObject json = new JSONObject(cacheObj);
            editor.putString(cacheKey,json.toString());
            editor.commit();
        }catch (Exception e) {
            Log.d(TAG,"Exception = \t"+e.getMessage());
        }


    }

    public String fetchCacheWithKey(String key) {

        String cacheKey = DMDISKCACHEKEYPREFIX + key;

        SharedPreferences preferences = ApplicationContext.INSTANCE.getSharedPreferences(DMDISKCACHEShARENAME, Context.MODE_PRIVATE);

        int cacheTime = 0;
        long updateTime = 0;
        String response = null;
        if (preferences.contains(cacheKey)) {

            String cacheResponseStr = preferences.getString(cacheKey,null);
            try{
                JSONObject json = new JSONObject(cacheResponseStr);
                response = json.getString(cacheKey);
                cacheTime = json.getInt(DMDISKCACHETIME);
                updateTime = json.getLong(DMDISKCACHEUPDATE);

                if (new Date().getTime()-updateTime > cacheTime) {
                    // 过期
                    removeCacheByKey(cacheKey);
                }
                return response;

            } catch (Exception e) {
                Log.d(TAG,"json error  = \t"+e.getMessage());
            }

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
