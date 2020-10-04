package com.example.newsapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newsapp.data.NewsData;
import com.example.newsapp.database.MainRepository;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.database.NewsRoomDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG ="MainViewModel" ;
    private MainRepository repository;
    private LiveData<List<NewsEntity>> allNews;
    private MutableLiveData<List<NewsData>> topNewsLiveData = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
        allNews = repository.getAllNotes();
        topNewsLiveData = repository.getTopNewsLiveData();
        getTopNews();
    }

    //Wrapper methods for database operations.
    public void insert(NewsEntity note){
        Log.d(TAG, "insert: Red: insert ");
        repository.insert(note);
    }
    public void delete(NewsEntity note){
        repository.delete(note);
    }
    public void deleteAll(){
        repository.deleteAll();
    }
    public LiveData<List<NewsEntity>> getAllNotes(){
        return allNews;
    }
    public void getTopNews(){
        Log.d(TAG, "getTopNews: Yellow");
        repository.getTopNews();
    }

    public MutableLiveData<List<NewsData>> getTopNewsLiveData()
    {
        Log.d(TAG, "getTopNewsLiveData: Yellow... Called.");
        return topNewsLiveData;
    }



}
