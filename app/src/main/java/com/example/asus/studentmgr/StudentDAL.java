package com.example.asus.studentmgr;

import android.database.Cursor;

/**
 * Created by asus on 2019/5/3.
 */

public class StudentDAL {
    private StudentDBHelper studentDBHelper;
    public StudentDAL(StudentDBHelper studentDBHelper){
        this.studentDBHelper=studentDBHelper;
    }
    public Cursor selectAllStudent(){
        return studentDBHelper.getReadableDatabase().rawQuery("select * from student",null);
    }
    public Cursor select(String s){
        s="%"+s+"%";
        return studentDBHelper.getReadableDatabase().rawQuery("select * from student where ID like ? or name like ? or college like ? or major like ?",new String[]{s,s,s,s});
    }
    public void update(String id,String name,String college,String major,int position){
        studentDBHelper.getReadableDatabase().execSQL("update student set ID=?,name=?,college=?,major=? where _id=?",new Object[]{id,name,college,major,position});
    }
    public void insert(byte[] image,String id,String name,String college,String major){
        studentDBHelper.getReadableDatabase().execSQL("insert into student values(null,?,?,?,?,?)",new Object[]{image,id,name,college,major});
    }
    public void delete(int position){
        studentDBHelper.getReadableDatabase().execSQL("delete from student where _id=?",new Integer[]{position});
    }
}
