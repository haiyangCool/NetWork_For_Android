package com.haiyangwang.summer.HomePage;

import android.util.Log;

import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerValidator;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

import org.json.JSONObject;

import java.util.Map;

public class HomeServiceValidator implements ApiManagerValidator {

    private static final String HomeService_RETURN_CODE = "return_value";
    public HomeServiceValidator() {}

    @Override
    public VVPublicDefines.RequestFailureType getResponseIsCorrect(VVBaseApiManager manager, VVURLResponse response) {
        // 对同一Service返回的接口数据，可以把判断数据是否可用进行一次封装
        JSONObject jsonObject = response.getContent();
        if (jsonObject != null) {
            try {
                String returnValue = jsonObject.getString(HomeService_RETURN_CODE);
                if (returnValue.equals("OK")) {
                    return VVPublicDefines.RequestFailureType.noException;
                }
            }catch (Exception e) {}

        }
        return VVPublicDefines.RequestFailureType.resultNotCorrect;    }

    @Override
    public VVPublicDefines.RequestFailureType getParamIsCorrect(VVBaseApiManager manager, Map<String, String> params) {
        return VVPublicDefines.RequestFailureType.noException;
    }
}
