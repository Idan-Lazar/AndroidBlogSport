package com.example.idanl.blogsport.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idanl.blogsport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserChangePassFragment extends Fragment {


    public UserChangePassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_change_pass, container, false);
    }

}
