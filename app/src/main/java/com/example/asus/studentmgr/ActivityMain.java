package com.example.asus.studentmgr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class ActivityMain extends Activity {
    private ArrayList<Student> student = new ArrayList<>();
    private StudentDBHelper studentDBHelper;
    private StudentDAL studentDAL;
    private ListView listView;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    private int position;
    private SearchView searchView;
    private InputMethodManager inputMethodManager;
    View decorView;
    float downX, downY;
    float screenWidth, screenHeight;
    private boolean mIsExit;
    private ClipboardManager clipboardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentDBHelper=new StudentDBHelper(this,"studentmanage.db3",1);
        studentDAL=new StudentDAL(studentDBHelper);
        Intent intent = getIntent();
        //判断是否Student页面有值传过来
        setArrayList(intent);       //设置ListView列表项
        registerForContextMenu(listView);      //为ListView注册上下文菜单
        searchView=findViewById(R.id.searchView);
        setSearchView(searchView);             //设置搜索框操作
        searchView.setFocusable(false);        //设置进入此activity后焦点不在搜索框内
        getScreenInfor();                       //获取屏幕高度宽度等信息
    }
    //设置ListView列表项
    private void setArrayList(Intent intent){
        Student studentTakeValue=(Student)intent.getSerializableExtra("takevalue");
        if(studentTakeValue==null){
            if(intent.getExtras()!=null){
                AddNewInfor(intent);
            }
        }
        cursor=studentDAL.selectAllStudent();
        SetListView(cursor);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
        menu.add(1, 1, 1, "编辑");
        menu.add(1, 2, 1, "删除");
        menu.add(1,3,1,"复制");
    }
    //重写，执行编辑和删除
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = menuInfo.position;
        switch (item.getItemId()) {
            case 1:
                Map<String,Object> mapList=GetListViewValueAtPostion(position);
                Student EditInfor=new Student(mapList.get("image").toString().getBytes(),mapList.get("ID").toString(),mapList.get("name").toString(),mapList.get("college").toString(),mapList.get("major").toString());
                Intent intent = new Intent(ActivityMain.this, ActivityStudent.class);
                Bundle bundle = new Bundle();
                bundle.putInt("EditID", Integer.parseInt(mapList.get("_id").toString()));
                bundle.putSerializable("EditStudentInfor", EditInfor);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 2:
                listView = findViewById((R.id.mylist));
                Dialog(listView);
            case 3:
                listView = findViewById((R.id.mylist));
                final LinearLayout main=(LinearLayout) listView.getChildAt(position);
                final TextView ID=(TextView)main.findViewById(R.id.ID) ;
                clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null,ID.getText().toString()));
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    //设置ListView适配器
    private void SetListView(Cursor cursor){
        simpleCursorAdapter = new SimpleCursorAdapter(this,  R.layout.simple_item,cursor, new String[]{"image","ID", "name", "college", "major"}, new int[]{R.id.image,R.id.ID, R.id.name, R.id.college, R.id.major}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        SimpleCursorAdapter.ViewBinder viewBinder=new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(cursor.getColumnIndex("image")==columnIndex){
                    ImageView image = (ImageView) view;
                    byte[] byteArr = cursor.getBlob(columnIndex);
                    image.setImageBitmap(BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length));
                }
                else if(cursor.getColumnIndex("ID")==columnIndex){
                    TextView ID=(TextView) view;
                    ID.setText(cursor.getString(columnIndex));
                }
                else if(cursor.getColumnIndex("name")==columnIndex){
                    TextView name=(TextView)view;
                    name.setText(cursor.getString(columnIndex));
                }
                else if(cursor.getColumnIndex("college")==columnIndex){
                    TextView college=(TextView)view;
                    college.setText(cursor.getString(columnIndex));
                }
                else if(cursor.getColumnIndex("major")==columnIndex){
                    TextView major=(TextView)view;
                    major.setText(cursor.getString(columnIndex));
                }
                return true;
            }
        };
        simpleCursorAdapter.setViewBinder(viewBinder);
        listView = findViewById((R.id.mylist));
        listView.setAdapter(simpleCursorAdapter);
    }
    //添加或编辑新项
    private void AddNewInfor(Intent intent){
        Student students = (Student) intent.getSerializableExtra("student");
        student.add(new Student(students.image, students.ID, students.names, students.college, students.major));
        int editPosition = intent.getIntExtra("EditID", -1);
        cursor=studentDAL.selectAllStudent();
        if(editPosition!=-1) {
            studentDAL.update(student.get(0).ID,student.get(0).names,student.get(0).college,student.get(0).major,editPosition);
        }
        else {
            studentDAL.insert(student.get(0).image,student.get(0).ID,student.get(0).names,student.get(0).college,student.get(0).major);
        }
    }
    //获取ListView中在某一项的值
    private Map<String,Object> GetListViewValueAtPostion(int position){
        listView = findViewById(R.id.mylist);
        cursor.moveToPosition(position);
        Map<String,Object> mapList=new HashMap<>();
        mapList.put("_id",cursor.getString(0));
        mapList.put("image",cursor.getBlob(1));
        mapList.put("ID",cursor.getString(2));
        mapList.put("name",cursor.getString(3));
        mapList.put("college",cursor.getString(4));
        mapList.put("major",cursor.getString(5));
        return mapList;
    }
    private void Dialog(View source){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this)
                .setTitle("删除框")
                .setMessage("确定删除吗");
        setPositiveButton(builder);
        setNegativeButton(builder)
                .create()
                .show();
    }
    //重写监听器，确定则删除成功，取消则返回
    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder){
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ToastView.ShowText(ActivityMain.this,"删除成功！",0,true);
                cursor.moveToPosition(position);
                studentDAL.delete(cursor.getInt(0));
                cursor=studentDAL.selectAllStudent();
                SetListView(cursor);
                searchView=findViewById(R.id.searchView);
                searchView.setFocusable(false);
            }
        });
    }
    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder){
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){      //设置菜单项
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){    //菜单项操作
        switch (menuItem.getItemId()){
            case R.id.add:
                Intent intent = new Intent(ActivityMain.this, ActivityStudent.class);
                startActivity(intent);
                break;
            case R.id.fresh:
                break;
            case R.id.phone:
                Intent intent1=new Intent(ActivityMain.this,ActivityPhonePlace.class);
                startActivity(intent1);
                break;
            case R.id.config:
                Intent intent2=new Intent(ActivityMain.this,ActivityConfig.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
    private void setSearchView(final SearchView searchView){    //设置搜索框，重写监听器
        listView=findViewById(R.id.mylist);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("请输入姓名、学院、专业");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                cursor=studentDAL.select(s);
                if(!cursor.moveToNext()){
                    ToastView.ShowText(ActivityMain.this,"没有查询到结果",0,true);
                }
                SetListView(cursor);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    listView.clearTextFilter();
                    cursor=studentDAL.selectAllStudent();
                }
                else {
                    listView.setFilterText(s);
                    cursor=studentDAL.select(s);
                }
                SetListView(cursor);
                return true;
            }
        });
        searchView.setFocusable(false);
        searchView.clearFocus();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(studentDBHelper!=null){
            studentDBHelper.close();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mIsExit){
                this.finish();
                Intent home=new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }
            else {
                ToastView.ShowText(ActivityMain.this,"再按一次退出", Toast.LENGTH_SHORT,screenWidth,screenHeight);
                mIsExit=true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit=false;
                    }
                },2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.linears:
                if(null==inputMethodManager){         //点击空白是软键盘放下
                    inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                }
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
    //重写onTouchEvent事件，实现左滑右滑功能
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = event.getX();

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float moveDistanceX = event.getX()-downX;
            if(moveDistanceX>=-screenWidth/10){

            }
            else if(moveDistanceX < -screenWidth/10){
                decorView.setAlpha(0.5f);
                decorView.setX(moveDistanceX);
            }
            if(event.getX()-downY>screenHeight/4){
                decorView.setScrollBarFadeDuration(100);
            }

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            float moveDistanceX =event.getX()-downX;
            if(moveDistanceX>=0){
                rebackToLeft(moveDistanceX);
                decorView.setX(0);
                decorView.setAlpha(1);
            }
            else if(moveDistanceX>=-screenWidth/10){

            }
            else if(moveDistanceX < -3*screenWidth / 14){
                continueMove(moveDistanceX);
                Student studentValue = (Student) getIntent().getSerializableExtra("takevalue");
                Intent intent = new Intent(ActivityMain.this, ActivityStudent.class);
                if(studentValue!=null){                              //一般不为空
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("takevalue",studentValue);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }else{
                rebackToLeft(moveDistanceX);
                decorView.setX(0);
                decorView.setAlpha(1);
            }
        }
        return super.onTouchEvent(event);
    }
    //重写dispatchTouchEvent方法，防止滑动操作被拦截
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

    private void rebackToLeft(float moveDistanceX){
        ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
    }
    //获取屏幕的高度、宽度
    private void getScreenInfor(){
        decorView = getWindow().getDecorView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }
}
