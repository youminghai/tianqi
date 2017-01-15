package com.haige.taiyangdabo.tianqi.gson;


/**
 * Created by Administrator on 2017 2017/1/15 12:46.
 * DESC:生活指数
 */
public class Suggestion {
    Air air;//
    Comf comf;//舒适度指数
    CW cw;//洗车指数
    Drsg drsg;//穿衣指数
    Flu flu;//感冒指数
    Sport sport;//运动指数
    Trav trav;//旅游指数
    UV uv;//紫外线指数

    private class Air {
        String brf;//简介
        String txt;//详细描述
    }

    private class Comf {
        String brf;
        String txt;
    }


    private class CW {
        String brf;
        String txt;
    }

    private class Drsg {
        String brf;
        String txt;
    }

    private class Flu {
        String brf;
        String txt;
    }

    private class Sport {
        String brf;
        String txt;
    }

    private class Trav {
        String brf;
        String txt;
    }

    private class UV {
        String brf;
        String txt;
    }
}
