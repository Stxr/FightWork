package com.stxr.fightwork.view;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by stxr on 2018/5/16.
 */

public class MyCountDownTimer {
    public static final int MSG = 1;
    private final long mMillisInFuture;
    private final long mCountdownInterval;
    private boolean mPause = false;
    private boolean mCancel = false;
    private long mStopTime;
    private long mLeftTime;
    private CountingDownCallBack mCallback;

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public MyCountDownTimer start() {
        if (mMillisInFuture <= 0) {
            mCallback.onFinish();
            return this;
        }
        if (mPause) {
            mPause = false;
            mStopTime = mLeftTime + SystemClock.elapsedRealtime();
        } else {
            mStopTime = mMillisInFuture + SystemClock.elapsedRealtime();
        }
        handler.sendMessage(handler.obtainMessage(MSG));
        return this;
    }

    public void cancel() {
        mCancel = true;
        handler.removeMessages(MSG);
    }


    public void pause() {
        mPause = true;
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (MyCountDownTimer.this) {
                if (mCancel||mPause) {
                    return;
                }
                mLeftTime = mStopTime - SystemClock.elapsedRealtime();
                if (mLeftTime <= 0) {
                    mCallback.onFinish();
                } else {
                    long lastTick = SystemClock.elapsedRealtime();
                    mCallback.onTick(mLeftTime);
                    //计算执行onTick所需要的时间
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTick;
                    long delay;
                    //如果剩下的时间比间隔时间还少的话
                    if (mLeftTime < mCountdownInterval) {
                        delay = mLeftTime - lastTickDuration;
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;
                        while (delay < 0) delay += mCountdownInterval;
                    }
                    if (!mPause) {
                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        }
    };

    public void setOnCountingDownCallBack(CountingDownCallBack callBack) {
        mCallback = callBack;
    }

    public interface CountingDownCallBack {
        void onTick(long millisUntilFinished);

        void onFinish();

    }
}
