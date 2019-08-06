package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostRepository;

public class PostDeleteViewModel extends AndroidViewModel {
    public PostDeleteViewModel(Application application) {
        super(application);
    }
    public void delete(Post post, PostRepository.InsertPostListener listener) { PostRepository.instance.insert(post,listener); }

    public void saveBlogImage(Uri imageBitmap, PostRepository.SaveImageListener listener) {
        PostRepository.instance.saveBlogImage(imageBitmap,listener);
    }
}
