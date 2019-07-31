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
    ModelFirebase modelFirebase = ModelFirebase.instance;

    PostRepository() {
        AppLocalDbRepository db = ModelSql.db;

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

    class PostDetailLiveData extends MutableLiveData<Post>{
        String postKey = null;

        public void setPostKey(final String postKey) {
            this.postKey = postKey;
            PostAsyncDao.getPost(postKey, new GetPostListener() {
                @Override
                public void onResponse(Post p) {
                    Log.d("TAG","post received from room" + postKey );
                    setValue(p);
                }

                @Override
                public void onError() {

                }
            });
        }

        public PostDetailLiveData() {
            super();
        }

        @Override
        protected void onActive() {
            super.onActive();
            if(postKey!=null){
                modelFirebase.getPost(this.postKey, new GetPostListener() {
                    @Override
                    public void onResponse(Post p) {
                        Log.d("TAG","post received from firebase" + postKey );
                        setValue(p);
                        PostAsyncDao.insertPost(p);
                    }

                    @Override
                    public void onError() {
                        PostAsyncDao.getPost(postKey, new GetPostListener() {
                            @Override
                            public void onResponse(Post p) {
                                Log.d("TAG","post received from room" + postKey );
                                setValue(p);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

        }

        @Override
        protected void onInactive() {
            super.onInactive();
            Log.d("TAG","cancellGetPost");
        }
    }

    public interface GetPostListener{
        void onResponse(Post p);
        void onError();
    }
    public interface GetAllPostsListener{
        void onResponse(List<Post> list);
        void onError();
    }
    PostListLiveData postListLiveData = new PostListLiveData();
    PostDetailLiveData postDetailLiveData = new PostDetailLiveData();


    public LiveData<List<Post>> getmAllPosts() {
        return postListLiveData;
    }

    public LiveData<Post> getPost(String postKey){
        postDetailLiveData.setPostKey(postKey);
        return postDetailLiveData;
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

