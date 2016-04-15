package com.sgiosviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.Calendar;

/**
 * Created by shubhank on 10/04/16.
 */

public class SGPickerView1 extends FrameLayout {
    private Context mContext;
    private MyScrollView mScrollView;

    private int height = 360;

    public SGPickerView1(Context context) {
        super(context);
        mContext = context;
    }


    public SGPickerView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //setBackgroundColor(Color.argb(50, 225, 225, 225));


        //FrameLayout frame = new FrameLayout(mContext);
        //this.addView(frame);

        mScrollView = new MyScrollView(context);
        this.addView(mScrollView);
        mScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        mScrollView.setVerticalScrollBarEnabled(false);


        TextView debugLabel = new TextView(mContext);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,30);
        //debugLabel.setText("Something");
        //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        debugLabel.setLayoutParams(params1);
        this.addView(debugLabel);
        mScrollView.debugLabel = debugLabel;

        final RelativeLayout layout = new RelativeLayout(mContext);
        mScrollView.addView(layout);


        View highlighter = new View(context);
        //highlighter.setBackgroundColor(Color.argb(20,40,40,80));
        highlighter.setBackgroundColor(Color.LTGRAY);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        params.topMargin = height/2 - 40;
        highlighter.setLayoutParams(params);
        this.addView(highlighter);

        View highlighter2 = new View(context);
        //highlighter.setBackgroundColor(Color.argb(20,40,40,80));
        highlighter2.setBackgroundColor(Color.LTGRAY);
         params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        params.topMargin = height/2 + 40;
        highlighter2.setLayoutParams(params);
        this.addView(highlighter2);


        LinearLayout layout1 = new LinearLayout(mContext);
        layout1.setOrientation(LinearLayout.VERTICAL);
        layout1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(layout1);


        for (int i = 0 ; i < 40 ; i++) {
            TextView tv1 = new TextView(context);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            //params3.topMargin = 5;
            tv1.setLayoutParams(params3);
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);

            //tv1.setBackgroundColor(Color.RED);
            tv1.setTextSize(24);
            if (i < 2 || i > 37) {
                tv1.setText("");
            }
            else {

                tv1.setText("Number " + String.valueOf(i * 3));
            }

            layout1.addView(tv1);

        }


        /*
        final Context a = mContext;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("tag", " inner scroll changed to false");
                Log.w("tag", " scrolled by dis " + String.valueOf(a));
                //mScrollView.smoothScrollBy(0, a);
                View highlighter2 = new View(a);
                highlighter2.setBackgroundColor(Color.RED);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
                //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                params.topMargin = mScrollView.getScrollY() + height/2;
                highlighter2.setLayoutParams(params);
                layout.addView(highlighter2);
            }
        },8000);

        */
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //mScrollView.setOnScrollChangeListener(this);

    }


    private class MyScrollView extends ScrollView {
        private Runnable scrollerTask;
        private int initialPosition;
        private int newCheck = 400;
        private boolean mIsFling;
        private long lastScrollDateTime;
        private boolean innerScrolling;
        public TextView debugLabel;

        public int getMiddle() {
            return  getScrollY() + height/2;
        }

        public MyScrollView(Context context) {
            super(context);



            scrollerTask = new Runnable() {
                @Override
                public void run() {
                    //Log.w("tag","last time = " + String.valueOf(Calendar.getInstance().getTimeInMillis() - lastScrollDateTime ));
                    if (Calendar.getInstance().getTimeInMillis() - lastScrollDateTime >= newCheck) {
                        innerScrolling = true;
                        Log.w("tag", " inner scroll changed to true");
                        lastScrollDateTime = Calendar.getInstance().getTimeInMillis();
                        RelativeLayout relLayout = (RelativeLayout)getChildAt(0);
                        LinearLayout linLayout = (LinearLayout)relLayout.getChildAt(0);

                        boolean found = false;
                        int distance = 0;
                        for(int _numChildren = linLayout.getChildCount() - 1 ; _numChildren >= 0 ; --_numChildren) {
                            View _child = linLayout.getChildAt(_numChildren);
                            Rect _bounds = new Rect();
                            _child.getHitRect(_bounds);

                            if (_bounds.contains(0, MyScrollView.this.getScrollY())) {
                                TextView tv = (TextView)linLayout.getChildAt(_numChildren + 3);
                                TextView secondTv = (TextView)linLayout.getChildAt(_numChildren + 4);

                                distance = (-36 -  (tv.getTop() - getMiddle())) * -1;


                                int firstDist = 0;
                                int secondDist = 0;
                                boolean positiveScrollFirst = true;
                                boolean positiveScrollSecond = true;

                                //Log.w("tag", " first diff " + String.valueOf(Math.abs(((getScrollY() + height / 2)) - secondTv.getTop())) + " second tv diff =  " + String.valueOf(Math.abs(((getScrollY() + height / 2)) - tv.getTop())));
                                /*

                                if (secondTv.getTop() > getMiddle() ) {
                                    Log.w("tag", " second tv diff from middle is " + String.valueOf(secondTv.getTop() - getMiddle()));
                                    firstDist = secondTv.getTop() - getMiddle();
                                }
                                else {
                                    Log.w("tag", " second tv diff from middle is  B " + String.valueOf( getMiddle() - secondTv.getTop() ));
                                    firstDist = getMiddle() - secondTv.getTop();
                                    positiveScrollFirst = false;
                                }


                                if (tv.getTop() > getMiddle() ) {
                                    Log.w("tag", " first tv diff from middle is " + String.valueOf(tv.getTop() - getMiddle()));
                                    secondDist = tv.getTop() - getMiddle();
                                }
                                else {
                                    Log.w("tag", " first tv diff from middle is  B " + String.valueOf( getMiddle() - tv.getTop() ));
                                    secondDist = getMiddle() - tv.getTop();
                                    positiveScrollFirst = false;
                                }

                                if (firstDist > secondDist) {
                                    distance = firstDist;
                                    if (!positiveScrollFirst) { distance = distance * -1;; }
                                }
                                else {
                                    distance = secondDist;
                                    if (!positiveScrollSecond) { distance = distance * -1; }
                                }

                                if (Math.abs(((getScrollY() + height / 2)) - secondTv.getTop()) <  Math.abs(((getScrollY() + height / 2)) - tv.getTop())) {
                                    distance = (secondTv.getTop() -  (getScrollY() + height / 2));
                                }
                                else {
                                    distance = ((getScrollY() + height/2) - tv.getTop());
                                }
                                */

                                Log.w("tag", " first tv text " + tv.getText().toString() + " second tv text " + ((TextView)linLayout.getChildAt(_numChildren + 4)).getText().toString());
                                if (((getScrollY() + height/2))   - tv.getTop() > 22) {
                                    Log.w("tag", " using second tv");
                                    tv = (TextView)linLayout.getChildAt(_numChildren + 4);
                                    //distance = tv.getTop() - ((getScrollY() + height / 2));
                                }
                                Log.w("tag", " middle Y =  " + String.valueOf(getScrollY() + height/2));
                                Log.w("tag", " fourth TV Y =  " + String.valueOf(tv.getTop()));
                                found = true;
                                break;


                            }
                        }

                        final int a = distance;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.w("tag", " inner scroll changed to false");
                                Log.w("tag", " scrolled by dis " + String.valueOf(a));
                                mScrollView.smoothScrollBy(0, a);
                                /*
                                View highlighter2 = new View(mContext);
                                highlighter2.setBackgroundColor(Color.RED);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,2);
                                //params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                                params.topMargin = mScrollView.getScrollY() + a;
                                highlighter2.setLayoutParams(params);
                                RelativeLayout relLayout = (RelativeLayout)getChildAt(0);
                                relLayout.addView(highlighter2);
                                */
                                innerScrolling = false;
                            }
                        },200);
                    }
                }
            };
            /*
            scrollerTask = new Runnable() {

                public void run() {
                    Log.w("tag", " running");


                    int newPosition = getScrollY();
                    if(initialPosition - newPosition == 0){//has stopped
                        RelativeLayout relLayout = (RelativeLayout)getChildAt(0);
                        LinearLayout linLayout = (LinearLayout)relLayout.getChildAt(0);

                        for(int _numChildren = linLayout.getChildCount() - 1 ; _numChildren >= 0 ; --_numChildren) {
                            View _child = linLayout.getChildAt(_numChildren);
                            Rect _bounds = new Rect();
                            _child.getHitRect(_bounds);

                            if (_bounds.contains(0, MyScrollView.this.getScrollY())) {
                                TextView tv = (TextView)linLayout.getChildAt(_numChildren + 3);
                                if (((getScrollY() + height/2))   - tv.getTop() > 22) {
                                    tv = (TextView)linLayout.getChildAt(_numChildren + 4);
                                }
                                Log.w("tag", " middle Y =  " + String.valueOf(getScrollY() + height/2));
                                Log.w("tag", " fourth TV Y =  " + String.valueOf(tv.getTop()));
                                mScrollView.smoothScrollBy(0, ((getScrollY() + height/2))   - tv.getTop() );
                            }
                        }

                    }else{
                        initialPosition = getScrollY();
                        MyScrollView.this.postDelayed(scrollerTask, newCheck);
                    }
                }
            };


            this.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        initialPosition = getScrollY();
                        MyScrollView.this.postDelayed(scrollerTask, newCheck);
                    }

                    return false;
                }
            });
            */
        }


        @Override
        public void fling(int velocityY) {
            super.fling(velocityY);
            mIsFling = true;
        }


        @Override
        protected void onScrollChanged(int newX, int newY, int oldX, int oldY) {
            super.onScrollChanged(newX, newY, oldX, oldY);

            if (!innerScrolling) {
                lastScrollDateTime = Calendar.getInstance().getTimeInMillis();
                MyScrollView.this.postDelayed(scrollerTask, newCheck);
            }


            //Log.w("tag", " x = " + String.valueOf(newX) + " y = " + String.valueOf(newY));
            RelativeLayout relLayout = (RelativeLayout)getChildAt(0);
            LinearLayout linLayout = (LinearLayout)relLayout.getChildAt(0);

            int _numChildren = relLayout.getChildCount();
            for(_numChildren = linLayout.getChildCount() - 1 ; _numChildren >= 0 ; --_numChildren)
            {
                View _child = linLayout.getChildAt(_numChildren);
                Rect _bounds = new Rect();
                _child.getHitRect(_bounds);
//                Rect myViewRect = new Rect();
//                _child.getLocalVisibleRect(myViewRect);
//                Log.w("tag", String.valueOf(myViewRect.top));

                if (_bounds.contains(newX, newY)) {
                    //Log.w("tag",_child.toString());
                    //Log.w("tag",relLayout.toString());
                   // Log.w("tag",linLayout.toString());
                    //_child.setBackgroundColor(Color.BLUE);

                    //Log.w("tag","child top = " + String.valueOf(_child.getTop()) + " new Y " + String.valueOf(newY));
                    //Log.w("tag","diff = " + (newY - _child.getTop()) );

                    //linLayout.getChildAt(_numChildren + 1).setBackgroundColor(Color.RED);

                    TextView firstTv = (TextView)_child;
                    TextView secondTv = (TextView)linLayout.getChildAt(_numChildren + 1);
                    TextView thirdTv = (TextView)linLayout.getChildAt(_numChildren + 2);
                    TextView fourthTv = (TextView)linLayout.getChildAt(_numChildren + 3);
                    TextView fifthTv = (TextView)linLayout.getChildAt(_numChildren + 4);
                    TextView sixthTv = (TextView)linLayout.getChildAt(_numChildren + 5);
                    TextView sevenTv = (TextView)linLayout.getChildAt(_numChildren + 6);

                    //debugLabel.setText(String.valueOf(fourthTv.getTop() - getMiddle()));

                    /*
                    firstTv.setTextSize(15 - ((newY - firstTv.getTop()) / 100.0f));
                    secondTv.setTextSize(19  - ((newY - secondTv.getTop())/100.0f) );
                    thirdTv.setTextSize(24 - ((newY - thirdTv.getTop())/100.0f)  );
                    fourthTv.setTextSize(28);
                    fifthTv.setTextSize(24);
                    sixthTv.setTextSize(19);
                    sevenTv.setTextSize(15);
                    */


                    firstTv.setTextSize(16 - ((newY - firstTv.getTop()) / 100.0f));
                    secondTv.setTextSize(20  - ((newY - secondTv.getTop())/80.0f) );
                    thirdTv.setTextSize(24 - ((newY - thirdTv.getTop())/60.0f)  );
                    fourthTv.setTextSize(28);
                    fifthTv.setTextSize(24 + ((newY - thirdTv.getTop())/60.0f));
                    sixthTv.setTextSize(20 + ((newY - secondTv.getTop())/80.0f));
                    sevenTv.setTextSize(16 + ((newY - firstTv.getTop()) / 100.0f));

                    firstTv.setRotationX(40);
                    secondTv.setRotationX(30);
                    thirdTv.setRotationX(10);
                    fourthTv.setRotationX(0);
                    fifthTv.setRotationX(-10);
                    sixthTv.setRotationX(-30);
                    sevenTv.setRotationX(-40);

                    int grayColor = Color.rgb(180, 180, 180);
                    firstTv.setTextColor(grayColor);
                    secondTv.setTextColor(grayColor);
                    thirdTv.setTextColor(grayColor);
                    fourthTv.setTextColor(Color.DKGRAY);
                    fifthTv.setTextColor(grayColor);
                    sixthTv.setTextColor(grayColor);
                    sevenTv.setTextColor(grayColor);

                    /*
                    Rect myViewRect = new Rect();
                    _child.getGlobalVisibleRect(myViewRect);
                    Log.w("tag", String.valueOf(myViewRect.top));

                    Rect scrollRect = new Rect();
                    this.getLocalVisibleRect(scrollRect);
                    Log.w("tag", String.valueOf(scrollRect.top));
                    */
                }
            }
        }
    }

}

/*
public class SGPickerView extends ListView {

    private Context mContext;
    private LayoutInflater inflater;

    public SGPickerView(Context context) {
        super(context);
        mContext = context;
    }

    public SGPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(getContext());
        this.setDividerHeight(0);
        this.setAdapter(new SGPickerViewAdapter());
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


            int firstVisible = this.getFirstVisiblePosition()
                    - this.getHeaderViewsCount();
            int lastVisible = this.getLastVisiblePosition()
                    - this.getHeaderViewsCount();

            View child = this.getChildAt(lastVisible
                    - firstVisible);
            int offset = child.getTop() + child.getMeasuredHeight()
                    - this.getMeasuredHeight();
            if (offset > 0) {
                this.smoothScrollBy(offset, 200);
            }
         ///

    }

    private class SGxwPickerViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = inflater.inflate(R.layout.textview, null);



            return v;
        }

    }

}
*/