package com.example.tomate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomate.ui.model.User;
import com.example.tomate.ui.model.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class RankingFragment extends Fragment {
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        userRecyclerView = view.findViewById(R.id.RankingList); //랭킹리스트 리사이클러뷰 참조 가져오기

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




        userAdapter = new UserAdapter(getContext(),userList);
        userRecyclerView.setAdapter(userAdapter);

        userRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
        return view;

    }
}
