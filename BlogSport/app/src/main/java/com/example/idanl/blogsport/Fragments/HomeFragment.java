package com.example.idanl.blogsport.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.idanl.blogsport.Activities.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.idanl.blogsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CircleImageView homeUserImage;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DialogFragment f = new DialogFragment();

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        homeUserImage = v.findViewById(R.id.imageViewHome);
        Glide.with(HomeFragment.this).load(currentUser.getPhotoUrl()).into(homeUserImage);

        return v;
    }




}
