package com.haige.taiyangdabo.tianqi.gson;

/**
 * Created by Administrator on 2017 2017/1/15 12:42.
 * DESC:空气质量
 */
public class AQI {
    public City city;

    public class City {
        public String aqi;//空气质量指数
        public String pm10;//pm10指数
        public String pm25;//pm25指数
        public String qlty;//污染指数
    }
}

