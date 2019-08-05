package com.example.idanl.blogsport.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.firebase.database.ServerValue;

import java.util.Date;

@Entity
public class Comment {
    @NonNull
    private String content, uid, uimg, uname;

    private Date timestamp;

    @Ignore
    public Comment(String content, String uid, String uimg, String uname, Date timestamp) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.timestamp = timestamp;
    }


    public Comment(String content, String uid, String uimg, String uname) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
    }

    public Comment() {
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }

    public String getUimg() {
        return uimg;
    }

    public String getUname() {
        return uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
