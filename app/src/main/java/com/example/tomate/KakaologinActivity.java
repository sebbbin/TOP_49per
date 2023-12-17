package com.example.tomate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class KakaologinActivity extends AppCompatActivity {

    private static final String TAG = "KakaologinActivity";
    private View loginButton, logoutButton, gotoMainButton;
    private TextView nickName;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_login);
        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);
        KakaoSdk.init(this,"6b761aebb82413c0a8e1c6a44cb77377");

        showDialog();

        Button debugBtn = findViewById(R.id.kakaologin_for_debug_btn);
        debugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                // SharedPreferences.Editor 객체를 사용하여 데이터를 저장합니다.
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userId", "3225907701");

                // 변경사항을 커밋합니다.
                editor.apply();

                Intent intent = new Intent(KakaologinActivity.this, MainActivity.class);
                intent.putExtra("flag", true);
                startActivity(intent);
                finish(); // 현재 액티비티를 종료합니다.
            }
        });

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG, "CallBack Method");
                if (oAuthToken != null) {
                    updateKakaoLoginUi();
                } else {
                    Log.e(TAG, throwable.getMessage());
                    Log.e(TAG, "invoke: login fail");
                }
                return null;
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(KakaologinActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(KakaologinActivity.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(KakaologinActivity.this, callback);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });

        updateKakaoLoginUi();
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User kakaoUser, Throwable throwable) {
                if (kakaoUser != null) {
                    // KakaoUser 정보를 바탕으로 앱의 User 객체 생성
                    com.example.tomate.ui.model.User appUser = new com.example.tomate.ui.model.User(
                            kakaoUser.getId(), // Kakao 로그인에서 얻은 userId
                            kakaoUser.getKakaoAccount().getProfile().getNickname(), // 예를 들어 닉네임을 userName으로 설정
                            "씨앗", // 초기 티어 설정
                            0, // 초기 토마토 소지 개수
                            "00:00",
                            R.drawable.tomato_3d, // 초기 총 학습 시간
                            getTodayDate()
                    );

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference usersRef = mDatabase.child("User");

                    usersRef.child(String.valueOf(appUser.getUserId())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                            } else {
                                // 만약 User가 새로운 유저면 추가한다.
                                mDatabase.child("User").child(String.valueOf(appUser.getUserId())).setValue(appUser);
                            }
                            SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                            // SharedPreferences.Editor 객체를 사용하여 데이터를 저장합니다.
                            SharedPreferences.Editor editor = sharedPref.edit();
                            String userId = String.valueOf(appUser.getUserId()); // 여기에 저장하고 싶은 String 값을 넣습니다.
                            editor.putString("userId", userId);

                            // 변경사항을 커밋합니다.
                            editor.apply();

                            Intent intent = new Intent(KakaologinActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티를 종료합니다.
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // 에러가 발생했을 때의 처리 로직
                            System.out.println("Database error: " + databaseError.getCode());
                        }
                    });

                }
                return null;
            }
        });
    }

    private String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        System.out.println("오늘 날짜: " + dateString);
        return dateString;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notice);
        dialog.setCancelable(false);

         dialog.findViewById(R.id.notice_x_iv).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog.dismiss();
             }
         });
        dialog.show();
    }
}


