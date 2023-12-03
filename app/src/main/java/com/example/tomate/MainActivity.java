package com.example.tomate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.tomate.ui.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tomate.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        User user1 = new User(1, "이민희", "토마토마스터", 1523, "00:20:59", 0);
        User user2 = new User(2, "나세빈", "방울토마토", 123, "00:20:59", 1);
        User user3 = new User(3, "황서현", "토마토꽃", 53, "00:20:59", 2);
        User user4 = new User(4, "김민희", "본잎", 24, "00:20:59", 3);
        User user5 = new User(5, "박세빈", "떡잎", 12, "00:20:59", 4);
        User user6 = new User(6, "김지원", "씨앗", 3, "00:20:59", 5);
        User user123 = new User(12345678, "정지원", "토마토마스터", 3, "00:20:59", 0);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child("user1").setValue(user1);
        mDatabase.child("User").child("user2").setValue(user2);
        mDatabase.child("User").child("user3").setValue(user3);
        mDatabase.child("User").child("user4").setValue(user4);
        mDatabase.child("User").child("user5").setValue(user5);
        mDatabase.child("User").child("user6").setValue(user6);
        mDatabase.child("User").child("user123").setValue(user123);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = getIntent().getStringExtra("userId");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (userId == null) {
            userId = "6";
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MypageFragment()).commit();

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_record:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new StudyrecordFragment(userId)).commit();
                        break;
                    case R.id.navigation_ranking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new RankingFragment()).commit();
                        break;
                }
                return;
            }
        });

        ImageButton timerButton = findViewById(R.id.timer_button);
        Drawable drawable = getResources().getDrawable(R.drawable.tomato_3d);
        timerButton.setImageDrawable(drawable);

        timerButton.setAdjustViewBounds(true);
        timerButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                if (userId == null) {
                    userId = "1";
                }
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

}