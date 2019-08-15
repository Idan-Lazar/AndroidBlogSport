package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.idanl.blogsport.Models.Entities.User;
import com.example.idanl.blogsport.Models.UserRepository;


import java.util.List;

public class UserListViewModel extends AndroidViewModel {


    private LiveData<List<User>> data;

    public UserListViewModel(Application application) {
        super(application);
        data = UserRepository.instance.getmAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return data;}


}