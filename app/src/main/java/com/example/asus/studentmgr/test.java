package com.example.asus.studentmgr;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by asus on 2019/5/6.
 */

public class test extends Activity {
    public static final int RC_CHOOSE_PHOTO = 2;
    private  static  List<Bitmap> bitmaps;
    private RecyclerView recyclerView=null;
    private Adapter adapter;
    private PictureAdapter pictureAdapter;
    public static  Bitmap bitmap=null;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_choose);
        bitmap=null;
        recyclerView=findViewById(R.id.recycler);
        bitmaps=ImageFinder.getImages(test.this,null);
        bitmaps.add(BitmapFactory.decodeResource(test.this.getResources(),R.drawable.add));
        recyclerView.setLayoutManager(new GridLayoutManager(test.this,3));
        adapter=new Adapter(bitmaps);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new myOnLongClickListener());
        adapter.setOnItemClickListener(new myOnClickListener());
    }
    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
        }
    }
    private class myOnClickListener implements Adapter.OnItemClickListener{
        @Override
        public void onItemClick(View view, int position, Bitmap data) {
            if(position==bitmaps.size()-1){
                if(ContextCompat.checkSelfPermission(test.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                    ActivityCompat.requestPermissions(test.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
                }
                else {
                    //已授权，获取照片
                    choosePhoto();
                }
            }
            else {
                pictureAdapter = new PictureAdapter(data);
                zoomImageFromThumb(view, data);
            }
        }
    }
    private class myOnLongClickListener implements Adapter.OnItemLongClickListener{
        @Override
        public void  onItemLongClick(View view, int position, Bitmap data){
            bitmap=bitmaps.get(position);
            Intent intent=new Intent(test.this,ActivityStudent.class);
            startActivity(intent);
        }
    }
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
            {
                Bitmap bitmap =null;
                bitmap = ImageUtils.handleImageOnKitKat(test.this, data);
                if(bitmap==null){
                    bitmap=BitmapFactory.decodeResource(test.this.getResources(),R.drawable.add);
                }
                bitmaps.add(bitmaps.size()-1,bitmap);
                adapter=new Adapter(bitmaps);
                recyclerView=findViewById(R.id.recycler);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new myOnClickListener());
                break;
            }
            default:
                break;
        }
    }
    private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // 如果有动画正在运行，取消这个动画
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.image_big_show, null);

        // 加载显示大图的ImageView
        final ImageView expandedImageView = (ImageView) view.findViewById(
                R.id.big_image);
        expandedImageView.setImageBitmap(bitmap);

        // 计算初始小图的边界位置和最终大图的边界位置。
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // 小图的边界就是小ImageView的边界，大图的边界因为是铺满全屏的，所以就是整个布局的边界。
        // 然后根据偏移量得到正确的坐标。
        thumbView.getGlobalVisibleRect(startBounds);
        view.findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // 计算初始的缩放比例。最终的缩放比例为1。并调整缩放方向，使看着协调。
        float startScale=0;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // 横向缩放
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // 竖向缩放
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        // 将大图的缩放中心点移到左上角。默认是从中心缩放
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        //对大图进行缩放动画
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setAdapter(pictureAdapter);

        // 点击大图时，反向缩放大图，然后隐藏大图，显示小图。
        //final float startScaleFinal = startScale;
        pictureAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Bitmap data) {
               /* if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();*/
                //mCurrentAnimator = set;
                recyclerView=findViewById(R.id.recycler);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemLongClickListener(new myOnLongClickListener());
                adapter.setOnItemClickListener(new myOnClickListener());
            }
        });
    }
}
