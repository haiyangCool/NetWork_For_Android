package com.haiyangwang.summer.NetWork;

public class VVLog extends Object {

    private static String mState = null;
    private static String mExceptionType = null;
    private static String mApiName = null;
    private static String mRequestType = null;
    private static String mRequestParams = null;
    private static String mDataString = null;
    private static Boolean mIsCache = false;
    private static String mExceptionCause = null;
    private static String mExceptionMessage = null;



    public static String logApiRequestWithResponse(VVURLResponse response, String apiManagerExceptionMessage) {
        StringBuilder builder = new StringBuilder();
        builder.append("VVLog:*****************************************************  \n");
        if (response != null) {
            if (response.getStatus() != null) {
                mState = response.getStatus().toString();
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
            if (response.isIsCache()) {
                mIsCache = response.isIsCache();
            }

            if (response.getException() != null) {
                mExceptionCause = response.getException().toString();
                mExceptionMessage = response.getException().getMessage();
            }

            mExceptionType = apiManagerExceptionMessage;
            builder.append("VV Service Response State: == " + mState + ".\n");
            builder.append("VV Response Exception by Api Manager Define: == " + mExceptionType + ".\n");
            if (mIsCache == true) {
                builder.append("VV Request Response isCache == " + mIsCache + ", 加载缓存，未进行网络请求"+ ".\n");
            }else {
                builder.append("VV Request Response isCache == " + mIsCache + ".\n");
            }
            builder.append("VV Request Service Exception cause == " + mExceptionCause + ".\n");
            builder.append("VV Request Service Exception message == " + mExceptionMessage + ".\n");
            builder.append("VV Request Url address == " + mApiName + ".\n");
            builder.append("VV Request type == " + mRequestType + ".\n");
            builder.append("VV Request Params == " + mRequestParams + ".\n");
            builder.append("VV Request Response DataStr == " + mDataString + ".\n");


            builder.append("VV Log end**************************************************************");


        }
        return new String(builder);
    }

}
