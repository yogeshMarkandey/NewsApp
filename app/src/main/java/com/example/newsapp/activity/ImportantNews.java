package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        //for handling the swipe to delete feature..
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getAllNotes().observe(ImportantNews.this, new Observer<List<NewsEntity>>() {
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
    public void onCardTaped(int position) {
        Toast.makeText(this, "Tapped on card", Toast.LENGTH_SHORT).show();
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == ItemTouchHelper.RIGHT){
                int position = viewHolder.getAdapterPosition();
                NewsEntity entity = adapter.getItemAtPosition(position);
                viewModel.delete(entity);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    };
}