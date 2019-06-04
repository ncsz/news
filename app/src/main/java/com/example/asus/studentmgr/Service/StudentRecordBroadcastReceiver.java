package com.example.asus.studentmgr.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.asus.studentmgr.ActivityStudent;

/**
 * Created by asus on 2019/5/13.
 */

public class StudentRecordBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(ClipboardMonitorService.HAS_STUDENT_RECORD)){
            String ID=intent.getStringExtra("has_student_record");
            Intent intent1=new Intent(context, ActivityStudent.class);
            intent1.putExtra("ID",ID);
            context.startActivity(intent1);
        }
    }
}
