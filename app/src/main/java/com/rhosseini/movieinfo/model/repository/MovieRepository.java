package com.rhosseini.movieinfo.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.MovieDatabase;
import com.rhosseini.movieinfo.model.database.dao.MovieDao;
import com.rhosseini.movieinfo.model.database.entity.Movie;

import java.util.List;

public class MovieRepository {

    private static MovieRepository movieRepository;
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;


    public static MovieRepository getInstance(Application app) {
        if (movieRepository == null) {
            movieRepository = new MovieRepository(app);
        }
        return movieRepository;
    }


    public MovieRepository(Application app) {
        movieDao = MovieDatabase.getINSTANCE(app).movieDao();

        allMovies = movieDao.getAllMovies();
    }

    /* get all Movies */
    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
}
