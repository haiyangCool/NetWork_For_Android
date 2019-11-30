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

                   dataMap.put(FilmDataKey.FilmName,filmName);
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
