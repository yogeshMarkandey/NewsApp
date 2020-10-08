package com.example.newsapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp.R;
import com.example.newsapp.data.NewsData;

import java.util.List;


public class TopNewsRVListViewAdapter
        extends ListAdapter<NewsData, TopNewsRVListViewAdapter.NewsViewHolder> {

    private Context context;
    private OnCardTapListener listener;

    public static final DiffUtil.ItemCallback<NewsData> DIFF_CALLBACK = new DiffUtil.ItemCallback<NewsData>() {
        @Override
        public boolean areItemsTheSame(@NonNull NewsData oldItem, @NonNull NewsData newItem) {
            return oldItem.getNewsId() == newItem.getNewsId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NewsData oldItem, @NonNull NewsData newItem) {
            return oldItem.getNewsHeading().equals(newItem.getNewsHeading()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    public TopNewsRVListViewAdapter(Context context, OnCardTapListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_top_highlights, parent, false);
        return new NewsViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsData data = getItem(position);
        holder.bodyTv.setText(data.getNewsBody());
        holder.headingTv.setText(data.getNewsHeading());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(context)
                .load(data.getImagesUrl())
                .apply(options)
                .into(holder.imageView);

    }

    public NewsData getItemAtPosition(int position){
        return getItem(position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView headingTv, bodyTv;
        ImageView starImageView, imageView;
        OnCardTapListener listener;
        public NewsViewHolder(@NonNull View itemView, OnCardTapListener listener) {
            super(itemView);
            this.listener = listener;
            headingTv = itemView.findViewById(R.id.textView_heading_top_news );
            bodyTv = itemView.findViewById(R.id.body_item_top_news );
            imageView = itemView.findViewById(R.id.imageView_top_news );


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageView_star:
                    //listener.onStarClicked(getItemAtPosition(getAdapterPosition()));
                    break;
                default:
                    listener.onCardTaped(getAdapterPosition(), getCurrentList());
                    break;
            }
        }
    }

    public interface OnCardTapListener{
        void onCardTaped(int position, List<NewsData> list);

    }
}
