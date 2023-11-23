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

        databaseRef.orderByChild("Tomato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()
                ){
                    User user = dataSnapshot.getValue(User.class);
                    user.setTierImageID(tierList.get(user.getTier()));
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



        /*
        User user1 = new User("황에르메스");
        user1.setTier("토마토마스터");
        user1.setTomato((long)39);
        user1.setTierImageID(R.drawable.tomato_master);
        userList.add(user1);
        User user2 = new User("세빈챤");
        user2.setTier("토마토마스터");
        user2.setTomato((long)39);
        user2.setTierImageID(R.drawable.tomato_master);
        userList.add(user2);
        User user3 = new User("미니미");
        user3.setTier("토마토꽃");
        user3.setTomato((long)33);
        user3.setTierImageID(R.drawable.tomato_flower);
        userList.add(user3);
        User user4 = new User("지오니");
        user4.setTier("떡잎");
        user4.setTomato((long)26);
        user4.setTierImageID(R.drawable.baby_leaf);
        userList.add(user4);
        User user5 = new User("어쩌고");
        user5.setTier("씨앗");
        user5.setTomato((long)8);
        user5.setTierImageID(R.drawable.seed);
        userList.add(user5);

        userAdapter.notifyDataSetChanged();
        */


        return view;

    }
}
