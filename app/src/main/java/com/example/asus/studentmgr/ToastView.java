package com.example.asus.studentmgr;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by asus on 2019/3/17.
 */

public class ToastView extends Toast {
    private static ToastView toast;
    private static final int TYPE_HIDE=-1;
    private static final int TYPE_TRUE=0;
    private static final int TYPE_FALSE=1;
    private static ImageView toastImg;
    private static TextView toastText;
    public ToastView(Context context){
        super(context);
    }
    public static void cancelToast(){
        if(toast!=null){
            toast.cancel();
        }
    }
    public void cancel(){
        try {
            super.cancel();
        }
        catch (Exception e){

        }
    }
    public void show(){
        try {
            super.show();
        }
        catch (Exception e){

        }
    }
    private static void initToast(Context context,CharSequence text){
        try {
            cancelToast();
            toast=new ToastView(context);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout=inflater.inflate(R.layout.toast_layout,null);
            layout.setBackgroundResource((R.drawable.toast_show));
            toastImg=layout.findViewById(R.id.toastImg);
            toastText=layout.findViewById(R.id.toastText);
            toastText.setText(text);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER,0,70);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void initToast2(Context context,CharSequence text){
        try {
            cancelToast();
            toast=new ToastView(context);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout=inflater.inflate(R.layout.toast_layout,null);
            layout.setBackgroundResource((R.drawable.toast_back));
            toastImg=layout.findViewById(R.id.toastImg);
            toastText=layout.findViewById(R.id.toastText);
            toastText.setText(text);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER,0,300);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void showToast(Context context,CharSequence text,int time,int imgType){
        initToast(context,text);
        if(time==Toast.LENGTH_LONG){
            toast.setDuration(Toast.LENGTH_LONG);
        }
        else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        if(imgType==TYPE_HIDE){
            toastImg.setVisibility(View.GONE);
        }
        else if(imgType==TYPE_TRUE) {
            toastImg.setBackgroundResource(R.drawable.straw);
        }
        else {
            toastImg.setBackgroundResource(R.drawable.hamburger);
        }
        toastImg.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(toastImg,"rotationY",0,360).setDuration(1700).start();
        toast.show();
    }
    private static void showToast2(Context context,CharSequence text,int time,float screenWidth,float screenHeight){
        initToast2(context,text);
        toastImg.setVisibility(View.INVISIBLE);
        if(time==Toast.LENGTH_LONG){
            toast.setDuration(Toast.LENGTH_LONG);
        }
        else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
    public static void ShowText(Context context,CharSequence text){
        showToast(context,text,Toast.LENGTH_SHORT,TYPE_HIDE);
    }
    public static void ShowText(Context context,CharSequence text,boolean isCorrect){
        showToast(context,text,Toast.LENGTH_SHORT,isCorrect?TYPE_TRUE:TYPE_FALSE);
    }
    public static void ShowText(Context context,CharSequence text,int time){
        showToast(context,text,time,TYPE_HIDE);
    }
    public static void ShowText(Context context,CharSequence text,int time,boolean isCorrect){
        showToast(context,text,time,isCorrect?TYPE_TRUE:TYPE_FALSE);
    }
    public static void ShowText(Context context,CharSequence text,int time,float screenWidth,float screenHeight){
        showToast2(context,text,time,screenWidth,screenHeight);
    }
}
