package com.example.tomate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.tomate.ui.model.RecordData;
import com.example.tomate.ui.model.RecordDataAdapter;
import com.example.tomate.ui.model.User;
import com.example.tomate.ui.model.UserAdapter;

import java.util.ArrayList;
import java.util.List;


public class Studyrecord extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_studyrecord);
        RecordDataAdapter adapter;
        RecyclerView recyclerView;
        List<RecordData> recordData = new ArrayList<>();

        recyclerView = findViewById(R.id.Recordrecyclerview);
        adapter = new RecordDataAdapter(this, recordData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }
}