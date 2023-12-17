package com.example.tomate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.ui.RecordAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyrecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordAdapter recordAdapter;
    private DatabaseReference database;
    String userId;
    private List<RecordData> recordData = new ArrayList<>();

    public StudyrecordFragment(String userId) {
        this.userId = userId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studyrecord, container, false);

        ImageView mypageIv = view.findViewById(R.id.studyrecord_mypage_iv);
        mypageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeToMypageFragment();
            }
        });

        recyclerView = view.findViewById(R.id.Recordrecyclerview);
        recordAdapter = new RecordAdapter(recordData);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        database = FirebaseDatabase.getInstance().getReference("RecordData");

        // 데이터베이스에서 데이터 읽기
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 기존 데이터 클리어
                recordData.clear();

                // userId를 기준으로 데이터를 그룹화하는 맵
                Map<String, List<RecordData>> userRecordDataMap = new HashMap<>();

                // 데이터를 userId를 기준으로 그룹화
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RecordData data = dataSnapshot.getValue(RecordData.class);
                    if (data != null && data.getUserId().equals(userId)) {
                        String userId = data.getUserId();
                        if (!userRecordDataMap.containsKey(userId)) {
                            userRecordDataMap.put(userId, new ArrayList<>());
                        }
                        userRecordDataMap.get(userId).add(data);
                    }
                }

                // userId와 date를 기준으로 pure_study_time과 total_study_time을 합산하여 recordData에 추가
                for (Map.Entry<String, List<RecordData>> entry : userRecordDataMap.entrySet()) {
                    String userId = entry.getKey();
                    List<RecordData> userData = entry.getValue();

                    Map<String, RecordData> uniqueDateDataMap = new HashMap<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RecordData data = dataSnapshot.getValue(RecordData.class);
                        if (data != null && data.getUserId().equals(userId)) {
                            String date = data.getDate();
                            if (!uniqueDateDataMap.containsKey(date)) {
                                uniqueDateDataMap.put(date, data);
                            } else {
                                // 여기서 pure_study_time과 total_study_time을 합산
                                // existingData의 시간 필드를 더해주고 원본 데이터를 업데이트하는 방식으로 진행해야 함
                                RecordData existingData = uniqueDateDataMap.get(date);
                                if (existingData != null) {
                                    // 기존 데이터의 시간 값 파싱
                                    String[] existingPureTime = existingData.getPure_study_time().split(":");
                                    String[] existingTotalTime = existingData.getTotal_study_time().split(":");

                                    // 새로운 데이터의 시간 값 파싱
                                    String[] newPureTime = data.getPure_study_time().split(":");
                                    String[] newTotalTime = data.getTotal_study_time().split(":");

                                    // 초로 변환하여 더하기
                                    int existingPureSeconds = Integer.parseInt(existingPureTime[0]) * 3600 + Integer.parseInt(existingPureTime[1]) * 60 + Integer.parseInt(existingPureTime[2]);
                                    int existingTotalSeconds = Integer.parseInt(existingTotalTime[0]) * 3600 + Integer.parseInt(existingTotalTime[1]) * 60 + Integer.parseInt(existingTotalTime[2]);

                                    int newPureSeconds = Integer.parseInt(newPureTime[0]) * 3600 + Integer.parseInt(newPureTime[1]) * 60 + Integer.parseInt(newPureTime[2]);
                                    int newTotalSeconds = Integer.parseInt(newTotalTime[0]) * 3600 + Integer.parseInt(newTotalTime[1]) * 60 + Integer.parseInt(newTotalTime[2]);

                                    // 총 시간 더하기
                                    int updatedPureSeconds = existingPureSeconds + newPureSeconds;
                                    int updatedTotalSeconds = existingTotalSeconds + newTotalSeconds;

                                    // 초를 시간:분:초 형식으로 변환
                                    int updatedPureHours = updatedPureSeconds / 3600;
                                    int updatedPureMinutes = (updatedPureSeconds % 3600) / 60;
                                    int updatedPureSecs = updatedPureSeconds % 60;

                                    int updatedTotalHours = updatedTotalSeconds / 3600;
                                    int updatedTotalMinutes = (updatedTotalSeconds % 3600) / 60;
                                    int updatedTotalSecs = updatedTotalSeconds % 60;

                                    // 업데이트된 값을 기존 데이터에 설정
                                    existingData.setPure_study_time(String.format("%02d:%02d:%02d", updatedPureHours, updatedPureMinutes, updatedPureSecs));
                                    existingData.setTotal_study_time(String.format("%02d:%02d:%02d", updatedTotalHours, updatedTotalMinutes, updatedTotalSecs));
                                    existingData.setTomato_cnt(String.valueOf(Integer.parseInt(existingData.getTomato_cnt()) + Integer.parseInt(data.getTomato_cnt())));

                                    List<Integer> a = existingData.getSeconds();
                                    List<Integer> b = data.getSeconds();
                                    List<Integer> mergedList = new ArrayList<>();
                                    mergedList.addAll(a);
                                    mergedList.addAll(b);
                                    existingData.setSeconds(mergedList);
                                }
                            }
                        }
                    }

                    // recordData에 추가
                    recordData.addAll(uniqueDateDataMap.values());
                }

                // 데이터가 추가되었는지 로그로 출력
                Log.d("RecordData", "recordData size: " + recordData.size());

                recordAdapter.notifyDataSetChanged(); // 어댑터에 변경된 데이터 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });

        return view;

    }
}
