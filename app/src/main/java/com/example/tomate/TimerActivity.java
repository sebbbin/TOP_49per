package com.example.tomate;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomate.databinding.ActivityTimerBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.logging.Handler;


public class TimerActivity extends AppCompatActivity {
    private Dialog exitDialog;
    private Dialog timeControlDialog;
    BackgroundFragment backgroundFragment;
    public int timer_second = 0;
    public int timer_minute = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.tomate.databinding.ActivityTimerBinding timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());
        backgroundFragment = new BackgroundFragment();

        // firebase test code
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("message").push().setValue("2");

        exitDialog = new Dialog(TimerActivity.this);       // Dialog 초기화
        exitDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀 제거
        exitDialog.setContentView(R.layout.dialog_exit);             // xml 레이아웃 파일과 연결

        TextView exitButton = findViewById(R.id.activity_timer_exit_tv);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // execute
                addBackgroundFragment();
                showExitDialog();
            }
        });

        showTimeControlDialog();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int second = 0;
//                int minute = 0;
//                while (true) {
//                    // 코드 작성
//                    second++;
//                    minute = second / 60;
//                    Log.d("time", String.format("%d", second));
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }
    private void showTimeControlDialog(){
        timeControlDialog  = new Dialog(TimerActivity.this);
        timeControlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeControlDialog.setContentView(R.layout.dialog_timecontrol);
        timeControlDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NumberPicker hourPicker = timeControlDialog.findViewById(R.id.hourPicker);
        NumberPicker minutePicker = timeControlDialog.findViewById(R.id.minutePicker);
        NumberPicker secondsPicker = timeControlDialog.findViewById(R.id.secondsPicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        SharedPreferences preferences = getSharedPreferences("timeControl", MODE_PRIVATE);
        int hour = preferences.getInt("hour", 0);
        int minute = preferences.getInt("minute", 0);
        int second = preferences.getInt("second", 0);
        hourPicker.setValue(hour);
        minutePicker.setValue(minute);
        secondsPicker.setValue(second);

        TimerFragment timerFragment = new TimerFragment(0, -1);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_timer_main_frm, timerFragment);
        transaction.commit();

        ImageView noBtn = timeControlDialog.findViewById(R.id.dialog_timecontrol_no_iv);
        ImageView yesBtn = timeControlDialog.findViewById(R.id.dialog_timecontrol_yes_iv);

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기본 25분 0초로 설정
                //TimerFragment timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.activity_timer_main_frm);
                if (timerFragment != null) {
                    timer_minute = 25;
                    timer_second = 0;
                    timerFragment.setTime(25,0);
                    timerFragment.startTimer(); // 타이머 시작
                }
                timeControlDialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 선택한 시간으로 설정
                int selectedHour = hourPicker.getValue();
                int selectedMinute = minutePicker.getValue();
                int selectedSeconds = secondsPicker.getValue();

                SharedPreferences preferences = getSharedPreferences("timeControl", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("hour", selectedHour);
                editor.putInt("minute", selectedMinute);
                editor.putInt("second", selectedSeconds);
                editor.commit();

                // TimerFragment의 시간 설정
                //TimerFragment timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.activity_timer_main_frm);
                if (timerFragment != null) {
                    timer_minute = selectedMinute;
                    timer_second = selectedSeconds;
                    timerFragment.setTime(selectedMinute, selectedSeconds);
                    timerFragment.startTimer(); // 타이머 시작
                }

                timeControlDialog.dismiss();
            }
        });

        timeControlDialog.show();
    }

    private void showExitDialog(){
        exitDialog.show();
        exitDialog.setCancelable(false);
        ImageView noBtn = exitDialog.findViewById(R.id.dialog_exit_no_iv);
        ImageView yesBtn = exitDialog.findViewById(R.id.dialog_exit_yes_iv);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss(); // 다이얼로그 닫기
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(backgroundFragment);
                transaction.commit();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // execute
                // Fragment 생성




            }
        });
    }

    private void addBackgroundFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.activity_timer_main_frm, backgroundFragment);
        transaction.commit();
    }

    public void makeRestFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        RestFragment restFragment = new RestFragment();

        transaction.replace(R.id.activity_timer_main_frm, restFragment);
//        transaction.setTransition(TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }

    public void makeTimerFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        Log.d("timer_minute, second", String.format("%02d:%02d", timer_minute, timer_second));

        TimerFragment timerFragment = new TimerFragment(timer_minute, timer_second);
//        transaction.setTransition(TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.activity_timer_main_frm, timerFragment);
        transaction.commit();
    }

}

