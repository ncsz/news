package com.example.asus.studentmgr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * Created by asus on 2019/4/24.
 */

public class SizeConfig extends Activity {
    protected boolean up=false;
    @Override
    public Resources getResources(){
        Resources resources=super.getResources();
        if(resources!=null){
            Configuration configuration=resources.getConfiguration();
            SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            configuration.fontScale=Float.parseFloat(sp.getString("text_size","1.0f"));
            resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        }
        return  resources;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(up){
            onCreate(null);
            up=false;
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        up=true;
    }
}
