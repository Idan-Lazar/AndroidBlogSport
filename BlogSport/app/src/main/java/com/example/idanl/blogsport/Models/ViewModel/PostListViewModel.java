package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostAsyncDao;
import com.example.idanl.blogsport.Models.PostRepository;

import java.util.LinkedList;
import java.util.List;

public class PostListViewModel extends AndroidViewModel {


    private LiveData<LinkedList<Post>> data;

    public PostListViewModel(Application application) {
        super(application);
        data = new PostListLiveData();
    }
    class PostListLiveData extends MutableLiveData<LinkedList<Post>> {
        @Override
        protected void onActive() {
            super.onActive();
            PostRepository.instance.activateGetPostsFirebaseListener(new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    Log.d("TAG","FB data = " + list.size() );
                    setValue(list);
                    PostAsyncDao.deleteAll();
                    PostAsyncDao.insertPosts(list);

                }
                public void onError()
                {
                    PostRepository.instance.getAllPostsDao(new PostRepository.GetAllPostsListener() {
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
            PostRepository.instance.disActivateGetAllPostsListener();
            Log.d("TAG","cancellGetAllStudents");
        }
        public PostListLiveData() {
            super();
            PostRepository.instance.getAllPostsDao(new PostRepository.GetAllPostsListener() {
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
    public LiveData<LinkedList<Post>> getAllPosts() { return data;}


}