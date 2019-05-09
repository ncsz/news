package com.example.asus.studentmgr;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by asus on 2019/4/2.
 */

public class TransparentProgressDialog {
    public static Dialog createLoadingDialog(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.transparent_dialog,null);
        LinearLayout layout=view.findViewById(R.id.transparent_dialog_view);
        ImageView imageView= view.findViewById(R.id.transparent_progress);
        Animation animation= AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        imageView.startAnimation(animation);
        Dialog transparentProgressDialog=new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        transparentProgressDialog.setCancelable(false);
        transparentProgressDialog.setContentView(layout,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        return transparentProgressDialog;
    }
}
