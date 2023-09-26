package com.example.tomate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.tomate.databinding.ActivityTimerBinding;


public class TimerActivity extends AppCompatActivity {
    private ActivityTimerBinding timerBinding;
    private TimerFragment timerFragment;
    private RestFragment restFragment;
    private TextView exitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timerBinding = ActivityTimerBinding.inflate(getLayoutInflater());
        setContentView(timerBinding.getRoot());

//        makeRestFragment();
//        makeTimerFragment();

        exitButton = findViewById(R.id.activity_timer_exit_tv);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // execute
            }
        });
    }

    private void makeRestFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        restFragment = new RestFragment();

        transaction.replace(R.id.activity_timer_main_frm, restFragment);
        transaction.commit();
    }

    private void makeTimerFragment() {
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        timerFragment = new TimerFragment();

        transaction.replace(R.id.activity_timer_main_frm, timerFragment);
        transaction.commit();
    }
}
