package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostAsyncDao;
import com.example.idanl.blogsport.Models.PostRepository;

import java.util.LinkedList;

public class PostsPerUserViewModel extends AndroidViewModel {

    private PostsPerUserLiveData data;

    PostsPerUserViewModel(@NonNull Application application, String userId) {
        super(application);
        data = new PostsPerUserLiveData(userId);
    }
    class PostsPerUserLiveData extends MutableLiveData<LinkedList<Post>>{
        private  String userId;
        @Override
        protected void onActive() {
            super.onActive();
            PostRepository.instance.activatePostPerUserFirebaseListener(userId,new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    Log.d("GetPostsPerUser","FB data = " + list.size() );
                    setValue(list);

                }
                public void onError()
                {
                    PostAsyncDao.getPostsPerUser(userId,new PostRepository.GetAllPostsListener() {
                        @Override
                        public void onResponse(LinkedList<Post> list) {
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
            PostRepository.instance.disActivatePostsPerUserListener();
            Log.d("TAG","cancellGetPostsPerUser");
        }
        public PostsPerUserLiveData(String userId) {
            super();
            this.userId = userId;
            PostAsyncDao.getAllPosts(new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    setValue(list);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
    public void refresh()
    {
        this.data.onActive();
    }
    public LiveData<LinkedList<Post>> getPosts()
    {
       return data;
    }
}
