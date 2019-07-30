package com.example.idanl.blogsport.Models;

import android.app.Application;
import android.os.AsyncTask;
import android.view.Display;

import androidx.lifecycle.LiveData;

import com.example.idanl.blogsport.Adapters.MyApplication;

import java.util.List;

public class PostRepository {
    private PostDao mPostDao;
    private LiveData<List<Post>> mAllPosts;

    PostRepository(Application application) {
        AppLocalDbRepository db = ModelSql.db;
        mPostDao = db.postDao();
        mAllPosts = mPostDao.getAllPosts();
    }
    LiveData<List<Post>> getmAllPosts() {
        return mAllPosts;
    }


    public void insert (Post post) {
        new insertAsyncTask(mPostDao).execute(post);
    }

    private static class insertAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao mAsyncTaskDao;

        insertAsyncTask(PostDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Post... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

