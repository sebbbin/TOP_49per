package com.example.tomate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tomate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MypageFragment()).commit();

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_record:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new StudyrecordFragment()).commit();
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
                startActivity(intent);
            }
        });
    }

}