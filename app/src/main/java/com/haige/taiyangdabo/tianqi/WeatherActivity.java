package com.haige.taiyangdabo.tianqi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haige.taiyangdabo.tianqi.global.Global;
import com.haige.taiyangdabo.tianqi.gson.DailyForecast;
import com.haige.taiyangdabo.tianqi.gson.HeWeather;
import com.haige.taiyangdabo.tianqi.util.HttpUtils;
import com.haige.taiyangdabo.tianqi.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气详情界面
 */
public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private HeWeather weather;
    private TextView tvTitle;
    private TextView tvUpdateTime;
    private TextView tvTmp;
    private TextView tvCond;
    private LinearLayout llForecast;
    private TextView tvForecast;
    private TextView tvAQI;
    private TextView tvPM10;
    private TextView tvPM25;
    private LinearLayout llAQi;
    private TextView tvQlty;
    private TextView tvAir;
    private TextView tvComf;
    private TextView tvCw;
    private TextView tvDrsg;
    private TextView tvFlu;
    private TextView tvSport;
    private TextView tvTrav;
    private TextView tvUv;
    private SharedPreferences pref;
    private TextView tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        setContentView(R.layout.activity_weather);

        tvTitle = (TextView) findViewById(R.id.tv_weather_title);
        tvUpdateTime = (TextView) findViewById(R.id.tv_weather_update_time);

        tvTmp = (TextView) findViewById(R.id.tv_weather_tmp);
        tvCond = (TextView) findViewById(R.id.tv_weather_cond);
        tvWind = (TextView) findViewById(R.id.tv_weather_wind);

        tvForecast = (TextView) findViewById(R.id.tv_forecast);
        llForecast = (LinearLayout) findViewById(R.id.ll_weather_forecast);

        llAQi = (LinearLayout) findViewById(R.id.ll_weather_aqi);
        tvQlty = (TextView) findViewById(R.id.tv_qlty);
        tvAQI = (TextView) findViewById(R.id.tv_aqi);
        tvPM10 = (TextView) findViewById(R.id.tv_pm10);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);

        tvAir = (TextView) findViewById(R.id.tv_air);
        tvComf = (TextView) findViewById(R.id.tv_comf);
        tvCw = (TextView) findViewById(R.id.tv_cw);
        tvDrsg = (TextView) findViewById(R.id.tv_drsg);
        tvFlu = (TextView) findViewById(R.id.tv_flu);
        tvSport = (TextView) findViewById(R.id.tv_sport);
        tvTrav = (TextView) findViewById(R.id.tv_trav);
        tvUv = (TextView) findViewById(R.id.tv_uv);

        pref = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this);
        String weatherData = pref.getString("weather_data", null);
        if (weatherData != null) {
            HeWeather weather = Utility.handlerWeatherResponse(weatherData);
            showWeathre(weather);
        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            //查询天气
            queryWeather(weatherId);
        }
    }

    /**
     * 查询天气
     *
     * @param weatherId 天气id
     */
    public void queryWeather(final String weatherId) {
        String address = Global.SERVER_WEATHER + weatherId + "&key=" + Global
                .KEY;

        Log.d(TAG, "queryWeather: address==" + address);
        HttpUtils.sendRequestWithOKHttp(address, new Callback() {
            //对异常进行处理
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //得到服务器返回的具体数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: weather==" + responseData);

                pref.edit().putString("weather_data", responseData).commit();

                weather = Utility.handlerWeatherResponse(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeathre(weather);
                    }
                });
            }
        });
    }

    private void showWeathre(HeWeather weather) {
        String status = weather.status;//接口状态
        if ("ok".equals(status)) {
            String city = weather.basic.city;//城市名
            String loc = weather.basic.update.loc;//更新时间
            String tmp = weather.now.tmp;
            String txt = weather.now.cond.txt;//天气
            String dir = weather.now.wind.dir;//东风
            String sc = weather.now.wind.sc;//风力
            String spd = weather.now.wind.spd;//风速

            List<DailyForecast> dailyForecastList = weather.daily_forecast;//7--10天预报
            if (dailyForecastList == null) {
                System.out.println("dailyForecastList为null");
            } else {
                System.out.println("dailyForecastList不为null");
            }
            tvTitle.setText(city);
            tvUpdateTime.setText(loc.substring(loc.length() - 5) + " 更新");
            tvTmp.setText(tmp + "°");
            tvCond.setText(txt);
            tvWind.setText(dir+"  风力："+sc+"  风速："+spd+"kmph");
            tvForecast.setText("未来" + dailyForecastList.size() + "天预报");

            for (DailyForecast dailyForecast : dailyForecastList) {
                View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout
                                .item_weather_forecast,
                        llForecast, false);
                TextView tvDate = (TextView) view.findViewById(R.id.tv_weather_date);
                TextView tvCondd = (TextView) view.findViewById(R.id.tv_weather_cond_d);
                TextView tvMaxTmp = (TextView) view.findViewById(R.id.tv_weather_max_tmp);
                TextView tvMinTmp = (TextView) view.findViewById(R.id.tv_weather_min_tmp);
                tvDate.setText(dailyForecast.date);
                tvCondd.setText(dailyForecast.cond.txt_d);
                tvMaxTmp.setText(dailyForecast.tmp.max);
                tvMinTmp.setText(dailyForecast.tmp.min);

                llForecast.addView(view);
            }

            if (weather.aqi != null) {
                tvQlty.setText(weather.aqi.city.qlty);
                tvAQI.setText(weather.aqi.city.aqi);
                tvPM10.setText(weather.aqi.city.pm10);
                tvPM25.setText(weather.aqi.city.pm25);
                llAQi.setVisibility(View.VISIBLE);
            } else {
                llAQi.setVisibility(View.GONE);
            }

            tvAir.setText("空气质量指数:   " + weather.suggestion.air.brf + "\n" + "   " + weather
                    .suggestion.air
                    .txt);
            tvComf.setText("舒适度指数:   " + weather.suggestion.comf.brf + "\n" + "   " + weather
                    .suggestion
                    .comf.txt);
            tvCw.setText("洗车指数:   " + weather.suggestion.cw.brf + "\n" + "   " + weather
                    .suggestion.cw.txt);
            tvDrsg.setText("穿衣指数:   " + weather.suggestion.drsg.brf + "\n" + "   " + weather
                    .suggestion.drsg
                    .txt);
            tvFlu.setText("感冒指数:   " + weather.suggestion.flu.brf + "\n" + "   " + weather
                    .suggestion.flu
                    .txt);
            tvSport.setText("运动指数:   " + weather.suggestion.sport.brf + "\n" + "   " + weather
                    .suggestion
                    .sport.txt);
            tvTrav.setText("旅游指数:   " + weather.suggestion.trav.brf + "\n" + "   " + weather
                    .suggestion.trav
                    .txt);
            tvUv.setText("紫外线指数:   " + weather.suggestion.uv.brf + "\n" + "   " + weather
                    .suggestion.uv
                    .txt);
        }
    }
}
