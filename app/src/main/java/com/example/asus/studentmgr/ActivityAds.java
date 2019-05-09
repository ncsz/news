package com.example.asus.studentmgr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by asus on 2019/3/25.
 */


public class ActivityAds extends Activity{
    private int recLen = 13;
    private TextView tv;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;
    private VideoView myVideoView;
    private Dialog transparentProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        initView();
        tv.setText("跳过13");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recLen--;
                        tv.setText("跳过 " + recLen);
                        if (recLen < 0) {
                            timer.cancel();
                            tv.setVisibility(View.GONE);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        myVideoView =  findViewById(R.id.ad_video);
        final String videoPath = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.huaweisea).toString();
        myVideoView.setVideoPath(videoPath);
        myVideoView.start();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }});
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                myVideoView.setVideoPath(videoPath);
                myVideoView.start();
            }
        });
        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                myVideoView.pause();
                transparentProgressDialog= TransparentProgressDialog.createLoadingDialog(ActivityAds.this);
                transparentProgressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ActivityAds.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        }, 14000);
    }
    private void initView() {
        tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                timer.cancel();
                myVideoView.pause();
                transparentProgressDialog= TransparentProgressDialog.createLoadingDialog(ActivityAds.this);
                transparentProgressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ActivityAds.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    }
                },2000);
            }
        });
    }
}

