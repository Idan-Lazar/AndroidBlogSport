package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostRepository;
import com.example.idanl.blogsport.Models.UserRepository;

public class UserUpdateViewModel extends AndroidViewModel {
    public UserUpdateViewModel(Application application) {
        super(application);
    }
    public void updatePost(Post post, PostRepository.InsertPostListener listener) { PostRepository.instance.updatePost(post,listener); }

    public void updateUserIndo(String name,Uri imageBitmap, UserRepository.UpdateUserInfoListener listener) {
        UserRepository.instance.updateUserInfo(name,imageBitmap, listener);
    }
    public void isUserValid(String userId, UserRepository.ExistUserListener listener)
    {
        UserRepository.instance.isUserValid(userId, listener);
    }
}
