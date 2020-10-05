package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adpter.NewsRVAAdapter;
import com.example.newsapp.adpter.NewsRVListViewAdapter;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.util.NPALinerLayoutManager;
import com.example.newsapp.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NewsRVListViewAdapter.OnCardTapListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";


    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSports;
    private RecyclerView recyclerViewGovScheme;
    private RecyclerView recyclerViewCovid;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private TextView durg_tv, all_tv, rajnandgaon_tv, raipur_tv, korba_tv, dantewara_tv, bhilai_tv, bilaspur_tv, dhamtari_tv, jagdalpur_tv, raigarh_tv;



    // LiveData
    private MutableLiveData<List<NewsData>> topNewsLiveData;


    private NewsRVListViewAdapter adapter, adapterSports, adapterGov, adapterCovid, adapterEconomy;


    private MainViewModel viewModel;

    private TextView currentTv, previousTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        progressBar = findViewById(R.id.progress_bar_main);
        recyclerView = findViewById(R.id.rv_news);
        recyclerViewSports = findViewById(R.id.rv_news_sports);
        recyclerViewGovScheme = findViewById(R.id.rv_news_gov_schemes);
        recyclerViewCovid = findViewById(R.id.rv_news_covid);
        progressBar.setVisibility(View.VISIBLE);

        durg_tv = findViewById(R.id.btn_durg);
        bhilai_tv = findViewById(R.id.tv_bhilai);
        dantewara_tv = findViewById(R.id.tv_dantewara);
        raigarh_tv = findViewById(R.id.tv_raigarh);
        raipur_tv = findViewById(R.id.tv_raipur);
        jagdalpur_tv = findViewById(R.id._jagdalpur);
        bilaspur_tv = findViewById(R.id.bilaspur);
        korba_tv = findViewById(R.id.tv_korba);
        rajnandgaon_tv = findViewById(R.id.tv_rajnandgaon);
        dhamtari_tv = findViewById(R.id.tv_dhamtari);
        all_tv = findViewById(R.id.btn_all);

        previousTv = all_tv;
        currentTv = all_tv;
        previousTv.setBackground(getDrawable(R.drawable.back_selected));
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getLocationNews("all");
        setVariableObservers();
        initRecyclerViews();
        settingOnClickListeners();

    }

    private void selectTheButton(TextView prev, TextView curr){

        if(curr == prev){
            return;
        }

        curr.setBackground(getDrawable(R.drawable.back_selected));

        prev.setBackground(getDrawable(R.drawable.button_all_background));
        previousTv = curr;
    }

    private void settingOnClickListeners() {
        durg_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "durg", Toast.LENGTH_SHORT).show();
                viewModel.getLocationNews("durg");
                selectTheButton(previousTv, durg_tv);
            }
        });


        bhilai_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "bhilai", Toast.LENGTH_SHORT).show();
                viewModel.getLocationNews("bhilai");
                selectTheButton(previousTv, bhilai_tv);

            }
        });
        dantewara_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "dantewara", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, dantewara_tv);

                viewModel.getLocationNews("dantewara");
            }
        });
        raigarh_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "raigarh", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, raigarh_tv);

                viewModel.getLocationNews("raigarh");
            }
        });
        raipur_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "raipur", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, raipur_tv);

                viewModel.getLocationNews("raipur");
            }
        });
        jagdalpur_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "jagdalpur", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, jagdalpur_tv);

                viewModel.getLocationNews("jagdalpur");
            }
        });
        bilaspur_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "bilaspur", Toast.LENGTH_SHORT).show();

                selectTheButton(previousTv, bilaspur_tv);

                viewModel.getLocationNews("bilaspur");
            }
        });
        korba_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "korba", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, korba_tv);

                viewModel.getLocationNews("korba");
            }
        });
        rajnandgaon_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "rajnandgaon", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, rajnandgaon_tv);

                viewModel.getLocationNews("rajnandgaon");
            }
        });
        dhamtari_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "dhamtari", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, dhamtari_tv);

                viewModel.getLocationNews("dhamtari");
            }
        });
        all_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // load all here
                /*Todo: make a method in View model to load all data.*/
                Toast.makeText(MainActivity.this, "All", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, all_tv);
                viewModel.getLocationNews("all");
            }
        });
    }

    private void setVariableObservers() {
        Log.d(TAG, "setVariableObservers: Setting up Observers.");
        topNewsLiveData = viewModel.getTopNewsLiveData();
        topNewsLiveData.observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                Log.d(TAG, "onChanged: Yellow .. called");
                adapter.submitList(newsData);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getProgressLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getCovidNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {

                adapterCovid.submitList(newsData);
                adapterCovid.notifyDataSetChanged();

            }
        });

        viewModel.getSportsNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterSports.submitList(newsData);
                adapterSports.notifyDataSetChanged();

            }
        });

        viewModel.getGovSchemeNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterGov.submitList(newsData);
                adapterGov.notifyDataSetChanged();

            }
        });
    }

    private void initRecyclerViews() {
        Log.d(TAG, "initRecyclerViews: Setting RecyclerView...");
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager1 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager3 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setHasFixedSize(true);


        adapter = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterSports = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterCovid = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterEconomy = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterGov = new NewsRVListViewAdapter(getApplicationContext(), this);


        recyclerViewSports.setAdapter(adapterSports);
        recyclerViewGovScheme.setAdapter(adapterGov);
        recyclerViewCovid.setAdapter(adapterCovid);

        recyclerView.setLayoutManager(manager);
        recyclerViewSports.setLayoutManager(manager1);
        recyclerViewGovScheme.setLayoutManager(manager2);
        recyclerViewCovid.setLayoutManager(manager3);

        recyclerView.setAdapter(adapter);
        recyclerViewSports.setHasFixedSize(true);
        recyclerViewGovScheme.setHasFixedSize(true);
        recyclerViewCovid.setHasFixedSize(true);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_important:
                Toast.makeText(this, "Important", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ImportantNews.class);
                startActivity(intent);
                break;
            case R.id.menu_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_favorites:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




   /* public void uploadNewsData(){

        String id = "news_id_" + System.currentTimeMillis();
        String heading = "This is the Second News in the History";
        String body = "This is the Second News in the History showed by this app. \nHello World this app show you news from Chhattisgarh. \nDo Like and Share this app. ";
        String url = "https://ichef.bbci.co.uk/news/385/cpsprodpb/121A2/production/_111764147_breaking_news_bigger-nc.png";
        NewsData data = new NewsData(id, heading, body,url);
        data.setDate("Oct 3, 2020");
        data.setLocation("Durg");

        *//*firebaseFirestore.collection("News Posts")
                .add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Data uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                }); *//*


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


    }*/

    @Override
    public void onStarClicked(NewsData data) {
        NewsEntity newsEntity =
                new NewsEntity(data.getNewsId(), data.getLocation(), data.getNewsHeading(),
                        data.getNewsBody(), data.getImagesUrl(), data.getDate(), data.getCategory());
        Log.d(TAG, "onStarTaped:Red: calling Insert");
        viewModel.insert(newsEntity);
        Toast.makeText(this, "Adding", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCardTaped(NewsData data) {
        Toast.makeText(this, "Opening Card " + data.getNewsId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ShowNewsActivity.class);
        startActivity(intent);
    }
}