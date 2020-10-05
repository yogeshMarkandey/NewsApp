package com.example.newsapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "news_table")
public class NewsEntity {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String newsId;
    private String location;
    private String newsHeading;
    private String newsBody;
    private String imagesUrl;
    private String date;
    private String category;
    private Date timestamp;

    public NewsEntity() {
        //Empty constructor
    }

    public NewsEntity( String newsId, String location, String newsHeading,
                      String newsBody, String imagesUrl, String date, String category) {
        this.newsId = newsId;
        this.location = location;
        this.newsHeading = newsHeading;
        this.newsBody = newsBody;
        this.imagesUrl = imagesUrl;
        this.date = date;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setters

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
