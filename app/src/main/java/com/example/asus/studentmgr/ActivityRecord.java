package com.example.asus.studentmgr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asus.studentmgr.View.ToastView;

import java.io.File;

/**
 * Created by sun on 2019/5/21.
 */

public class ActivityRecord extends Activity
        implements View.OnClickListener
{
    // 定义界面上的两个按钮
    ImageButton record, stop,record1,stop1;
    Button button;
    // 系统的音频文件
    File soundFile;
    MediaRecorder mRecorder;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // 获取程序界面中的两个按钮
        record =  findViewById(R.id.record);
        stop =  findViewById(R.id.stop);
        record1=findViewById(R.id.record1);
        stop1=findViewById(R.id.stop1);
        button=findViewById(R.id.set);
        // 为两个按钮的单击事件绑定监听器
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        record1.setOnClickListener(this);
        stop1.setOnClickListener(this);
        button.setOnClickListener(this);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (soundFile != null && soundFile.exists())
        {
            // 停止录音
            mRecorder.stop();
            // 释放资源
            mRecorder.release();
            mRecorder = null;
        }
    }
    @Override
    public void onClick(View source)
    {
        switch (source.getId())
        {
            // 单击录音按钮
            case R.id.record:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED))
                {
                    Toast.makeText(ActivityRecord.this, "SD卡不存在，请插入SD卡！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    try {
                        // 创建保存录音的音频文件
                        soundFile = new File(Environment
                                .getExternalStorageDirectory().getCanonicalFile()
                                + "/sound1.amr");
                        mRecorder = new MediaRecorder();
                        // 设置录音的声音来源
                        mRecorder.setAudioSource(MediaRecorder
                                .AudioSource.MIC);
                        // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
                        mRecorder.setOutputFormat(MediaRecorder
                                .OutputFormat.THREE_GPP);
                        mRecorder.setAudioEncoder(MediaRecorder
                                .AudioEncoder.AMR_NB);
                        mRecorder.setOutputFile(soundFile.getAbsolutePath());
                        mRecorder.prepare();
                        mRecorder.start();
                        ToastView.ShowText(this, "开始录制", Toast.LENGTH_LONG,true);
                    }
                catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            // 单击停止按钮
            case R.id.stop:
                if (soundFile != null && soundFile.exists())
                {
                    ToastView.ShowText(this, "结束录制", Toast.LENGTH_LONG,true);
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                break;
            case R.id.record1:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED))
                {
                    Toast.makeText(ActivityRecord.this, "SD卡不存在，请插入SD卡！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    try {
                        // 创建保存录音的音频文件
                        soundFile = new File(Environment
                                .getExternalStorageDirectory().getCanonicalFile()
                                + "/sound2.amr");
                        mRecorder = new MediaRecorder();
                        // 设置录音的声音来源
                        mRecorder.setAudioSource(MediaRecorder
                                .AudioSource.MIC);
                        // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
                        mRecorder.setOutputFormat(MediaRecorder
                                .OutputFormat.THREE_GPP);
                        mRecorder.setAudioEncoder(MediaRecorder
                                .AudioEncoder.AMR_NB);
                        mRecorder.setOutputFile(soundFile.getAbsolutePath());
                        mRecorder.prepare();
                        mRecorder.start();
                        ToastView.ShowText(this, "开始录制", Toast.LENGTH_LONG,true);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            // 单击停止按钮
            case R.id.stop1:
                if (soundFile != null && soundFile.exists())
                {
                    ToastView.ShowText(this, "结束录制", Toast.LENGTH_LONG,true);
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                }
                break;
            case R.id.set:
                Intent intent=new Intent(ActivityRecord.this,ActivityStudent.class);
                startActivity(intent);
                break;
        }
    }
}
