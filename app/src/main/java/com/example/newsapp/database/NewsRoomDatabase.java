package com.example.newsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.newsapp.data.NewsData;
import com.example.newsapp.util.DateConverter;

@Database(entities = {NewsEntity.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class NewsRoomDatabase extends RoomDatabase {

    public static NewsRoomDatabase instance ;

    public abstract NewsDao NewsDao();

    public static synchronized NewsRoomDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context, NewsRoomDatabase.class, "News_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }



}
