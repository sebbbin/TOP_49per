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
        user1.setTomato((long)33);
        user1.setTierImageID(R.drawable.tomato_master);
        userList.add(user1);

        userAdapter = new UserAdapter(getContext(),userList);
        userRecyclerView.setAdapter(userAdapter);

        userRecyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
        return view;

    }
}
