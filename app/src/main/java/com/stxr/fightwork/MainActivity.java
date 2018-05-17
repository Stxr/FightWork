package com.stxr.fightwork;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.stxr.fightwork.view.CountingDownClock;
import com.stxr.fightwork.view.MyCountDownTimer;

public class MainActivity extends AppCompatActivity implements MyCountDownTimer.CountingDownCallBack {
    public static final String CHANNEL_ID_1 = "channel_id_1";
    private CountingDownClock clock;
    private int startTime;
    private Notification.Builder builder;
    private Vibrator vibrator;
    private MyCountDownTimer countDownTimer;
    private boolean mPause = true;
    private LinearLayout mLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLl = findViewById(R.id.ll_time_select);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final FloatingActionButton fab = findViewById(R.id.fab);
        clock = findViewById(R.id.clock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPause) {
                    countDownTimer.start();
                    mLl.setVisibility(View.GONE);
                    mPause = false;
                    fab.setImageResource(R.drawable.ic_action_pause);
                } else {
                    mLl.setVisibility(View.VISIBLE);
                    countDownTimer.pause();
                    fab.setImageResource(R.drawable.ic_action_start);
                    mPause = true;
                }
            }
        });


    }
    @Override
    public void onTick(long millis) {
        int hour = (int) (millis / 1000 / 3600);
        int minutes = (int) ((millis / 1000 - hour * 3600) / 60);
        int second = (int) (millis / 1000 - minutes * 60 - hour * 3600);
        clock.setTime(hour, minutes, second);
        clock.setProgress(startTime - (float) millis / startTime * 100);
    }

    @Override
    public void onFinish() {
        mLl.setVisibility(View.VISIBLE);
        clock.setProgress(100);
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //通知栏
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, 254));
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_1,
                    "channel_name_1", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(MainActivity.this, CHANNEL_ID_1);
        } else {
            vibrator.vibrate(1000);
            builder = new Notification.Builder(MainActivity.this);
        }
        Notification notification = builder
                .setContentText("时间到了")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notification.defaults = Notification.DEFAULT_ALL; // 设置通知震动和声音为默认
        manager.notify(101, notification);
    }

    public void timeSelect(View view) {
        int id = view.getId();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (id == R.id.tv_5) {
            startTime = 5 * 1000 * 60;
            clock.setTime(0, 5, 0);
        } else if (id == R.id.tv_10) {
            startTime = 10 * 1000 * 60;
            clock.setTime(0, 10, 0);
        }else if (id == R.id.tv_15) {
            startTime = 15 * 1000 * 60;
            clock.setTime(0, 15, 0);
        }else if (id == R.id.tv_30) {
            startTime = 30 * 1000 * 60;
            clock.setTime(0, 30, 0);
        }else if (id == R.id.tv_60) {
            startTime = 60 * 1000 * 60;
            clock.setTime(0, 60, 0);
        }
        countDownTimer = new MyCountDownTimer(startTime, 1000);
        countDownTimer.setOnCountingDownCallBack(MainActivity.this);
    }
}
