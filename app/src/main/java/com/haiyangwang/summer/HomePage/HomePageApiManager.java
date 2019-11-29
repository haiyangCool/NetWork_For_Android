package com.haiyangwang.summer.HomePage;


import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerParameterSource;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerService;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerValidator;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

import java.util.HashMap;
import java.util.Map;

public class HomePageApiManager extends VVBaseApiManager implements
        ApiManager,
        ApiManagerValidator,
        ApiManagerParameterSource {

    public HomePageApiManager() {

        setChild(this);
        setValidator(this);
        setParamSource(this);
    }

    @Override
    public ApiManagerService getService() {
        return new HomeService();
    }


    @Override
    public String getApiAddress() {
        return "carousel/getList.json";
    }

    @Override
    public VVPublicDefines.ManagerRequestType getRequestType() {
        return VVPublicDefines.ManagerRequestType.GET;
    }

    @Override
    public VVPublicDefines.CachePolicy getCachePolicy() {
        return VVPublicDefines.CachePolicy.none;
    }

    @Override
    public Map<String, String> reformParams(Map<String, String> params) {
        return params;
    }

    @Override
    public Map<String, String> getParametersForApi(VVBaseApiManager manager) {
        Map<String ,String > params = new HashMap<>();
        params.put("relationType","2");
        params.put("fixedParameter","home");
        return params;
    }

    @Override
    public VVPublicDefines.RequestFailureType getResponseIsCorrect(VVBaseApiManager manager, VVURLResponse response) {

        return VVPublicDefines.RequestFailureType.noException;
    }

    @Override
    public VVPublicDefines.RequestFailureType getParamIsCorrect(VVBaseApiManager manager, Map<String, String> params) {
        return VVPublicDefines.RequestFailureType.noException;
    }
}
