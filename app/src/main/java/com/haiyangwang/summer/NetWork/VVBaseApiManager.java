package com.haiyangwang.summer.NetWork;

import android.support.annotation.Nullable;

import com.haiyangwang.summer.NetWork.CacheCenter.VVCacheCenter;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerInterceptor;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerParameterSource;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResponseDataReformer;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerService;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerValidator;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class VVBaseApiManager extends Object {

    private static final String TAG  = "VVBaseApiManager";

    private ApiManagerService mService;             // 服务方

    private WeakReference<ApiManager> mChild;                      // 子类apiManager

    private WeakReference<ApiManagerParameterSource> mParamSource; // 参数提供

    private WeakReference<ApiManagerValidator> mValidator;         // 验证器

    private WeakReference<ApiManagerResultCallBackDelegate> mDelegate; // 结果回调

    private WeakReference<ApiManagerInterceptor> mInterceptor;     // 拦截器

    // 是否忽略缓存，shouldIgnoreCache为true是，则跳过缓存，直接进行网络请求
    private boolean shouldIgnoreCache = false;
    // 默认请求超时时间 15秒
    private int requestOutTime = 15*1000;
    // 内存缓存默认2分钟
    private int memoryCacheTime = 2*60*1000;
    // 硬盘缓存默认2分钟
    private int diskCacheTime = 2*60*1000;


    // Private info
    // 失败信息
    private String failureMessage = VVPublicDefines.RequestFailureType.noException.rawValue();
    // 失败类型
    private VVPublicDefines.RequestFailureType failureType = VVPublicDefines.RequestFailureType.noException;
    // loading flag
    private boolean isLoading = false;
    // 服务器返回的原始数据 VVURLResponse>>mResponseData
    private VVURLResponse rawResponse = new VVURLResponse();
    // 缓存
    private VVCacheCenter cacheCenter;
    // 请求列表
    private List<Number> requestList;


    /**Public methods ***********************************/
    // 所有的公共方法子类不要重载和重写
    // 加载数据
    public int loadData() {
        if (isLoading) { return 0; }

        Map<String,String> mps = null;
        if (mParamSource != null) {
            mps = mParamSource.get().getParametersForApi(this);
        }
        return loadDataWithParams(mps);
    }

    // 直接通过携带参数加载数据
    public int loadDataWith(Map<String,String> params, final VVNetProxy.VVNetResponseCallBack callBack) {

        // 默认GET
        VVPublicDefines.ManagerRequestType requestType = VVPublicDefines.ManagerRequestType.GET;
        if (mChild != null) {
            requestType = mChild.get().getRequestType();
        }
        VVRequest request = VVRequest.getInstance().request(requestType,getApiAddress(),params);

        if (requestType == VVPublicDefines.ManagerRequestType.GET) {
            return VVNetProxy.getInstance().getApiRequest(request,callBack).intValue();
        }
        if (requestType == VVPublicDefines.ManagerRequestType.POST) {
            return VVNetProxy.getInstance().postApiRequest(request,callBack).intValue();
        }
        return 0;
    }

    // 获取响应数据
    public Object fetchResponseDataWithReformer(ApiManagerResponseDataReformer reformer) {

        if (reformer != null) {
            return reformer.reformerData(this,rawResponse.getContentString());
        }
        return this.rawResponse.getContentString();
    }

    // 获取请求的Log Debug
    public String fetchLog() {
        return rawResponse.getLog();
    }

    // 失败原因,根据不同的失败原因可以做定制化操作
    public String fetchFailureType() {
        return failureType.rawValue();
    }

    // 取消请求by id
    public void cancelRequestById(int requestId) {

        Number requestO = (Number)requestId;
        if (getRequestList().contains(requestO)) {
            requestList.remove(requestO);
            VVNetProxy.getInstance().cancelRequestById(requestO);
        }
    }

    // 取消所有请求
    public void cancelAllRequset() {
        getRequestList().clear();
        VVNetProxy.getInstance().cancelAllRequest();
    }

    /**Public methods end*********************************/

    /**Private methods*/
    // api request
    private int loadDataWithParams(Map<String,String > params) {

        if (mChild != null) {
            mService = mChild.get().getService();
        }else {
            throw new SecurityException("---------mChild 必须实现---------") ;

        }
        // 请求Api之前询问拦截器是否允许
        if (!beforePerformCallApiWithParams(params)) {
            return 0;
        }
        Map<String ,String> reformedParams = params;
        if (mChild != null) {
            reformedParams = mChild.get().reformParams(params);
        }

        // 参数验证
        if (mValidator != null && mValidator.get().getParamIsCorrect(this,reformedParams) != VVPublicDefines.RequestFailureType.noException) {
            failureMessage = VVPublicDefines.RequestFailureType.parameterException.rawValue();
            faildCallApi(rawResponse, VVPublicDefines.RequestFailureType.parameterException);
            return 0;
        }
        VVURLResponse cacheResponse = null;
        // 查看缓存
        if (mChild != null && !shouldIgnoreCache) {

            VVPublicDefines.CachePolicy policy = mChild.get().getCachePolicy();
            if (policy == VVPublicDefines.CachePolicy.memory) {
                // 查找内存缓存
                cacheResponse = loadFromMemory(reformedParams);
            }

            if (policy == VVPublicDefines.CachePolicy.disk) {
                // 查找硬盘缓存
                cacheResponse = loadFromDisk(reformedParams);

            }

            if (policy == VVPublicDefines.CachePolicy.memoryAndDisk) {
                // 首先查找内存，如果内存缓存已经过期或被销毁，再去查找硬盘缓存
                VVURLResponse mR = loadFromMemory(reformedParams);
                if (mR == null) {
                    cacheResponse = loadFromDisk(reformedParams);
                }else {
                    cacheResponse = mR;
                }
            }
        }
        if (cacheResponse != null) {
            rawResponse = cacheResponse;
            successCallApi(cacheResponse);
            isLoading = false;

            return 0;
        }

        if (!getIsHaveNetLink()) {
            failureMessage = VVPublicDefines.RequestFailureType.netException.rawValue();
            faildCallApi(rawResponse, VVPublicDefines.RequestFailureType.netException);
            return 0;
        }

        VVPublicDefines.ManagerRequestType requestType = VVPublicDefines.ManagerRequestType.GET;
        if (mChild != null) {
            requestType = mChild.get().getRequestType();
        }

        isLoading = true;

        VVRequest request = VVRequest.getInstance().request(requestType,getApiAddress(),reformedParams);
        Number requstId = VVNetProxy.getInstance().callApi(request, new VVNetProxy.VVNetResponseCallBack() {
            @Override
            public void responseSuccess(VVURLResponse response) {

                successCallApi(response);
            }

            @Override
            public void responseFaild(VVURLResponse response) {

                faildCallApi(response,VVPublicDefines.RequestFailureType.defaultException);

            }
        });
        getRequestList().add(requstId);
        return requstId.intValue();
    }

    // 内存缓存是否存在
    private boolean getIsHaveMemoryCache(String requestType,String apiAddress,Map<String,String> params) {
        Object obj = getCacheCenter().fetchMemoryCacheDataWith(requestType,apiAddress,params);
        if (obj == null) {
            return false;
        }
        VVURLResponse response = new VVURLResponse().transformToResponse((byte[]) obj);
        successCallApi(response);
        return false;
    }

    // 加载内存缓存
    private VVURLResponse loadFromMemory(Map<String,String> requestParams) {

        VVURLResponse response = getCacheCenter().fetchMemoryCacheDataWith(mChild.get().getRequestType().toString(),
                getApiAddress(),
                requestParams);
        return response;
    }

    // 加载硬盘存储
    private VVURLResponse loadFromDisk(Map<String,String> requestParams) {
        VVURLResponse response = getCacheCenter().fetchDiskCacheDataWith(mChild.get().getRequestType().toString(),
                getApiAddress(),
                requestParams);
        return response;
    }

    // success
    private void successCallApi(VVURLResponse response) {
        isLoading = false;
        rawResponse = response;
        failureMessage = VVPublicDefines.RequestFailureType.noException.rawValue();
        if (response.getStatus() != null && response.getStatus() == VVURLResponse.VVURLResponseStatus.dataDecodeException) {
            failureMessage = VVPublicDefines.RequestFailureType.serviceDataDecodeError.rawValue();
            faildCallApi(rawResponse, VVPublicDefines.RequestFailureType.serviceDataDecodeError);
            return;
        }

        if (mValidator != null && mValidator.get().getResponseIsCorrect(this,response) != VVPublicDefines.RequestFailureType.noException) {
            // 验证失败
            failureMessage = VVPublicDefines.RequestFailureType.resultNotCorrect.rawValue();
            faildCallApi(rawResponse,VVPublicDefines.RequestFailureType.resultNotCorrect);
            return;
        }

        // 移除该请求
        cancelRequestById(response.getRequestId());

        if (rawResponse.isIsCache() == false) {
            if (mChild != null) {

                VVPublicDefines.CachePolicy cachePolicy = mChild.get().getCachePolicy();
                if (cachePolicy == VVPublicDefines.CachePolicy.memory) {
                    getCacheCenter().saveCacheByMemoryWith(response,
                            mChild.get().getRequestType().toString(),
                            getApiAddress(),
                            response.getRequestParams()
                            ,memoryCacheTime);
                }
                if (cachePolicy == VVPublicDefines.CachePolicy.disk) {
                    getCacheCenter().saveCacheByDiskWith(response,
                            mChild.get().getRequestType().toString(),
                            getApiAddress(),
                            response.getRequestParams(),
                            diskCacheTime);
                }
                if (cachePolicy == VVPublicDefines.CachePolicy.memoryAndDisk) {
                    getCacheCenter().saveCacheByMemoryWith(response,
                            mChild.get().getRequestType().toString(),
                            getApiAddress(),
                            response.getRequestParams(),
                            memoryCacheTime);
                    getCacheCenter().saveCacheByDiskWith(response,
                            mChild.get().getRequestType().toString(),
                            getApiAddress(),
                            response.getRequestParams(),
                            diskCacheTime);
                }
            }
        }
        rawResponse.setLog(VVLog.logApiRequestWithResponse(response,failureMessage));

        beforePerformSuccessWithResponse(rawResponse);
        if (mDelegate != null) {
            mDelegate.get().managerCallApiDidSuccess(this);
        }
        afterPerformSuccessWithResponse(rawResponse);

    }

    // 请求失败
    private void faildCallApi(@Nullable VVURLResponse response, VVPublicDefines.RequestFailureType
            type) {
        isLoading = false;
        failureType = type;

        if (response != null) {
            rawResponse = response;
            // 移除该请求
            cancelRequestById(response.getRequestId());
        }

        if (response.getStatus() == VVURLResponse.VVURLResponseStatus.timeOut) {
            failureType = VVPublicDefines.RequestFailureType.timeOut;
            failureMessage = failureType.rawValue();

        }

        if (response.getStatus() == VVURLResponse.VVURLResponseStatus.netException) {
            failureType = VVPublicDefines.RequestFailureType.netException;
            failureMessage = failureType.rawValue();

        }
        rawResponse.setLog(VVLog.logApiRequestWithResponse(response,failureMessage));


        // 如果服务提供（Service）可以处理该错误，就不向下传递
        if (mService != null && mService.handleFailure(this,response,failureType)) {
            return;
        }

        beforePerformFailureWithResponse(rawResponse);

        if (mDelegate != null) {
            mDelegate.get().managerCallApiManagerFaild(this);
        }

        afterPerformFailureWithResponse(rawResponse);

    }
    // 获取Api
    private String getApiAddress() {

        String serviceAddress = "";
        String apiAddress = "";
        if (mService != null) {
            serviceAddress = mService.getServiceAddress();
        }
        if (mChild != null) {
            apiAddress = mChild.get().getApiAddress();
        }
        return serviceAddress+apiAddress;

    }

    // 网络是否连接
    private boolean getIsHaveNetLink() {

        return VVNetLink.getNetIsAvaliable();
    }

    /**Interceptor methods ***********************************/

    public boolean beforePerformSuccessWithResponse(VVURLResponse response) {

        if (interceptorIsImplement()) {
            return mInterceptor.get().beforePerformSuccessWithResponse(this,response);
        }
        return true;
    }


    public void afterPerformSuccessWithResponse(VVURLResponse response) {
        if (interceptorIsImplement()) {
            mInterceptor.get().afterPerformSuccessWithResponse(this,response);
        }
    }


    public boolean beforePerformFailureWithResponse(VVURLResponse response) {
        if (interceptorIsImplement()) {
           return mInterceptor.get().beforePerformFailureWithResponse(this,response);
        }
        return true;
    }

    public void afterPerformFailureWithResponse(VVURLResponse response) {

        if (interceptorIsImplement()) {
            mInterceptor.get().afterPerformFailureWithResponse(this,response);
        }
    }

    public boolean beforePerformCallApiWithParams(Map<String, String> params) {
        if (interceptorIsImplement()) {
            mInterceptor.get().beforePerformCallApiWithParams(this, params);
        }
        return true;
    }

    public void afterPerformCallApiWithParams(Map<String, String> params) {

        if (interceptorIsImplement()) {
            mInterceptor.get().afterPerformCallApiWithParams(this, params);
        }
    }

    public void didReceivedResponse(VVURLResponse response) {
        if (interceptorIsImplement()) {
            mInterceptor.get().didReceivedResponse(this, response);
        }
    }
    // 无论子类或者其他类实现拦截器接口，必须全部实现，否是就认为没有实现
    private boolean interceptorIsImplement() {
        if (mInterceptor != null && (mInterceptor instanceof ApiManagerInterceptor)) {
            return true;
        }
        return false;
    }

    /**Interceptor methods end***********************************/

    /**Getter Setter methods ***********************************/

    // Setter method
    public void setService(ApiManagerService mService) {
        this.mService = mService;
    }

    public void setChild(WeakReference<ApiManager> mChild) {
        this.mChild = mChild;
    }

    public void setParamSource(WeakReference<ApiManagerParameterSource> mParamSource) {
        this.mParamSource = mParamSource;
    }

    public void setValidator(WeakReference<ApiManagerValidator> mValidator) {
        this.mValidator = mValidator;
    }

    public void setDelegate(WeakReference<ApiManagerResultCallBackDelegate> mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setInterceptor(WeakReference<ApiManagerInterceptor> mInterceptor) {
        this.mInterceptor = mInterceptor;
    }

    public void setShouldIgnoreCache(boolean shouldIgnoreCache) {
        this.shouldIgnoreCache = shouldIgnoreCache;
    }

    public void setRequestOutTime(int requestOutTime) {
        this.requestOutTime = requestOutTime;
    }

    public void setMemoryCacheTime(int memoryCacheTime) {
        this.memoryCacheTime = memoryCacheTime;
    }

    public void setDiskCacheTime(int diskCacheTime) {
        this.diskCacheTime = diskCacheTime;
    }

    // Getter
    public VVCacheCenter getCacheCenter() {
        if (cacheCenter == null) {
            cacheCenter = new VVCacheCenter();
        }
        return cacheCenter;
    }

    public List<Number> getRequestList() {
        if (requestList == null) {
            requestList = new ArrayList<>();
        }
        return requestList;
    }
    /**Getter Setter methods end***********************************/

}
