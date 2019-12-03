package com.haiyangwang.summer.NetWork;

import android.util.Log;


public class VVLog extends Object {

    private static final String TAG  = "VVLog";
    private static String mState = null;
    private static String mExceptionType = null;
    private static String mApiName = null;
    private static String mRequestType = null;
    private static String mRequestParams = null;
    private static String mDataString = null;
    private static Boolean mIsCache = false;
    private static String mExceptionCause = null;
    private static String mExceptionMessage = null;
    private static String mRequestStartTime = null;
    private static String mRequestEndTime = null;

    public static String logApiRequestWithResponse(VVURLResponse response, String apiManagerExceptionMessage) {

        StringBuilder builder = new StringBuilder();
        builder.append("VVLog:*****************************************************  \n");
        if (response != null) {

            mIsCache = response.isIsCache();

            if (response.getStatus() != null) {
                mState = response.getStatus().rawValue();
            }
            if (response.getRequestUrl() != null) {
                mApiName = response.getRequestUrl();
            }
            if (response.getMethod() != null) {
                mRequestType = response.getMethod();
            }
            if (response.getRequestParams() != null) {
                mRequestParams = response.getRequestParams().toString();
            }
            if (response.getContentString() != null) {
                mDataString = response.getContentString();
            }

            if (response.getException() != null) {
                mExceptionCause = response.getException().toString();
                mExceptionMessage = response.getException().getMessage();
            }

            long startTime = response.getRequestStartTime();
            long endTime = response.getRequestEndTime();
            long minusTime = endTime - startTime;

            mRequestStartTime = startTime + "（毫秒）";
            mRequestEndTime = endTime + "（毫秒）";
            String mMinusTime = minusTime + "（毫秒）";

            mExceptionType = apiManagerExceptionMessage;

            builder.append("VV：响应状态:\t" + mState + ".\n");
            builder.append("VV：自定义异常类型:\t" + mExceptionType + ".\n");
            if (mIsCache == true) {
                mRequestStartTime = "0 （毫秒）";
                mRequestEndTime = "0 （毫秒）";
                mMinusTime = "0 （毫秒）";
                builder.append("VV：是否加载的缓存:\t" + mIsCache + ", 加载缓存，不进行网络请求"+ ".\n");
            }else {
                builder.append("VV：是否加载的缓存:\t" + mIsCache + ".\n");
            }
            builder.append("VV：网络服务异常发生原因:\t" + mExceptionCause + ".\n");
            builder.append("VV：网络服务异常信息:\t" + mExceptionMessage + ".\n");
            builder.append("VV：Api地址:\t" + mApiName + ".\n");
            builder.append("VV：请求类型:\t" + mRequestType + ".\n");
            if (mRequestType.equals("GET")) {
                if (mIsCache) {
                    builder.append("VV：请求参数【加载缓存,无需网络请求,不拼接】:\t" + mRequestParams + ".\n");

                }else {
                    builder.append("VV：请求参数【参数拼接到URL】:\t" + mRequestParams + ".\n");
                }
            }
            if (mRequestType.equals("POST")) {
                builder.append("VV：请求参数【参数需放入请求体】:\t" + mRequestParams + ".\n");
            }
            builder.append("VV：请求开始时间:\t" + mRequestStartTime + ".\n" );
            builder.append("VV：请求结束时间:\t" + mRequestEndTime + ".\n" );
            builder.append("VV：请求用时:\t" + mMinusTime + ".\n" );

            builder.append("VV：服务器原始数据:\t" + mDataString + ".\n");

        }
        builder.append("VV Log end**************************************************************");

        return new String(builder);
    }

}
