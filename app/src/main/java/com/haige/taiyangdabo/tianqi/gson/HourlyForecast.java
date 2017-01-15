package com.haige.taiyangdabo.tianqi.gson;

/**
 * Created by Administrator on 2017 2017/1/15 15:53.
 * DESC:
 */
public class HourlyForecast {
    Cond cond;//天气状况
    Wind wind;//风力风向
    String date;//时间
    String hum;//相对湿度（%）
    String pop;//降水概率
    String pres;//气压
    String tmp;//温度

    private class Cond {
        String code;//天气状况代码
        String txt;//天气状况描述
    }

    private class Wind {
        String deg;//风向（360度）
        String dir;//风向，东北风
        String sc;//风力，几级
        String spd;//风速（kmph）
    }
}
