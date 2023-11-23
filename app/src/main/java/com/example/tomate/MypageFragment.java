package com.example.tomate;

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

import java.util.Collections;
import java.util.Iterator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MypageFragment extends Fragment {
    ViewGroup rootView;
    String userName;
    String tierStr;
    String startDate;

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
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
        DatabaseReference user1Ref = mDatabase.child("user2");

        // 데이터를 읽기 위해 ValueEventListener 추가
        user1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot를 통해 user1의 데이터를 가져올 수 있음
                User user = dataSnapshot.getValue(User.class);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
