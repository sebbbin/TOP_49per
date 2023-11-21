package com.example.tomate;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        KakaoSdk.init(this, "98e113d9908358266ecb973dd3790031");
    }
}
