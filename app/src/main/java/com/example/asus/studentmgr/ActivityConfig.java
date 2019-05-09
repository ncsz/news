package com.example.asus.studentmgr;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.style.UpdateAppearance;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by asus on 2019/4/24.
 */

public class ActivityConfig extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(hasHeaders()){
            Button button=new Button(this);
            button.setText("设置操作");
            setListFooter(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ActivityConfig.this,ActivityLogin.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public void onBuildHeaders(List<Header> target){
        loadHeadersFromResource(R.xml.activity_config,target);
    }
    @Override
    public boolean isValidFragment(String fragmentName){
        return  true;
    }
    public static class mainFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource((R.xml.preference_account));
        }
    }
    public static class commonUseFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferece_common);
        }
    }
    public static class aboutFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);
        }
    }
}
