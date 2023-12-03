package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tomate.ui.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.user.UserApiClient;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Iterator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MypageFragment extends Fragment {
    ViewGroup rootView;
    String userName;
    String tierStr;
    String startDate;
    String userId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity)getActivity();
        userId = mainActivity.userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        getUserData();

        TextView signoutTv = rootView.findViewById(R.id.fragment_mypage_member_out_tv);
        signoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
                DatabaseReference userRef = mDatabase.child("user6");

                // user1 데이터 삭제
                userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 데이터 삭제 성공 시 처리
                        System.out.println("User1 data successfully deleted.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 삭제 실패 시 처리
                        System.out.println("Error deleting User1 data: " + e.getMessage());
                    }
                });

            }
        });

        TextView logoutTv = rootView.findViewById(R.id.fragment_mypage_logout_tv);
        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아읏 버튼
                logoutTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
//                                updateKakaoLoginUi();
                                return null;
                            }
                        });
                    }
                });
            }
        });
        return rootView;
    }
    private void getUserData() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        // userId에 해당하는 user의 total_cnt를 조회
        databaseRef.child("User").orderByChild("userId").equalTo(Long.parseLong(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                userName = user.getUserName();
                                tierStr = user.getTier();
                                TextView tierTv = rootView.findViewById(R.id.fragment_mypage_tier_tv);
                                tierTv.setText(tierStr);
                                TextView userNameTv = rootView.findViewById(R.id.fragment_mypage_name_tv);
                                userNameTv.setText(userName);
                                ImageView tierIv = rootView.findViewById(R.id.fragment_mypage_tear_iv);
                                if (tierStr.equals("토마토마스터")) {
                                    tierIv.setImageResource(R.drawable.tomato_master);
                                } else if (tierStr.equals("방울토마토")) {
                                    tierIv.setImageResource(R.drawable.cherry_tomato);
                                } else if (tierStr.equals("토마토꽃")) {
                                    tierIv.setImageResource(R.drawable.tomato_flower);
                                } else if (tierStr.equals("본잎")) {
                                    tierIv.setImageResource(R.drawable.adult_leaf);
                                } else if (tierStr.equals("떡잎")) {
                                    tierIv.setImageResource(R.drawable.baby_leaf);
                                } else {
                                    tierIv.setImageResource(R.drawable.seed);
                                }
                                break; // 첫 번째 일치하는 데이터만 사용
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onCancelled", "onCancelled");
                    }
                });
    }
}
