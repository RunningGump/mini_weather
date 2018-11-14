package cn.edu.pku.gengzehao.miniweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import cn.edu.pku.gengzehao.app.MyApplication;
import cn.edu.pku.gengzehao.bean.TodayWeather;
import cn.edu.pku.gengzehao.fragment.FirstWeatherFragment;
import cn.edu.pku.gengzehao.fragment.SecondWeatherFragment;
import cn.edu.pku.gengzehao.util.NetUtil;
import cn.edu.pku.gengzehao.adapter.WeatherPagerAdapter;


// 新建一个继承Activity的类
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 定义了一个整型变量UPDATE_TODAY_WEATHER,用于表示更新TodayWeather的动作
    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int UPDATE_LOCATION = 2;


    private ImageView mUpdataBtn;
    private ImageView mCitySelect;
    private ProgressBar mProgressbar;
    private ImageView mTitleLocation;

    // 声明类型为TextView的控件，用来显示各种文本
    private TextView cityTv, wenduTv, timeTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;


    private ImageView weatherImg, pmImg;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private WeatherPagerAdapter mWeatherPagerAdapter;
    private LocationManager locationManager;

    //  声明一个LocationClient类
    public LocationClient mLocationClient=null;
    // 声明并实例化一个MyLocationLister类，监听定位结果，异步获取方式。
    private MyLocationListener myListener = new MyLocationListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过setContentView方法加载布局
        setContentView(R.layout.weather_info);

        // 给更新按钮注册了一个点击事件
        mUpdataBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdataBtn.setOnClickListener(this);

        // 实例化一个LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        // 配置定位SDK参数
        configLocationParameters();


        // 实例化一个更新按钮进度条
        mProgressbar = (ProgressBar) findViewById(R.id.title_update_progress);


        //  检查网络信息
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
        }

        // 实例化一个选择城市按钮，并注册了一个点击事件
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        // 初始化天气信息
        initView();

        //初始化多日天气
        fragments = new ArrayList<Fragment>();
        fragments.add(new FirstWeatherFragment());
        fragments.add(new SecondWeatherFragment());
        mViewPager = (ViewPager) findViewById(R.id.weather_viewpager);
        mWeatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mWeatherPagerAdapter);

        // 实例化一个定位按钮，并注册一个点击事件。
        mTitleLocation = (ImageView) findViewById(R.id.title_location);
        mTitleLocation.setOnClickListener(this);

    }

    /*
    配置定位SDK参数
     */
    private void configLocationParameters(){
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }


    /*
 定义并创建一个Handler实例，用于实现异步消息处理机制：即子线程用于从网络获取天气信息并通过Sendmessage()方法，
 将获取到天气信息传递到UI线程中，在UI线程中更改天气信息。
  */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    ((FirstWeatherFragment) mWeatherPagerAdapter.getItem(0)).updateWeather((TodayWeather) msg.obj);
                    ((SecondWeatherFragment) mWeatherPagerAdapter.getItem(1)).updateWeather((TodayWeather) msg.obj);
                    mProgressbar.setVisibility(View.GONE);
                    mUpdataBtn.setVisibility(View.VISIBLE);
                    break;
//                case UPDATE_LOCATION:
//                    String cityCode = (String) msg.obj;
//                    queryWeatherCode(cityCode);
//                    break;
                default:
                    break;
            }
        }
    };

    private Handler locationHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_LOCATION:
                    if(msg.obj != null){
                        if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORN_NONE) {
                            Log.d("myWeather", "网络OK");
                            queryWeatherCode((String) msg.obj);
                        } else {
                            Log.d("myWeather", "网络挂了");
                            Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
                        }

                    }
                    myListener.cityCode  = null;
                    break;
                default:
                    break;
            }

        }
    };


    // 初始化主界面的天气信息函数
    private void initView(){
            city_name_Tv = (TextView) findViewById(R.id.title_city_name);
            cityTv = (TextView) findViewById(R.id.city);
            wenduTv = (TextView) findViewById(R.id.wendu);
            timeTv = (TextView) findViewById(R.id.time);
            humidityTv = (TextView) findViewById(R.id.humidity);
            weekTv = (TextView) findViewById(R.id.week_today);
            pmDataTv = (TextView) findViewById(R.id.pm_data);
            pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
            pmImg = (ImageView) findViewById(R.id.pm2_5_img);
            temperatureTv = (TextView) findViewById(R.id.temperature);
            climateTv = (TextView) findViewById(R.id.climate);
            windTv = (TextView) findViewById(R.id.wind);
            weatherImg = (ImageView) findViewById(R.id.weather_img);
            city_name_Tv.setText("N/A");
            cityTv.setText("N/A");
            wenduTv.setText("N/A");
            timeTv.setText("N/A");
            humidityTv.setText("N/A");
            pmDataTv.setText("N/A");
            pmQualityTv.setText("N/A");
            weekTv.setText("N/A");
            temperatureTv.setText("N/A");
            climateTv.setText("N/A");
            windTv.setText("N/A");
    }


    // 分别对点击操作设置处理逻辑
    @Override
    public void onClick(View view) {
        /*
         如果触发了选择城市按钮，则：
         通过意图实现从第一个Activity转向第二个Activity
         并且可以通过使用intent.putExtra()函数，将intent中的数据传到下一个Activity。
          */
        if (view.getId() == R.id.title_city_manager){
            Intent intent = new Intent(this, SelectCity.class);
            startActivityForResult(intent, 1);
        }

        /*
        如果触发了更新天气按钮，则：
        从sharedPreferences对象中获取城市代码；
        如果网络OK，则根据城市码来获取该城市天气信息。
         */
        if (view.getId() == R.id.title_update_btn) {
            mUpdataBtn.setVisibility(View.GONE); // 点击更新按钮时更新按钮设置不可见，更新进度条显示出来
            mProgressbar.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("many_city_code", "101060101");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId() == R.id.title_location){
            //  启动定位SDK
            mLocationClient.start();
            Log.d("log", "start location");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (myListener.cityCode == null) {
                            Thread.currentThread().sleep(1000);
                            Log.d("log", "location...");
                        }
                        Log.d("CITYCODE",myListener.cityCode);
                        Message msg = new Message();
                        msg.what = UPDATE_LOCATION;
                        msg.obj = myListener.cityCode;
                        locationHandler.sendMessage(msg);
                        mLocationClient.stop();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }


    /*
    获取其他Activity中传递来的数据。此处，获取SelectCity活动中选择的城市代码
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
                if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }

    }

    // 对爬取的天气信息进行解析，最终返回含有今日天气信息和未来4天天气信息的TodayWeather对象
    private  TodayWeather parseXML(String xmldata) {
        Log.d("xmldata", xmldata);
        TodayWeather todayWeather = null;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac =  XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType){
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null){
                            if (xmlPullParser.getName().equals("city")){
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")){
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")){
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")){
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")){
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")){
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount== 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate1(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount== 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate2(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount== 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate3(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount== 4){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate4(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount== 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount== 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount== 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount== 4){
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh4(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount== 1){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount== 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount== 3){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount== 4){
                                eventType = xmlPullParser.next();
                                todayWeather.setLow4(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 1) {
                                eventType = xmlPullParser.next();
//                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 3) {
                                eventType = xmlPullParser.next();
//                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 4){
                                eventType = xmlPullParser.next();
                                todayWeather.setType2(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 5) {
                                eventType = xmlPullParser.next();
//                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 6){
                                eventType = xmlPullParser.next();
                                todayWeather.setType3(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 7) {
                                eventType = xmlPullParser.next();
//                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount== 8){
                                eventType = xmlPullParser.next();
                                todayWeather.setType4(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 1){
                                eventType = xmlPullParser.next();
//                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 2){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 3){
                                eventType = xmlPullParser.next();
//                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 4){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli2(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 5){
                                eventType = xmlPullParser.next();
//                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 6){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli3(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 7){
                                eventType = xmlPullParser.next();
//                                todayWeather.setFengli1(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount== 8){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli4(xmlPullParser.getText());
                                fengliCount++;
                            }
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }



    /*
     传入城市代码，通过开启新的线程来从网络获取天气信息，
     然后，通过parseXML函数获取天气信息对象todayWeather，
     然后将todayWeather对象赋值给msg.obj，通过Handler将消息发送给主线程UI
      */
    private void queryWeatherCode(String cityCode)  {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;

        Log.d("myWeather", address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);

                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather = parseXML(responseStr);


                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
//                        Thread.currentThread().sleep(2000); // 测试更新进度条
                        mHandler.sendMessage(msg);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();


    }

    // 传入TodayWeather对象，更新今日天气。
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        wenduTv.setText(todayWeather.getWendu()+ "℃");
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度:"+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow().split("\\s+")[1]+"~"+todayWeather.getHigh().split("\\s+")[1]);
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        Toast.makeText(MainActivity.this,"更新成功!",Toast.LENGTH_SHORT).show();
    }

}


