package com.example.newsapp.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newsapp.activity.MainActivity;
import com.example.newsapp.data.NewsData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainRepository {
    private static final String TAG = "MainRepository";

    public static final String NEWS_POST = "News Posts";

    private NewsDao newsDao;
    private LiveData<List<NewsEntity>> allNews;
    private FirebaseFirestore firebaseFirestore;

    private List<NewsData> locationList = new ArrayList<>();
    private List<NewsData> topNewsList = new ArrayList<>();
    private Set<String> topNewsSet = new HashSet<>();
    private List<NewsData> sportsNewsList = new ArrayList<>();
    private Set<String> sportsNewsSet = new HashSet<>();
    private List<NewsData> govSchemNewsList = new ArrayList<>();
    private Set<String> govSchemeNewsSet = new HashSet<>();
    private List<NewsData> economyNewsList = new ArrayList<>();
    private Set<String> economyNewsSet = new HashSet<>();
    private List<NewsData> covidNewsList = new ArrayList<>();
    private Set<String> covidNewsSet = new HashSet<>();



    MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> topNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> sportsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> govSchemeNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> covidNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> economyNewsLiveData = new MutableLiveData<>();

    public MainRepository (Application application){
        NewsRoomDatabase database = NewsRoomDatabase.getInstance(application);
        firebaseFirestore = FirebaseFirestore.getInstance();
        newsDao = database.NewsDao();
        allNews = newsDao.getAllNotes();

    }


    public LiveData<List<NewsEntity>> getAllNotes() {
        return allNews;
    }
    public MutableLiveData<List<NewsData>> getTopNewsLiveData() {
        Log.d(TAG, "getTopNewsLiveData: Yellow called...");
        return topNewsLiveData;
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

    public MutableLiveData<Boolean> getProgressLiveData() {
        return progressLiveData;
    }

    public void insert(NewsEntity news) {
        new InsertNewsAsyncTask(newsDao).execute(news);
    }

    public void delete(NewsEntity news) {
        new DeleteNewsAsyncTask(newsDao).execute(news);
    }

    public void deleteAll() {
        new DeleteAllNewsAsyncTask(newsDao).execute();
    }



    public void getTopNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        progressLiveData.postValue(true);
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
                            progressLiveData.postValue(false);
                            return;
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                if(!topNewsSet.contains(data.getNewsId())){
                                    topNewsSet.add(data.getNewsId());
                                    Log.d(TAG, "onComplete: Yellow Adding to list");
                                    topNewsList.add(data);
                                }
                            }
                        }
                        Log.d(TAG, "onComplete: Yellow All news added. Size : " + topNewsList.size() );
                        topNewsLiveData.postValue(topNewsList); // putting in live data variable
                        progressLiveData.postValue(false);

                    }
                });
    }

    public void getSportsNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        progressLiveData.postValue(true);
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp")
                .whereEqualTo("category","sports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
                            progressLiveData.postValue(false);
                            return;
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                if(!sportsNewsSet.contains(data.getNewsId())){
                                    sportsNewsSet.add(data.getNewsId());
                                    Log.d(TAG, "onComplete: Yellow Adding to list");
                                    sportsNewsList.add(data);
                                }
                            }
                        }
                        Log.d(TAG, "onComplete: QQQ All news added. sports  : " + sportsNewsList.size() );
                        sportsNewsLiveData.postValue(sportsNewsList); // putting in live data variable
                        progressLiveData.postValue(false);

                    }
                });
    }
    public void getGovNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        progressLiveData.postValue(true);
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp")
                .whereEqualTo("category","gov schemes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
                            progressLiveData.postValue(false);
                            return;
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                if(!govSchemeNewsSet.contains(data.getNewsId())){
                                    govSchemeNewsSet.add(data.getNewsId());
                                    Log.d(TAG, "onComplete: Yellow Adding to list");
                                    govSchemNewsList.add(data);
                                }
                            }
                        }
                        Log.d(TAG, "onComplete: QQQ All news added. Size : " + govSchemNewsList.size() );
                        govSchemeNewsLiveData.postValue(govSchemNewsList); // putting in live data variable
                        progressLiveData.postValue(false);

                    }
                });
    }

    public void getCovidNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        progressLiveData.postValue(true);
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp")
                .whereEqualTo("category","covid")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
                            progressLiveData.postValue(false);
                            return;
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                if(!covidNewsSet.contains(data.getNewsId())){
                                    covidNewsSet.add(data.getNewsId());
                                    Log.d(TAG, "onComplete: Yellow Adding to list");
                                    covidNewsList.add(data);
                                }
                            }
                        }
                        Log.d(TAG, "onComplete: QQQ All news added. covid : " + covidNewsList.size() );
                        covidNewsLiveData.postValue(covidNewsList); // putting in live data variable
                        progressLiveData.postValue(false);

                    }
                });
    }
    public void getEconomyNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        progressLiveData.postValue(true);
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp")
                .whereEqualTo("category","economy")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
                            progressLiveData.postValue(false);
                            return;
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                if(!economyNewsSet.contains(data.getNewsId())){
                                    economyNewsSet.add(data.getNewsId());
                                    Log.d(TAG, "onComplete: Yellow Adding to list");
                                    economyNewsList.add(data);
                                }
                            }
                        }
                        Log.d(TAG, "onComplete: QQQ All news added. economy : " + economyNewsList.size() );
                        economyNewsLiveData.postValue(economyNewsList); // putting in live data variable
                        progressLiveData.postValue(false);

                    }
                });
    }

    public void getLocalNews(final String location){
        progressLiveData.postValue(true);
        locationList.clear();
        topNewsList.clear();
        economyNewsList.clear();
        covidNewsList.clear();
        govSchemNewsList.clear();
        sportsNewsList.clear();

        if(location.equals("all")){
            getAllLocationNews();
            return;
        }




        firebaseFirestore.collection(NEWS_POST)
                .whereEqualTo("location", location)
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.d(TAG, "onComplete: Failed... getting location data.");
                            return;
                        }

                        List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot s:
                            snapshots) {
                            NewsData data = s.toObject(NewsData.class);

                            if(data != null){
                                locationList.add(data);
                            }
                        }
                        Log.d(TAG, "onComplete: Loaction list size :  " + locationList.size());

                        for (NewsData d :
                                locationList) {
                            placeInRespectiveList(d);
                        }

                        topNewsLiveData.postValue(topNewsList);
                        sportsNewsLiveData.postValue(sportsNewsList);
                        covidNewsLiveData.postValue(covidNewsList);
                        govSchemeNewsLiveData.postValue(govSchemNewsList);
                        economyNewsLiveData.postValue(economyNewsList);
                        progressLiveData.postValue(false);
                    }
                });
    }

    private void getAllLocationNews (){
        firebaseFirestore.collection(NEWS_POST)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG, "onComplete: Failed... getting location data.");
                    return;
                }

                List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                for (DocumentSnapshot s:
                        snapshots) {
                    NewsData data = s.toObject(NewsData.class);

                    if(data != null){
                        locationList.add(data);
                    }
                }
                Log.d(TAG, "onComplete: All Loaction list size :  " + locationList.size());

                for (NewsData d :
                        locationList) {
                    placeInRespectiveList(d);
                }

                topNewsLiveData.postValue(topNewsList);
                sportsNewsLiveData.postValue(sportsNewsList);
                covidNewsLiveData.postValue(covidNewsList);
                govSchemeNewsLiveData.postValue(govSchemNewsList);
                economyNewsLiveData.postValue(economyNewsList);
                progressLiveData.postValue(false);
            }
        });
    }
    private void placeInRespectiveList(NewsData data){

        String category = data.getCategory();
        switch (category){
            case "top news":
                topNewsList.add(data);
                break;
            case "sports":
                sportsNewsList.add(data);
                break;
            case "gov schemes":
                govSchemNewsList.add(data);
                break;
            case "covid":
                covidNewsList.add(data);
                break;
            case "economy":
                economyNewsList.add(data);
                break;
            case "political":
                break;
            case "awards":
                break;
            case "arts and culture":
                break;
            case "person in news":
                break;
        }
    }


    private static class InsertNewsAsyncTask extends AsyncTask<NewsEntity, Void, Void> {

        private static final String TAG = "InsertNewsAsyncTask";
        private NewsDao newsDao;

        private InsertNewsAsyncTask(NewsDao newsDao) {
            this.newsDao = newsDao;
            Log.d(TAG, "InsertNewsAsyncTask: Red: called");
        }

        @Override
        protected Void doInBackground(NewsEntity... news) {
            newsDao.insert(news[0]);
            return null;
        }
    }



    private static class DeleteNewsAsyncTask extends AsyncTask<NewsEntity, Void, Void> {

        private NewsDao noteDao;

        private DeleteNewsAsyncTask(NewsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NewsEntity... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNewsAsyncTask extends AsyncTask<Void, Void, Void> {

        private NewsDao noteDao;

        private DeleteAllNewsAsyncTask(NewsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAll();
            return null;
        }


    }

}
