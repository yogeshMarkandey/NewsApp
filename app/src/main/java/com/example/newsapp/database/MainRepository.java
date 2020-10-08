package com.example.newsapp.database;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newsapp.activity.MainActivity;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainRepository {
    private static final String TAG = "MainRepository";

    public static final String NEWS_POST = "News Posts";
    public static final String USERS_DATABASE = "User Database";

    private NewsDao newsDao;
    private LiveData<List<NewsEntity>> allNews;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;

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
    private List<NewsData> politicsNewsList = new ArrayList<>();
    private Set<String> politicsNewsSet = new HashSet<>();
    private List<NewsData> awardsNewsList = new ArrayList<>();
    private Set<String> awardsNewsSet = new HashSet<>();
    private List<NewsData> artsNewsList = new ArrayList<>();
    private Set<String> artsNewsSet = new HashSet<>();
    private List<NewsData> personNewsList = new ArrayList<>();
    private Set<String> personNewsSet = new HashSet<>();



    MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> topNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> sportsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> govSchemeNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> covidNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> economyNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> politicsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> awardsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> artsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> personNewsLiveData = new MutableLiveData<>();

    private MutableLiveData<UserData> userMutableLiveData = new MutableLiveData<>();


    public MainRepository (Application application){
        NewsRoomDatabase database = NewsRoomDatabase.getInstance(application);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        newsDao = database.NewsDao();
        allNews = newsDao.getAllNotes();

    }


    public MutableLiveData<List<NewsData>> getPoliticsNewsLiveData() {
        return politicsNewsLiveData;
    }

    public MutableLiveData<UserData> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<List<NewsData>> getAwardsNewsLiveData() {
        return awardsNewsLiveData;
    }

    public MutableLiveData<List<NewsData>> getArtsNewsLiveData() {
        return artsNewsLiveData;
    }

    public MutableLiveData<List<NewsData>> getPersonNewsLiveData() {
        return personNewsLiveData;
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
        politicsNewsList.clear();
        artsNewsList.clear();
        awardsNewsList.clear();
        personNewsList.clear();

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
                        personNewsLiveData.postValue(personNewsList);
                        artsNewsLiveData.postValue(artsNewsList);
                        awardsNewsLiveData.postValue(awardsNewsList);
                        personNewsLiveData.postValue(personNewsList);
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
                personNewsLiveData.postValue(personNewsList);
                artsNewsLiveData.postValue(artsNewsList);
                awardsNewsLiveData.postValue(awardsNewsList);
                politicsNewsLiveData.postValue(politicsNewsList);
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
                politicsNewsList.add(data);
                break;
            case "awards":
                awardsNewsList.add(data);
                break;
            case "arts and culture":
                artsNewsList.add(data);
                break;
            case "person in news":
                personNewsList.add(data);
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

    public void saveUserDataInFirestore(String uId, String email){

        UserData userDetails = new UserData();
        userDetails.setEmailId(email);
        userDetails.setuId(uId);
        String name = email.substring(0, email.indexOf('@'));
        userDetails.setName(name);
        userDetails.setUserName(email.substring(0, email.indexOf('@')));

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection(USERS_DATABASE)
                .document(uId)
                .set(userDetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Successful.");
                        }else{
                            Log.d(TAG, "onComplete: UnSuccessful.");
                        }
                    }
                });

    }

    public void savePhotoAndUpdateDatabase(Uri imageUri){
        //When post is clicked
        //saving image in firestore
        final StorageReference profile = storageRef.child("profile/" + System.currentTimeMillis());
        profile.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: image save was successful");
                        // save the profile link in user data base
                        profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "onSuccess: Setting the image url to the database");

                                HashMap<String, String> profileMap = new HashMap<>();
                                profileMap.put("avatar", uri.toString());
                                /*Todo: Update all the user details.*/
                                /*Todo: Something is wrong here...
                                *  uploading imageurl deletes other fields*/
                                DocumentReference document = firebaseFirestore
                                        .collection("User Database")
                                        .document(FirebaseAuth.getInstance().getUid());
                                document.set(profileMap, SetOptions.merge());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error : " + e.getMessage());
                    }
                });
    }

    public void updateUI(){
        firebaseFirestore.collection(USERS_DATABASE)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UserData data = task.getResult().toObject(UserData.class);
                            if(data != null){
                                userMutableLiveData.postValue(data);
                            }
                        }
                    }
                });
    }

    public void resetPassword(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Password Reset email sent.");
                        }
                    }
                });
    }

    public void deleteUserDatabase(String uid){
        firebaseFirestore.collection(USERS_DATABASE)
                .document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Deleted User Details..");
                    }
                });

    }
}
