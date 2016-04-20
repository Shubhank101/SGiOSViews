package com.sgiosviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shubhank on 10/04/16.
 */

public class SGPickerView extends FrameLayout {

    private Context mContext;
    private SGPickerScrollView mScrollView;
    private LinearLayout mLinLayout;
    private int height;

    private boolean debugging = false;

    private ArrayList<String> items;
    private int defaultItemTextColor = Color.LTGRAY;
    private int selectedItemTextColor = Color.DKGRAY;

    private SGPickerViewListener mListener;


    public interface SGPickerViewListener {
        void itemSelected(String item, int index);
    }

    public SGPickerView(Context context) {
        super(context);
        mContext = context;
    }

    public SGPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point dSize = new Point();
        display.getSize(dSize);

        height = (int)(dSize.y * 0.23f);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        mScrollView = new SGPickerScrollView(context);
        mScrollView.setLayoutParams(params);
        mScrollView.setFillViewport(true);

        this.addView(mScrollView, params);
        mScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        mScrollView.setVerticalScrollBarEnabled(false);



        final RelativeLayout layout = new RelativeLayout(mContext);
        mScrollView.addView(layout);

        mLinLayout = new LinearLayout(mContext);
        mLinLayout.setOrientation(LinearLayout.VERTICAL);
        mLinLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        layout.addView(mLinLayout);

        // highlighters
        View highlighter = new View(context);
        highlighter.setBackgroundColor(defaultItemTextColor);
         params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
        params.topMargin = height/2 - (int)(height * 0.12f);
        highlighter.setLayoutParams(params);
        this.addView(highlighter);

        View highlighter2 = new View(context);
        highlighter2.setBackgroundColor(defaultItemTextColor);
        params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
        params.topMargin = height/2 + (int)(height * 0.12f);
        highlighter2.setLayoutParams(params);
        this.addView(highlighter2);

        if (debugging) {
            TextView debugLabel = new TextView(mContext);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,50);
            //debugLabel.setText("Something");
            //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            debugLabel.setLayoutParams(params1);
            this.addView(debugLabel);
            mScrollView.debugLabel = debugLabel;
        }

        TypedArray values = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SGPickerView, 0, 0);

        try {
            defaultItemTextColor = values.getColor(R.styleable.SGPickerView_defaultTextColor, defaultItemTextColor);
            selectedItemTextColor = values.getColor(R.styleable.SGPickerView_selectedTextColor, selectedItemTextColor);
        }
        catch (Exception e) {

        }

        values.recycle();

    }


    public void setItems(ArrayList<String> items) {
        this.items = items;

        mLinLayout.removeAllViews();

        View upperView = new View(mContext);
        //upperView.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height/2 - (int)(height * 0.12f));
        upperView.setLayoutParams(params);
        mLinLayout.addView(upperView);


        for (int i = 0 ; i < this.items.size(); i++) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params3);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, height * 0.17f);
            textView.setTextColor(defaultItemTextColor);
            textView.setText(items.get(i));
            mLinLayout.addView(textView);
        }

        View bottomView;
        bottomView = new View(mContext);
        //bottomView.setBackgroundColor(Color.BLUE);
        bottomView.setLayoutParams(params);
        mLinLayout.addView(bottomView);

        mScrollView.scrollToParticularElement = true;
        mScrollView.elementIndexToScroll = 0;
        mScrollView.currentSelectedIndex = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollBy(0, 4);
            }
        }, 100);

    }


    public int getCurrentSelectedItemIndex() {
        return mScrollView.currentSelectedIndex;
    }

    public String getCurrentSelectedItem() {
        return this.items.get(getCurrentSelectedItemIndex());
    }

    public void setPickerListener(SGPickerViewListener listener) {
        this.mListener = listener;

    }

    public void setDefaultItemTextColor(int newColor) {
        this.defaultItemTextColor = newColor;
        this.mScrollView.scrollBy(0,1);
    }

    public void setSelectedItemTextColor(int newColor) {
        this.selectedItemTextColor = newColor;
        this.mScrollView.scrollBy(0,1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, height);
    }


    // Custom scrollview to detect end of scroll as well to manipulate views on scroll
    private class SGPickerScrollView extends ScrollView {

        private int newCheck = 400;
        private boolean innerScrolling;
        public TextView debugLabel;

        public boolean scrollToParticularElement = false;
        public int elementIndexToScroll = -1;
        public int currentSelectedIndex = -1;

        private Runnable scrollerTask = new Runnable() {
            @Override
            public void run() {
                    innerScrolling = true;
                    if (debugging) {
                        Log.w("tag", " inner scroll changed to true");
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (debugging) {
                                Log.w("tag", " inner scroll changed to false");
                            }
                            scrollToParticularElement = true;
                            elementIndexToScroll = currentSelectedIndex;
                            mScrollView.smoothScrollBy(0,2);

                            changeInnerScrolling();
                        }
                    },200);
                }
        };


        public SGPickerScrollView(Context context) {
            super(context);

            this.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        SGPickerScrollView.this.postDelayed(scrollerTask, newCheck);
                    }
                    return false;
                }
            });
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(widthMeasureSpec, height);
        }

        private void changeInnerScrolling() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    innerScrolling = false;
                }
            },200);
        }

        public int getMiddle() {
            return  getScrollY() + height/2;
        }

        @Override
        protected void onScrollChanged(int newX, int newY, int oldX, int oldY) {
            super.onScrollChanged(newX, newY, oldX, oldY);

            if (!innerScrolling && !scrollToParticularElement) {

            }

            RelativeLayout relLayout = (RelativeLayout)getChildAt(0);
            LinearLayout linLayout = (LinearLayout)relLayout.getChildAt(0);

            for(int _numChildren =  0 ; _numChildren < linLayout.getChildCount() ; _numChildren++)
            {
                View _child = linLayout.getChildAt(_numChildren);
                if (_child instanceof TextView == false) {
                    continue;
                }

                TextView textview = (TextView)_child;
                textview.setTextColor(defaultItemTextColor);

                boolean isCenterField = false;
                //Log.w("tag", " f text " + f.getText().toString() + " f.top " + String.valueOf(f.getTop()) + " middle" + String.valueOf(getMiddle()));
                if (textview.getTop() < getMiddle()) {
                    textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, height * 0.19f - ((getMiddle() - textview.getTop()) / 10.0f));
                    textview.setRotationX((getMiddle() - textview.getTop()) * 0.1f);
                    if (getMiddle() - (textview.getTop() + textview.getHeight()/2)  < 50) {
                        textview.setTextColor(selectedItemTextColor);
                        textview.invalidate();
                        isCenterField = true;
                    }
                }
                else {
                    textview.setTextSize(TypedValue.COMPLEX_UNIT_PX, height * 0.19f - ((textview.getTop() - getMiddle()) / 10.0f));
                    textview.setRotationX((textview.getTop() - getMiddle()) * - 0.1f);
                    if (textview.getTop() + textview.getHeight()/2 - getMiddle() < 50) {
                        textview.setTextColor(selectedItemTextColor);
                        textview.invalidate();
                        isCenterField = true;
                    }
                }


                if (isCenterField) {
                    currentSelectedIndex = SGPickerView.this.items.indexOf(textview.getText().toString());
                }

                if (scrollToParticularElement) {
                    int index = SGPickerView.this.items.indexOf(textview.getText().toString());

                    if (isCenterField) {
                        if (debugging) {
                            Log.w("tag", "center field " + String.valueOf(textview.getText().toString()));
                            debugLabel.setText(String.valueOf(textview.getTop() - getMiddle()));
                        }


                        if (index == elementIndexToScroll) {

                            if (textview.getTop() + textview.getHeight()/2  - getMiddle() > 4) {
                                mScrollView.smoothScrollBy(0, 1);
                            }
                            else if (getMiddle() - (textview.getTop() + textview.getHeight()/2) > 4) {
                                mScrollView.smoothScrollBy(0, -1);
                            }
                            else {
                                scrollToParticularElement = false;
                                if (SGPickerView.this.mListener != null) {
                                    SGPickerView.this.mListener.itemSelected(textview.getText().toString(), index);
                                }
                            }
                        }
                        else if (index < elementIndexToScroll) {
                            mScrollView.smoothScrollBy(0, 1);
                        }
                        else {
                            mScrollView.smoothScrollBy(0, -1);
                        }
                    }
                }


            }
        }
    }

}
