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

        lackAccessToken("Token已过期"),

        needLogin("需要登录"),

        defaultException("没有进行网络请求"),

        timeOut("请求超时"),

        parameterException("参数错误"),

        // 在发生该错误时，可以通过解析response，获取服务器返回的具体的错误码和错误信息
        resultNotCorrect("服务器数据异常"),

        loseNet("您似乎断开了网络连接"),

        netException("网络异常"),

        serviceDataDecodeError("解析服务器数据出错"),

        noException("未发生错误");


        private final String rawValue;

        private RequestFailureType(String rawValue) {
            this.rawValue = rawValue;
        }

        public String rawValue() {
            return this.rawValue;
        }


    }
}
