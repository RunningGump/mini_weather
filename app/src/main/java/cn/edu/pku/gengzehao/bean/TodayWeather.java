package cn.edu.pku.gengzehao.bean;

import android.text.style.TtsSpan;

import cn.edu.pku.gengzehao.miniweather.PreferenceManager;

public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;

    private String date1;
    private String date2;
    private String date3;
    private String date4;

    private String high1;
    private String high2;
    private String high3;
    private String high4;

    private String low1;
    private String low2;
    private String low3;
    private String low4;

    private String type1;
    private String type2;
    private String type3;
    private String type4;

    private String fengli1;
    private String fengli2;
    private String fengli3;
    private String fengli4;



    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getWendu() {
        return wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }


    public String getFengli() {
        return fengli;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getType() {
        return type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }


    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getDate3() {
        return date3;
    }

    public void setDate3(String date3) {
        this.date3 = date3;
    }

    public String getDate4() {
        return date4;
    }

    public void setDate4(String date4) {
        this.date4 = date4;
    }

    public String getHigh1() {
        return high1;
    }

    public void setHigh1(String high1) {
        this.high1 = high1;
    }

    public String getHigh2() {
        return high2;
    }

    public void setHigh2(String high2) {
        this.high2 = high2;
    }

    public String getHigh3() {
        return high3;
    }

    public void setHigh3(String high3) {
        this.high3 = high3;
    }

    public String getHigh4() {
        return high4;
    }

    public void setHigh4(String high4) {
        this.high4 = high4;
    }

    public String getLow1() {
        return low1;
    }

    public void setLow1(String low1) {
        this.low1 = low1;
    }

    public String getLow2() {
        return low2;
    }

    public void setLow2(String low2) {
        this.low2 = low2;
    }

    public String getLow3() {
        return low3;
    }

    public void setLow3(String low3) {
        this.low3 = low3;
    }

    public String getLow4() {
        return low4;
    }

    public void setLow4(String low4) {
        this.low4 = low4;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

    public String getFengli1() {
        return fengli1;
    }

    public void setFengli1(String fengli1) {
        this.fengli1 = fengli1;
    }

    public String getFengli2() {
        return fengli2;
    }

    public void setFengli2(String fengli2) {
        this.fengli2 = fengli2;
    }

    public String getFengli3() {
        return fengli3;
    }

    public void setFengli3(String fengli3) {
        this.fengli3 = fengli3;
    }

    public String getFengli4() {
        return fengli4;
    }

    public void setFengli4(String fengli4) {
        this.fengli4 = fengli4;
    }

    @Override
    public String toString(){
        return "TodayWeather{" +
                "city='" + city +'\'' +
                ", updatetime='" + updatetime + '\'' +
                ", wendu='" + wendu + '\'' +
                ", shidu='" + shidu + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", fengli='" + fengli + '\'' +
                ", date='" + date + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date1 + '\'' +
                ", date='" + date2 + '\'' +
                ", date='" + date3 + '\'' +
                ", date='" + date4 + '\'' +
                ", high='" + high1 + '\'' +
                ", high='" + high2 + '\'' +
                ", high='" + high3 + '\'' +
                ", high='" + high4 + '\'' +
                ", low='" + low1 + '\'' +
                ", low='" + low2 + '\'' +
                ", low='" + low3 + '\'' +
                ", low='" + low4 + '\'' +
                ", type='" + type1 + '\'' +
                ", type='" + type2 + '\'' +
                ", type='" + type3 + '\'' +
                ", type='" + type4 + '\'' +
                ", fengli='" + fengli1 + '\'' +
                ", fengli='" + fengli2 + '\'' +
                ", fengli='" + fengli3 + '\'' +
                ", fengli='" + fengli4 + '\'' +
                '}';
    }
}
