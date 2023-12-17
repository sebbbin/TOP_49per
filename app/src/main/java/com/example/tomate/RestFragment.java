package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

public class RestFragment extends Fragment {
    private TimerActivity timerActivity;
    private ViewGroup rootView;
    private int second = 5 * 60;
    private int minute = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        timerActivity = (TimerActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rest, container, false);
        this.startTimer();
        return rootView;
    }

    public void startTimer(){
        TextView timerTv = rootView.findViewById(R.id.fragment_rest_time_tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (second >= 0) {
                    if (timerActivity == null || timerActivity.isFinishing()) break;
                    minute = second / 60;
                    Log.d("rest", String.format("%d", second));
                    int finalMinute = minute;
                    int finalSecond = second;
                    if (timerActivity == null || timerActivity.isFinishing()) break;
                    timerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (timerActivity != null && !timerActivity.isFinishing()) {
                                timerTv.setText(String.format("%02d:%02d", finalMinute, finalSecond % 60));
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (second == 0) {
                        // 토마토 개수 올라가는 기능
                        if (timerActivity != null && !timerActivity.isFinishing()) {
                            timerActivity.makeTimerFragment();
                        }
                    }
                    if (timerActivity == null || timerActivity.isFinishing()) break;
                    timerActivity.total_study_time++;
                    second--;
                }
            }
        }).start();
    }
}
