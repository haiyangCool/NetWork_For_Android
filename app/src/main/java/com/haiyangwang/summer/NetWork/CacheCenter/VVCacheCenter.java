package com.haiyangwang.summer.NetWork.CacheCenter;


import com.haiyangwang.summer.NetWork.MD5;
import com.haiyangwang.summer.NetWork.VVURLResponse;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/* 缓存中心，对服务其返回的数据进行缓存
*  Memory Cache: 内存缓存
*  Disk Cache: 硬盘缓存
* */
public class VVCacheCenter extends Object{

    private static final String TAG  = "VVCacheCenter";
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
                cacheKey(methodIdentifier,apiName,params),
                cacheTime);

    }

    public void saveCacheByDiskWith(VVURLResponse response,
                                    String methodIdentifier,
                                    String apiName,
                                    Map<String,String> params,
                                    int cacheTime) {
        mDiskCacheCenter.saveCacheDataWith(response.getContentString(),
                cacheKey(methodIdentifier,apiName,params),
                cacheTime);

    }

    public VVURLResponse fetchMemoryCacheDataWith(String methodIdentifier,
                                                                    String apiName,
                                                                    Map<String,String> params) {
        VVMemoryCacheCenter.CacheObject obj = mMemoryCacheCenter.fetchCacheDataWith(cacheKey(methodIdentifier,
                apiName,
                params));
        if (obj != null) {
            VVURLResponse response = (VVURLResponse)obj.getContent();
            response.setIsCache(true);
            response.setMethod(methodIdentifier);
            response.setRequestUrl(apiName);
            response.setRequestParams(params);
            return response;
        }
        return null;
    }

    public VVURLResponse fetchDiskCacheDataWith(String methodIdentifier,
                                         String apiName,
                                         Map<String,String> params) {
        String responseStr = mDiskCacheCenter.fetchCacheWithKey(cacheKey(methodIdentifier,
                apiName,
                params));
        if (responseStr != null && responseStr != "") {
            VVURLResponse response = new VVURLResponse().transformStringToResponse(responseStr);
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
    private String cacheKey(String methodIdentifier, String apiName, Map<String,String> params) {
        String cacheKey = methodIdentifier+apiName;

        if (params != null) {

            List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(params.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
                // 进行一次升序的排列
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            });

            cacheKey = cacheKey+list.toString();
        }
        return MD5.md5(cacheKey);

    }
}
