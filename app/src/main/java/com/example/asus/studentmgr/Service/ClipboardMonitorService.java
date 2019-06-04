package com.example.asus.studentmgr.Service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by asus on 2019/5/13.
 */

public class ClipboardMonitorService extends Service {
    private ClipboardManager clipboardManager;
    public static String HAS_STUDENT_RECORD="com.example.asus.studentmgr.clipboard";
    public IBinder onBind(Intent intent){

        return null;
    }
    public void onCreate(){
        clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clipData=clipboardManager.getPrimaryClip();
                ClipData.Item item=clipData.getItemAt(0);
                if(item.getText().toString().trim().startsWith("SE")) {
                    Intent intent1 = new Intent();
                    intent1.setAction(HAS_STUDENT_RECORD);
                    intent1.putExtra("has_student_record", item.getText().toString());
                    sendBroadcast(intent1);
                }
            }
        });
    }
}
