package com.example.asus.studentmgr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static com.example.asus.studentmgr.ActivityPhoto.RC_CHOOSE_PHOTO;

/**
 * Created by sun on 2019/5/23.
 */

public class Permission {
    public static void setPermission(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
        }
        else  if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.RECORD_AUDIO}, RC_CHOOSE_PHOTO);
        }
        else if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RC_CHOOSE_PHOTO);
        }
        else if(ContextCompat.checkSelfPermission(context,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.INTERNET},RC_CHOOSE_PHOTO);
        }
    }
}
