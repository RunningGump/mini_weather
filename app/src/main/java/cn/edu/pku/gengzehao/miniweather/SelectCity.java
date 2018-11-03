package cn.edu.pku.gengzehao.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.gengzehao.app.MyApplication;
import cn.edu.pku.gengzehao.bean.City;


public class SelectCity extends Activity implements View.OnClickListener{

    // 声明成员变量
    private ImageView mBackBtn;
    private ListView mlistView;
    private MyApplication myApplication;
    private List<City> mCityList;
    private ArrayList<String> cityNameList;
    private City[] cityArray;
    private String[] cityNameArray;
    private String cityCode = "";
    private TextView current_city_Tv;
    private EditText mEditText;
    private City currentCity;
    private ArrayAdapter<String> adapter; // 适配器
    private ProgressBar progressBar; // 进度条


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 加载布局文件
        setContentView(R.layout.select_city);



        // 获得MyApplication实例，然后，调用实例的getCityList函数获得城市列表
        myApplication = MyApplication.getInstance();
        mCityList = myApplication.getCityList();

        initListViews();
        // 给后退按钮注册了一个事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.search_edit);
        mEditText.addTextChangedListener(mTextwatcher);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        //刚进入界面时，进度条不可见，选择完城市并点击返回按钮后进度条可见。
        progressBar.setVisibility(View.INVISIBLE);
    }

    private TextWatcher mTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.clear();
            filerCityList(s.toString());
//            adapter.addAll(city_String);
            adapter = new ArrayAdapter<String>(
                    SelectCity.this, android.R.layout.simple_list_item_1, cityNameList);
            mlistView.setAdapter(adapter);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void filerCityList(String string){
        cityNameList = new ArrayList<String>();
        // 若输入信息为空，则城市列表仍包含数据库中的全部城市
        if(TextUtils.isEmpty(string)){
            for(City city:mCityList){
                Log.d("filterCity", city.getCity());
                cityNameList.add(city.getCity());
            }
        }

        else{
            for(City city:mCityList){
                if(city.getCity().indexOf(string)!= -1){
                    cityNameList.add(city.getCity());
                }
            }
        }
    }


    // 点击操作的处理逻辑
    @Override
    public void onClick(View v){
        switch (v.getId()){
            /*
            如果点击后退按钮，则通过Intent将在select_city活动中选择的城市代码传递到主Activity中。
            此处的Intent仅仅适用于传递数据，它没有指定任何"意图"。
             */
            case R.id.title_back:

                progressBar.setVisibility(View.VISIBLE);

                Intent i = new Intent();
                // 向主Activity返回城市代码
                i.putExtra("cityCode", currentCity.getNumber());
                setResult(RESULT_OK, i);
                // 销毁当前活动
                finish();
                break;
            default:
                break;
        }
    }

    private void initListViews(){
        // 从城市列表中取出城市名字，并将城市名字列表转化为城市名字数组
        cityNameList = new ArrayList<>();
        for(int i=0; i<mCityList.size();i++){
            cityNameList.add(mCityList.get(i).getCity());
        }

        cityNameArray = cityNameList.toArray(new String[cityNameList.size()]);


        mlistView = (ListView) findViewById(R.id.list_view);

        // 构建适配器对象!!!!!!!!
        adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, cityNameList);

        // 将构建好的适配器对象传递进去
        mlistView.setAdapter(adapter);


        // 为ListView注册了一个监听器，以及设置点击处理逻辑
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String cityName = cityNameList.get(position);
                for(City city:mCityList){
                    if(cityName.equals(city.getCity())){
                        currentCity = city;
                    }
                }


                Toast.makeText(SelectCity.this, "你单击了:" + currentCity.getNumber(), Toast.LENGTH_SHORT).show();
                current_city_Tv = (TextView) findViewById(R.id.title_name);
                current_city_Tv.setText("当前城市：" + currentCity.getCity());
            }

        });
    }
}
