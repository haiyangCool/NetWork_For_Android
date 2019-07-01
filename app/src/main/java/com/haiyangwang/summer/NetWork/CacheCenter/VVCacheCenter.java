package com.haiyangwang.summer.NetWork.CacheCenter;

import com.haiyangwang.summer.NetWork.VVURLResponse;

import org.json.JSONObject;

import java.util.Map;

/* 缓存中心，对服务其返回的数据进行缓存
*  Memory Cache: 内存缓存
*  Disk Cache: 硬盘缓存
* */
public class VVCacheCenter extends Object{

    private VVMemoryCacheCenter mMemoryCacheCenter = new VVMemoryCacheCenter();
    private VVDiskCacheCenter mDiskCacheCenter = new VVDiskCacheCenter();
    public VVCacheCenter() {}
    //single
    private static class CacheCenterHolder {
        private static  final VVCacheCenter instance = new VVCacheCenter();
    }
    public static final VVCacheCenter getInstance() {
        return CacheCenterHolder.instance;
    }

    public void saveCacheByMemoryWith(VVURLResponse response,
                                      String methodIdentifier,
                                      String apiName,
                                      Map<String,String> params,
                                      int cacheTime) {
        mMemoryCacheCenter.saveCacheDataWith(response,
                generatorCacheKey(methodIdentifier,apiName,params),
                cacheTime);

    }

    public void saveCacheByDiskWith(VVURLResponse response,
                                    String methodIdentifier,
                                    String apiName,
                                    Map<String,String> params,
                                    int cacheTime) {
        mDiskCacheCenter.saveCacheDataWith(response.getmContentString(),
                generatorCacheKey(methodIdentifier,apiName,params),
                cacheTime);

    }

    public VVURLResponse fetchMemoryCacheDataWith(String methodIdentifier,
                                                                    String apiName,
                                                                    Map<String,String> params) {
        VVMemoryCacheCenter.CacheObject obj = mMemoryCacheCenter.fetchCachaDataWith(generatorCacheKey(methodIdentifier,
                apiName,
                params));
        if (obj != null) {
            VVURLResponse response = (VVURLResponse)obj.getContent();
            response.setmIsCache(true);
            response.setmMethod(methodIdentifier);
            response.setmRequestUrl(apiName);
            response.setmRequestParams(params);
            return response;
        }
        return null;
    }

    public VVURLResponse fetchDiskCacheDataWith(String methodIdentifier,
                                         String apiName,
                                         Map<String,String> params) {
        String responseStr = mDiskCacheCenter.fetchCacheWithKey(generatorCacheKey(methodIdentifier,
                apiName,
                params));
        if (responseStr != null && responseStr != "") {
            VVURLResponse response = new VVURLResponse().transformStrignToResponse(responseStr);
            return  response;
        }
        return null;
    }

    public void clearAllMemoryCacheBy() {
        mMemoryCacheCenter.clearAllCache();
    }

    public void clearAllDiskCache() {
        mDiskCacheCenter.clearAllCache();
    }

    // Private method
    // return Cache Key
    private String generatorCacheKey(String methodIdentifier, String apiName, Map<String,String> params) {
        String tempKey = methodIdentifier+apiName;

        if (params != null) {

            JSONObject jsonObject = new JSONObject(params);
            String parasStr = jsonObject.toString();
            tempKey = tempKey+parasStr;
        }
        //可以对Key跑一边MD5
        return tempKey;


    }
}
