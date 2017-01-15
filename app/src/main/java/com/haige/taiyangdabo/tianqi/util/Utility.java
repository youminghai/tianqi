package com.haige.taiyangdabo.tianqi.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.haige.taiyangdabo.tianqi.db.City;
import com.haige.taiyangdabo.tianqi.db.County;
import com.haige.taiyangdabo.tianqi.db.Province;
import com.haige.taiyangdabo.tianqi.gson.HeWeather;

/**
 * Created by Administrator on 2017 2017/1/14 19:28.
 * DESC:
 */

public class Utility {
    public static final String TAG = "Utility";

    /**
     * 解析服务器返回的省数据，并将其保存在数据库中
     *
     * @param response 服务器返回的省级数据
     * @return 处理省级数据是否成功
     */
    public static boolean handlerProvincesResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的市级数据，并将其保存在数据库中
     *
     * @param response   服务器返回的市级数据
     * @param provinceId 省id
     * @return 处理市级数据是否成功
     */
    public static boolean handlerCitiesResponse(String response, int provinceId) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                City city = new City();
                city.setCityCode(jsonObject.getInt("id"));
                city.setCityName(jsonObject.getString("name"));
                city.setProvinceId(provinceId);
                city.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解析服务器返回的县级数据，并将其保存在数据库中
     *
     * @param response 服务器返回的县级数据
     * @param cityId   市级id
     * @return 处理服务器返回的市级数据是否成功
     */
    public static boolean handlerCountiesResponse(String response, int cityId) {
        try {
            Log.d(TAG, "handlerCountiesResponse: " + response);
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                County county = new County();
                county.setCountyCode(jsonObject.getInt("id"));
                county.setCountyName(jsonObject.getString("name"));
                county.setWeatherId(jsonObject.getString("weather_id"));
                county.setCityId(cityId);
                county.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean handlerWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            JSONObject heWeather = jsonArray.getJSONObject(0);
            Gson gson=new Gson();
            HeWeather weather = gson.fromJson(heWeather.toString(), HeWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
