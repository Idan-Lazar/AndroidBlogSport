package com.example.idanl.blogsport.Models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.idanl.blogsport.Adapters.MyApplication;


@Database(entities = {Post.class}, version = 20, exportSchema = false)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();

    private static volatile AppLocalDbRepository INSTANCE;

    static AppLocalDbRepository getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppLocalDbRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppLocalDbRepository.class, "database.db")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PostDao mDao;

        PopulateDbAsync(AppLocalDbRepository db) {
            mDao = db.postDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Post p = new Post("Cake","gg","gfdgg","ggfdfg","https://firebasestorage.googleapis.com/v0/b/blogsport-29b88.appspot.com/o/blog_images%2Fimage%3A39363?alt=media&token=156c6edd-0826-4ed3-9833-f5dcecc66073","rmz2xeA9jJSorDxVsdjh0ePrSgQ2","https://firebasestorage.googleapis.com/v0/b/blogsport-29b88.appspot.com/o/users_photos%2Fimage%3A22100?alt=media&token=8e8f1f5f-4528-4df3-b0cf-587d8e0667e2","idan",3);
            p.setPostKey("ggfdgfdfgdfc");
            mDao.insert(p);
            p = new Post("Cake","gg","gfdgg","ggfdfg","https://firebasestorage.googleapis.com/v0/b/blogsport-29b88.appspot.com/o/blog_images%2Fimage%3A39363?alt=media&token=156c6edd-0826-4ed3-9833-f5dcecc66073","rmz2xeA9jJSorDxVsdjh0ePrSgQ2","https://firebasestorage.googleapis.com/v0/b/blogsport-29b88.appspot.com/o/users_photos%2Fimage%3A22100?alt=media&token=8e8f1f5f-4528-4df3-b0cf-587d8e0667e2","idan",3);
            p.setPostKey("gfdgfdhfds4r55d");
            mDao.insert(p);
            return null;
        }
    }
}

public class ModelSql {
        static public AppLocalDbRepository db = AppLocalDbRepository.getDatabase(MyApplication.getContext());


}

