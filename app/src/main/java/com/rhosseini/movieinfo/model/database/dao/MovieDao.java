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

    @Query("SELECT * FROM movie WHERE title LIKE  '%' || :title || '%'")
    LiveData<List<Movie>> getMoviesByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Movie> movies);

    @Query("SELECT * FROM movie WHERE imdbId = :imdbId LIMIT 1")
    LiveData<Movie> getMovieById(String imdbId);
}
