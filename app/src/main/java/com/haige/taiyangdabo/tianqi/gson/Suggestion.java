package com.haige.taiyangdabo.tianqi.gson;


/**
 * Created by Administrator on 2017 2017/1/15 12:46.
 * DESC:生活指数
 */
public class Suggestion {
    public Air air;//
    public Comf comf;//舒适度指数
    public CW cw;//洗车指数
    public Drsg drsg;//穿衣指数
    public Flu flu;//感冒指数
    public Sport sport;//运动指数
    public Trav trav;//旅游指数
    public UV uv;//紫外线指数

    public class Air {
        public String brf;//简介
        public String txt;//详细描述
    }

    public class Comf {
        public String brf;
        public String txt;
    }


    public class CW {
        public String brf;
        public String txt;
    }

    public class Drsg {
        public String brf;
        public String txt;
    }

    public class Flu {
        public String brf;
        public String txt;
    }

    public class Sport {
        public String brf;
        public String txt;
    }

    public class Trav {
        public String brf;
        public String txt;
    }

    public class UV {
        public String brf;
        public String txt;
    }
}
