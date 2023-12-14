package com.example.tomate;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomate.databinding.ActivityTimerBinding;
import com.example.tomate.ui.model.User;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Handler;


public class TimerActivity extends AppCompatActivity {
    private Dialog exitDialog;
    private Dialog timeControlDialog;
    BackgroundFragment backgroundFragment;
    public int timer_second = 0;
    public int timer_minute = 0;
    public int total_study_time = 0;
    public int pure_study_time = 0;
    public List<Integer> seconds = new ArrayList<>();
    private String userId;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (seconds.size() == 1 && seconds.get(0) == -1) {
            return;
        }
        LocalDate now = LocalDate.now();
        TextView tomato_cnt_tv = findViewById(R.id.activity_timer_bin_tv);
        String tomato_cnt = (String) tomato_cnt_tv.getText();
        RecordData record = new RecordData(userId, now, total_study_time, pure_study_time, tomato_cnt, seconds);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("RecordData").push().setValue(record);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        // userId에 해당하는 user의 total_cnt를 조회
        databaseRef.child("User").orderByChild("userId").equalTo(Long.parseLong(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                Log.d("user", user.getUserName());
                                String time1 = String.format("%02d:%02d:%02d", total_study_time / 3600, (total_study_time % 3600) / 60, total_study_time % 60);
                                String time2 = user.getTotalStudyTime();
                                // LocalTime 객체로 변환
                                LocalTime t1 = LocalTime.parse(time1);
                                LocalTime t2 = LocalTime.parse(time2);

                                // 두 시간의 차이를 Duration으로 계산
                                Duration duration = Duration.between(LocalTime.MIN, t1).plus(Duration.between(LocalTime.MIN, t2));

                                // 결과 시간 계산
                                LocalTime totalTime = LocalTime.MIN.plus(duration);
                                System.out.println("Total Time: " + totalTime.toString());

                                snapshot.getRef().child("totalStudyTime").setValue(totalTime.toString())
                                        .addOnSuccessListener(aVoid -> {
                                            // 업데이트 성공
                                            Log.d("Update", "totalStudyTime successfully updated.");
                                        })
                                        .addOnFailureListener(e -> {
                                            // 업데이트 실패
                                            Log.e("Update", "Failed to update totalStudyTime: " + e.getMessage());
                                        });


                                long new_tomato_cnt = user.getTomato() + Long.parseLong(tomato_cnt);
                                snapshot.getRef().child("tomato").setValue(new_tomato_cnt)
                                        .addOnSuccessListener(aVoid -> {
                                            // 업데이트 성공
                                            Log.d("Update", "tomato_cnt successfully updated.");
                                        })
                                        .addOnFailureListener(e -> {
                                            // 업데이트 실패
                                            Log.e("Update", "Failed to update tomato_cnt: " + e.getMessage());
                                        });
                                break; // 첫 번째 일치하는 데이터만 사용
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onCancelled", "onCancelled");
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.tomate.databinding.ActivityTimerBinding timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());
        backgroundFragment = new BackgroundFragment();
        userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            Log.d("timerActivity userId", userId);
        }

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
    }
    private void showTimeControlDialog(){
        timeControlDialog  = new Dialog(TimerActivity.this);
        timeControlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timeControlDialog.setContentView(R.layout.dialog_timecontrol);
        timeControlDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        NumberPicker hourPicker = timeControlDialog.findViewById(R.id.hourPicker);
        NumberPicker minutePicker = timeControlDialog.findViewById(R.id.minutePicker);
        NumberPicker secondsPicker = timeControlDialog.findViewById(R.id.secondsPicker);

//        hourPicker.setMinValue(0);
//        hourPicker.setMaxValue(0);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        SharedPreferences preferences = getSharedPreferences("timeControl", MODE_PRIVATE);
        int hour = preferences.getInt("hour", 0);
        int minute = preferences.getInt("minute", 0);
        int second = preferences.getInt("second", 0);
//        hourPicker.setValue(hour);
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
//                if (timerFragment != null) {
//                    timer_minute = 25;
//                    timer_second = 0;
//                    timerFragment.setTime(25,0);
//                    timerFragment.startTimer(); // 타이머 시작
//                }
                timeControlDialog.dismiss();
                finish();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 선택한 시간으로 설정
//                int selectedHour = hourPicker.getValue();
                int selectedMinute = minutePicker.getValue();
                int selectedSeconds = secondsPicker.getValue();

                SharedPreferences preferences = getSharedPreferences("timeControl", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
//                editor.putInt("hour", selectedHour);
                editor.putInt("minute", selectedMinute);
                editor.putInt("second", selectedSeconds);
                editor.commit();

                // TimerFragment의 시간 설정
                //TimerFragment timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.activity_timer_main_frm);
                if (timerFragment != null) {
                    timer_minute = selectedMinute;
                    timer_second = selectedSeconds;
                    timerFragment.setTime(selectedMinute, selectedSeconds);
                    if (timer_minute == 0 && timer_second == 0) {
                        timerFragment.setTime(25, 0);
                    }
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
                finish();
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

    private String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        System.out.println("오늘 날짜: " + dateString);
        return dateString;
    }

    public static String addTimes(String time1, String time2) {
        // 시간 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 문자열을 LocalTime 객체로 파싱
        LocalTime localTime1 = LocalTime.parse(time1, formatter);
        LocalTime localTime2 = LocalTime.parse(time2, formatter);

        // 두 시간을 합침
        LocalTime combinedTime = localTime1.plusHours(localTime2.getHour())
                .plusMinutes(localTime2.getMinute())
                .plusSeconds(localTime2.getSecond());

        // 합친 시간을 문자열로 변환
        return combinedTime.format(formatter);
    }
}

