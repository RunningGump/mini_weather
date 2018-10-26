package cn.edu.pku.gengzehao.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.gengzehao.app.MyApplication;
import cn.edu.pku.gengzehao.bean.City;


public class SelectCity extends Activity implements View.OnClickListener{

    private ImageView mBackBtn;
    private ListView mlistView;
    private MyApplication myApplication;
    private List<City> mCityList;
    private List<String> cityNameList;
    private City[] cityArray;
    private String[] cityNameArray;
    private String cityCode = "";
    private TextView current_city_Tv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 加载布局文件
        setContentView(R.layout.select_city);

        // 给后退按钮注册了一个事件
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        // 获得MyApplication实例，然后，调用实例的getCityList函数获得城市列表
        myApplication = MyApplication.getInstance();
        mCityList = myApplication.getCityList();

        // 从城市列表中取出城市名字，并将城市名字列表转化为城市名字数组
        cityNameList = new ArrayList<>();
        for(int i=0; i<mCityList.size();i++){
            cityNameList.add(mCityList.get(i).getCity());
        }
        cityNameArray = cityNameList.toArray(new String[cityNameList.size()]);


        mlistView = (ListView) findViewById(R.id.list_view);
        // 构建适配器对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                SelectCity.this, android.R.layout.simple_list_item_1, cityNameArray);

        // 将构建好的适配器对象传递进去
        mlistView.setAdapter(adapter);

        // 为ListView注册了一个监听器，以及设置点击处理逻辑
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityCode = mCityList.get(i).getNumber();
                Toast.makeText(SelectCity.this, "你单击了:" + cityCode, Toast.LENGTH_SHORT).show();
                current_city_Tv = (TextView) findViewById(R.id.title_name);
                current_city_Tv.setText("当前城市：" + cityNameArray[i]);

            }
        });
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
                Intent i = new Intent();
                // 向主Activity返回城市代码
                i.putExtra("cityCode", cityCode);
                setResult(RESULT_OK, i);
                // 销毁当前活动
                finish();
                break;
            default:
                break;
        }
    }
}
