package com.haige.taiyangdabo.tianqi.gson;

/**
 * Created by Administrator on 2017 2017/1/15 15:44.
 * DESC:
 */
public class DailyForecast {
    public Astro astro;//天文数值
    public Cond cond;//天气状况
    public Tmp tmp;//温度
    public Wind wind;//风力风向
    public String date;//预报日期
    public String hum;//相对湿度（%）
    public String pcpn;//降水量（mm)
    public String pop;//降水概率
    public String pres;//气压
    public String uv;//
    public String vis;//能见度

    //天文数值
    public class Astro {
        String mr;//月升时间
        String ms;//月落时间
        String sr;//日出时间
        String ss;//日落时间
    }

    //天气状况
    public class Cond {
        public String code_d;//白天天气状况代码
        public String code_n;//夜间天气状况代码
        public String txt_d;//白天天气状况描述
        public String txt_n;//夜间天气状况描述
    }

    //温度
    public class Tmp {
        public String max;//最高温度
        public String min;//最低温度
    }

    //风力风向
    public class Wind {
        public String deg;//风向（360度）
        public String dir;//风向，北风
        public String sc;//风力等级
        public String spd;//风速（kmph）
    }
}
