package com.haige.taiyangdabo.tianqi.gson;

import com.haige.taiyangdabo.tianqi.db.City;

import java.util.List;

import static android.graphics.Path.Direction.CW;

/**
 * Created by Administrator on 2017 2017/1/15 12:21.
 * DESC:
 */

public class HeWeather {
    String status;//接口状态
    AQI aqi;//pm污染
    Basic basic;//基本信息
    List<DailyForecast> dailyForecastList;//7--10天天气预报
    List<HourlyForecast> hourlyForecastList;//当天每小时天气预报
    Now now;//实况天气
    Suggestion suggestion;//生活指数
}
