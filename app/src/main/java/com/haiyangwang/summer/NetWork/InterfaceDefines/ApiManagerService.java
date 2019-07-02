package com.haiyangwang.summer.NetWork.InterfaceDefines;

import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

/* 服务提供者
 * 一个App内，服务的提供方可能由不同的团队来提供
 * 通过使用该服务，可以对不同团队提供的服务做细分处理
 * 比如，对同一个api错误的定义，不同团队定义的错误码，和错误message可能不同
 * 通过细分服务解决*/
public interface ApiManagerService {


    /*提供服务的域名地址, Android 第三方网路库并不完全基于URLRequest进行请求的发起
    * 所以在这里仅提供服务地址*/
    String getServiceAddress();

    /*开发环境*/
    VVPublicDefines.VVAPIEnvironmnet getEnvironment();

    /*是否对错误进行响应 （被动响应）
     * 返回true时,表示服务方进行处理，返回false,表示服务方不处理，错误将向上传递到业务层
     * */
    boolean handleFailure(VVBaseApiManager manager,
                               VVURLResponse response,
                          VVPublicDefines.RequestFailureType type);
}