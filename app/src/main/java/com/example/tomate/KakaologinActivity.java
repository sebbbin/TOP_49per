package com.example.tomate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

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
                            "Default Tier", // 초기 티어 설정
                            0, // 초기 토마토 소지 개수
                            "00:00",
                            R.drawable.tomato_3d// 초기 총 학습 시간
                    );

                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);

                } else {
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);

                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                }
                Intent intent = new Intent(KakaologinActivity.this, MainActivity.class);
                // Intent에 userId 추가
                intent.putExtra("userId", String.valueOf(kakaoUser.getId()));
                startActivity(intent);
                return null;
            }
        });
    }
}

