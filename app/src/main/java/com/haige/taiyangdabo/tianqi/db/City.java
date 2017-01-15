package com.haige.taiyangdabo.tianqi.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017 2017/1/14 17:51.
 * DESC: 表 City  的模型
 */

public class City extends DataSupport{
    private int id;
    private int cityCode;
    private String cityName;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
