package com.haiyangwang.summer.NetWork.InterfaceDefines;


import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;

import java.util.Map;

/* 拦截器（AOP）
 * 通过AOP 划分切面
 * 拦截器的功能可以由通过继承由子类来实现，或者由其他的类通过实现接口来实现
 * 一般如果仅仅实现拦截器的功能 通过其他的类实现接口来实现拦截器的功能
 * 如果需要对拦截器的功能进行扩展，或者Manager本身需要在拦截器的执行过程中插入逻辑
 * 可以通过子类（继承）实现接口完成*/
// 通过子类来实现的模式其实就是装饰器模式
public interface ApiManagerInterceptor {

    //确认成功返回之前 （执行success之前），如果返回结果为true,继续执行，
    // false 则进入失败的Manager的逻辑
    boolean beforePerformSuccessWithResponse(VVBaseApiManager manager,
                                             VVURLResponse response);
    // 执行完Success逻辑之后
    void afterPerformSuccessWithResponse(VVBaseApiManager manager,
                                         VVURLResponse response);

    // 确认失败之前
    boolean beforePerformFailureWithResponse(VVBaseApiManager manager,
                                             VVURLResponse response);
    // 执行完Failure逻辑之后
    void afterPerformFailureWithResponse(VVBaseApiManager manager,
                                         VVURLResponse response);

    // 在执行Call Api 之前
    boolean beforePerformCallApiWithParams(VVBaseApiManager manager,
                                           Map<String, String> params);

    void  afterPerformCallApiWithParams(VVBaseApiManager manager,
                                        Map<String, String> params);
    // 接收到response之后
    void didReceivedResponse(VVBaseApiManager manager,
                             VVURLResponse response);
}
