package com.haiyangwang.summer.NetWork.InterfaceDefines;

import com.haiyangwang.summer.NetWork.VVBaseApiManager;

import java.util.Map;

/* Api 请求参数
 * */
public interface ApiManagerParameterSource {

    /* 配置参数 */
    Map<String,String> getParametersForApi(VVBaseApiManager manager);
}
