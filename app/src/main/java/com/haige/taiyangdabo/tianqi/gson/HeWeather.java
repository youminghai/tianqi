package com.haige.taiyangdabo.tianqi.gson;

import com.haige.taiyangdabo.tianqi.db.City;

import java.util.List;

import static android.graphics.Path.Direction.CW;

/**
 * Created by Administrator on 2017 2017/1/15 12:21.
 * DESC:
 */

public class HeWeather {
    public String status;//接口状态
    public AQI aqi;//pm污染
    public Basic basic;//基本信息
    public List<DailyForecast> daily_forecast;//7--10天天气预报
    public List<HourlyForecast> hourly_forecast;//当天每小时天气预报
    public Now now;//实况天气
    public Suggestion suggestion;//生活指数
}
