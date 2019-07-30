package com.example.idanl.blogsport.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PostDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("DELETE FROM post_table")
    void deleteAll();

    @Query("DELETE FROM POST_TABLE WHERE postKey = :postKey ")
    void deletePost(int postKey);

    @Query("SELECT * FROM POST_TABLE")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM POST_TABLE WHERE postKey = :postKey")
    LiveData<Post> getPost(int postKey);
}