package com.haiyangwang.summer.NetWork;


import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

//
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/* 网络代理层
 * 在该层使用OK HTTP 作为发起网络请求的代理
 * 当离开该层后，就与具体的执行网络操作的底层脱离关系，如果需要更换网络底层库，
 * 只需要修改Proxy即可
 * */
public class VVNetProxy extends Object {

    private static final String TAG = "VVNetProxy";
    private Map<Number, Call> requestMap = new HashMap<>();
    public interface VVNetResponseCallBack {

        void responseSuccess(VVURLResponse response);

        void responseFaild(VVURLResponse response);
    }

    public VVNetProxy() {}
    //single
    private static class ProxyHolder {
        private static  final VVNetProxy instance = new VVNetProxy();
    }
    public static final VVNetProxy getInstance() {
        return ProxyHolder.instance;
    }


    // Get
    public Number getApiRequest(VVRequest request, final VVNetResponseCallBack callBack) {

        return startRequestWith("GET",request.getUrl(),request.getParams(),callBack);
    }

    // Post
    public Number postApiRequest(VVRequest request, final VVNetResponseCallBack callBack) {


        return startRequestWith("POST",request.getUrl(),request.getParams(),callBack);
    }

    public  Number callApi(VVRequest request, final VVNetResponseCallBack callBack) {

       if (request.getRequestType() == VVPublicDefines.ManagerRequestType.GET) {
           return startRequestWith("GET",request.getUrl(),request.getParams(),callBack);
       }
        if (request.getRequestType() == VVPublicDefines.ManagerRequestType.POST) {
            return startRequestWith("POST",request.getUrl(),request.getParams(),callBack);
        }
       return 0;
    }

    /* cancel request call by id*/
    public void cancelRequestById(Number requestIdNum) {

        Call call = requestMap.get(requestIdNum);
        if (call != null) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
        if (requestMap.containsKey(requestIdNum)) {
            requestMap.remove(requestIdNum);
        }
    }

    /* cancel All request */
    public void cancelAllRequest() {
        if (requestMap.size() <= 0) {return;}
        for (Call call: requestMap.values()) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
        requestMap.clear();
    }

    /** 可以在此处随时切换底层网络框架而不必影响上层的实现方式*/
    //get post
    private Number startRequestWith(String method,
                                   String apiAddress,
                                   Map<String, String> params,
                                   final VVNetResponseCallBack callBack) {

        final Map<String,String> requestParams = params;
        final Request request = generatorRequest(method,apiAddress,requestParams);
        OkHttpClient okHttpClient = new OkHttpClient();
        final long startTimeMills = System.currentTimeMillis();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Request faildRequest = call.request();
                VVURLResponse response = new VVURLResponse().responseFaild(null,
                        null,
                        faildRequest.method(),
                        faildRequest.url().toString(),
                        requestParams,
                        faildRequest.hashCode(),
                        e);
                long endTimeMills = System.currentTimeMillis();
                response.setRequestStartTime(startTimeMills);
                response.setRequestEndTime(endTimeMills);
                callBack.responseFaild(response);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Request successRequest = call.request();
                ResponseBody responseBody = response.body();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // request the entire body.
                Buffer buffer = source.getBuffer();
                // clone buffer before reading from it
                String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
                VVURLResponse successResponse = new VVURLResponse().responseSuccess(responseBodyString,
                        responseBody.bytes(),
                        successRequest.method(),
                        successRequest.url().toString(),
                        requestParams,
                        successRequest.hashCode(),
                        VVURLResponse.VVURLResponseStatus.success);
                long endTimeMills = System.currentTimeMillis();
                successResponse.setRequestStartTime(startTimeMills);
                successResponse.setRequestEndTime(endTimeMills);
                callBack.responseSuccess(successResponse);
            }
        });

        Number callIdNumber = (Number) call.hashCode();
        requestMap.put(callIdNumber,call);
        return callIdNumber;
    }


    // 生成Request
    private Request generatorRequest(String method, String apiAddress, Map<String,String> params) {


        if (method == "GET") {
            // 此时apiAddress 已经是完整的地址
            return new Request.Builder().url(apiAddress).get().build();

        }
        if (method == "POST") {
            if (params == null || params.isEmpty()) {
                return new Request.Builder().url(apiAddress).post(new FormBody.Builder().build()).build();
            }
            FormBody.Builder build= new FormBody.Builder();

            for (String key: params.keySet()) {
                build.add(key,params.get(key));
            }
            RequestBody body = build.build();
            return new Request.Builder().url(apiAddress).post(body).build();
        }
        return new Request.Builder().url(apiAddress).get().build();

    }
}
