package com.stxr.fightwork.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.stxr.fightwork.R;

import java.util.Locale;

/**
 * Created by stxr on 2018/5/16.
 */

public class CountingDownClock extends View {

    private int mCircleColor;
    private int mTextColor;
    private float mSize;
    private int mStartTime;
    private Paint mPaintCircle;
    private RectF mRectF;
    private float mProgress;
    private Paint mPaintText;
    private String mText;
    private float textSize;
    private int mSecond;
    private int mMinutes;
    private int mHour;


    public CountingDownClock(Context context) {
        super(context);
    }

    public CountingDownClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public CountingDownClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mPaintCircle = new Paint();
        mPaintText = new Paint();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountingDownClock);
        mCircleColor = ta.getColor(R.styleable.CountingDownClock_circle_color, Color.YELLOW);
        mTextColor = ta.getColor(R.styleable.CountingDownClock_text_color, Color.WHITE);
        mStartTime = ta.getInt(R.styleable.CountingDownClock_start_time, 0);
        mText = ta.getString(R.styleable.CountingDownClock_text);
        ta.recycle();
        mPaintCircle.setColor(mCircleColor);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintText.setColor(mTextColor);
        mPaintText.setAntiAlias(true);//抗锯齿
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setAntiAlias(true); //抗锯齿
        setProgress(100);
        setTime(0, 0, 0);
    }

    public void setTime(int hour, int minutes, int second) {
        if (hour >= 0) {
            mHour = Math.min(hour, 24);
        }
        if (minutes >= 0) {
            mMinutes =  Math.min(minutes, 60);
        }
        if (second >= 0) {
            mSecond =  Math.min(second, 60);
        }
        mText = String.format(Locale.CHINESE,"%d:%02d:%02d", mHour, mMinutes, mSecond);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(setMeasure(widthMeasureSpec), setMeasure(heightMeasureSpec));

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("---------", "getMeasuredHeight(): " + getMeasuredHeight() + " getMeasuredWidth():  " + getMeasuredWidth());
        mSize = Math.min(right, bottom);
        mRectF = new RectF((float) (mSize * 0.1),
                (float) (mSize * 0.1),
                (float) (mSize * 0.9),
                (float) (mSize * 0.9));
        mPaintCircle.setStrokeWidth(mSize / 50);
        textSize = mSize / 6;
        mPaintText.setTextSize(textSize);
    }

    int setMeasure(int measure) {
        int result = 500;
        int mode = MeasureSpec.getMode(measure);
        int size = MeasureSpec.getSize(measure);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(size, 500);
                break;
            default:
                break;
        }
        return result;
    }

    public void setProgress(float  progress) {
        if (progress == 100) {
            mProgress = 360;
        } else {
            mProgress = (float) ((progress % 100) * 3.6);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawArc(mRectF, 270, mProgress, false, mPaintCircle);
        canvas.drawText(mText, 0, mText.length(), mSize / 2 - mText.length() * textSize / 4, mSize / 2 + textSize / 2, mPaintText);
        invalidate();
    }
}
