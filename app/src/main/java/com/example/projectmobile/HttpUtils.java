package com.example.projectmobile;
import com.loopj.android.http.*;
public class HttpUtils {
    private static final String BASE_URL = "http://courses.uit.edu.vn/";
    public  static  void setMaxRetriesAndTimeout(){
        client.setMaxRetriesAndTimeout(5,1000);
    }
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void renew()
    {
        client = new AsyncHttpClient();
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void cancelRequest(){
        client.cancelAllRequests(false);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
