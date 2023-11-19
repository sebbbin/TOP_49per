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

import com.example.tomate.ui.model.RecordData;
import com.example.tomate.ui.model.RecordDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudyrecordFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecordDataAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_studyrecord, container, false);
        List<RecordData> recordData = new ArrayList<>();

        recyclerView = view.findViewById(R.id.Recordrecyclerview);
        adapter = new RecordDataAdapter(getContext(), recordData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return view;
    }
}
