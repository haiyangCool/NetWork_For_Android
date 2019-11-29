package com.haiyangwang.summer.NetWork.InterfaceDefines;

import com.haiyangwang.summer.NetWork.VVBaseApiManager;

/* Api 请求结果回调
* */
public interface ApiManagerResultCallBackDelegate {

    /*成功
     * */
    void managerCallApiDidSuccess(VVBaseApiManager manager);

    /*失败
     * 对于服务提供者Service可以处理的异常由(服务提供者Service)解决,不会反馈到业务层
     * Service无法解决的问题（无法统一处理），反馈给业务层进行处理*/
    void managerCallApiManagerFaild(VVBaseApiManager manager);
}
