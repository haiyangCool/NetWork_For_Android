package com.haiyangwang.summer.NetWork;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;

import java.util.Map;

/*
* 为了避免Request与ok库的耦合出现，VVRequest并不直接继承  ok 的Request，
* 而是提供发起一个request所必须的数据
* Note: 一般来说 get 请求，最终生成的 url 基本上都是
* GET: http(https)://xxx.yy.zz?userName=xx&age=18 这种格式，但也有一些比较奇葩的接口提供者
* 定义的 get api 比如  http(https)://xxx.yy.zz-userName=xx&age=18 ,
* 更有甚者，你完全不知道传过去的参数是什么意思http(https)://xxx.yy.zz-ccc-12-23-bbm.json
* 一般来说，如果时开发阶段，可以打回去重新定义，如果已经上线，或者是
* 重构项目时，如果能有接口文档还好，如果没有……*/
public class VVRequest {

    // 请求方式
    private VVPublicDefines.ManagerRequestType requestType;
    // 请求地址
    private @NonNull String url;
    // 头部参数 （可以为空）
    private @Nullable Map<String,String> headers;
    // 请求体（可以为空）
    private @Nullable Map<String,String> body;


    /*为了应对各种奇葩的 get api，对参数的拼接方式
    * 如果是符合规范的 http(https)://xxx.yy.zz?userName=xx&age=18 这类格式
    * 可以直接通过键值对Map 在参数配置接口中赋值即可
    * 如果是，比较奇葩的格式，则由业务层把拼接好的参数通过
    * 【GET_UNIQUE_PARAM_KEY:拼接好的参数】的方式*/
    private static final String GET_UNIQUE_PARAM_KEY  = "GET_UNIQUE_PARAM_KEY";


    private static class VVRequestHolder {
        private static  final VVRequest instance = new VVRequest();
    }

    public static final VVRequest getInstance() {
        return  VVRequestHolder.instance;
    }

    public VVRequest request(VVPublicDefines.ManagerRequestType requestType,@NonNull String url,@Nullable  Map<String,String> params) {

        this.requestType = requestType;
        this.url = url;
        if (requestType == VVPublicDefines.ManagerRequestType.GET) {
            if (params == null || params.isEmpty()) {
                // join api and params
                StringBuilder paramsStr = new StringBuilder();
                for (String key: params.keySet()) {

                    // 如果是唯一键值（处理比较奇葩的参数拼接方式），直接拼接
                    if (key.equals(GET_UNIQUE_PARAM_KEY)) {
                        this.url = this.url + params.get(key);
                    }else {
                        paramsStr.append(key+"="+params.get(key)+"&");
                    }
                }
                paramsStr.deleteCharAt(paramsStr.length()-1);
                this.url = this.url+"?"+paramsStr.toString();
            }
            // ?xx="google"&yy="android"

        }

        // Post 请求直接发键值对参数原样返回，由 代理网络使用自己的 Request body
        if (requestType == VVPublicDefines.ManagerRequestType.POST) {
            this.body = params;
        }

        return this;
    }


    /*Getter */
    public VVPublicDefines.ManagerRequestType getRequestType() {
        return requestType;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Nullable
    public Map<String, String> getBody() {
        return body;
    }
}
