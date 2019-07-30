package com.example.idanl.blogsport.Models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.idanl.blogsport.Adapters.MyApplication;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository mRepository;

    private LiveData<List<Post>> mAllPosts;

    public PostViewModel (Application application) {
        super(application);
        mRepository = new PostRepository(application);
        mAllPosts = mRepository.getmAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() { return mAllPosts; }

    public void insert(Post post) { mRepository.insert(post); }
}