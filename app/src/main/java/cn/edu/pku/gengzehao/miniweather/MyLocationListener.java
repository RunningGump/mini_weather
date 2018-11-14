package cn.edu.pku.gengzehao.miniweather;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import java.util.List;

import cn.edu.pku.gengzehao.app.MyApplication;
import cn.edu.pku.gengzehao.bean.City;

public class MyLocationListener extends BDAbstractLocationListener {
    public String realCity;
    public String cityCode;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可以获得定位相关的全部结果
        //下面会列出一些信息的获取方法

        String addr = bdLocation.getAddrStr();  //获取详细地址信息
        String country = bdLocation.getCountry();   //获取国家
        String province = bdLocation.getProvince(); //获取省份
        String city = bdLocation.getCity(); //获取城市
        String district = bdLocation.getDistrict(); //获取区县
        Log.d("Location_city", city);
        realCity = city.replace("市", "");

        List<City> mCityList;
        MyApplication myApplication = MyApplication.getInstance();
        mCityList = myApplication.getCityList();
        for(City onCity:mCityList){
            if(onCity.getCity().equals(realCity)){
                cityCode = onCity.getNumber();
                Log.d("location_code", cityCode);
            }
        }
    }
}
