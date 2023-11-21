package com.example.tomate.ui.model;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.R;

import java.util.List;

public class RecordDataAdapter extends RecyclerView.Adapter<RecordDataAdapter.ViewHolder> {

    private List<RecordData> datalist;
    private Context context;

    public RecordDataAdapter(Context context, List<RecordData> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public RecordDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studyrecord, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordDataAdapter.ViewHolder holder, int position) {
        RecordData data = datalist.get(position);
        holder.Date.setText(String.valueOf(data.getDate()));
        holder.pure.setText(String.valueOf(data.getPure()));
        holder.total.setText(String.valueOf(data.getTotal()));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Date;
        TextView pure;
        TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Date = (TextView) itemView.findViewById(R.id.StudyRecordButton_day);
            pure = (TextView) itemView.findViewById(R.id.StudyRecordButton_puretime);
            total = (TextView) itemView.findViewById(R.id.StudyRecordButton_totaltime);
        }
        void onBind(RecordData data){
            Date.setText(data.getDate());
            pure.setText(data.getPure());
            total.setText(data.getTotal());

        }
    }
}
