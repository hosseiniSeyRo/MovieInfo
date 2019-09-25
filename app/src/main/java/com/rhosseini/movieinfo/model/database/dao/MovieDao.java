package com.rhosseini.movieinfo.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rhosseini.movieinfo.model.database.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movie WHERE title LIKE  '%' || :title || '%' LIMIT :size")
    LiveData<List<Movie>> getMoviesByTitle(String title, Integer size);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);
}
