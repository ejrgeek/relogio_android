package com.example.relogio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private ViewHolder mViewHolder = new ViewHolder();
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private boolean mRunnableStopped = false;

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int lvl = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mViewHolder.batteryLevel.setText(lvl + "%");
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.mViewHolder.textHourMin = findViewById(R.id.idHourMinute);
        this.mViewHolder.textSeconds = findViewById(R.id.idSeconds);
        this.mViewHolder.checkBatteryLevel = findViewById(R.id.idCheckBattery);
        this.mViewHolder.batteryLevel = findViewById(R.id.idBatteryLevel);
        this.mViewHolder.showSecond = findViewById(R.id.idShowSeconds);
        this.mViewHolder.date = findViewById(R.id.idDate);
        this.mViewHolder.showDate = findViewById(R.id.idShowDate);


        this.mViewHolder.checkBatteryLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.checkBatteryLevel.isChecked()){
                    mViewHolder.batteryLevel.setVisibility(View.VISIBLE);
                }else{
                    mViewHolder.batteryLevel.setVisibility(View.INVISIBLE);
                }
            }
        });

        this.mViewHolder.showSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.showSecond.isChecked()){
                    mViewHolder.textSeconds.setVisibility(View.VISIBLE);
                }else if (!mViewHolder.showSecond.isChecked()){
                    mViewHolder.textSeconds.setVisibility(View.INVISIBLE);
                }
            }
        });

        this.mViewHolder.showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.showDate.isChecked()){
                    mViewHolder.date.setVisibility(View.VISIBLE);
                }else{
                    mViewHolder.date.setVisibility(View.INVISIBLE);
                }
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);;

        this.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    public void onResume() {
        super.onResume();
        this.mRunnableStopped = false;
        this.startClock();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mRunnableStopped = true;
    }


    private void startClock(){
        final Calendar calendar = Calendar.getInstance();
        this.mRunnable = new Runnable() {
            @Override
            public void run() {

                if (mRunnableStopped){
                    return;
                }

                calendar.setTimeInMillis(System.currentTimeMillis());

                String hourMinutes = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                String second = String.format("%02d", calendar.get(Calendar.SECOND));

                String lang = Locale.getDefault().getLanguage();
                String date;

                if (lang.equals("pt")){
                    date = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault()).format(new Date());
                }else{
                    date = new SimpleDateFormat("MM/dd/yyy", Locale.getDefault()).format(new Date());
                }

                mViewHolder.date.setText(date);
                mViewHolder.textHourMin.setText(hourMinutes);
                mViewHolder.textSeconds.setText(second);

                long now = SystemClock.uptimeMillis();

                now += (1000 - now%1000);
                mHandler.postAtTime(mRunnable, now);

            }
        };

        this.mRunnable.run();

    }

    @Override
    public void onClick(View view) {
        if (mViewHolder.checkBatteryLevel.isChecked()){
            mViewHolder.batteryLevel.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.batteryLevel.setVisibility(View.INVISIBLE);
        }

        if (mViewHolder.showSecond.isChecked()){
            mViewHolder.textSeconds.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.textSeconds.setVisibility(View.INVISIBLE);
        }

    }


    private static class ViewHolder{
        TextView textHourMin;
        TextView textSeconds;
        TextView batteryLevel;
        TextView date;
        CheckBox showSecond;
        CheckBox showDate;
        CheckBox checkBatteryLevel;

    }

}
