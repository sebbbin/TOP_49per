package com.example.tomate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomate.databinding.ActivityTimerBinding;
import com.example.tomate.ui.TimerFragment;


public class TimerActivity extends AppCompatActivity {
    private ActivityTimerBinding timerBinding;
    private TimerFragment timerFragment;
    private TextView exitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());

        makeTimerFragment();

        exitButton = findViewById(R.id.activity_timer_exit_tv);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setText("success!");
            }
        });
    }

    private void makeTimerFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        timerFragment = new TimerFragment();

        transaction.add(R.id.activity_timer_main_frm, timerFragment);
        transaction.commit();
    }
}
