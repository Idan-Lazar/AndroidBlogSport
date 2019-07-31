package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostRepository;

import java.util.List;

public class PostListViewModel extends AndroidViewModel {


    private LiveData<List<Post>> data;

    public PostListViewModel(Application application) {
        super(application);
        data = PostRepository.instance.getmAllPosts();
    }

    public LiveData<List<Post>> getAllPosts() { return data;}


}