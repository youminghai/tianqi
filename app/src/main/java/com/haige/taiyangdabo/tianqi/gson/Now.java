package com.haige.taiyangdabo.tianqi.gson;

import java.util.List;

/**
 * Created by Administrator on 2017 2017/1/15 15:31.
 * DESC:实况天气
 */
public class Now {
    public String status;//接口状态
    public String fl;//体感温度
    public String hum;//相对湿度（%）
    public String pcpn;//降水量（mm）
    public String pres;//气压
    public String tmp;//温度
    public String vis;//能见度（km）

    public Cond cond;//天气状况
    public Wind wind;//风力风速

    //天气状况
    public class Cond {
        public String code;//天气状况代码
        public String txt;//天气状况描述
    }

    //风力风向
    public class Wind {
        public String deg;//风向（360度）
        public String dir;//风向，东南风
        public String sc;//风力，几级
        public String spd;//风速（kmph）
    }
}
