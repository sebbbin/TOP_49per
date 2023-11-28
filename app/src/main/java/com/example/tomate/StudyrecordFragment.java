package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

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

        recyclerView = view.findViewById(R.id.Recordrecyclerview);
        recordAdapter = new RecordAdapter(recordData);
        recyclerView.setAdapter(recordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        database = FirebaseDatabase.getInstance().getReference("Record");

        // 데이터베이스에서 데이터 읽기
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recordData.clear(); // 기존 데이터 클리어

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RecordData data = dataSnapshot.getValue(RecordData.class);
                    if (data != null) {
                        recordData.add(data);
                    }
                }

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
