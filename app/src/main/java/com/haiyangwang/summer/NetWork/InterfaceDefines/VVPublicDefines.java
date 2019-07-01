package com.haiyangwang.summer.NetWork.InterfaceDefines;

public class VVPublicDefines {

    /* 开发环境*/
    public enum VVAPIEnvironmnet {
        DEVELOP,  // 开发
        RELEASE   // 发布
    }

    /* 请求方式*/
    public enum ManagerRequestType {
        GET,
        POST
    }

    /* 缓存策略*/
    public enum CachePolicy {
        // 内存缓存
        memory,
        // 硬盘缓存
        disk,
        // 同时缓存
        memoryAndDisk,
        // 不缓存
        none
    }

    /* Api 请求失败类型*/
    public enum RequestFailureType {
        // 缺失AccessToken或token过期
        lackAccessToken,
        // 需要登录
        needLogin,
        // 默认错误，没有进行网络请求
        defaultException,
        //请求超时
        timeOut,
        //参数错误
        parameterException,
        //返回的数据异常
        resultNotCorrect,
        //网络异常
        netException,
        // 服务器数据解析出错
        serviceDataDecodeError,
        // 没有错误
        noException

    }
}
