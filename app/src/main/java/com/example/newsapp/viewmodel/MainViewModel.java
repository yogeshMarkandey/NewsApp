package com.example.newsapp.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.newsapp.activity.MainActivity;
import com.example.newsapp.activity.RegisterActivity;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.data.UserData;
import com.example.newsapp.database.MainRepository;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.database.NewsRoomDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private MutableLiveData<List<NewsData>> politicsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> awardsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> artsNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NewsData>> personNewsLiveData = new MutableLiveData<>();
    private MutableLiveData<UserData> userMutableLiveData = new MutableLiveData<>();

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
        this.politicsNewsLiveData = repository.getPoliticsNewsLiveData();
        this.awardsNewsLiveData = repository.getAwardsNewsLiveData();
        this.artsNewsLiveData = repository.getArtsNewsLiveData();
        this.personNewsLiveData = repository.getPersonNewsLiveData();
        this.userMutableLiveData = repository.getUserMutableLiveData();


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

    public MutableLiveData<UserData> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void getLocationNews(String location){
        repository.getLocalNews(location);
    }


    public MutableLiveData<List<NewsData>> getTopNewsLiveData() {
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

    public MutableLiveData<List<NewsData>> getPoliticsNewsLiveData() {
        return politicsNewsLiveData;
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

    public MutableLiveData<List<NewsData>> getEconomyNewsLiveData() {
        return economyNewsLiveData;
    }



    // for merging the Guest account to email account.
    public void mergeThisUser(Activity activity, String email, String password){
        progressLiveData.postValue(true);
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            //updateUI here
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            /*Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                            //updateUI(null);
                        }

                        progressLiveData.postValue(false);
                        // ...
                    }
                });

    }

    public void sendEmailVerificationLink(){

        FirebaseAuth
                .getInstance()
                .getCurrentUser()
                .sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Email sent Succesfully..");
                        }else {
                            Log.d(TAG, "onComplete: Email not send try again.");
                        }
                    }
                });
    }


    public void saveUserDataOnDatabase(String uid, String email){
        repository.saveUserDataInFirestore(uid, email);
    }

    public void savePhotoAndUpdateDatabase(Uri imageUri){
        repository.savePhotoAndUpdateDatabase(imageUri);
    }
    public void updateUI(){
        repository.updateUI();
    }
    public void resetPassword(String email){
        repository.resetPassword(email);
    }
    public void deleteUserDatabase(String uid){
        repository.deleteUserDatabase(uid);
    }

}
