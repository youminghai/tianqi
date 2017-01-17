package com.haige.taiyangdabo.tianqi.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.haige.taiyangdabo.tianqi.global.Global;
import com.haige.taiyangdabo.tianqi.gson.HeWeather;
import com.haige.taiyangdabo.tianqi.util.HttpUtils;
import com.haige.taiyangdabo.tianqi.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //每次通过startService启动服务时都会调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();//更新天气
        updateBingPic();//更新必应每日一图

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 8 * 60 * 60 * 1000;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新每日一图
     */
    private void updateBingPic() {
        HttpUtils.sendRequestWithHttpUrlConnection(Global.SERVER_BING, new HttpUtils
                .HttpCallBackListener() {


            @Override
            public void onFinish(String response) {
                PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit()
                        .putString("bing_pic", response).apply();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 更新天气
     */
    private void updateWeather() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String wether_data = pref.getString("weather_data", null);
        HeWeather weather = Utility.handlerWeatherResponse(wether_data);
        String weatherId = weather.basic.id;
        String address = Global.SERVER_WEATHER + weatherId + "&key=" + Global
                .KEY;
        HttpUtils.sendRequestWithOKHttp(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                HeWeather heWeather = Utility.handlerWeatherResponse(responseData);
                if (heWeather != null && "ok".equals(heWeather.status)) {
                    PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit()
                            .putString("weather_data", responseData).apply();
                }
            }
        });
    }
}
