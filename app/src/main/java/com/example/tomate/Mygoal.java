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
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.tomate.ui.model.RecordData;
import com.example.tomate.ui.model.RecordDataAdapter;
import com.example.tomate.ui.model.User;
import com.example.tomate.ui.model.UserAdapter;

import java.util.ArrayList;
import java.util.List;
public class Mygoal extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mygoal);

        NumberPicker npker = findViewById(R.id.mygoal_np);

        String[] numbers = {"1개", "2개", "3개", "4개", "5개", "6개", "7개", "8개", "9개", "10개"};

        npker.setMinValue(0);
        npker.setMaxValue(numbers.length - 1);
        npker.setValue(0);
        npker.setDisplayedValues(numbers);

        npker.setWrapSelectorWheel(false);

    }
}
