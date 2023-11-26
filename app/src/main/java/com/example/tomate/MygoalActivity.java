package com.example.tomate;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class MygoalActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygoal);

        NumberPicker npker = findViewById(R.id.mygoal_np);

        npker.setMinValue(0);
        npker.setMaxValue(20);
        npker.setValue(0);
        npker.setWrapSelectorWheel(false);

        Button mygoalButton = findViewById(R.id.mygoal_fin_button);
        mygoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //목표 설정 완료
                npker.getValue();
            }
        });

    }
}
