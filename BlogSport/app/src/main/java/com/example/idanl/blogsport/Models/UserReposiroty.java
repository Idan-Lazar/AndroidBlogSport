package com.example.idanl.blogsport.Models;


import android.net.Uri;

import com.example.idanl.blogsport.Models.Entities.User;
import com.google.firebase.auth.FirebaseUser;

public class UserReposiroty {

    final public static UserReposiroty instance = new UserReposiroty();
    ModelFirebase modelFirebase = ModelFirebase.instance;
    public UserReposiroty(){

    }

    public boolean isSigned() {
        return modelFirebase.isSigned();
    }

    public Uri getUserImageUrl() {
        return modelFirebase.getUserImageUrl();
    }

    public String getUid() {
        return modelFirebase.getUid();
    }

    public String getDisplayName() {
        return modelFirebase.getDisplayName();
    }

    public interface SignInListener{
        void onSuccess();
        void onFailer(Exception e);
        void onOffiline();
    }
    public void signIn(String email, String password, UserReposiroty.SignInListener listener) {
        modelFirebase.signIn(email,password, listener);
    }

    public interface SignUpListener{
        void onSuccess();
        void onFailer(Exception e);
        void onOffiline();
    }
    public void signUp(String email, String password,SignUpListener listener)
    {
        modelFirebase.signUp(email,password,listener);
    }

    public FirebaseUser getCurrentUser()
    {
        return modelFirebase.getCurrentUser();
    }
    public interface UpdateUserInfoListener{
        void onSuccess();
        void onFailer(Exception e);
        void onOffiline();
    }
    public void updateUserInfo(final String userName, Uri pickerImgUri, UpdateUserInfoListener listener)
    {
        modelFirebase.updateUserInfo(userName,pickerImgUri,getCurrentUser(),listener);
    }

}
