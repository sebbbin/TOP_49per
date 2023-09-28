package com.example.tomate.ui.model;

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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context;

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

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getUserName());
        holder.tier.setText(user.getTier());
        holder.Tomato.setText(String.valueOf(user.getTomato()));
        holder.tierImageID.setImageResource(user.getTierImageID());
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

        public UserViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            tier = view.findViewById(R.id.tier);
            Tomato = view.findViewById(R.id.Tomato);
            tierImageID = view.findViewById(R.id.tierImageID);
        }
    }

}
