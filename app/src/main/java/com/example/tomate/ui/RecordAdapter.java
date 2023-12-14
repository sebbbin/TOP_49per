package com.example.tomate.ui;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.MainActivity;
import com.example.tomate.R;
import com.example.tomate.RecordActivity;
import com.example.tomate.RecordData;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private static List<RecordData> recordList;

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

    public static class RecordViewHolder extends RecyclerView.ViewHolder{
        private TextView dateTextView;
        private TextView totalStudyTimeTextView;
        private TextView pureStudyTimeTextView;
        private Button studyRecordButton;


        public RecordViewHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_day);
            totalStudyTimeTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_totaltime);
            pureStudyTimeTextView = (TextView) itemView.findViewById(R.id.StudyRecordButton_puretime);
            studyRecordButton = itemView.findViewById(R.id.StudyRecordButton);

            studyRecordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 버튼 클릭 시 실행할 코드
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecordActivity.class);
                    intent.putExtra("date", dateTextView.getText().toString());
                    intent.putExtra("recordData", recordList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

        public void bind(RecordData record) {
            dateTextView.setText(record.getDate());
            totalStudyTimeTextView.setText(record.getTotal_study_time());
            pureStudyTimeTextView.setText(record.getPure_study_time());
        }

    }
}