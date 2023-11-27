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

        gotoMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 이동
                Intent intent = new Intent(KakaologinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                Log.e(TAG, "CallBack Method");
                if (oAuthToken != null) {
                    updateKakaoLoginUi();
                } else {
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
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    Log.d(TAG, "invoke: id = " + user.getId());
                    Log.d(TAG, "invoke: email = " + user.getKakaoAccount().getEmail());
                    Log.d(TAG, "invoke: nickname = " + user.getKakaoAccount().getProfile().getNickname());
                    Log.d(TAG, "invoke: gender = " + user.getKakaoAccount().getGender());
                    Log.d(TAG, "invoke: age = " + user.getKakaoAccount().getAgeRange());

                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());

                    // Firebase Realtime Database에 사용자 정보 저장
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(String.valueOf(user.getId()))
                            .setValue(user);

                    loginButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(KakaologinActivity.this, MainActivity.class);
                    // Intent에 userId 추가
                    intent.putExtra("userId", String.valueOf(user.getId()));
                    startActivity(intent);
                } else {
                    nickName.setText(null);
                    profileImage.setImageBitmap(null);

                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);
                }
                return null;
            }
        });
    }
}


