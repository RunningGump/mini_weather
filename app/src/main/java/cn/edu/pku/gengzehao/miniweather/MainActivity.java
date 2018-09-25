package cn.edu.pku.gengzehao.miniweather;

import android.app.Activity;
import android.os.Bundle;

// 新建一个继承Activity的类
public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);  //通过setContentView方法加载布局
    }
}
