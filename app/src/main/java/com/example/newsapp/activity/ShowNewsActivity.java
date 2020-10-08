package com.example.newsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adpter.ShowNewsRVAdapter;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.viewmodel.MainViewModel;

import java.util.List;

public class ShowNewsActivity extends AppCompatActivity {

    private static final String TAG = "ShowNewsActivity";
    private RecyclerView recyclerView;
    private MainViewModel viewModel;
    private ShowNewsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        recyclerView = findViewById(R.id.rv_show_news_activity);

        LinearLayoutManager manager =
                new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(manager);

        SnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        Intent intent = getIntent();
        List<NewsData>  data = intent.getParcelableArrayListExtra("List");
        int position = intent.getIntExtra("Position", 0);

        recyclerView.setHasFixedSize(true);

        adapter = new ShowNewsRVAdapter(getApplicationContext());

        recyclerView.setAdapter(adapter);

        if(data.size() != 0){
            adapter.submitList(data);
            recyclerView.scrollToPosition(position);
        }

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        /*viewModel.getGovSchemeNewsLiveData().observe(this, new Observer<List<NewsData>>() {

            @Override
            public void onChanged(List<NewsData> newsData) {
                adapter.submitList(newsData);
            }
        });*/
        next();

    }

    public void next(){
        //kiuhiu

        Toast.makeText(this, "new", Toast.LENGTH_SHORT).show();
    }
}