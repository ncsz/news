package com.example.asus.studentmgr;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by asus on 2019/5/4.
 */

public class StudentProvider extends ContentProvider {
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final int studentsign=1;
    private StudentDBHelper studentDBHelper;
    static {
        matcher.addURI("com.example.asus.studentmgr.StudentProvider","student",1);
    }
    @Override
    public boolean onCreate(){
        studentDBHelper=new StudentDBHelper(this.getContext(),"studentmanage.db3",1);
        return true;
    }
    @Override
    public String getType(Uri uri){
        switch (matcher.match(uri)){
            case studentsign:
                return "";
            default:
                throw new IllegalArgumentException("未知Uri:"+uri);
        }
    }
    @Override
    public Cursor query(Uri uri,String [] projection,String where,String[] whereArgs,String sortOrder){
        switch (matcher.match(uri)){
            case studentsign:
                return studentDBHelper.getReadableDatabase().query("student",projection,where,whereArgs,null,null,sortOrder);
            default:
                throw new IllegalArgumentException("未知Uri:"+uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values){
        switch (matcher.match(uri)){
            case studentsign:
                long rowId=studentDBHelper.getReadableDatabase().insert("student",null,values);
                if(rowId>0){
                    Uri studentUri= ContentUris.withAppendedId(uri,rowId);
                    getContext().getContentResolver().notifyChange(studentUri,null);
                    return studentUri;
                }
                break;
            default:
                throw new IllegalArgumentException("未知Uri:"+uri);
        }
        return null;
    }
    @Override
    public int update(Uri uri,ContentValues values,String where,String[] whereArgs){
        int num=0;
        switch (matcher.match(uri)){
            case studentsign:
                num=studentDBHelper.getReadableDatabase().update("student",values,where,whereArgs);
                break;
            default:
                throw new IllegalArgumentException("未知Uri:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return num;
    }
    @Override
    public int delete(Uri uri,String where,String[] whereArgs){
        int num=0;
        switch (matcher.match(uri)){
            case studentsign:
                num=studentDBHelper.getReadableDatabase().delete("student",where,whereArgs);
                break;
            default:
                throw new IllegalArgumentException("未知Uri:"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return num;
    }

}
