package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tomate.R;
import com.example.tomate.TimerActivity;

import org.w3c.dom.Text;

public class TimerFragment extends Fragment {
    private TimerActivity timerActivity;
    private ViewGroup rootView;
    //second랑 minute 임의로 빼놨음..!!
    private int second;
    private int minute;

    public TimerFragment(int timer_minute, int timer_second) {
        second = timer_second + (timer_minute * 60);
        minute = 0;
    }

    public static int convertToSeconds(String time) {
        if (time == null || !time.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid time format");
        }
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        timerActivity = (TimerActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("resume", "on");
        this.startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        // second - current second
        TextView timerTv = rootView.findViewById(R.id.fragment_timer_time_tv);
        int current_second =  convertToSeconds((String) timerTv.getText());
        timerActivity.seconds.add(second - current_second);
        Log.d("second - current_second", String.valueOf(second - current_second));
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//        if (timerActivity != null) {
//            second = timerActivity.timer_second;
//            minute = timerActivity.timer_minute;
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_timer, container, false);
//        if (timerActivity != null) {
//            second = timerActivity.timer_second;
//            minute = timerActivity.timer_minute;
//        }
        return rootView;
    }
    //공부시간조정에 쓰일 메서드..내가 임의로 추가햇는데 지워도됨
    public void setTime(int setMinute, int setSecond) {
        this.minute = setMinute;
        this.second = setSecond + setMinute * 60;
    }
    public void startTimer(){
        TextView timerTv = rootView.findViewById(R.id.fragment_timer_time_tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int tmpMinute = minute;
                int tmpSecond = second;
                while (tmpSecond >= 0) {
                    tmpMinute = tmpSecond / 60;
                    Log.d("time", String.format("%d", tmpSecond));

                    int finalTmpSecond = tmpSecond;
                    int finalTmpMinute = tmpMinute;
                    timerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerTv.setText(String.format("%02d:%02d", finalTmpMinute, finalTmpSecond % 60));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (tmpSecond == 0) {
                        // 토마토 개수 올라가는 기능
                        TextView tomato_cnt_tv = timerActivity.findViewById(R.id.activity_timer_bin_tv);
                        timerActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tomato_cnt_tv.setText(String.format("%d", Integer.parseInt(tomato_cnt_tv.getText().toString()) + 1));
                            }
                        });
                        timerActivity.makeRestFragment();
                    }
                    timerActivity.total_study_time++;
                    timerActivity.pure_study_time++;
                    tmpSecond--;
                }
            }
        }).start();
    }
}
