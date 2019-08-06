package com.example.idanl.blogsport.Models.Entities;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.navigation.Navigator;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @Ignore
    public User() {
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String uid;

    private String name;
    @NonNull
    private String email;

    private String userImage;
    public User(@NonNull String uid,@NonNull String email, String name,  String userImage) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userImage = userImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


}
