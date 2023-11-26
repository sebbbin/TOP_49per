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

import java.util.ArrayList;
import java.util.List;

public class StudyrecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private List<RecordData> recordData = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studyrecord, container, false);


        recyclerView = view.findViewById(R.id.Recordrecyclerview);
        //adapter = new RecordAdapter(getContext(), recordData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return view;
    }
}
