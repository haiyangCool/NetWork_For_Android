package com.haiyangwang.summer.HomePage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResponseDataReformer;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** 从Reformer中出去的数据是直接可用的，免去了二次转化，在Reformer中实现
 * 部分的弱业务逻辑（弱业务逻辑一般是不会变的）*/
public class FilmDataReformer implements ApiManagerResponseDataReformer {
    private static final String TAG  = "FilmDataReformer";
    public FilmDataReformer() {}

    @Override
    public Object reformerData(VVBaseApiManager manager, String jsonString) {

        if (manager instanceof  HomePageApiManager) {
           if (!jsonString.isEmpty()) {

               Map<String, Object> filmMap = new HashMap<>();

               JSONObject jsonObject = JSON.parseObject(jsonString);
               String returnValue =  jsonObject.getString("return_value");
               int returnCode = jsonObject.getIntValue("return_code");
               JSONArray dataList = jsonObject.getJSONArray("rows");

                filmMap.put(FilmDataKey.Code,returnCode);
                filmMap.put(FilmDataKey.Message,returnValue);

               List filmList = new ArrayList();
               for (int i = 0; i < dataList.size(); i++) {
                   Map<String, String> dataMap = new HashMap<>();
                   JSONObject dataObj = dataList.getJSONObject(i);
                   String filmName = dataObj.getString("carouselContentName");
                   int filmId = dataObj.getIntValue("carouselContentId");
                   String filmImageAddress = dataObj.getString("carouselImageUrl");

                   dataMap.put(FilmDataKey.FilmName,"电影："+filmName);
                   dataMap.put(FilmDataKey.FilmID,""+filmId);
                   dataMap.put(FilmDataKey.FilmImageAddress,filmImageAddress);

                   filmList.add(dataMap);

               }
               filmMap.put(FilmDataKey.FilmInfo,filmList);
               return filmMap;


           }
        }
        return null;
    }
}
