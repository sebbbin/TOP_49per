package com.example.tomate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.ui.model.User;
import com.example.tomate.ui.model.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RankingFragment extends Fragment {
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    private Map<String,Integer> tierList = new HashMap<String,Integer>(){{
        put("토마토마스터",R.drawable.tomato_master);
        put("방울토마토",R.drawable.cherry_tomato);
        put("토마토꽃",R.drawable.tomato_flower);
        put("본잎",R.drawable.adult_leaf);
        put("떡잎",R.drawable.baby_leaf);
        put("씨앗",R.drawable.seed);

    }};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        userRecyclerView = view.findViewById(R.id.RankingList); //랭킹리스트 리사이클러뷰 참조 가져오기
        userAdapter = new UserAdapter(getContext(),userList);
        userRecyclerView.setAdapter(userAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));

        ImageView mypageIv = view.findViewById(R.id.ranking_mypage_iv);
        mypageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeToMypageFragment();
            }
        });

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("User");

        ImageView goldMedal = view.findViewById(R.id.goldMedal);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slide);
        goldMedal.startAnimation(animation1);
        ImageView silverMedal = view.findViewById(R.id.silverMedal);
        Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slide);
        silverMedal.startAnimation(animation2);
        ImageView bronzeMedal = view.findViewById(R.id.bronzeMedal);
        Animation animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slide);
        bronzeMedal.startAnimation(animation3);

        databaseRef.orderByChild("tomato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()
                ){
                    User user = dataSnapshot.getValue(User.class);
                    //user.setTierImageID(tierList.get(user.getTier()).intValue());
                    Integer tierImageId = tierList.get(user.getTier());
                    if (tierImageId != null) {
                        user.setTierImageID(tierImageId.intValue());
                    }
                    userList.add(user);

                }
                Collections.reverse(userList);
                userAdapter.notifyDataSetChanged();

                Iterator<User> iter = userList.iterator();

                User tempUser= iter.next();
                TextView topOneName = view.findViewById(R.id.topOneName);
                TextView topOneTomato = view.findViewById(R.id.topOneTomato);
                topOneName.setText(tempUser.getUserName());
                topOneTomato.setText(tempUser.getTomato()+"개");

                tempUser= iter.next();
                TextView topTwoName = view.findViewById(R.id.topTwoName);
                TextView topTwoTomato = view.findViewById(R.id.topTwoTomato);
                topTwoName.setText(tempUser.getUserName());
                topTwoTomato.setText(tempUser.getTomato()+"개");

                tempUser= iter.next();
                TextView topThreeName = view.findViewById(R.id.topThreeName);
                TextView topThreeTomato = view.findViewById(R.id.topThreeTomato);
                topThreeName.setText(tempUser.getUserName());
                topThreeTomato.setText(tempUser.getTomato()+"개");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });





        return view;

    }
}
