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
    private MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>();

    private MutableLiveData<List<NewsData>> sportsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> govSchemeNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> covidNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> economyNewsLiveData = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
        allNews = repository.getAllNotes();


        this.topNewsLiveData = repository.getTopNewsLiveData();
        this.progressLiveData = repository.getProgressLiveData();
        this.sportsNewsLiveData = repository.getSportsNewsLiveData();
        this.govSchemeNewsLiveData = repository.getGovSchemeNewsLiveData();
        this.covidNewsLiveData = repository.getCovidNewsLiveData();
        this.economyNewsLiveData = repository.getEconomyNewsLiveData();



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

    public void callAllNewsChannels(){
        getTopNews();
        repository.getSportsNews();
        repository.getCovidNews();
        repository.getEconomyNews();
        repository.getGovNews();
    }

    public void getLocationNews(String location){
        repository.getLocalNews(location);
    }


    public MutableLiveData<List<NewsData>> getTopNewsLiveData()
    {
        Log.d(TAG, "getTopNewsLiveData: Yellow... Called.");
        return topNewsLiveData;
    }

    public MutableLiveData<Boolean> getProgressLiveData() {
        return progressLiveData;
    }

    public MutableLiveData<List<NewsData>> getSportsNewsLiveData() {
        return sportsNewsLiveData;
    }

    public MutableLiveData<List<NewsData>> getGovSchemeNewsLiveData() {
        return govSchemeNewsLiveData;
    }

    public MutableLiveData<List<NewsData>> getCovidNewsLiveData() {
        return covidNewsLiveData;
    }

    public MutableLiveData<List<NewsData>> getEconomyNewsLiveData() {
        return economyNewsLiveData;
    }
}
