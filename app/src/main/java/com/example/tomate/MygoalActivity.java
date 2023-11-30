package com.example.tomate;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.tomate.ui.model.Goal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;


public class MygoalActivity extends AppCompatActivity{

    String userId;
    private int new_goal;

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

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userGoalsRef = database.getReference("Goal");

                new_goal = npker.getValue();
                Goal goal = null;
                userId = getIntent().getStringExtra("userId");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    goal = new Goal(userId, LocalDate.now(), new_goal);
                }

                userGoalsRef.child(userId).push().setValue(goal)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // 목표가 성공적으로 저장됨
                                Log.d("MygoalActivity", "목표가 저장되었습니다!");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 에러 처리
                                Log.e("MygoalActivity", "목표 저장 실패: " + e.getMessage());
                                // 선택적으로 사용자에게 에러 메시지를 보여줄 수 있습니다.
                            }
                        });
            }
        });
    }
}

