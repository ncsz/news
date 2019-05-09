package com.example.asus.studentmgr;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by asus on 2019/4/9.
 */

public class ScreenSize {
    private static float screenWidth, screenHeight;
    public static void getScreenInfor(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
    public static float getScreenWidth(){
        return  screenWidth;
    }
    public static float getScreenHeight(){
        return screenHeight;
    }
}
