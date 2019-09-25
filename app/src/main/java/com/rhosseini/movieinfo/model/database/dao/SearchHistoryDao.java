package com.rhosseini.movieinfo.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rhosseini.movieinfo.model.database.entity.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchHistory searchHistories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SearchHistory> searchHistories);

    @Query("SELECT * FROM search_history ORDER BY title")
    LiveData<List<SearchHistory>> getAllSearchHistories();

}
