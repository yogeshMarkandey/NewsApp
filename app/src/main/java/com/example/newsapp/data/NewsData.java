package com.example.newsapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NewsData implements Parcelable {

    private String newsId;
    private String newsHeading;
    private String newsBody;
    private String imagesUrl;
    private String date;
    private String location;
    private String category;
    //Category
    private @ServerTimestamp Date timestamp;

    public NewsData(){
        // Empty Constructor
    }

    public NewsData(String newsId, String newsHeading, String newsBody,String imagesUrl){
        this.newsBody = newsBody;
        this.imagesUrl = imagesUrl;
        this.newsId = newsId;
        this.newsHeading = newsHeading;
    }


    protected NewsData(Parcel in) {
        newsId = in.readString();
        newsHeading = in.readString();
        newsBody = in.readString();
        imagesUrl = in.readString();
        date = in.readString();
        location = in.readString();
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    // Getters and Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "newsId='" + newsId + '\'' +
                ", newsHeading='" + newsHeading + '\'' +
                ", newsBody='" + newsBody + '\'' +
                ", imagesUrl='" + imagesUrl + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(newsId);
        parcel.writeString(newsHeading);
        parcel.writeString(newsBody);
        parcel.writeString(imagesUrl);
        parcel.writeString(date);
        parcel.writeString(location);
    }
}
