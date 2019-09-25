package com.rhosseini.movieinfo.model.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_history")
public class SearchHistory {
    @PrimaryKey
    @NonNull
    private String title;
    private Long time;

    public SearchHistory(@NonNull String title) {
        this.title = title;
    }

    @Ignore
    public SearchHistory(@NonNull String title, Long time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
