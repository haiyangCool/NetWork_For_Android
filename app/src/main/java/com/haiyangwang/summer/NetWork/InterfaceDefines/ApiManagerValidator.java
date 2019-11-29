package com.haiyangwang.summer.NetWork.InterfaceDefines;

import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

import java.util.Map;

/*
 * 验证：由每个api自己实现验证
 * 验证不提供默认正确的设置，让每个业务接口开发者一开始就对自己的参数配置和服务器是否返回数据做好验证*/
public interface ApiManagerValidator {

    /* 验证由服务器返回的数据对业务方是否可用
     * 网络层只完成对服务器是否正确返回数据的验证
     * 不验证时，可直接返回noException*/
    VVPublicDefines.RequestFailureType getResponseIsCorrect(VVBaseApiManager manager, VVURLResponse response);
    /*
     * 验证请求参数是否正确
     * 参数验证在发起网络请求之前其实是十分重要的，对于参数不正确的情况在客户端就可以直接过滤
     * example: 用户进行注册登录时，对邮箱格式的验证可直接在该处验证，避免不必要网络资源浪费
     * 不验证可直接返回noException*/
    VVPublicDefines.RequestFailureType getParamIsCorrect(VVBaseApiManager manager, Map<String, String> params);
}