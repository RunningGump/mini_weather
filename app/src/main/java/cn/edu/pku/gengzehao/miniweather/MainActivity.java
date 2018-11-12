package cn.edu.pku.gengzehao.miniweather;

import android.content.Intent;
import android.content.SharedPreferences;
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


import cn.edu.pku.gengzehao.bean.TodayWeather;
import cn.edu.pku.gengzehao.fragment.FirstWeatherFragment;
import cn.edu.pku.gengzehao.fragment.SecondWeatherFragment;
import cn.edu.pku.gengzehao.util.NetUtil;
import cn.edu.pku.gengzehao.adapter.WeatherPagerAdapter;


// 新建一个继承Activity的类
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // 定义了一个整型变量UPDATE_TODAY_WEATHER,用于表示更新TodayWeather的动作
    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int UPDATE_OTHER_WEATHER = 2;



    private ImageView mUpdataBtn;
    private ImageView mCitySelect;
    private ProgressBar mProgressbar;

    // 定义类型为TextView的私有变量
    private TextView cityTv, wenduTv, timeTv, humidityTv, weekTv, pmDataTv,
            pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;

    private ImageView weatherImg, pmImg;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private WeatherPagerAdapter mWeatherPagerAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过setContentView方法加载布局
        setContentView(R.layout.weather_info);
        // 给更新按钮注册了一个点击事件
        mUpdataBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdataBtn.setOnClickListener(this);


        // 给更新按钮进度条显示不可见
        mProgressbar = (ProgressBar) findViewById(R.id.title_update_progress);
//        mProgressbar.setVisibility(View.GONE);

        //  检查网络信息
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
        }

        // 给城市选择按钮注册了一个点击事件
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


                default:
                    break;
            }
        }
    };

//    private Handler otherWeatherHandler = new Handler(){
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case UPDATE_OTHER_WEATHER:
//                    ((FirstWeatherFragment) mWeatherPagerAdapter.getItem(0)).updateWeather((List<OtherWeather>) msg.obj);
//                    ((SecondWeatherFragment) mWeatherPagerAdapter.getItem(1)).updateWeather((List<OtherWeather>) msg.obj);
//                    break;
//
//                default:
//                    break;
//            }
//        }
//
//    };

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
            String cityCode = sharedPreferences.getString("many_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
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
//                    parseXML(responseStr);
                    todayWeather = parseXML(responseStr);


                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());

                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
//                        Thread.currentThread().sleep(2000); // 测试更新进度条
                        mHandler.sendMessage(msg);
                    }

//                    XMLPullParserHandler parser = new XMLPullParserHandler();
//                    otherWeathers = parser.parse(in);
//                    if (otherWeathers != null) {
//                        Log.d("myWeather", otherWeathers.toString());
//
//                        Message msg =new Message();
//                        msg.what = UPDATE_OTHER_WEATHER;
//                        msg.obj=otherWeathers;
////                        Thread.currentThread().sleep(2000); // 测试更新进度条
//                        mHandler.sendMessage(msg);
//                    }

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

    // 传入今日天气对象，然后在主界面更新今日天气。
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


