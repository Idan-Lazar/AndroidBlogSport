package com.example.idanl.blogsport.Models;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

public class PostInsertViewModel extends AndroidViewModel {
    public PostInsertViewModel(Application application) {
        super(application);
    }
    public void insert(Post post, PostRepository.InsertPostListener listener) { PostRepository.instance.insert(post,listener); }

    public void saveBlogImage(Uri imageBitmap, PostRepository.SaveImageListener listener) {
        PostRepository.instance.saveBlogImage(imageBitmap,listener);
    }
}
