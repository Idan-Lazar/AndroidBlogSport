package com.example.idanl.blogsport.Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.idanl.blogsport.Activities.MainActivity;
import com.example.idanl.blogsport.Adapters.PostAdapter;
import com.example.idanl.blogsport.Models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.idanl.blogsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView postRecyclerView;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;
    ProgressBar progressBar;
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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        // Inflate the layout for this fragment
        postRecyclerView = v.findViewById(R.id.post_RV);
        progressBar = v.findViewById(R.id.home_progressBar);
        postRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get List Posts from the database

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        postList = new ArrayList<>();
        for (DataSnapshot postsnap : dataSnapshot.getChildren())
        {
            Post post = postsnap.getValue(Post.class);
            postList.add(post);
        }

        postAdapter = new PostAdapter(getActivity(), postList);
        postRecyclerView.setAdapter(postAdapter);
        postRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
