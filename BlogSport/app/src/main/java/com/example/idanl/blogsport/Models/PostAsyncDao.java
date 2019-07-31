package com.example.idanl.blogsport.Models;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import javax.annotation.Nullable;

@Dao
interface PostDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("DELETE FROM post_table")
    void deleteAll();

    @Query("DELETE FROM POST_TABLE WHERE postKey = :postKey ")
    void deletePost(int postKey);

    @Query("SELECT * FROM POST_TABLE")
    List<Post> getAllPosts();

    @Query("SELECT * FROM POST_TABLE WHERE postKey = :postKey")
    Post getPost(int postKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPosts(List<Post> posts);
}

public class PostAsyncDao{

    public static void getAllPosts(final PostRepository.GetAllPostsListener listener) {
        new AsyncTask<String,String,List<Post>>(){

            @Override
            protected List<Post> doInBackground(String... strings) {
               List<Post> list = ModelSql.db.postDao().getAllPosts();
               return list;
            }

            @Override
            protected void onPostExecute(List<Post> data) {
                super.onPostExecute(data);
                listener.onResponse(data);

            }
        }.execute();

    }
    public static void insertPosts(final List<Post> posts)
    {
        new AsyncTask<String,String,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                ModelSql.db.postDao().insertPosts(posts);
                return posts.size();
            }


        }.execute();
    }
    public static void insertPost(final Post post)
    {
       new AsyncTask<Void, Void, Void>() {

           @Override
           protected Void doInBackground(Void... voids) {
               ModelSql.db.postDao().insert(post);
               Log.d("SQL", "Insert post to sql");
               return null;
           }


        }.execute();
    }

    public static void deleteAll()
    {
        new AsyncTask<String,String, String>(){

            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.postDao().deleteAll();
                Log.d("SQL", "Delete all the db");
                return null;
            }


        }.execute();
    }
}