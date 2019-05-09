package com.example.asus.studentmgr;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.studentmgr.ImageUtils.getImage;

/**
 * Created by asus on 2019/5/6.
 */

public class ImageFinder {
    public static final String NONE = "none";
    public static final String TYPE_GIF = "image/gif";
    public static final String TYPE_JPEG = "image/jpeg";
    public static final String TYPE_jpg = "image/jpg";
    public static final String TYPE_PNG = "image/png";

    //第二个参数是配置需要屏蔽的图片格式
    public static List<Bitmap> getImages(Context context, String typeShield){
        String shield = typeShield;
        List<ImageInfor> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            if (type.equals(shield)) continue;
            list.add(0,new ImageInfor(path,name));
        }
        List<Bitmap> bitmaps=new ArrayList<Bitmap>();
        for(int i=0;i<list.size();i++){
            bitmaps.add(getImage(list.get(i).getImagePath()));
        }
        return bitmaps;
    }
}