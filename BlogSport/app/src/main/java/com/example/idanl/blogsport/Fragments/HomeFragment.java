package com.example.idanl.blogsport.Fragments;


import android.os.Bundle;

import com.example.idanl.blogsport.Adapters.MyApplication;
import com.example.idanl.blogsport.Adapters.PostAdapter;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.ViewModel.PostListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.idanl.blogsport.Models.ViewModel.UserViewModel;
import com.example.idanl.blogsport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView postRecyclerView;
    final PostAdapter adapter = new PostAdapter();
    /*FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/
    List<Post> postList;
    ProgressBar progressBar;
    private PostListViewModel mPostListViewModel;
    private UserViewModel mUserViewModel;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        mPostListViewModel = ViewModelProviders.of(this).get(PostListViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DialogFragment f = new DialogFragment();
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        postRecyclerView = v.findViewById(R.id.post_RV);
        progressBar = v.findViewById(R.id.home_progressBar);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postRecyclerView.setAdapter(adapter);
        mPostListViewModel.getAllPosts().observe(this, new Observer<LinkedList<Post>>() {
                    @Override
                    public void onChanged(@Nullable final LinkedList<Post> posts){

                        adapter.setPosts(posts);
                        postRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }


                });


        return v;
    }



    @Override
    public void onStart() {
        super.onStart();

    }
}
