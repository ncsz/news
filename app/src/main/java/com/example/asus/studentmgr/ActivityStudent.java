package com.example.asus.studentmgr;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.studentmgr.View.ToastView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by asus on 2019/4/17.
 */

public  class ActivityStudent extends Activity implements View.OnClickListener{
    private EditText editTextID;
    private EditText editTextName;
    private Button btnSubmit;
    private Spinner spinnerCollege;
    private Spinner spinnerMajor;
    private ArrayAdapter<String> adapterCollege;
    private ArrayAdapter<String> adapterMajor;
    private SpinnerAdapter spinnerCollegeAdapter;
    private Student studentEdit,studentTakeValue;
    private String ID;
    private int position;
    private TextView birthday;
    private CustomDatePicker  chooseDate;
    private InputMethodManager inputMethodManager;
    private EditText friendIntroduce;
    private Button readIntroduce;
    private Button writeIntroduce;
    private ImageView headImage;
    private View decorView;
    float downX;
    float screenWidth, screenHeight;
    final String FILE_NAME="introduce.txt";
    private MediaPlayer mediaPlayer=new MediaPlayer();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isOrientationScreen=sp.getBoolean("start_orientation_screen",false);
        if(isOrientationScreen){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        FindView();
        adapterCollege=new ArrayAdapter<String>(ActivityStudent.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.college));
        spinnerCollege.setAdapter(adapterCollege);
        spinnerCollege.setOnItemSelectedListener(new spinnerCollegeOnItemSelectedListener());
        Intent intent=getIntent();
        position=intent.getIntExtra("EditID",-1);
        ID=intent.getStringExtra("ID");
        if(ID!=null){
            editTextID.setText(ID);
        }
        studentEdit=(Student)intent.getSerializableExtra("EditStudentInfor");
        if(studentEdit!=null){
            SetView(studentEdit);
        }
        studentTakeValue=(Student)getIntent().getSerializableExtra("takevalue");
        if(studentTakeValue!=null){
            SetView(studentTakeValue);
        }
        if(ActivityPhoto.bitmap!=null){
            headImage.setImageBitmap(ActivityPhoto.bitmap);
            ActivityPhoto.bitmap=null;
        }
        if(ActivityDraw.bitmap!=null){
            headImage.setImageBitmap(ActivityDraw.bitmap);
            headImage.setBackgroundColor(Color.WHITE);
            ActivityDraw.bitmap=null;
        }
        readIntroduce.setOnClickListener(new readClickListener());
        writeIntroduce.setOnClickListener(new writeClickListener());
        btnSubmit = findViewById(R.id.button2);
        btnSubmit.setOnClickListener(new btnSumbitOnclickListener());
        findViewById(R.id.chooseDate).setOnClickListener(this);
        initDatePicker();
        getScreenInfor();
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottom_Dialog.showBottomDialog(ActivityStudent.this);
            }
        });
    }
    private class btnSumbitOnclickListener implements View.OnClickListener{
        public void onClick(View v){
            FindView();
            String TakePicFileName = "sound1.amr";
            String TakePicFileName1="sound2.amr";
            String TakePicFilePath = Environment.getExternalStorageDirectory().toString();
            if(editTextID.getText().toString().trim().equals("")||editTextName.getText().toString().trim().equals("")){
                File tmpFile = new File(TakePicFilePath, TakePicFileName);
                if(!tmpFile.exists()){
                    try{
                        mediaPlayer=MediaPlayer.create(ActivityStudent.this,R.raw.fail);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Uri uri = Uri.fromFile(tmpFile);
                    try{
                        mediaPlayer.setDataSource(ActivityStudent.this,uri);
                        mediaPlayer.prepare();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                mediaPlayer.start();
                ToastView.ShowText(ActivityStudent.this,"提交失败，请确认输入内容不为空",0,false);
            }
            else {
                File tmpFile = new File(TakePicFilePath, TakePicFileName1);
                if(!tmpFile.exists()){
                    try{
                        mediaPlayer=MediaPlayer.create(ActivityStudent.this,R.raw.success);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    Uri uri = Uri.fromFile(tmpFile);
                    try{
                        mediaPlayer.setDataSource(ActivityStudent.this,uri);
                        mediaPlayer.prepare();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                ToastView.ShowText(ActivityStudent.this,"  提交成功  ",0,true);
                Intent intent = new Intent(ActivityStudent.this, ActivityMain.class);
                sendValue(intent,"student","EditID");
                startActivity(intent);
                finish();
            }
        }
    }
    private void sendValue(Intent intent,String extra1,String extra2){
        Bundle bundle = new Bundle();
        bundle.putSerializable(extra1,new Student(getBitmapByte(headImage),editTextID.getText().toString(),editTextName.getText().toString(),spinnerCollege.getSelectedItem().toString(),spinnerMajor.getSelectedItem().toString()));
        bundle.putInt(extra2,position);
        intent.putExtras(bundle);
    }
    private byte[] getBitmapByte(ImageView view) {
       /*view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
       view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());*/
        view.buildDrawingCache();
        Bitmap bitmap=view.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
    private class spinnerCollegeOnItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String collegeSelected=spinnerCollege.getSelectedItem().toString().trim();
            if(collegeSelected.equals(getResources().getStringArray(R.array.college)[0])){
                adapterMajor=new ArrayAdapter<String>(ActivityStudent.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.major));
            }
            else{
                adapterMajor=new ArrayAdapter<String>(ActivityStudent.this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.majors));

            }
            spinnerMajor.setAdapter(adapterMajor);
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    /* private void ErrorInforDialog(){
         builder=new AlertDialog.Builder(this);
         builder.setTitle("错误提示框");
         builder.setMessage("输入信息不能为空");
         builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
     }*/
    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("1990-02-01", false);
        long endTimestamp = System.currentTimeMillis();
        birthday.setText(DateFormatUtils.long2Str(endTimestamp, false));
        chooseDate = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                birthday.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        chooseDate.setCancelable(false);
        chooseDate.setCanShowPreciseTime(false);
        chooseDate.setScrollLoop(false);
        chooseDate.setCanShowAnim(false);
    }
    protected void onDestroy() {
        super.onDestroy();
        chooseDate.onDestroy();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseDate:
                chooseDate.show(birthday.getText().toString());
                break;
            case R.id.linear:
                if(null==inputMethodManager){
                    inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
    private String read(){
        try{
            FileInputStream fis=openFileInput(FILE_NAME);
            byte [] buff=new byte[1024];
            int hasRead=0;
            StringBuilder sb=new StringBuilder("");
            while ((hasRead=fis.read(buff))>0){
                sb.append(new String(buff,0,hasRead));
            }
            fis.close();
            return sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    private void write(String content){
        try{
            deleteFile(FILE_NAME);
            FileOutputStream fos=openFileOutput(FILE_NAME,MODE_APPEND);
            PrintStream ps=new PrintStream(fos);
            ps.println(content);
            ps.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = event.getX();

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float moveDistanceX = event.getX()-downX;
            if(Math.abs(moveDistanceX)<screenWidth/10){

            }
            else if (moveDistanceX > screenWidth/10){
                decorView.setAlpha(0.5f);
                decorView.setX(moveDistanceX);
            }

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            float moveDistanceX =event.getX()-downX;
            if(moveDistanceX>0&&moveDistanceX<screenWidth/10){

            }
            else if(moveDistanceX > 3*screenWidth / 14){
                continueMove(moveDistanceX);
                Intent intent = new Intent(ActivityStudent.this, ActivityMain.class);
                sendValue(intent,"takevalue","position");
                startActivity(intent);
            }else{
                rebackToLeft(moveDistanceX);
                decorView.setX(0);
                decorView.setAlpha(1);
            }
        }
        return super.onTouchEvent(event);
    }
    public boolean dispatchTouchEvent(MotionEvent event){
        if(onTouchEvent(event)){
            return true;
        }
        else {
            return super.dispatchTouchEvent(event);
        }
    }
    private void continueMove(float moveDistanceX){
        // 从当前位置移动到右侧。
        ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
        anim.setDuration(1000); // 一秒的时间结束, 为了简单这里固定为1秒
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 位移
                float x = (float) (animation.getAnimatedValue());
                decorView.setX(x);
            }
        });

        // 动画结束时结束当前Activity
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
    }

    /**
     * Activity被滑动到中途时，滑回去~
     */
    private void rebackToLeft(float moveDistanceX){
        ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
    }
    private void getScreenInfor(){
        decorView = getWindow().getDecorView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
    private void FindView(){
        editTextID=findViewById((R.id.editText));
        editTextName=findViewById(R.id.editText2);
        spinnerCollege=findViewById(R.id.spinner);
        spinnerMajor=findViewById(R.id.spinner2);
        birthday = findViewById(R.id.birthday);
        friendIntroduce=findViewById(R.id.friend_introduce);
        readIntroduce=findViewById(R.id.read_introduce);
        writeIntroduce=findViewById(R.id.write_introduce);
        headImage=findViewById(R.id.head_image);
    }
    private void SetView(Student stu){
        FindView();
        editTextID.setText(stu.ID);
        editTextName.setText(stu.names);
        spinnerCollegeAdapter=spinnerCollege.getAdapter();
        for(int i=0;i<spinnerCollegeAdapter.getCount();i++){
            if(stu.college.equals(spinnerCollegeAdapter.getItem(i).toString())){
                spinnerCollege.setSelection(i);
                break;
            }
        }
    }
    private class readClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            friendIntroduce.setText(null);
            friendIntroduce.setText(read());
        }
    }
    private class writeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            write(friendIntroduce.getText().toString());
            ToastView.ShowText(ActivityStudent.this,"写入成功", Toast.LENGTH_LONG,screenWidth,screenHeight);
        }
    }
}
