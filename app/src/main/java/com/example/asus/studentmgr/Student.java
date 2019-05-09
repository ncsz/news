package com.example.asus.studentmgr;

/**
 * Created by asus on 2019/4/17.
 */

import java.io.Serializable;
public class Student implements Serializable {
    public byte[] image;
    public String ID;
    public String names;
    public String college;
    public String major;
    public Student(byte[] i,String I,String n,String c,String m){
        image=i;
        ID=I;
        names=n;
        college=c;
        major=m;
    }
}
