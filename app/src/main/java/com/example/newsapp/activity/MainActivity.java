package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;
import com.example.newsapp.adpter.NewsRVAAdapter;
import com.example.newsapp.adpter.NewsRVListViewAdapter;
import com.example.newsapp.adpter.TopNewsRVListViewAdapter;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.data.UserData;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.util.NPALinerLayoutManager;
import com.example.newsapp.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NewsRVListViewAdapter.OnCardTapListener,
        NavigationView.OnNavigationItemSelectedListener,
        TopNewsRVListViewAdapter.OnCardTapListener {
    private static final String TAG = "MainActivity";


    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSports;
    private RecyclerView recyclerViewGovScheme;
    private RecyclerView recyclerViewCovid;
    private RecyclerView recyclerViewPolitical;
    private RecyclerView recyclerViewEconomy;
    private RecyclerView recyclerViewAwards;
    private RecyclerView recyclerViewPerson;
    private RecyclerView recyclerViewArts;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private TextView durg_tv, all_tv, rajnandgaon_tv, raipur_tv, korba_tv, dantewara_tv, bhilai_tv, bilaspur_tv, dhamtari_tv, jagdalpur_tv, raigarh_tv;


    int i =0;

    // navigation header
    private TextView username, userEmail;
    private ImageView profile_image, signin_image_view;

    // Dialog Box - Merge Account
    private EditText firstName, lastName, email, password;
    private Button register;
    // Verification
    private TextView tv_verify;
    private Button button_verify;



    // LiveData
    private MutableLiveData<List<NewsData>> topNewsLiveData;


    private NewsRVListViewAdapter adapter, adapterSports, adapterGov, adapterCovid, adapterEconomy;
    private NewsRVListViewAdapter adapterPolitcs, adapterAward, adapterArts, adapterPerson;
    private TopNewsRVListViewAdapter adapterTopNews;
    private MainViewModel viewModel;

    private TextView currentTv, previousTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_bar_text_layout);



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
        recyclerViewPolitical = findViewById(R.id.rv_news_politics);
        recyclerViewEconomy = findViewById(R.id.rv_news_economy);
        recyclerViewAwards = findViewById(R.id.rv_news_awards);
        recyclerViewPerson = findViewById(R.id.rv_news_persons_in_news);
        recyclerViewArts = findViewById(R.id.rv_news_art_and_culture);
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


        // Nav header widgets
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.text_view_email_nav_header);
        username = navigationView.getHeaderView(0).findViewById(R.id.text_view_name_nav_header);
        profile_image = navigationView.getHeaderView(0).findViewById(R.id.image_view_nav_header);
        signin_image_view = navigationView.getHeaderView(0).findViewById(R.id.image_view_sign_in);
        signin_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMergeDialogBox();
            }
        });

        //Verification
        tv_verify = findViewById(R.id.text_view_verify);
        button_verify = findViewById(R.id.button_verify);


        previousTv = all_tv;
        currentTv = all_tv;
        previousTv.setBackground(getDrawable(R.drawable.back_selected));
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getLocationNews("all");
        setVariableObservers();
        initRecyclerViews();
        settingOnClickListeners();

        //TODO: Think a place for Email Verification
        button_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.sendEmailVerificationLink();
                Toast.makeText(MainActivity.this, "Verification Link send to Email.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkDataConnection();

        signin_image_view.setVisibility(View.GONE);
        //Todo: add all the user data to navigation header. (Email, name ,Profile Image)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail.setText(currentUser.getEmail().isEmpty() ? "Email not found" : currentUser.getEmail());

        if(currentUser.isAnonymous()){
            signin_image_view.setVisibility(View.VISIBLE);
        }

        if(!currentUser.isAnonymous()){
            Log.d(TAG, "onStart: isAnonymous :" + currentUser.isAnonymous());
            if(!currentUser.isEmailVerified()){
                Log.d(TAG, "onStart: isEmailVerified : " +currentUser.isEmailVerified() );
                tv_verify.setVisibility(View.VISIBLE);
                button_verify.setVisibility(View.VISIBLE);
            }else {
                button_verify.setVisibility(View.GONE);
                tv_verify.setVisibility(View.GONE);
            }
        }

        if(i <1){
            i++;
            viewModel.updateUI();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(!currentUser.isAnonymous()){
            Log.d(TAG, "onResume: isAnonymous : " + currentUser.isAnonymous());
            if(!currentUser.isEmailVerified()){
                Log.d(TAG, "onResume: isEmailVerified : " + currentUser.isEmailVerified());
                tv_verify.setVisibility(View.VISIBLE);
                button_verify.setVisibility(View.VISIBLE);
            }else {
                button_verify.setVisibility(View.GONE);
                tv_verify.setVisibility(View.GONE);
            }
        }
    }

    // this method inflates the dialog box
    private void showMergeDialogBox(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog;
        View v = getLayoutInflater().inflate(R.layout.layout_merge_account, null);
        email = v.findViewById(R.id.editTextTextEmailAddress_dialog_box);
        password = v.findViewById(R.id.editTextTextPassword_dialog_box);
        firstName = v.findViewById(R.id.tv_first_name_dialog_box);
        lastName = v.findViewById(R.id.tv_last_name_dialog_box);
        register = v.findViewById(R.id.button_register_dialog_box);

        builder.setView(v) ;
        dialog = builder.create();
        dialog.show();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("")
                        || password.getText().toString().trim().equals("")
                        || firstName.getText().toString().trim().equals("")
                        || lastName.getText().toString().trim().equals("")
                ){
                    Toast.makeText(MainActivity.this, "Fill all the Details.", Toast.LENGTH_SHORT).show();
                    return;
                }

                viewModel.mergeThisUser(MainActivity.this,
                        email.getText().toString(),
                        password.getText().toString());

                //Todo: update the Navigation drawer with new Users Details.
                dialog.cancel();
            }
        });


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
                Toast.makeText(MainActivity.this, "All", Toast.LENGTH_SHORT).show();
                selectTheButton(previousTv, all_tv);
                viewModel.getLocationNews("all");
            }
        });
    }

    private void setVariableObservers() {
        Log.d(TAG, "setVariableObservers: Setting up Observers.");
        /*topNewsLiveData = viewModel.getTopNewsLiveData();
        topNewsLiveData.observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                Log.d(TAG, "onChanged: Yellow .. called");
                adapter.submitList(newsData);
                adapter.notifyDataSetChanged();
            }
        });*/

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

        viewModel.getEconomyNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterEconomy.submitList(newsData);
                adapterEconomy.notifyDataSetChanged();

            }
        });
        viewModel.getAwardsNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterAward.submitList(newsData);
                adapterAward.notifyDataSetChanged();

            }
        });
        viewModel.getPersonNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterPerson.submitList(newsData);
                adapterPerson.notifyDataSetChanged();

            }
        });
        viewModel.getArtsNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterArts.submitList(newsData);
                adapterArts.notifyDataSetChanged();

            }
        });
        viewModel.getPoliticsNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> newsData) {
                adapterPolitcs.submitList(newsData);
                adapterPolitcs.notifyDataSetChanged();

            }
        });

        viewModel.getUserMutableLiveData().observe(this, new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                userEmail.setText(userData.getEmailId());
                username.setText(userData.getName());

                RequestOptions options = new RequestOptions()
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher);

                Glide.with(MainActivity.this)
                        .load(userData.getAvatar())
                        .apply(options)
                        .into(profile_image);
            }
        });

        viewModel.getTopNewsLiveData().observe(this, new Observer<List<NewsData>>() {
            @Override
            public void onChanged(List<NewsData> list) {
                adapterTopNews.submitList(list);
                adapterTopNews.notifyDataSetChanged();
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
        LinearLayoutManager manager4 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager5 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager6 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager7 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager8 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager9 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager10 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager manager11 = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager managerTopNews = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);
        adapterTopNews = new TopNewsRVListViewAdapter(getApplicationContext(),this);


        recyclerView.setHasFixedSize(true);


        //adapter = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterSports = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterCovid = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterEconomy = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterPerson = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterArts = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterAward = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterGov = new NewsRVListViewAdapter(getApplicationContext(), this);
        adapterPolitcs = new NewsRVListViewAdapter(getApplicationContext(), this);



        recyclerViewSports.setAdapter(adapterSports);
        recyclerViewGovScheme.setAdapter(adapterGov);
        recyclerViewCovid.setAdapter(adapterCovid);
        recyclerViewEconomy.setAdapter(adapterEconomy);
        recyclerViewPerson.setAdapter(adapterPerson);
        recyclerViewArts.setAdapter(adapterArts);
        recyclerViewAwards.setAdapter(adapterAward);
        recyclerViewCovid.setAdapter(adapterCovid);
        recyclerViewPolitical.setAdapter(adapterPolitcs);

        recyclerView.setLayoutManager(managerTopNews);
        recyclerViewSports.setLayoutManager(manager1);
        recyclerViewGovScheme.setLayoutManager(manager2);
        recyclerViewCovid.setLayoutManager(manager3);
        recyclerViewEconomy.setLayoutManager(manager4);
        recyclerViewPerson.setLayoutManager(manager5);
        recyclerViewArts.setLayoutManager(manager6);
        recyclerViewAwards.setLayoutManager(manager7);
        recyclerViewPolitical.setLayoutManager(manager8);

        recyclerView.setAdapter(adapterTopNews);
        recyclerViewSports.setHasFixedSize(true);
        recyclerViewGovScheme.setHasFixedSize(true);
        recyclerViewCovid.setHasFixedSize(true);
        recyclerViewEconomy.setHasFixedSize(true);
        recyclerViewPerson.setHasFixedSize(true);
        recyclerViewArts.setHasFixedSize(true);
        recyclerViewAwards.setHasFixedSize(true);
        recyclerViewPolitical.setHasFixedSize(true);
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
                i = 0;
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(MainActivity.this, AccountSettings.class);
                startActivity(intent1);

                break;
            case R.id.menu_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_log_out:
                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
                signOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



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
    public void onCardTaped(List<NewsData> data, int position) {
        ArrayList<NewsData> list = new ArrayList<>(data);
        Toast.makeText(this, "Opening Card " + data.size(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( this, ShowNewsActivity.class);
        intent.putParcelableArrayListExtra("List", list);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

    private void checkDataConnection(){
        if (!isDataAvailable()) { //check if data is enabled or not
            new android.app.AlertDialog.Builder(this).setTitle("Unable to connect")
                    .setMessage("Enable data?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // if user clicks ok then it will open network settings
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        } else {
            Toast.makeText(this, "DATA IS ON", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDataAvailable() {
        // returns true or false based on whether data is enabled or not
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    //For Top news Rv
    @Override
    public void onCardTaped(int position, List<NewsData> data) {
        ArrayList<NewsData> list = new ArrayList<>(data);
        Toast.makeText(this, "Opening Card " + data.size(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( this, ShowNewsActivity.class);
        intent.putParcelableArrayListExtra("List", list);
        intent.putExtra("Position", position);
        startActivity(intent);
    }

}