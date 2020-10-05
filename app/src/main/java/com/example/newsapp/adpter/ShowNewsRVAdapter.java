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

public class ShowNewsRVAdapter extends ListAdapter<NewsData, ShowNewsRVAdapter.ShowNewsViewHolder> {

    private Context context;

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


    public ShowNewsRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ShowNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_show, parent, false);
        return new ShowNewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowNewsViewHolder holder, int position) {
        NewsData data = getItem(position);
        holder.body.setText(data.getNewsBody());
        holder.heading.setText(data.getNewsHeading());

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

    public class ShowNewsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView heading , body;

        public ShowNewsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView2);
            heading = itemView.findViewById(R.id.text_view_heading);
            body = itemView.findViewById(R.id.text_view_news_body);


        }
    }


}
