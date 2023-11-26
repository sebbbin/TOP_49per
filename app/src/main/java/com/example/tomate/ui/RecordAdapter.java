package com.example.tomate.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.R;
import com.example.tomate.RecordData;

import java.util.List;
import java.time.LocalDate;
import java.util.List;
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private List<RecordData> recordList;

    public RecordAdapter(List<RecordData> recordList) {
        this.recordList = recordList;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studyrecord, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        RecordData record = recordList.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView totalStudyTimeTextView;
        private TextView pureStudyTimeTextView;

        public RecordViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_day);
            totalStudyTimeTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_puretime);
            pureStudyTimeTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_totaltime);
        }

        public void bind(RecordData record) {
            dateTextView.setText(record.getDate());
            totalStudyTimeTextView.setText(record.getTotal_study_time());
            pureStudyTimeTextView.setText(record.getPure_study_time());
        }
    }
}