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
            if (response.getmStatus() != null) {
                mState = response.getmStatus().toString();
            }
            if (response.getmRequestUrl() != null) {
                mApiName = response.getmRequestUrl();
            }
            if (response.getmMethod() != null) {
                mRequestType = response.getmMethod();
            }
            if (response.getmRequestParams() != null) {
                mRequestParams = response.getmRequestParams().toString();
            }
            if (response.getmContentString() != null) {
                mDataString = response.getmContentString();
            }
            if (response.ismIsCache()) {
                mIsCache = response.ismIsCache();
            }

            if (response.getmException() != null) {
                mExceptionCause = response.getmException().toString();
                mExceptionMessage = response.getmException().getMessage();
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
