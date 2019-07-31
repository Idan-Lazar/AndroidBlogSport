package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.idanl.blogsport.Models.Entities.User;
import com.example.idanl.blogsport.Models.UserReposiroty;

public class UserViewModel extends AndroidViewModel {
    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void signUp(String email, String password, final UserReposiroty.SignUpListener listener){
       UserReposiroty.instance.signUp(email,password, listener);
    }
    public void updateUserInfo(final String userName, Uri pickerImgUri, UserReposiroty.UpdateUserInfoListener listener)
    {
        UserReposiroty.instance.updateUserInfo(userName,pickerImgUri,listener);
    }

    public void signIn(String email, String password, UserReposiroty.SignInListener listener) {
        UserReposiroty.instance.signIn(email,password,listener);
    }

    public boolean isSigned(){
        return UserReposiroty.instance.isSigned();
    }

    public Uri getUserImageUrl() {return UserReposiroty.instance.getUserImageUrl();}
    public String getUid() {return UserReposiroty.instance.getUid();}
    public String getDisplayName() {return UserReposiroty.instance.getDisplayName();}

}
