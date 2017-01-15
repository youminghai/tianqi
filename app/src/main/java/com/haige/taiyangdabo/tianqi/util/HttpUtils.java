package com.haige.taiyangdabo.tianqi.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017 2017/1/14 18:55.
 * DESC:
 */

public class HttpUtils {
    public static final String TAG = "HttpUtils";

    /**
     * 使用HttpUrlConnection发送Http请求
     *
     * @param url      网址
     * @param listener 获取服务器数据后会回调该监听器
     */
    public static void sendRequestWithHttpUrlConnection(final String url, final
    HttpCallBackListener
            listener) {
        new Thread(new Runnable() {

            private HttpURLConnection connection;

            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    connection = (HttpURLConnection) url1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (response != null) {
                        listener.onFinish(response.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();//创建url异常
                    Log.e(TAG, "创建url异常");
                } catch (IOException e) {
                    e.printStackTrace();//openConnection异常
                    Log.e(TAG, "openConnection异常");
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public interface HttpCallBackListener {
        void onFinish(String response);

        void onError(Exception e);
    }

    /**
     * 使用OKHttp发送http请求
     *
     * @param url      网址
     * @param callback OKHttp自带的回调接口
     */
    public static void sendRequestWithOKHttp(String url, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
