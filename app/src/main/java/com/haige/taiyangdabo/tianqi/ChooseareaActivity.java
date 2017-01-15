package com.haige.taiyangdabo.tianqi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haige.taiyangdabo.tianqi.db.City;
import com.haige.taiyangdabo.tianqi.db.County;
import com.haige.taiyangdabo.tianqi.db.Province;
import com.haige.taiyangdabo.tianqi.global.Global;
import com.haige.taiyangdabo.tianqi.util.HttpUtils;
import com.haige.taiyangdabo.tianqi.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;

public class ChooseareaActivity extends AppCompatActivity implements AdapterView
        .OnItemClickListener, View.OnClickListener {

    private static final int LEVEL_PROVINCE = 1;//省级数据
    private static final int LEVEL_CITY = 2;//市级数据
    private static final int LEVEL_COUNTY = 3;//县级数据
    private static final String TAG = "ChooseAreaActivity";

    private List<Province> provinceList;//省列表
    private List<City> cityList;//市列表
    private List<County> countyList;//县列表

    private Province selectProvince;//选中的省
    private City selectCity;//选中的市
    private County selectCounty;//选中的县
    private int selectLevel;//选中数据的级别

    private TextView tvTitle;
    private ListView lvArea;
    private Button btnBack;

    private ArrayAdapter<String> adaper;//ListView控件的适配器
    private List<String> dataList = new ArrayList<>();//adapter适配的数据

    private ProgressDialog progressDialog;//模拟数据加载过程的进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(1);
        setContentView(R.layout.activity_choose_area);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnBack = (Button) findViewById(R.id.btn_back);
        lvArea = (ListView) findViewById(R.id.lv_area);


        adaper = new ArrayAdapter<>(ChooseareaActivity.this, android.R.layout
                .simple_list_item_1, dataList);
        lvArea.setAdapter(adaper);

        lvArea.setOnItemClickListener(this);
        btnBack.setOnClickListener(this);

        queryProvince();//从服务器查询省级数据

    }

    /**
     * lvArea的子项点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selectLevel == LEVEL_PROVINCE) {
            selectProvince = provinceList.get(position);
            Toast.makeText(ChooseareaActivity.this, "省级级别", Toast.LENGTH_SHORT).show();
            queryCity();
        } else if (selectLevel == LEVEL_CITY) {
            selectCity = cityList.get(position);
            Toast.makeText(ChooseareaActivity.this, "市级级别", Toast.LENGTH_SHORT).show();
            queryCounty();
        } else if (selectLevel == LEVEL_COUNTY) {
            selectCounty = countyList.get(position);
            Toast.makeText(ChooseareaActivity.this, "县级级别=="+selectCounty.getCountyName(), Toast.LENGTH_SHORT).show();
            queryWeather(selectCounty);
        }
    }

    /**
     * 查询天气
     */
    private void queryWeather(County selectCounty) {
        Log.d(TAG, "queryWeather: ==="+Global.SERVER_WEATHER + selectCounty.getWeatherId() + "&key=" + Global
                .KEY);
        queryFromServer(Global.SERVER_WEATHER + selectCounty.getWeatherId() + "&key=" + Global
                .KEY, "weather");
    }

    /**
     * 优先从本地数据库中查询省级数据，
     * 查询不到再从服务器查询省级数据
     */
    private void queryProvince() {
        tvTitle.setText("中国");
        btnBack.setVisibility(View.INVISIBLE);
        //从本地数据库中查询省级数据
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            //从本地数据库中查询到省级数据
            Log.d(TAG, "本地有省级数据");
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adaper.notifyDataSetChanged();
            lvArea.setSelection(0);
            selectLevel = LEVEL_PROVINCE;
        } else {
            //从本地数据库中查询不到省级数据,则从服务器查询省级数据
            Log.d(TAG, "queryProvince: " + Global.SERVER_ROOT);
            queryFromServer(Global.SERVER_ROOT, "province");
        }
    }

    /**
     * 从服务器查询市级数据
     */
    private void queryCity() {
        tvTitle.setText(selectProvince.getProvinceName());
        btnBack.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId=?", String.valueOf(selectProvince.getProvinceCode
                ())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adaper.notifyDataSetChanged();
            lvArea.setSelection(0);
            selectLevel = LEVEL_CITY;
        } else {
            Log.d(TAG, "queryCity: " + Global.SERVER_ROOT + selectProvince.getProvinceCode());
            queryFromServer(Global.SERVER_ROOT + selectProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 从服务器查询县级数据
     */
    private void queryCounty() {
        tvTitle.setText(selectCity.getCityName());
        btnBack.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityId=?", String.valueOf(selectCity.getCityCode())).find
                (County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adaper.notifyDataSetChanged();
            lvArea.setSelection(0);
            selectLevel = LEVEL_COUNTY;
        } else {
            Log.d(TAG, "queryCounty: " + Global.SERVER_ROOT + selectProvince.getProvinceCode() +
                    "/" +
                    selectCity.getCityCode());
            queryFromServer(Global.SERVER_ROOT + selectProvince.getProvinceCode() + "/" +
                    selectCity.getCityCode(), "county");
        }
    }

    /**
     * 根据type查询不同的数据，省级，市级，县级
     *
     * @param address 网址
     * @param type    查询级别
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();

        HttpUtils.sendRequestWithHttpUrlConnection(address, new HttpUtils.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handlerProvincesResponse(response);
                } else if ("city".equals(type)) {
                    result = Utility.handlerCitiesResponse(response, selectProvince
                            .getProvinceCode());
                } else if ("county".equals(type)) {
                    Log.d(TAG, "处理县级数据");
                    result = Utility.handlerCountiesResponse(response, selectCity
                            .getCityCode());
                } else if ("weather".equals(type)) {
                    Log.d(TAG, "查询天气=="+response);
                    result = Utility.handlerWeatherResponse(response);
                }

                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();//关闭进度条
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                closeProgressDialog();
                Toast.makeText(ChooseareaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 显示一个进度条
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ChooseareaActivity.this);
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (selectLevel == LEVEL_CITY) {
                    queryProvince();
                } else if (selectLevel == LEVEL_COUNTY) {
                    queryCity();
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        if (selectLevel == LEVEL_CITY) {
            queryProvince();
        } else if (selectLevel == LEVEL_COUNTY) {
            queryCity();
        } else if (selectLevel == LEVEL_PROVINCE) {
            finish();
        }
    }
}
