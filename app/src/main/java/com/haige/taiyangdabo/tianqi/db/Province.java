package com.haige.taiyangdabo.tianqi.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017 2017/1/14 16:23.
 * DESC: 表 Province  的模型
 */

public class Province extends DataSupport{
    private int id;//id
    private int provinceCode;//省代号
    private String provinceName;//省名字

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}

