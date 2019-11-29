package com.haiyangwang.summer.NetWork;

import android.util.Log;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Map;

public class VVURLResponse extends Object {

    /** 作为api请求的直接数据接收者，这里不考虑返回的数据是否为空或者可用，
     * */
    public enum VVURLResponseStatus {
        // 除了网络请求超时，其他的异常全部当做网络异常来处理
        success,
        timeOut,
        netException,
        dataDecodeException
    }

    // response状态
    private VVURLResponseStatus mStatus;
    private int mRequestId;
    // 服务器返回的数据JSON字符串
    private String mContentString;
    // Data
    private byte[] mResponseData;
    // method get\post etc.
    private String mMethod;
    // request url
    private String mRequestUrl;
    // request body
    private Map<String,String> mRequestParams;
    // 服务器数据
    private JSONObject mContent;
    // 错误
    private Exception mException;
    // 是否为缓存数据
    private boolean mIsCache;
    // log
    private String mLog = "";


    public VVURLResponse() {}
    // response Success
    public VVURLResponse responseSuccess(String responseString,
                                         byte[] data,
                                         String method,
                                         String requestUrl,
                                         Map<String,String> requestParams,
                                         int requestId,
                                         VVURLResponseStatus status) {

        this.mContentString = responseString;
        this.mResponseData = data;
        this.mMethod = method;
        this.mRequestUrl = requestUrl;
        this.mRequestParams = requestParams;
        this.mRequestId = requestId;
        this.mException = null;
        this.mIsCache = false;
        this.mStatus = status;
        if (data != null) {
            try {
                String objStr = new String(data);
                JSONObject jsonObject = new JSONObject(objStr);
                this.mContent = jsonObject;
            }catch (Exception e) {
                Log.i("数据解析","Error");
                this.mStatus = VVURLResponseStatus.dataDecodeException;
            }
        }


        return this;

    }

    // response Faild
    public VVURLResponse responseFaild(String responseString,
                                       Object data,
                                       String method,
                                       String requestUrl,
                                       Map<String,String> requestParams,
                                       int resultId,
                                       Exception exception) {

        this.mContentString = "";
        this.mContent = null;
        this.mResponseData = null;
        this.mMethod = method;
        this.mRequestUrl = requestUrl;
        this.mRequestParams = requestParams;
        this.mRequestId = resultId;
        this.mIsCache = false;
        this.mException = exception;
        this.mStatus =  expectionStatus(exception);
        return this;

    }

    public VVURLResponse transformStrignToResponse(String dataStr) {

        this.mContentString = dataStr;
        this.mMethod = "";
        this.mRequestUrl = "";
        this.mRequestParams = null;
        this.mRequestId = 0;
        this.mIsCache = true;
        this.mException = null;
        this.mStatus = VVURLResponseStatus.success;
        if (dataStr != null) {

            try {
                this.mResponseData = dataStr.getBytes();

                JSONObject jsonObject = new JSONObject(dataStr);
                this.mContent = jsonObject;
            }catch (Exception e) {
                Log.i("数据转化失败","Error");
                this.mStatus = VVURLResponseStatus.dataDecodeException;

            }
        }
        return this;
    }
        // transform cache data to response
    public VVURLResponse transformToResponse(byte[] data) {

        this.mResponseData = data;
        this.mRequestId = 0;
        if (data != null) {
            try {
                String objStr = new String(data);
                this.mContentString = objStr;
                JSONObject jsonObject = new JSONObject(objStr);
                this.mStatus = VVURLResponseStatus.success;
                this.mContent = jsonObject;
            }catch (Exception e) {
                Log.i("数据解析","Error");
                this.mStatus = VVURLResponseStatus.dataDecodeException;
            }
        }
        this.mIsCache = true;
        return this;
    }


    private VVURLResponseStatus expectionStatus(Exception exception) {


        if (exception == null) { return  VVURLResponseStatus.success;}

        if (exception.equals(SocketTimeoutException.class)) {
            return  VVURLResponseStatus.timeOut;
        }
        return VVURLResponseStatus.netException;
    }

    /**gettter*/
    public VVURLResponseStatus getStatus() {
        return mStatus;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public String getContentString() {
        return mContentString;
    }

    public byte[] getmResponseData() {
        return mResponseData;
    }

    public String getMethod() {
        return mMethod;
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public Map<String, String> getRequestParams() {
        return mRequestParams;
    }

    public JSONObject getmContent() {
        return mContent;
    }

    public Exception getException() {
        return mException;
    }

    public String getLog() {
        return mLog;
    }

    public boolean isIsCache() {
        return mIsCache;
    }

    public void setIsCache(boolean mIsCache) {
        this.mIsCache = mIsCache;
    }

    public void setRequestUrl(String mRequestUrl) {
        this.mRequestUrl = mRequestUrl;
    }

    public void setRequestParams(Map<String, String> mRequestParams) {
        this.mRequestParams = mRequestParams;
    }

    public void setMethod(String mMethod) {
        this.mMethod = mMethod;
    }

    public void setLog(String mLog) {
        this.mLog = mLog;
    }
}
