package com.example.idanl.blogsport.Models;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String title;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    private String second_title;
    private String category;
    private String content;
    private String picture;
    private String userId;
    private String userPhoto;
    private Object timestamp;

    public Post(String title, String second_title, String category, String content, String picture, String userId, String userPhoto) {
        this.title = title;
        this.second_title = second_title;
        this.category = category;
        this.content = content;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post() {
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

    public Object getTimestamp() {
        return timestamp;
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

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
