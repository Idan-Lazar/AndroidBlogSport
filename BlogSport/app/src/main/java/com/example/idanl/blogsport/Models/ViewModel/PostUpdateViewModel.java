package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostRepository;

public class PostUpdateViewModel extends AndroidViewModel {
    public PostUpdateViewModel(Application application) {
        super(application);
    }
    public void updatePost(Post post, PostRepository.InsertPostListener listener) { PostRepository.instance.updatePost(post,listener); }

    public void saveBlogImage(Uri imageBitmap, PostRepository.SaveImageListener listener) {
        PostRepository.instance.saveBlogImage(imageBitmap,listener);
    }
    public void isPostExist(String postKey, PostRepository.ExistPostListener listener)
    {
        PostRepository.instance.isPostExist(postKey, listener);
    }
}
