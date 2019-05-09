package com.example.asus.studentmgr;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by asus on 2019/4/24.
 */

public class ActivityPhonePlace extends SizeConfig {
    private EditText phoneNumberInputing;
    private ImageView carrierIcon;
    private TextView phoneNumber;
    private TextView placeProvinceCountry;
    private TextView carrier;
    private TextView placeCarrier;
    private Button copyPhone;
    private Button returnResult;
    private ClipboardManager clipboardManager;
    private   static Phone phone=new Phone();
    private PhoneHandler mPhoneHandler = new PhoneHandler(this);
    String phoneNumber1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_phone_place);
        ScreenSize.getScreenInfor(this);
        findView();
        Intent intent=getIntent();
        phoneNumber1=intent.getStringExtra("phone_number");
        if(phoneNumber1!=null){
            phoneNumberInputing.setText(phoneNumber1);
            returnResult.setVisibility(View.VISIBLE);
            returnResult.setOnClickListener(new resultClickListener());
        }
        phoneNumberInputing.addTextChangedListener(new myaddTextChangeListener());
        copyPhone.setOnClickListener(new myOnclickListener());
    }
    private void findView() {
        phoneNumberInputing = findViewById(R.id.input_phone_number);
        carrierIcon = findViewById(R.id.carrier_icon);
        phoneNumber = findViewById(R.id.phone_number);
        placeProvinceCountry = findViewById(R.id.place_province_country);
        carrier = findViewById(R.id.carrier);
        placeCarrier = findViewById(R.id.place_carrier);
        copyPhone=findViewById(R.id.copy_phone_infor);
        returnResult=findViewById(R.id.return_result);
    }

    private class myaddTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()<7){
                WeakReference<Context> reference=new WeakReference<Context>(ActivityPhonePlace.this);
                ActivityPhonePlace activity = (ActivityPhonePlace) reference.get();
                activity.phoneNumber.setText("<空>");
                activity.placeProvinceCountry.setText("<空>");
                activity.carrier.setText("<空>");
                activity.placeCarrier.setText("<空>");
                activity.carrierIcon.setImageResource(R.drawable.icon_question_phone);
            }
            else if(charSequence.length()>=7&&charSequence.length()<12){
                String url="http://mobsec-dianhua.baidu.com/dianhua_api/open/location";
                String phoneNumber = phoneNumberInputing.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastView.ShowText(ActivityPhonePlace.this,getString(R.string.input_no_empty), Toast.LENGTH_LONG,ScreenSize.getScreenWidth(),ScreenSize.getScreenHeight());
                } else {
                    hideKeyboard(phoneNumberInputing);
                    url = url + "?tel=" + phoneNumber;
                    new PhoneThread(url, phoneNumber).start();
                }
            }
            else if(charSequence.length()>=12){
                ToastView.ShowText(ActivityPhonePlace.this,"号码输入有误!",Toast.LENGTH_LONG,ScreenSize.getScreenWidth(),ScreenSize.getScreenHeight());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    private class myOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null,phone.toString()));
            if(TextUtils.isEmpty(phoneNumberInputing.getText().toString().trim())){
                ToastView.ShowText(ActivityPhonePlace.this,"复制信息不为空!",Toast.LENGTH_LONG,ScreenSize.getScreenWidth(),ScreenSize.getScreenHeight());
            }
            else {
                ClipData data=clipboardManager.getPrimaryClip();
                ClipData.Item item=data.getItemAt(0);
                ToastView.ShowText(ActivityPhonePlace.this,item.getText().toString(),Toast.LENGTH_LONG,true);
            }
        }
    }
    private class resultClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            ComponentName componentName=new ComponentName("com.example.testplace","com.example.testplace.MainActivity");
            try{
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("phone_infor",phone);
                intent.putExtras(bundle);
                intent.setComponent(componentName);
                startActivity(intent);
                //startActivityForResult(intent,0);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class PhoneThread extends Thread {
        private String url;
        private String phoneNumber;

        private PhoneThread(String url, String phoneNumber) {
            this.url = url;
            this.phoneNumber = phoneNumber;
        }
        @Override
        public void run() {
            String jsonResult = getJsonResultFromURL(url);
            if (TextUtils.isEmpty(jsonResult)) {
                Looper.prepare();
                Toast.makeText(ActivityPhonePlace.this, R.string.attribution_return_null,
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                phone = convertJsonToPhone(jsonResult, phoneNumber);
                Message msg = Message.obtain();
                msg.obj = phone;
                mPhoneHandler.sendMessage(msg);
            }
        }
    }
    private String getJsonResultFromURL(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = new URL(url).openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("yh", "getJsonFromURL: " + stringBuilder.toString());
        return stringBuilder.toString();
    }


    private Phone convertJsonToPhone(String url, String number) {
        Phone phone = new Phone();
        try {
            JSONObject object = new JSONObject(url);
            JSONObject numberObj = object.getJSONObject("response").getJSONObject(number);
            JSONObject detailObj = numberObj.getJSONObject("detail");
            phone.setPhoneNumber(number);
            phone.setPlaceProvince(detailObj.getString("province"));
            phone.setCarrier(detailObj.getString("operator"));
            phone.setPlaceCarrier(numberObj.getString("location"));
            phone.setPlaceArea(detailObj.getString("area").split("[a-zA-Z{}:]")[6].replace('"',' '));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }

    private static class PhoneHandler extends Handler {
        private WeakReference<Context> reference;

        PhoneHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Phone phone = (Phone) msg.obj;
            ActivityPhonePlace activity = (ActivityPhonePlace) reference.get();
            activity.phoneNumber.setText(phone.getPhoneNumber());
            activity.placeProvinceCountry.setText(phone.getPlaceProvince()+phone.getPlaceArea());
            activity.carrier.setText(phone.getCarrier());
            activity.placeCarrier.setText(phone.getPlaceCarrier());
            if (phone.getCarrier() == null) {
                ToastView.ShowText(activity,activity.getString(R.string.attribution_no_result),Toast.LENGTH_LONG,ScreenSize.getScreenWidth(),ScreenSize.getScreenHeight());
                activity.carrierIcon.setImageResource(R.drawable.icon_question_phone);
            } else {
                switch (phone.getCarrier()) {
                    case "移动":
                        activity.carrierIcon.setImageResource(R.drawable.icon_mobile_phone);
                        break;
                    case "电信":
                        activity.carrierIcon.setImageResource(R.drawable.icon_telecom_phone);
                        break;
                    case "联通":
                        activity.carrierIcon.setImageResource(R.drawable.icon_unicom_phone);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive(view)) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }
}
