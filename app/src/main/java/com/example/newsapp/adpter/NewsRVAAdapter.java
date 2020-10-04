package com.example.newsapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;
import com.example.newsapp.data.NewsData;

import java.util.ArrayList;
import java.util.List;

public class NewsRVAAdapter extends RecyclerView.Adapter<NewsRVAAdapter.NewsViewHolder> {

    private List<NewsData> list = new ArrayList<>();
    private Context context;
    private OnCardTapListener listener;
    public NewsRVAAdapter(Context context, List<NewsData> list, OnCardTapListener listener) {
        this.list = list;
        this.context = context;
        this.listener  = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_main, parent, false);
        NewsViewHolder holder = new NewsViewHolder(v, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsData newsData = list.get(position);

        holder.newsBody.setText(newsData.getNewsBody());
        holder.newsHeading.setText(newsData.getNewsHeading());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(context)
                .load(newsData.getImagesUrl())
                .apply(options)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView newsHeading;
        TextView newsBody;
        ImageView imageView;
        ImageView starImage;
        OnCardTapListener listener;
        public NewsViewHolder(@NonNull View itemView, OnCardTapListener listener) {
            super(itemView);
            this.listener = listener;
            newsBody = itemView.findViewById(R.id.textView_news);
            newsHeading = itemView.findViewById(R.id.textView_heading);
            imageView = itemView.findViewById(R.id.imageView);
            starImage = itemView.findViewById(R.id.imageView_star);

            itemView.setOnClickListener(this);
            starImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageView_star:
                    listener.onStarTaped(getAdapterPosition());
                    break;
                default:
                    listener.onCardTap(getAdapterPosition());
                    break;
            }
        }
    }

    public interface OnCardTapListener{

        void onStarTaped(int position);
        void onCardTap(int position);
    }
}
