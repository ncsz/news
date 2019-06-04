package com.example.asus.studentmgr;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.asus.studentmgr.View.ToastView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sun on 2019/5/27.
 */

public class ActivityWeather extends Activity {
    private SimpleAdapter simpleAdapter;
    private List<Map<String,Object>> weatherInfo=new ArrayList<>();
    private SoapObject soapObject;
    private ListView weatherList;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        getWeatherInfo();
        weatherList=findViewById(R.id.weather_list);
        simpleAdapter=new SimpleAdapter(this,weatherInfo,R.layout.weather_item,new String[]{"date","weather","temper","wind","icon_start","icon_end"},new int[]{R.id.date,R.id.weather,R.id.temper,R.id.wind,R.id.icon1,R.id.icon2});
        weatherList.setAdapter(simpleAdapter);
    }
    private Handler mHandler=new Handler(){
        public void handleMessage(Message message){
            if(message.what==0x11){
                if(soapObject!=null){
                    init();
                    ToastView.ShowText(ActivityWeather.this,weatherInfo.get(0).toString());
                }
               else {
                    ToastView.ShowText(ActivityWeather.this,"错误");
                }
            }
        }
    };
    public void getWeatherInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                soapObject=WebServiceUtil.getWeatherByCity();
                mHandler.sendEmptyMessage(0x11);
            }
        }).start();
    }
    public void init(){
        for (int i=0;i<5;i++){
            Map<String,Object> map=new HashMap<>();
            String dateInfo=soapObject.getProperty(7+5*i).toString();
            String date=dateInfo.split(" ")[0];
            String weather=dateInfo.split(" ")[1];
            String temperInfo=soapObject.getProperty(7+5*i+1).toString();
            String windInfo=soapObject.getProperty(7+5*i+2).toString();
            String iconStart=soapObject.getProperty(7+5*i+3).toString();
            String iconEnd=soapObject.getProperty(7+5*i+4).toString();
            map.put("date","日期"+date);
            map.put("weather","天气"+weather);
            map.put("temper","天气"+temperInfo);
            map.put("wind","风向"+windInfo);
            map.put("icon_start", iconStart);
            map.put("icon_end",iconEnd);
            weatherInfo.add(map);
        }
    }
}
