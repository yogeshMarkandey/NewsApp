package com.example.newsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adpter.ImportantNewsRVAdapter;
import com.example.newsapp.adpter.NewsRVListViewAdapter;
import com.example.newsapp.data.NewsData;
import com.example.newsapp.database.NewsEntity;
import com.example.newsapp.viewmodel.MainViewModel;

import java.util.List;

public class ImportantNews extends AppCompatActivity implements ImportantNewsRVAdapter.OnCardTapListener {


    private RecyclerView recyclerView;

    private MainViewModel viewModel;
    private ImportantNewsRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_news);

        recyclerView = findViewById(R.id.re_important_news);
        adapter = new ImportantNewsRVAdapter(getApplicationContext(),this );
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getAllNotes().observe(this, new Observer<List<NewsEntity>>() {
            @Override
            public void onChanged(List<NewsEntity> newsEntities) {
                adapter.submitList(newsEntities);
            }
        });
    }


    @Override
    public void onStarClicked(NewsEntity data) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardTaped(NewsEntity data) {
        Toast.makeText(this, "Tapped on card", Toast.LENGTH_SHORT).show();
    }
}