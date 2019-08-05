package com.example.idanl.blogsport.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.idanl.blogsport.Adapters.MyApplication;
import com.example.idanl.blogsport.Models.Entities.Converters;
import com.example.idanl.blogsport.Models.Entities.Post;
import com.example.idanl.blogsport.Models.Entities.User;

import java.util.ArrayList;
import java.util.List;


@Database(entities = {Post.class, User.class}, version = 26, exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract UserDao userDao();
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
                }
            };


}

public class ModelSql {
        static AppLocalDbRepository db = AppLocalDbRepository.getDatabase(MyApplication.getContext());


}

