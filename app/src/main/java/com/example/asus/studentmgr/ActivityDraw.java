package com.example.asus.studentmgr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.asus.studentmgr.View.DrawView;

/**
 * Created by sun on 2019/5/21.
 */

public class ActivityDraw extends Activity {
    private DrawView drawView;
    public static Bitmap bitmap;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw);
        drawView=findViewById(R.id.draws);
        bitmap=null;
        drawView.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                bitmap=ImageUtils.getViewBitmap(drawView);
                Intent intent=new Intent(ActivityDraw.this,ActivityStudent.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){      //设置菜单项
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.menu_draw,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){    //菜单项操作
        switch (menuItem.getItemId()){
            case R.id.reset:
                drawView.reset();
                break;
            case R.id.set_head_image:
                bitmap=ImageUtils.getViewBitmap(drawView);
                Intent intent=new Intent(ActivityDraw.this,ActivityStudent.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    private   abstract class DoubleClickListener implements View.OnClickListener {
        private  final long DOUBLE_TIME = 500;
        private  long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
                onDoubleClick(v);
            }
            lastClickTime = currentTimeMillis;
        }

        public abstract void onDoubleClick(View v);
    }
}
