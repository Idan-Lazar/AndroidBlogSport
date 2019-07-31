package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostRepository;

import java.util.List;

public class PostDetailsViewModel extends AndroidViewModel {

    public PostDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Post> getPost(String postKey)
    {
       return PostRepository.instance.getPost(postKey);
    }
}
