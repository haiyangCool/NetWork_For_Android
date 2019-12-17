package com.haiyangwang.summer.HomePage;


import android.util.Log;

import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerLoadNextPage;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerParameterSource;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerService;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerValidator;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class HomePageApiManager extends VVBaseApiManager implements
        ApiManager,
//        ApiManagerValidator,
        ApiManagerParameterSource {


    public HomePageApiManager() {

        setChild(new WeakReference<>(this));
        setValidator(new WeakReference<>(new HomeServiceValidator()));
        setParamSource(new WeakReference<>(this));
    }

    @Override
    public ApiManagerService getService() {
        return HomeService.getInstance();
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
        params.put("fixedParameter","home");
        params.put("relationType","2");
        return params;
    }


}
