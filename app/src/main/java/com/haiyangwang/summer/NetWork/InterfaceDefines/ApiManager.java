package com.haiyangwang.summer.NetWork.InterfaceDefines;

/*APIManager 子类需要实现的接口
 * 这里提供公共方法让子类重写，使用接口对子类进行限定，防止业务方胡来*/

import java.lang.ref.WeakReference;
import java.util.Map;

public interface ApiManager {

    /* 服务方*/
    WeakReference<ApiManagerService> getService();

    /* 请求类型*/
    VVPublicDefines.ManagerRequestType getRequestType();

    /* api地址*/
    String getApiAddress();

    /* 缓存策略*/
    VVPublicDefines.CachePolicy getCachePolicy();

    /* 修改参数
    * 如果实现ApiManager接口的类不对自行实现，则默认返回原始参数*/
     Map<String,String> reformParams(Map<String, String> params);
}