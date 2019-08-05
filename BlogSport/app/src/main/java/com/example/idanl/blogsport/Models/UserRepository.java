package com.example.idanl.blogsport.Models;


import android.net.Uri;

import com.example.idanl.blogsport.Models.Entities.User;

import java.util.List;

public class UserRepository {

    final public static UserRepository instance = new UserRepository();
    ModelFirebaseUser modelFirebaseUser = ModelFirebaseUser.instance;
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UserRepository(){

    }

    public boolean isSigned() {
        if (modelFirebaseUser.isSigned())
        {
            modelFirebaseUser.getCurrentUser(new GetUserListener() {
                @Override
                public void onResponse(User user) {
                    currentUser = user;

                }

                @Override
                public void onError() {

                }
            });
            return true;
        }
        else
        {
            return false;
        }
    }

    public Uri getUserImageUrl() {
        return modelFirebaseUser.getUserImageUrl();
    }

    public String getUid() {
        return modelFirebaseUser.getUid();
    }

    public String getDisplayName() {
        return modelFirebaseUser.getDisplayName();
    }

    public void getUserFirebase(String userId, UserRepository.GetUserListener listener)
    {
        modelFirebaseUser.getUser(userId, listener);
    }
    public void getUserDao(String postKey, UserRepository.GetUserListener listener)
    {
        UserAsyncDao.getUser(postKey, listener);
    }
    public interface SignInListener{
        void onSuccess();
        void onFailer(Exception e);
        void onOffiline();
    }
    public void signIn(String email, String password, final UserRepository.SignInListener listener) {
        modelFirebaseUser.signIn(email, password, new SignInListener() {
            @Override
            public void onSuccess() {
                getCurrentUser(new GetUserListener() {
                    @Override
                    public void onResponse(User user) {
                        currentUser = user;
                        listener.onSuccess();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onFailer(Exception e) {
listener.onFailer(e);
            }

            @Override
            public void onOffiline() {
listener.onOffiline();
            }
        });
    }

    public interface SignUpListener{
        void onSuccess();
        void onFailer(Exception e);
        void onOffiline();
    }
    public void signUp(String email, String password,SignUpListener listener)
    {
        modelFirebaseUser.signUp(email,password,listener);
    }

    public void getCurrentUser(GetUserListener listener) {
        modelFirebaseUser.getCurrentUser(listener);
    }

    public interface UpdateUserInfoListener{
        void onSuccess(String uri);
        void onFailer(Exception e);
        void onOffiline();
    }
    public void updateUserInfo(final String userName, Uri pickerImgUri, final UpdateUserInfoListener listener)
    {
        modelFirebaseUser.updateUserInfo(userName, pickerImgUri, modelFirebaseUser.getCurrentUser(), new UpdateUserInfoListener() {
            @Override
            public void onSuccess(String uri) {
                assert modelFirebaseUser.getCurrentUser().getPhotoUrl()!=null;
                assert modelFirebaseUser.getCurrentUser().getEmail()!=null;
                currentUser = new User(modelFirebaseUser.getCurrentUser().getUid(), modelFirebaseUser.getCurrentUser().getEmail(), modelFirebaseUser.getCurrentUser().getDisplayName(),uri);
                UserAsyncDao.insertUser(currentUser);
                listener.onSuccess(uri);
            }

            @Override
            public void onFailer(Exception e) {

            }

            @Override
            public void onOffiline() {

            }
        });
    }

    public interface GetAllUsersListener {
        void onResponse(List<User> list);
        void onError();
    }

    public interface GetUserListener {
        void onResponse(User user);
        void onError();
    }
    public void getUser(String userId, GetUserListener listener)
    {
        if (modelFirebaseUser.isNetworkConnected())
        {
            this.getUserFirebase(userId,listener);
        }
        else
        {
            this.getUserDao(userId,listener);
        }
    }

    public interface SaveImageListener{
        void onComplete(Uri uri);
        void onOffline();
        void onError(Exception e);
    }

}
