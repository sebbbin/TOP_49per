package com.example.tomate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

public class RestFragment extends Fragment {
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
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_rest, container, false);
        return rootView;
    }
}
