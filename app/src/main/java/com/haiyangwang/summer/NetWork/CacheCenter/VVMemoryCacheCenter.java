package com.haiyangwang.summer.NetWork.CacheCenter;

import android.util.LruCache;

import java.util.Date;
/*内存缓存
* 默认对缓存数据保有2分钟，超时则缓存无效*/
public class VVMemoryCacheCenter extends Object {

    public VVMemoryCacheCenter() {}

    private LruCache<String,Object> mLruCache;


    public LruCache<String, Object> getLruCache() {
        if (mLruCache == null) {
            long memorySize = Runtime.getRuntime().maxMemory();

            mLruCache = new LruCache<String, Object>((int) memorySize/8);
        }
        return mLruCache;
    }

    /* 缓存数据*/
    public void saveCacheDataWith(Object cacheData, String key, int cacheTime){

        savaDataWith(cacheData,key,cacheTime);
    }

    /* 查找缓存*/
    public CacheObject fetchCachaDataWith(String key) {
        return fetCacheDataWith(key);
    }

    /* 清理缓存-*/
    public void clearCache(String key) {

        clearCacheWith(key);
    }

    /* 清理所有缓存*/
    public void clearAllCache() {
        getLruCache().evictAll();
    }

    /* Private methods*/
    // cache server response data
    private void savaDataWith(Object cacheData,String key,int cacheTime) {

        // 保存数据之前，根据生成的key进行缓存查找，如果已经有该key对应的数据，则更新当前数据
        CacheObject cacheObj = (CacheObject) getLruCache().get(key);
        if (cacheObj == null) {
            cacheObj = new CacheObject();
        }
        cacheObj.updateContent(cacheData,cacheTime);
        getLruCache().put(key,cacheObj);

    }

    // fetch cache
    private CacheObject fetCacheDataWith(String key) {

        CacheObject cacheObject = (CacheObject) getLruCache().get(key);
        if (cacheObject == null) {
            return null;
        }
        if (cacheObject.isOutDateCache() || cacheObject.isEmptyOfCache()) {
            clearCacheWith(key);
            return null;
        }
        return cacheObject;
    }

    // clear cache
    private void clearCacheWith(String key) {
        getLruCache().remove(key);
    }

    /* 缓存对象*/
    static class CacheObject extends Object {
        // 内容
        private Object content;
        // 更新时间
        private long updateTime;
        // 缓存保留时间
        private int cacheTime;

        CacheObject() {}

        // update data
        public void updateContent(Object content,int cacheTime) {
            this.content = content;
            this.updateTime = new Date().getTime();
            this.cacheTime = cacheTime;
        }

        // is timeout 2分钟超时
        public boolean isOutDateCache() {

            long timeInterval = new Date().getTime()-this.updateTime;
            return timeInterval>this.cacheTime;
        }

        // is Empty
        public boolean isEmptyOfCache() {
            return this.content == null;
        }


        public Object getContent() {
            return content;
        }

    }

}
