package cn.edu.pku.gengzehao.miniweather;

import android.support.v4.view.PagerAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.gengzehao.bean.OtherWeather;

public class XMLPullParserHandler {
    List<OtherWeather> otherWeathers;
    private OtherWeather otherWeather;
    private String text;

    public XMLPullParserHandler(){
        otherWeathers = new ArrayList<OtherWeather>();
    }

    public List<OtherWeather> getOtherWeathers() {
        return otherWeathers;
    }

//    public List<OtherWeather> parse(InputStream is) {
    public List<OtherWeather> parse(String is) {




        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(new StringReader(is));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("weather")) {
                            otherWeather = new OtherWeather();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("weather")) {
                            otherWeathers.add(otherWeather);
                        } else if (tagname.equalsIgnoreCase("date")) {
                            otherWeather.setDate(text);
                        } else if (tagname.equalsIgnoreCase("high")) {
                            otherWeather.setHigh(text);
                        } else if (tagname.equalsIgnoreCase("low")) {
                            otherWeather.setLow(text);
                        } else if (tagname.equalsIgnoreCase("type")) {
                            otherWeather.setType(text);
                        } else if (tagname.equalsIgnoreCase("fengli")) {
                            otherWeather.setFengli(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otherWeathers;
    }

}
