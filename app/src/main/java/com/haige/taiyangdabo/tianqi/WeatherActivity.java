package com.haige.taiyangdabo.tianqi;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.haige.taiyangdabo.tianqi.fragment.ChooseAreaWeatherFragment;
import com.haige.taiyangdabo.tianqi.global.Global;
import com.haige.taiyangdabo.tianqi.gson.DailyForecast;
import com.haige.taiyangdabo.tianqi.gson.HeWeather;
import com.haige.taiyangdabo.tianqi.util.HttpUtils;
import com.haige.taiyangdabo.tianqi.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 天气详情界面
 */
public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WeatherActivity";
    private HeWeather weather;
    private TextView tvTitle;
    private TextView tvUpdateTime;
    private TextView tvTmp;
    private TextView tvCond;
    private LinearLayout llForecast;
    private TextView tvForecast;
    private TextView tvAQI;
    private TextView tvPM10;
    private TextView tvPM25;
    private LinearLayout llAQi;
    private TextView tvQlty;
    private TextView tvAir;
    private TextView tvComf;
    private TextView tvCw;
    private TextView tvDrsg;
    private TextView tvFlu;
    private TextView tvSport;
    private TextView tvTrav;
    private TextView tvUv;
    private SharedPreferences pref;
    private TextView tvWind;
    private ImageView ivBing;//每日必应一图
    public SwipeRefreshLayout swipeRefreshLayout;
    private String weatherId;
    private Button btnHome;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        setContentView(R.layout.activity_weather);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnHome = (Button) findViewById(R.id.btn_home);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ivBing = (ImageView) findViewById(R.id.iv_bing);
        tvTitle = (TextView) findViewById(R.id.tv_weather_title);
        tvUpdateTime = (TextView) findViewById(R.id.tv_weather_update_time);

        tvTmp = (TextView) findViewById(R.id.tv_weather_tmp);
        tvCond = (TextView) findViewById(R.id.tv_weather_cond);
        tvWind = (TextView) findViewById(R.id.tv_weather_wind);

        tvForecast = (TextView) findViewById(R.id.tv_forecast);
        llForecast = (LinearLayout) findViewById(R.id.ll_weather_forecast);

        llAQi = (LinearLayout) findViewById(R.id.ll_weather_aqi);
        tvQlty = (TextView) findViewById(R.id.tv_qlty);
        tvAQI = (TextView) findViewById(R.id.tv_aqi);
        tvPM10 = (TextView) findViewById(R.id.tv_pm10);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);

        tvAir = (TextView) findViewById(R.id.tv_air);
        tvComf = (TextView) findViewById(R.id.tv_comf);
        tvCw = (TextView) findViewById(R.id.tv_cw);
        tvDrsg = (TextView) findViewById(R.id.tv_drsg);
        tvFlu = (TextView) findViewById(R.id.tv_flu);
        tvSport = (TextView) findViewById(R.id.tv_sport);
        tvTrav = (TextView) findViewById(R.id.tv_trav);
        tvUv = (TextView) findViewById(R.id.tv_uv);

        pref = PreferenceManager
                .getDefaultSharedPreferences(WeatherActivity.this);
        final String weatherData = pref.getString("weather_data", null);
        String bing_pic = pref.getString("bing_pic", null);

        weatherId = getIntent().getStringExtra("weather_id");

        btnHome.setOnClickListener(this);

        if (bing_pic != null) {
            Glide.with(WeatherActivity.this).load(bing_pic).into(ivBing);
        } else {
            loadBingPic();
        }

        if (weatherData != null) {
            HeWeather weather = Utility.handlerWeatherResponse(weatherData);
            showWeathre(weather);
        } else {
            //查询天气
            queryWeather(weatherId);
        }


        //设置下拉刷新时，环状进度条的颜色渐变
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);
        //设置下拉多少像素时才会触发下拉刷新
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        //设置下拉刷新时，环状进度条的背景颜色
        //swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor
        // (android.R.color.transparent));
        //设置下拉刷新时，环状进度条的大小
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        //下拉刷新的监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //更新天气
                //weatherId=getIntent().getStringExtra("weather_id");
                Log.d(TAG, "weatherid====" + weatherId);
                queryWeather(weatherId);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        HttpUtils.sendRequestWithOKHttp(Global.SERVER_BING, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                pref.edit().putString("bing_pic", bingPic).apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(ivBing);
                    }
                });
            }
        });
    }

    /**
     * 查询天气
     *
     * @param weatherId 天气id
     */
    public void queryWeather(final String weatherId) {
        loadBingPic();

        String address = Global.SERVER_WEATHER + weatherId + "&key=" + Global
                .KEY;

        Log.d(TAG, "queryWeather: address==" + address);
        HttpUtils.sendRequestWithOKHttp(address, new Callback() {
            //对异常进行处理
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();

                //停止刷新，没有改行代码环状进度条将会一直存在
                swipeRefreshLayout.setRefreshing(false);
            }

            //得到服务器返回的具体数据
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: weather==" + responseData);

                pref.edit().putString("weather_data", responseData).commit();

                weather = Utility.handlerWeatherResponse(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeathre(weather);
                        //停止刷新，没有改行代码环状进度条将会一直存在
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeathre(HeWeather weather) {
        String status = weather.status;//接口状态
        Toast.makeText(WeatherActivity.this, status, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showWeathre: 接口状态==000" + status);
        if ("ok".equals(status)) {
            String city = weather.basic.city;//城市名
            String loc = weather.basic.update.loc;//更新时间
            String tmp = weather.now.tmp;//实时温度
            String txt = weather.now.cond.txt;//天气
            String dir = weather.now.wind.dir;//东风
            String sc = weather.now.wind.sc;//风力
            String spd = weather.now.wind.spd;//风速

            List<DailyForecast> dailyForecastList = weather.daily_forecast;//7--10天预报
            if (dailyForecastList == null) {
                System.out.println("dailyForecastList为null");
            } else {
                System.out.println("dailyForecastList不为null");
            }
            tvTitle.setText(city);
            tvUpdateTime.setText(loc.substring(loc.length() - 5) + " 更新");
            tvTmp.setText(tmp + "°");
            tvCond.setText(txt);
            tvWind.setText(dir + "  风力：" + sc + "  风速：" + spd + "kmph");
            tvForecast.setText("未来" + dailyForecastList.size() + "天预报");


            llForecast.removeAllViews();
            for (DailyForecast dailyForecast : dailyForecastList) {
                View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout
                                .item_weather_forecast,
                        llForecast, false);
                TextView tvDate = (TextView) view.findViewById(R.id.tv_weather_date);
                TextView tvCondd = (TextView) view.findViewById(R.id.tv_weather_cond_d);
                TextView tvMaxTmp = (TextView) view.findViewById(R.id.tv_weather_max_tmp);
                TextView tvMinTmp = (TextView) view.findViewById(R.id.tv_weather_min_tmp);
                tvDate.setText(dailyForecast.date);
                tvCondd.setText(dailyForecast.cond.txt_d);
                tvMaxTmp.setText(dailyForecast.tmp.max + "°");
                tvMinTmp.setText(dailyForecast.tmp.min + "°");

                llForecast.addView(view);
            }

            if (weather.aqi != null) {
                tvQlty.setText(weather.aqi.city.qlty);
                tvAQI.setText(weather.aqi.city.aqi);
                tvPM10.setText(weather.aqi.city.pm10);
                tvPM25.setText(weather.aqi.city.pm25);
                llAQi.setVisibility(View.VISIBLE);
            } else {
                llAQi.setVisibility(View.GONE);
            }

            tvAir.setText("空气质量指数:   " + weather.suggestion.air.brf + "\n" + "   " + weather
                    .suggestion.air
                    .txt);
            tvComf.setText("舒适度指数:   " + weather.suggestion.comf.brf + "\n" + "   " + weather
                    .suggestion
                    .comf.txt);
            tvCw.setText("洗车指数:   " + weather.suggestion.cw.brf + "\n" + "   " + weather
                    .suggestion.cw.txt);
            tvDrsg.setText("穿衣指数:   " + weather.suggestion.drsg.brf + "\n" + "   " + weather
                    .suggestion.drsg
                    .txt);
            tvFlu.setText("感冒指数:   " + weather.suggestion.flu.brf + "\n" + "   " + weather
                    .suggestion.flu
                    .txt);
            tvSport.setText("运动指数:   " + weather.suggestion.sport.brf + "\n" + "   " + weather
                    .suggestion
                    .sport.txt);
            tvTrav.setText("旅游指数:   " + weather.suggestion.trav.brf + "\n" + "   " + weather
                    .suggestion.trav
                    .txt);
            tvUv.setText("紫外线指数:   " + weather.suggestion.uv.brf + "\n" + "   " + weather
                    .suggestion.uv
                    .txt);
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败,接口不ok", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                drawerLayout.openDrawer(GravityCompat.START);
                /*ChooseAreaWeatherFragment chooseAreaWeatherFragment = (ChooseAreaWeatherFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_choose_area);
                chooseAreaWeatherFragment.queryProvince();
                chooseAreaWeatherFragment.selectLevel=1;
*/
                break;
            default:
        }
    }
}
