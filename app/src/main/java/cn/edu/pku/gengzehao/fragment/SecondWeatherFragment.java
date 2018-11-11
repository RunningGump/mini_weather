package cn.edu.pku.gengzehao.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.pku.gengzehao.bean.OtherWeather;
import cn.edu.pku.gengzehao.miniweather.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondWeatherFragment extends Fragment {
    private TextView weekTv1, weekTv2, weekTv3;
    private ImageView weather_imgTv1, weather_imgTv2, weather_imgTv3;
    private TextView temperatureTv1, temperatureTv2, temperatureTv3;
    private TextView climateTv1, climateTv2, climateTv3;
    private TextView windTv1, windTv2, windTv3;

    public SecondWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.weather_item, container, false);

        View view1 = view.findViewById(R.id.subitem1);
        View view2 = view.findViewById(R.id.subitem2);
//        View view3 = view.findViewById(R.id.subitem3);

        weekTv1 = (TextView) view1.findViewById(R.id.week);
        weekTv2 = (TextView) view2.findViewById(R.id.week);
//        weekTv3 = (TextView) view3.findViewById(R.id.week);

//    weekTv1.setText(TimeUtil.getWeek(1, TimeUtil.XING_QI));
//    weekTv2.setText(TimeUtil.getWeek(2, TimeUtil.XING_QI));
//    weekTv3.setText(TimeUtil.getWeek(3, TimeUtil.XING_QI));

        weather_imgTv1 = (ImageView) view1.findViewById(R.id.weather_img);
        weather_imgTv2 = (ImageView) view2.findViewById(R.id.weather_img);
//        weather_imgTv3 = (ImageView) view3.findViewById(R.id.weather_img);
        temperatureTv1 = (TextView) view1.findViewById(R.id.temperature);
        temperatureTv2 = (TextView) view2.findViewById(R.id.temperature);
//        temperatureTv3 = (TextView) view3.findViewById(R.id.temperature);

        climateTv1 = (TextView) view1.findViewById(R.id.climate);
        climateTv2 = (TextView) view2.findViewById(R.id.climate);
//        climateTv3 = (TextView) view3.findViewById(R.id.climate);

        windTv1 = (TextView) view1.findViewById(R.id.wind);
        windTv2 = (TextView) view2.findViewById(R.id.wind);
//        windTv3 = (TextView) view3.findViewById(R.id.wind);

        climateTv1.setText("N/A");
        climateTv2.setText("N/A");
//        climateTv3.setText("N/A");

        weekTv1.setText("N/A");
        weekTv2.setText("N/A");
//        weekTv3.setText("N/A");


        weather_imgTv1.setImageResource(R.drawable.biz_plugin_weather_qing);
        weather_imgTv2.setImageResource(R.drawable.biz_plugin_weather_qing);
//        weather_imgTv3.setImageResource(R.drawable.biz_plugin_weather_qing);

        climateTv1.setText("N/A");
        climateTv2.setText("N/A");
//        climateTv3.setText("N/A");

        temperatureTv1.setText("N/A");
        temperatureTv2.setText("N/A");
//        temperatureTv3.setText("N/A");

        windTv1.setText("N/A");
        windTv2.setText("N/A");
//        windTv3.setText("N/A");
        return view;

    }

    public void updateWeather(List<OtherWeather> otherWeathers)
    {
        if (otherWeathers!= null) {

            weekTv1.setText(otherWeathers.get(2).getDate());
            weekTv2.setText(otherWeathers.get(3).getDate());
    //        weekTv3.setText("N/A");


            weather_imgTv1.setImageResource(R.drawable.biz_plugin_weather_qing);
            weather_imgTv2.setImageResource(R.drawable.biz_plugin_weather_qing);
    //        weather_imgTv3.setImageResource(R.drawable.biz_plugin_weather_qing);

            climateTv1.setText(otherWeathers.get(2).getType());
            climateTv2.setText(otherWeathers.get(3).getType());
    //        climateTv3.setText("N/A");

            temperatureTv1.setText(otherWeathers.get(2).getLow().split("\\s+")[1]+"~"+otherWeathers.get(2).getHigh().split("\\s+")[1]);
            temperatureTv2.setText(otherWeathers.get(3).getLow().split("\\s+")[1]+"~"+otherWeathers.get(3).getHigh().split("\\s+")[1]);

    //        temperatureTv3.setText("N/A");

            windTv1.setText(otherWeathers.get(2).getFengli());
            windTv2.setText(otherWeathers.get(3).getFengli());
    //        windTv3.setText("N/A");
    } else {

            climateTv1.setText("N/A");
            climateTv2.setText("N/A");
    //        climateTv3.setText("N/A");

            weekTv1.setText("N/A");
            weekTv2.setText("N/A");
    //        weekTv3.setText("N/A");


            weather_imgTv1.setImageResource(R.drawable.biz_plugin_weather_qing);
            weather_imgTv2.setImageResource(R.drawable.biz_plugin_weather_qing);
    //        weather_imgTv3.setImageResource(R.drawable.biz_plugin_weather_qing);

            climateTv1.setText("N/A");
            climateTv2.setText("N/A");
    //        climateTv3.setText("N/A");

            temperatureTv1.setText("N/A");
            temperatureTv2.setText("N/A");
    //        temperatureTv3.setText("N/A");

            windTv1.setText("N/A");
            windTv2.setText("N/A");
    //        windTv3.setText("N/A");
        }
    }
}


