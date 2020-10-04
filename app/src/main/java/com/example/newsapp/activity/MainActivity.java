package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adpter.NewsRVAAdapter;
import com.example.newsapp.adpter.NewsRVListViewAdapter;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsRVListViewAdapter.OnCardTapListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSports;
    private RecyclerView recyclerViewGovScheme;
    private RecyclerView recyclerViewCovid;
    private ProgressBar progressBar;

    // LiveData
    private MutableLiveData<List<NewsData>> topNewsLiveData;


    private FirebaseFirestore firebaseFirestore;
    private NewsRVListViewAdapter adapter;
    private List<NewsData> listNews = new ArrayList<>();

    private MainViewModel viewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar_main);
        recyclerView = findViewById(R.id.rv_news);
        recyclerViewSports = findViewById(R.id.rv_news_sports);
        recyclerViewGovScheme = findViewById(R.id.rv_news_gov_schemes);
        recyclerViewCovid = findViewById(R.id.rv_news_covid);
       // progressBar.setVisibility(View.VISIBLE);


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Log.d(TAG, "onCreate: Yellow.");
        topNewsLiveData = viewModel.getTopNewsLiveData();
        topNewsLiveData.observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                Log.d(TAG, "onChanged: Yellow .. called");
                adapter.submitList(newsData);
            }
        });

        /*TODO: To make sperate list of each recyclerview*/

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false );
        LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false );
        LinearLayoutManager manager2 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false );
        LinearLayoutManager manager3 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false );
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        adapter = new NewsRVListViewAdapter(getApplicationContext(), this);

        recyclerView.setAdapter(adapter);

        recyclerViewSports.setAdapter(adapter);
        recyclerViewGovScheme.setAdapter(adapter);
        recyclerViewCovid.setAdapter(adapter);

        recyclerViewSports.setLayoutManager(manager1);
        recyclerViewGovScheme.setLayoutManager(manager2);
        recyclerViewCovid.setLayoutManager(manager3);

        recyclerViewSports.setHasFixedSize(true);
        recyclerViewGovScheme.setHasFixedSize(true);
        recyclerViewCovid.setHasFixedSize(true);



        firebaseFirestore = FirebaseFirestore.getInstance();
        /*uploadNewsData();*/




    }
/*Todo : MVVM- Replace this method with live data.*/
    public void getNewsData(){

        firebaseFirestore.collection("News Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Error Loading Data.", Toast.LENGTH_SHORT).show();
                        }

                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                        for (DocumentSnapshot ds: documentSnapshots) {
                            NewsData data = new NewsData();
                            data = ds.toObject(NewsData.class);

                            if(data != null){
                                listNews.add(data);
                            }
                        }
                        Log.d(TAG, "onComplete: All news added.");
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    public void uploadNewsData(){

        String id = "news_id_" + System.currentTimeMillis();
        String heading = "This is the Second News in the History";
        String body = "This is the Second News in the History showed by this app. \nHello World this app show you news from Chhattisgarh. \nDo Like and Share this app. ";
        String url = "https://ichef.bbci.co.uk/news/385/cpsprodpb/121A2/production/_111764147_breaking_news_bigger-nc.png";
        NewsData data = new NewsData(id, heading, body,url);
        data.setDate("Oct 3, 2020");
        data.setLocation("Durg");

        /*firebaseFirestore.collection("News Posts")
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Data uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                }); */


        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference docRef  = firebaseFirestore.collection("News Posts").document();
        batch.set(docRef,new NewsData(id.concat("-1"), heading.concat("-1"), body, url));

        DocumentReference docRef2  = firebaseFirestore.collection("News Posts").document();
        batch.set(docRef2,new NewsData(id.concat("-2"), heading.concat("-2"), body, url));

        DocumentReference docRef3  = firebaseFirestore.collection("News Posts").document();
        batch.set(docRef3,new NewsData(id.concat("-3"), heading.concat("-3"), body, url));

        DocumentReference docRef4  = firebaseFirestore.collection("News Posts").document();
        batch.set(docRef4,new NewsData(id.concat("-4"), heading.concat("-4"), body, url));

        DocumentReference docRef5  = firebaseFirestore.collection("News Posts").document();
        batch.set(docRef5,new NewsData(id.concat("-5"), heading.concat("-5"), body, url));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Upload Error!!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStarClicked(NewsData data) {
        NewsEntity newsEntity =
                new NewsEntity(data.getNewsId(),data.getLocation(),data.getNewsHeading(),
                        data.getNewsBody(), data.getImagesUrl(), data.getDate(), data.getCategory());
        Log.d(TAG, "onStarTaped:Red: calling Insert");
        viewModel.insert(newsEntity);

    }

    @Override
    public void onCardTaped(NewsData data) {
        Toast.makeText(this, "Opening Card " + data.getNewsId(), Toast.LENGTH_SHORT).show();
    }
}