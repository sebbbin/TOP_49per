package com.example.tomate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MypageFragment extends Fragment {
    ViewGroup rootView;
    String userName;
    String tierStr;
    String startDate;
    String userId;
    MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
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
                SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                // "userId" 키로 저장된 값을 가져옵니다. 기본값으로 "" (빈 문자열)을 사용합니다.
                String userId = sharedPref.getString("userId", "");
                Log.d("TAG", "UserId: " + userId);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
                DatabaseReference userRef = mDatabase.child(userId);

                // user1 데이터 삭제
                userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 데이터 삭제 성공 시 처리
                        System.out.println(userId + "data successfully deleted.");
                        mainActivity.logoutOrSignout();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 삭제 실패 시 처리
                        System.out.println("Error deleting user data: " + e.getMessage());
                    }
                });

            }
        });

        TextView logoutTv = rootView.findViewById(R.id.fragment_mypage_logout_tv);

        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SharedPreferences 객체를 가져옵니다. "MyPrefs"는 SharedPreferences 파일의 이름입니다.
                SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                // SharedPreferences.Editor 객체를 사용하여 데이터를 수정합니다.
                SharedPreferences.Editor editor = sharedPref.edit();

                // "userId" 키에 해당하는 데이터를 제거합니다.
                editor.remove("userId");

                // 변경사항을 커밋합니다.
                editor.apply();
                mainActivity.logoutOrSignout();
            }
        });
        return rootView;
    }

    private void getUserData() {
        // SharedPreferences 객체를 가져옵니다. "MyPrefs"는 SharedPreferences 파일의 이름입니다.
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // "userId" 키로 저장된 값을 가져옵니다. 기본값으로 "" (빈 문자열)을 사용합니다.
        String userId = sharedPref.getString("userId", "");
        Log.d("TAG", "UserId: " + userId);

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
                                TextView startDateTv = rootView.findViewById(R.id.fragment_mypage_day_count_tv);
                                startDateTv.setText(dateDiff(user.getStartDate())+"일째");
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
    private String getTodayDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        System.out.println("오늘 날짜: " + dateString);
        return dateString;
    }

    private String dateDiff(String startDate) {
        String currentDate = getTodayDate();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate current = LocalDate.parse(currentDate);

        // 두 날짜 사이의 차이를 일(days) 단위로 계산
        long daysBetween = ChronoUnit.DAYS.between(start, current) + 1;

        return String.valueOf(daysBetween);
    }
}
