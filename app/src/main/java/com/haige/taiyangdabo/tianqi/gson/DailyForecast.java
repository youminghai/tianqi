package com.haige.taiyangdabo.tianqi.gson;

import android.view.Window;

/**
 * Created by Administrator on 2017 2017/1/15 15:44.
 * DESC:
 */
public class DailyForecast {
    Astro astro;//天文数值
    Cond cond;//天气状况
    Tmp tmp;//温度
    Wind wind;//风力风向
    String date;//预报日期
    String hum;//相对湿度（%）
    String pcpn;//降水量（mm)
    String pop;//降水概率
    String pres;//气压
    String uv;//
    String vis;//能见度

    //天文数值
    private class Astro {
        String mr;//月升时间
        String ms;//月落时间
        String sr;//日出时间
        String ss;//日落时间
    }

    //天气状况
    private class Cond {
        String code_d;//白天天气状况代码
        String code_n;//夜间天气状况代码
        String txt_d;//白天天气状况描述
        String txt_n;//夜间天气状况描述
    }

    //温度
    private class Tmp {
        String max;//最高温度
        String min;//最低温度
    }

    //风力风向
    private class Wind {
        String deg;//风向（360度）
        String dir;//风向，北风
        String sc;//风力等级
        String spd;//风速（kmph）
    }
}
