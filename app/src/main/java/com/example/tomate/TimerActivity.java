package com.example.tomate;

import android.app.Dialog;
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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomate.databinding.ActivityTimerBinding;

import java.util.logging.Handler;


public class TimerActivity extends AppCompatActivity {
    private Dialog exitDialog;
    BackgroundFragment backgroundFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.tomate.databinding.ActivityTimerBinding timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());
        backgroundFragment = new BackgroundFragment();
//        makeRestFragment();
        makeTimerFragment();

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

    private void makeRestFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        RestFragment restFragment = new RestFragment();

        transaction.replace(R.id.activity_timer_main_frm, restFragment);
        transaction.commit();
    }

    private void makeTimerFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        TimerFragment timerFragment = new TimerFragment();

        transaction.replace(R.id.activity_timer_main_frm, timerFragment);
        transaction.commit();
    }
}

