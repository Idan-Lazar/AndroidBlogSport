package com.example.idanl.blogsport.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.idanl.blogsport.Models.CommentAsyncDao;
import com.example.idanl.blogsport.Models.CommentRepository;
import com.example.idanl.blogsport.Models.Entities.Comment;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.PostAsyncDao;
import com.example.idanl.blogsport.Models.PostRepository;

import java.util.List;

public class CommentListViewModel extends AndroidViewModel {
    private CommentRepository repository = CommentRepository.instance;
    private CommentListLiveData data;
    CommentListViewModel(@NonNull Application application, String postKey) {
        super(application);
        data = new CommentListLiveData(postKey);
    }

    class CommentListLiveData extends MutableLiveData<List<Comment>>{
        String postKey;

        @Override
        protected void onActive() {
            super.onActive();
            CommentRepository.instance.getAllCommentsFirebase(this.postKey,new CommentRepository.GetAllCommentsListener() {
                @Override
                public void onResponse(List<Comment> list) {
                    Log.d("TAG","Comments data = " + list.size() );
                    setValue(list);
                    CommentAsyncDao.deleteAll(postKey);
                    CommentAsyncDao.insertComments(list);

                }
                public void onError()
                {
                    CommentRepository.instance.getAllCommentsDao(postKey,new CommentRepository.GetAllCommentsListener() {
                        @Override
                        public void onResponse(List<Comment> list) {
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
            Log.d("TAG","cancellGetAllComment");
        }
        public CommentListLiveData(String postKey) {
            super();
            this.postKey = postKey;
            CommentRepository.instance.getAllCommentsDao(postKey,new CommentRepository.GetAllCommentsListener() {
                @Override
                public void onResponse(List<Comment> list) {
                    setValue(list);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public void addComment(Comment c, CommentRepository.InsertCommentListener listener)
    {
        repository.insert(c,listener);
    }
    public LiveData<List<Comment>> getComments()
    {
       return data;
    }
    public void notifyChange(){
        data.onActive();
    }
}
