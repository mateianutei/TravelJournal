package com.example.anuteistravelingjournal.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "memories")

public class Memory implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int  id;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="date_time")
    private String dateTime;

    @ColumnInfo(name="subtitle")
    private String subtitle;

    @ColumnInfo(name="text")
    private String text;
    @ColumnInfo(name="image_path")
    private String imagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        return "Memory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
