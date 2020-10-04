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

    private List<NewsData> topNewsList = new ArrayList<>();
    private Set<String> topNewsSet = new HashSet<>();

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


    public void insert(NewsEntity news) {
        new InsertNewsAsyncTask(newsDao).execute(news);
    }

    public void delete(NewsEntity news) {
        new DeleteNewsAsyncTask(newsDao).execute(news);
    }

    public void deleteAll() {
        new DeleteAllNewsAsyncTask(newsDao).execute();
    }


    /*Todo: Edit is required here.. . whereEqualTo()*/
    public void getTopNews(){
        Log.d(TAG, "getTopNews: Yellow Called.");
        firebaseFirestore.collection(NEWS_POST)
                .whereEqualTo("location", "Durg")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            // if task is not complete.
                            Log.d(TAG, "onComplete: Yellow Failed...");
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

                    }
                });
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
