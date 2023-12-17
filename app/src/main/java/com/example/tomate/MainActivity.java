package com.example.tomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tomate.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.user.UserApiClient;

import java.time.LocalDate;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userId = sharedPref.getString("userId", "");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MypageFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_record:getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new StudyrecordFragment(userId)).commit();
                        break;
                    case R.id.navigation_ranking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new RankingFragment()).commit();
                        break;
                }
                return false;
            }
        });

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            boolean flag = receivedIntent.getBooleanExtra("flag", false);

            if (flag) {
                Intent myGoalIntent = new Intent(this, MygoalActivity.class);
                myGoalIntent.putExtra("userId", userId);
                startActivity(myGoalIntent);
            }
        }

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

    public void logoutOrSignout() {
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                updateKakaoLoginUi();
                return null;
            }
        });

        // SharedPreferences 객체를 가져옵니다. "MyPrefs"는 SharedPreferences 파일의 이름입니다.
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("userId");
        editor.apply();

        Intent intent = new Intent(this, KakaologinActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<com.kakao.sdk.user.model.User, Throwable, Unit>() {
            @Override
            public Unit invoke(com.kakao.sdk.user.model.User kakaoUser, Throwable throwable) {
                if (kakaoUser != null) {

                }
                return null;
            }
        });
    }

    public void changeToMypageFragment() {
        // MyPageFragment 인스턴스 생성
        MypageFragment myPageFragment = new MypageFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_frame, myPageFragment);
        fragmentTransaction.commit();

    }
}