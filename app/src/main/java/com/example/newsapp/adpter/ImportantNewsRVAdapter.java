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
import com.example.newsapp.database.NewsEntity;


public class ImportantNewsRVAdapter
        extends ListAdapter<NewsEntity, ImportantNewsRVAdapter.NewsViewHolder> {

    private Context context;
    private OnCardTapListener listener;

    public static final DiffUtil.ItemCallback<NewsEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<NewsEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull NewsEntity oldItem, @NonNull NewsEntity newItem) {
            return oldItem.getNewsId() == newItem.getNewsId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NewsEntity oldItem, @NonNull NewsEntity newItem) {
            return oldItem.getNewsHeading().equals(newItem.getNewsHeading()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    public ImportantNewsRVAdapter(Context context, OnCardTapListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_rv_main, parent, false);
        return new NewsViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsEntity data = getItem(position);
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

    public NewsEntity getItemAtPosition(int position){
        return getItem(position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView headingTv, bodyTv;
        ImageView starImageView, imageView;
        OnCardTapListener listener;
        public NewsViewHolder(@NonNull View itemView, OnCardTapListener listener) {
            super(itemView);
            this.listener = listener;
            headingTv = itemView.findViewById(R.id.textView_heading );
            bodyTv = itemView.findViewById(R.id.textView_news );
            imageView = itemView.findViewById(R.id.imageView );
            starImageView = itemView.findViewById(R.id.imageView_star );

            itemView.setOnClickListener(this);
            starImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageView_star:
                    listener.onStarClicked(getItemAtPosition(getAdapterPosition()));
                    break;
                default:
                    listener.onCardTaped(getItemAtPosition(getAdapterPosition()));
                    break;
            }
        }
    }

    public interface OnCardTapListener{
        void onStarClicked(NewsEntity data);
        void onCardTaped(NewsEntity data);
    }
}
