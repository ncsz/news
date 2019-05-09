package com.example.asus.studentmgr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by asus on 2019/4/17.
 */

public class ActivityLogin extends SizeConfig {
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private Button button;
    private EditText editText1;
    private EditText editText2;
    private InputMethodManager inputMethodManager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button=findViewById(R.id.button);
        ErrorLoginDialog();
        LoginingDialog();
        editText1 = findViewById(R.id.UserName);
        editText2 = findViewById(R.id.Password);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editText1.setText(sp.getString("username",""));
        editText2.setText(sp.getString("password",""));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1 = findViewById(R.id.UserName);
                editText2 = findViewById(R.id.Password);
                //progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(editText1.getText().toString().trim().equals("ncs")&&editText2.getText().toString().trim().equals("2580")){
                            Intent intent=new Intent(ActivityLogin.this,ActivityAds.class);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }
                        else {
                            progressDialog.dismiss();
                            builder.show();
                        }
                    }
                },0);
            }
        });
    }
    private void ErrorLoginDialog(){
        builder=new AlertDialog.Builder(this);
        builder.setTitle("错误提示框");
        builder.setMessage("您输入的信息有误，请重新输入");
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void LoginingDialog(){
        progressDialog=new ProgressDialog(ActivityLogin.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("登录中");
        progressDialog.setMessage("Login in.........");
        progressDialog.setCancelable(false);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.constraint:
                if(null==inputMethodManager){
                    inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}
