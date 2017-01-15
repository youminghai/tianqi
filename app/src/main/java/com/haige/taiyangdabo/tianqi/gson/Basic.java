package com.haige.taiyangdabo.tianqi.gson;

/**
 * Created by Administrator on 2017 2017/1/15 12:43.
 * DESC:基本信息
 */
public class Basic {
    String city;//城市名
    String cnty;//所属国
    String id;//城市id
    String lat;//维度
    String lon;//经度
    Update update;//更新时间

    class Update {
        String loc;//当地时间
        String utc;//UTC时间
    }
}
