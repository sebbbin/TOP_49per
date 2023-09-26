package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tomate.R;
import com.example.tomate.TimerActivity;

public class TimerFragment extends Fragment {
    private TimerActivity timerActivity;
    private ViewGroup rootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        timerActivity = (TimerActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_timer, container, false);
        return rootView;
    }
}
