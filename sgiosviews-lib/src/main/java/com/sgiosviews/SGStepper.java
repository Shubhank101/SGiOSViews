package com.sgiosviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shubhank on 07/04/16.
 */
public class SGStepper extends View {

    private Activity mActivity;

    // border Paint
    private Paint mPaint;

    // fill paint
    private Paint fPaint;

    // variables for size
    private int start;
    private int width;
    private int height;
    private int roundedRadius;

    // detecting presses
    private boolean decrementPressed = false;
    private boolean incrementPressed = false;

    private int valueCounter = 0;

    private Timer decrementTimer = null;
    private Timer incrementTimer = null;

    private SGStepperListener valueChangedlistener;

    public interface SGStepperListener {
        void valuesChanged(SGStepper stepper, int newValue);
        void afterValueChanged(SGStepper stepper, int finalValue);
    }

    public void setValueChangedlistener(SGStepperListener newListener) {
        this.valueChangedlistener = newListener;
    }


    public void setBorderColor(int color) {
        if (mPaint != null) {
            mPaint.setColor(color);
        }
    }

    public void setHighlightColor(int color) {
        if (fPaint != null) {
            fPaint.setColor(color);
        }
        fPaint.setAlpha(40);

    }

    public  SGStepper(Context context) {
        super(context);
    }

    public  SGStepper(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()){
            return;
        }

        if(context instanceof Activity) {
            mActivity = (Activity)context;
        }

        start = 0;
        width = 250;
        width = (int)SGStepper.convertDpToPixel(90,context);
        height = (int)SGStepper.convertDpToPixel(30,context);;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        fPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int highlightDefColor = Color.BLUE;
        fPaint.setStyle(Paint.Style.FILL);

        TypedArray values = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SGStepper, 0, 0);

        try {
            int borderColor = values.getColor(R.styleable.SGStepper_bordercolor, Color.BLUE);
            mPaint.setColor(borderColor);

            int highlightColor = values.getColor(R.styleable.SGStepper_highlightcolor, highlightDefColor);
            fPaint.setColor(highlightColor);

            roundedRadius = values.getDimensionPixelSize(R.styleable.SGStepper_roundedradius,0);
        }
        catch (Exception e) {

        }
        fPaint.setAlpha(40);

        values.recycle();

    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isInEditMode()){
            return;
        }

        super.onDraw(canvas);

        int padding = 5;

        Path roundedPath = RoundedRect(padding + start, 0, width, height, roundedRadius, roundedRadius, false);
        canvas.drawPath(roundedPath, mPaint);

        if (decrementPressed) {
            Path leftHalfPath = LeftRect(start, 2, width / 2 - padding, height - 2, roundedRadius, roundedRadius, true);
            canvas.drawPath(leftHalfPath, fPaint);
        }

        if (incrementPressed) {
            Path leftHalfPath = RightRect(start + width / 2, 2, width, height - 2, roundedRadius, roundedRadius, true);
            canvas.drawPath(leftHalfPath, fPaint);
        }

        // middle line
        canvas.drawLine(width / 2, 0, width / 2, height, mPaint);

        float signWidth = height - (height * 0.23f);
        // draw + icon vertical line
        canvas.drawLine(width * 0.75f, height - signWidth , width * 0.75f, signWidth, mPaint);
        // draw + icon horizontal line
        canvas.drawLine(width * 0.75f - (signWidth * 0.45f), height * 0.5f, (width * 0.75f) + (signWidth * 0.45f), height * 0.5f, mPaint);

        // draw the - line
        canvas.drawLine(width * 0.25f - (signWidth * 0.45f),  height * 0.5f, width * 0.25f  + (signWidth * 0.45f),  height * 0.5f, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth() + width;
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);


        setMeasuredDimension(w, height);
    }

    static public Path LeftRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right - rx, top);
        //path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        //path.lineTo(0, -ry);

        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        if (conformToOriginalPost) {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(width - rx + 3, 0);
           // path.rLineTo(0, -ry);
        }
        else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.lineTo(right, -height);

        path.close();//Given close, last lineto can be removed.

        return path;
    }


    static public Path RightRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-width + rx, 0);
        //path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, height);

        if (conformToOriginalPost) {
            //path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        }
        else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    static public Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean conformToOriginalPost) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        if (conformToOriginalPost) {
            path.rLineTo(0, ry);
            path.rLineTo(width, 0);
            path.rLineTo(0, -ry);
        }
        else {
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
            path.rLineTo(widthMinusCorners, 0);
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();



        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < width/2) {
                    decrementPressed = true;
                    decrementPressedMethod();
                }
                if (x > width/2) {
                    incrementPressed = true;
                    incrementPressedMethod();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                decrementPressed = false;
                incrementPressed = false;
                if (decrementTimer != null) {
                    decrementTimer.cancel();
                    decrementTimer = null;
                }

                if (incrementTimer != null) {
                    incrementTimer.cancel();
                    incrementTimer = null;
                }

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (SGStepper.this.valueChangedlistener != null) {
                            SGStepper.this.valueChangedlistener.afterValueChanged(SGStepper.this, valueCounter);
                        }
                    }
                });

        }

        invalidate();

        return true;
    }


    private void decrementPressedMethod() {

        decrementTimer = new Timer();
        decrementTimer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        if (valueCounter > 0) {
                            valueCounter--;
                        }


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (SGStepper.this.valueChangedlistener != null) {
                                    SGStepper.this.valueChangedlistener.valuesChanged(SGStepper.this,valueCounter);
                                }
                            }
                        });
                    }
                }, 0, 200);

    }

    private void incrementPressedMethod() {

        incrementTimer = new Timer();
        incrementTimer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        valueCounter++;

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (SGStepper.this.valueChangedlistener != null) {
                                    SGStepper.this.valueChangedlistener.valuesChanged(SGStepper.this,valueCounter);
                                }
                            }
                        });

                    }
                }, 0, 200);
    }



}
