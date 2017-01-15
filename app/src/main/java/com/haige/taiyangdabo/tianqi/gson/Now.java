package com.haige.taiyangdabo.tianqi.gson;

import java.util.List;

/**
 * Created by Administrator on 2017 2017/1/15 15:31.
 * DESC:实况天气
 */
public class Now {
    String status;//接口状态
    String fl;//体感温度
    String hum;//相对湿度（%）
    String pcpn;//降水量（mm）
    String pres;//气压
    String tmp;//温度
    String vis;//能见度（km）

    Cond cond;//天气状况
    Wind wind;//风力风速

    //天气状况
    private class Cond {
        String code;//天气状况代码
        String txt;//天气状况描述
    }

    //风力风向
    private class Wind {
        String deg;//风向（360度）
        String dir;//风向，东南风
        String sc;//风力，几级
        String spd;//风速（kmph）
    }
}
