package com.haiyangwang.summer.HomePage;

import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerService;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

public class HomeService implements ApiManagerService {

    public HomeService() {

    }

    @Override
    public String getServiceAddress() {
        if (getEnvironment() == VVPublicDefines.VVAPIEnvironmnet.DEVELOP) {
            return "http://api.app.pthv.gitv.tv/";
        }
        return "http://api.app.pthv.gitv.tv/";
    }

    @Override
    public VVPublicDefines.VVAPIEnvironmnet getEnvironment() {
        return VVPublicDefines.VVAPIEnvironmnet.DEVELOP;
    }

    @Override
    public boolean handleFailure(VVBaseApiManager manager, VVURLResponse response, VVPublicDefines.RequestFailureType type) {

        /* 错误为token过期或者未登录状态（业务层不用关心的错误类型），服务层可以直接进行处理
        * 错误不会继续传递到业务层*/
        /** 在这里处理业务方不需要处理的问题*/
        return false;
    }
}
