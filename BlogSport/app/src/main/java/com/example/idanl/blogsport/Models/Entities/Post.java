package com.example.idanl.blogsport.Models.Entities;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.navigation.NavType;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_table")
public class Post  implements Serializable {

    @PrimaryKey
    @NonNull
    private String postKey;

    @NonNull
    private String title;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    @NonNull
    private String second_title;
    @NonNull
    private String category;
    @NonNull
    private String content;
    @NonNull
    private String picture;
    @NonNull
    private String userId;
    @NonNull
    private int likes;
    @NonNull
    private String userName;
    @NonNull
    private String userPhoto;

    /*
    To change the timestap into data time
    * */
    @Ignore
    private Object timestamp;


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;

    }

    public Post(String title, String second_title, String category, String content, String picture, String userId, String userPhoto, String userName, int likes) {
        this.likes = likes;
        this.title = title;
        this.second_title = second_title;
        this.category = category;
        this.content = content;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timestamp = ServerValue.TIMESTAMP;
        this.userName = userName;
    }


    public Object getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Ignore
    public Post() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public String getSecond_title() {
        return second_title;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSecond_title(String second_title) {
        this.second_title = second_title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }


}
