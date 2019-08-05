package com.example.idanl.blogsport.Fragments;


import android.os.Bundle;

import com.example.idanl.blogsport.Adapters.PostAdapter;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.ViewModel.PostListViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.idanl.blogsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private RecyclerView postRecyclerView;
    final PostAdapter adapter = new PostAdapter();
    /*FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/
    List<Post> postList;
    ProgressBar progressBar;
    private PostListViewModel mPostListViewModel;
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
        /*firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");*/
        // Inflate the layout for this fragment
        postRecyclerView = v.findViewById(R.id.post_RV);
        progressBar = v.findViewById(R.id.home_progressBar);
        postRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setAdapter(adapter);

        mPostListViewModel = ViewModelProviders.of(this).get(PostListViewModel.class);

        mPostListViewModel.getAllPosts().observe(this, new Observer<List<Post>>() {


                    @Override
                    public void onChanged(@Nullable final List<Post> posts){
                        postRecyclerView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter.setPosts(posts);
                        postRecyclerView.setAdapter(adapter);
                        postRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                });

        return v;
    }



    @Override
    public void onStart() {
        super.onStart();

        // Get List Posts from the database
/*
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
        });*/

    }
}
