package com.haiyangwang.summer.NetWork.InterfaceDefines;

/*APIManager 子类需要实现的接口
 * 这里提供公共方法让子类重写，使用接口对子类进行限定，防止业务方胡来*/

import java.util.Map;

public interface ApiManager {

    /** 子类必须实现的接口*/
    /* 服务方*/
    ApiManagerService getService();

    /* api地址*/
    String getApiAddress();

    /** 子类可选实现的接口*/
    /* 请求类型 默认为 GET 请求*/
    default VVPublicDefines.ManagerRequestType getRequestType() {
        return VVPublicDefines.ManagerRequestType.GET;
    };

    /* 缓存策略 默认 不缓存*/
    default VVPublicDefines.CachePolicy getCachePolicy() {
        return  VVPublicDefines.CachePolicy.none;
    };

    /* 修改参数
    * 如果实现ApiManager接口的类不对自行实现，则默认返回原始参数*/
    default Map<String,String> reformParams(Map<String, String> params) {
        return  params;
    };
}