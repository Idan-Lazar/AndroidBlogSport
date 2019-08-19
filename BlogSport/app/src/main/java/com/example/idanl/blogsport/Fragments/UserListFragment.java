package com.example.idanl.blogsport.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idanl.blogsport.Adapters.MyApplication;
import com.example.idanl.blogsport.Adapters.UserAdapter;
import com.example.idanl.blogsport.Helper.SpacesItemDecoration;
import com.example.idanl.blogsport.Models.Entities.User;
import com.example.idanl.blogsport.Models.ViewModel.UserListViewModel;
import com.example.idanl.blogsport.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private UserListViewModel userListViewModel;
    final UserAdapter adapter = new UserAdapter();
    public UserListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_user_list, container, false);
        userListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        userRecyclerView = v.findViewById(R.id.user_list_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyApplication.getContext(),3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.DefaultSpanSizeLookup());
        gridLayoutManager.setMeasurementCacheEnabled(true);
        userRecyclerView.setLayoutManager(gridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        userRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        userRecyclerView.setClipToPadding(true);
        userRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userRecyclerView.setAdapter(adapter);
        userListViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });



       
       
       
       
       
       return v;
    }

}
