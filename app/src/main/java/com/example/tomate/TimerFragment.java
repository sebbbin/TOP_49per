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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        timerActivity = (TimerActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_timer, container, false);

        TextView timerTv = rootView.findViewById(R.id.fragment_timer_time_tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int second = 0;
                int minute = 0;
                while (true) {
                    // 코드 작성
                    second++;
                    minute = second / 60;
                    Log.d("time", String.format("%d", second));
                    int finalMinute = minute;
                    int finalSecond = second;
                    timerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timerTv.setText(String.format("%02d:%02d", finalMinute, finalSecond % 60));
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return rootView;
    }
}
