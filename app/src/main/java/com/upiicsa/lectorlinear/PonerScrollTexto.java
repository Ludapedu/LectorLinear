package com.upiicsa.lectorlinear;

/**
 * Created by luis.perez on 17/11/2016.
 */

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

public class PonerScrollTexto extends TextView {

    // scrolling feature
    private Scroller mSlr;

    // milliseconds for a round of scrolling
    private int mRndDuration = 50000;

    // the X offset when paused
    private int mXPaused = 0;

    // whether it's being paused
    private boolean mPaused = true;

    private int num = 0;

    private OnScrollEndListener listener;


    public PonerScrollTexto(Context context) {
        super(context);
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);

    }

    public PonerScrollTexto(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    /*
    * constructor
    */
    public PonerScrollTexto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // customize the TextView
        setSingleLine();
        setEllipsize(null);
        setVisibility(INVISIBLE);
    }

    public void startScroll() {
        setVisibility(View.VISIBLE);
        // begin from the very right side
        mXPaused = -1 * getWidth();
        // assume it's paused
        mPaused = true;
        resumeScroll();
    }

    public void resumeScroll() {

        if (!mPaused)
            return;

        // Do not know why it would not scroll sometimes
        // if setHorizontallyScrolling is called in constructor.
        setHorizontallyScrolling(true);

        // use LinearInterpolator for steady scrolling
        mSlr = new Scroller(this.getContext(), new LinearInterpolator());
        setScroller(mSlr);

        int scrollingLen = calculateScrollingLen();
        int distance = scrollingLen - (getWidth() + mXPaused);
        int duration = (new Double(mRndDuration * distance * 1.00000
                / scrollingLen)).intValue();
        setVisibility(VISIBLE);
        mSlr.startScroll(mXPaused, 0, distance, 0, duration);
        invalidate();
        mPaused = false;
    }

    private int calculateScrollingLen() {
        TextPaint tp = getPaint();
        Rect rect = new Rect();
        String strTxt = getText().toString();
        tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
        int scrollingLen = rect.width() + getWidth();
        rect = null;
        return scrollingLen;
    }

    public void pauseScroll() {
        if (null == mSlr)
            return;

        if (mPaused)
            return;

        mPaused = true;

        // abortAnimation sets the current X to be the final X,
        // and sets isFinished to be true
        // so current position shall be saved
        mXPaused = mSlr.getCurrX();
        mSlr.abortAnimation();
    }

    public void stopScroll(){
        setVisibility(View.INVISIBLE);
        mSlr.abortAnimation();
    }

    public void computeScroll() {
        super.computeScroll();

        if (null == mSlr)
            return;

        if (mSlr.isFinished() && (!mPaused)) {
            mSlr.abortAnimation();
            listener.OnScrollEnd();
            num += 1;
        }
    }

    public boolean computeScrollOffset(){
        if (mSlr.isFinished())
            return true;
        else
            return false;
    }

    public void SetOnScrollEndListener(OnScrollEndListener l){
        listener = l;
    }

    public int getRndDuration() {
        return mRndDuration;
    }

    public void setRndDuration(int duration) {
        this.mRndDuration = duration;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public int getNumberOfScrolls()
    {
        return num;
    }

    public int getmXPaused()
    {
        return mXPaused;
    }

    public void setmXPaused(int setmXPause)
    {
        this.mXPaused = setmXPause;
    }



}

