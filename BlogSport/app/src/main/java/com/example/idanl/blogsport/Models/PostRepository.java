package com.example.idanl.blogsport.Models;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.idanl.blogsport.Models.Entities.Post;

import java.util.List;

public class PostRepository {
    final public static PostRepository instance = new PostRepository();
    private PostDao mPostDao;
    ModelFirebasePost modelFirebase = ModelFirebasePost.instance;

    interface changeLikePostListener {
        void onLike();
        void onDislike();
        void onOffline();
        void onError(Exception e);
    }
    PostRepository() {
        AppLocalDbRepository db = ModelSql.db;

    }
    class PostListLiveData extends MutableLiveData<List<Post>>{
        @Override
        protected void onActive() {
            super.onActive();
            modelFirebase.activateGetAllPostsListener(new GetAllPostsListener() {
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
            modelFirebase.removeGetAllPostsListener();
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

    public void updatePost (Post p, InsertPostListener listener)
    {
        modelFirebase.updatePost(p,listener);
    }
    public interface GetPostListener{
        void onResponse(Post p);
        void onError();
    }
    public interface GetAllPostsListener{
        void onResponse(List<Post> list);
        void onError();
    }

    public void getPostFirebase(String postKey, GetPostListener listener)
    {
        modelFirebase.getPost(postKey, listener);
    }
    public void getPostDao(String postKey, GetPostListener listener)
    {
        PostAsyncDao.getPost(postKey, listener);
    }
    private PostListLiveData postListLiveData = new PostListLiveData();



    public LiveData<List<Post>> getmAllPosts() {
        return postListLiveData;
    }

    public interface InsertPostListener{
        void onComplete(boolean success);
        void onError(Exception e);
        void onOffline();
    }
    public void insert (final Post post, final InsertPostListener listener) {
       modelFirebase.addPost(post, new InsertPostListener() {
           @Override
           public void onComplete(boolean success) {
               PostAsyncDao.insertPost(post);
               listener.onComplete(success);
           }

           @Override
           public void onError(Exception e) {
listener.onError(e);
           }

           @Override
           public void onOffline() {
listener.onOffline();
           }
       });

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

