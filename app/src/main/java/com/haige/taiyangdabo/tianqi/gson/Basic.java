package com.haige.taiyangdabo.tianqi.gson;

/**
 * Created by Administrator on 2017 2017/1/15 12:43.
 * DESC:基本信息
 */
public class Basic {
    public String city;//城市名
    public String cnty;//所属国
    public String id;//城市id
    public String lat;//维度
    public String lon;//经度
    public Update update;//更新时间

    public class Update {
        public String loc;//当地时间
        public String utc;//UTC时间
    }
}
