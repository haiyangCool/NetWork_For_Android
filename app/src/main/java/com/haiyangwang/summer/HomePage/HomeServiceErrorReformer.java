package com.haiyangwang.summer.HomePage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResponseDataReformer;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;

import java.util.HashMap;
import java.util.Map;

public class HomeServiceErrorReformer implements ApiManagerResponseDataReformer {

    public HomeServiceErrorReformer() {}

    @Override
    public Object reformerData(VVBaseApiManager manager, String jsonString) {

        if (jsonString != null) {
            Map<String, String> errorInfo = new HashMap<>();

            JSONObject jsonObject = JSON.parseObject(jsonString);
            String returnValue =  jsonObject.getString("return_value");
            int returnCode = jsonObject.getIntValue("return_code");

            // 在这里通过对比服务端定义的各类 错误码 转化为可读错误
            errorInfo.put(HomeServiceErrorKey.Code,""+returnCode);
            errorInfo.put(HomeServiceErrorKey.Message,returnValue);
            return  errorInfo;
        }
        return null;
    }
}
