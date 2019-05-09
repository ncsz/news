package com.example.asus.studentmgr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

/**
 * Created by asus on 2019/5/3.
 */

public class StudentDBHelper extends SQLiteOpenHelper {
    private final String CREATE_TABLE_STUDENT ="create table student(_id integer"+" primary key autoincrement,"+"  image BLOB,ID varchar(20),name varchar(50),college varchar(30),major varchar(30))";
    public StudentDBHelper(Context context,String name,int version){
        super(context,name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_STUDENT);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
