package com.example.asus.studentmgr.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sun on 2019/5/21.
 */

public class DrawView extends View {
    private float mX;
    private float mY;
    private Paint mGesturePaint = null;
    private Path mPath = null;
    public DrawView(Context context, AttributeSet set){
        super(context,set);
        mPath = new Path();
        mGesturePaint = new Paint();
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(20);
        mGesturePaint.setColor(Color.GRAY);
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawPath(mPath, mGesturePaint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
        }
        invalidate();
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
    private void touchDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        mPath.moveTo(x, y);
    }
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        if (dx >= 3 || dy >= 3) {
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            mPath.quadTo(previousX, previousY, cX, cY);
            mX = x;
            mY = y;
        }
    }
    public void reset() {
        mPath.reset();
        invalidate();
    }
}
