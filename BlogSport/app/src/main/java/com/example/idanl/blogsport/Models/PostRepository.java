package com.example.idanl.blogsport.Models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PostRepository {
    final public static PostRepository instance = new PostRepository();
    private PostDao mPostDao;
    ModelFirebase modelFirebase;

    PostRepository() {
        AppLocalDbRepository db = ModelSql.db;
        modelFirebase = new ModelFirebase();

    }
    class PostListLiveData extends MutableLiveData<List<Post>>{
        @Override
        protected void onActive() {
            super.onActive();
            modelFirebase.getAllPosts(new GetAllPostsListener() {
                @Override
                public void onResponse(List<Post> list) {
                    Log.d("TAG","FB data = " + list.size() );
                    setValue(list);
                    PostAsyncDao.deleteAll();
                    PostAsyncDao.insertPosts(list);

                }
                public void onError()
                {
                    PostAsyncDao.getAllPosts(new GetAllPostsListener() {
                        @Override
                        public void onResponse(List<Post> list) {
                            setValue(list);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });

        }
        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancellGetAllStudents();
            Log.d("TAG","cancellGetAllStudents");
        }
        public PostListLiveData() {
            super();
            PostAsyncDao.getAllPosts(new GetAllPostsListener() {
                                         @Override
                                         public void onResponse(List<Post> list) {
                                             setValue(list);
                                         }

                @Override
                public void onError() {

                }
            });
        }
    }

    public interface GetAllPostsListener{
        void onResponse(List<Post> list);
        void onError();
    }
    PostListLiveData postListLiveData = new PostListLiveData();

    public LiveData<List<Post>> getmAllPosts() {
        return postListLiveData;
    }


    public interface InsertPostListener{
        void onComplete(boolean success);
        void onError(Exception e);
        void onOffline();
    }
    public void insert (Post post,InsertPostListener listener) {
       modelFirebase.addPost(post,listener);
       //PostAsyncDao.insertPost(post);
    }
    public interface SaveImageListener{
        void onComplete(String uri);
        void onOffline();
        void onError(Exception e);
    }
    public void saveBlogImage(Uri imageBitmap, SaveImageListener listener) {
        modelFirebase.saveBlogImage(imageBitmap, listener);
    }

}

