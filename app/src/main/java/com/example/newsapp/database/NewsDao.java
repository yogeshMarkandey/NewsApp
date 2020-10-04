package com.example.newsapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(NewsEntity news);
    @Update
    void update(NewsEntity news);
    @Delete
    void delete(NewsEntity news);
    @Query("DELETE FROM news_table")
    void deleteAll();

    @Query("SELECT * FROM news_table")
    LiveData<List<NewsEntity>> getAllNotes();

}
