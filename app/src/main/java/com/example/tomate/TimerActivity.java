package com.example.tomate;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tomate.databinding.ActivityMainBinding;
import com.example.tomate.databinding.ActivityTimerBinding;


public class TimerActivity extends AppCompatActivity {
    private ActivityTimerBinding timerBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());
    }
}
