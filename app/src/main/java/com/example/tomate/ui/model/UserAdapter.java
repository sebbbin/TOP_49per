package com.example.tomate.ui.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;
    private boolean isTotalStudyTime = false;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rankinglist_item, parent, false);
        return new UserViewHolder(itemView);
    }
    public void toggleStudyTimeVisibility(boolean shouldShowTotalStudyTime) {
        this.isTotalStudyTime = shouldShowTotalStudyTime;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getUserName());
        holder.tier.setText(user.getTier());
        if(isTotalStudyTime){
            holder.Tomato.setText(String.valueOf(user.getTotalStudyTime()));
        }else {
            holder.Tomato.setText(String.valueOf(user.getTomato()) + "개");
        }
        holder.tierImageID.setImageResource(user.getTierImageID());
        holder.Ranking.setText(String.valueOf(userList.indexOf(user)+1)+".");
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_slide);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView tier;
        public TextView Tomato;
        public ImageView tierImageID;



        public TextView Ranking;

        public UserViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            tier = view.findViewById(R.id.tier);
            Tomato = view.findViewById(R.id.Tomato);
            tierImageID = view.findViewById(R.id.tierImageID);
            Ranking = view.findViewById(R.id.Ranking);

        }
    }

}
