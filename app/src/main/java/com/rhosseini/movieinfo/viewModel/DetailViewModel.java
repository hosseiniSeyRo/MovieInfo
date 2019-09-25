package com.rhosseini.movieinfo.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.repository.MovieRepository;


public class DetailViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public DetailViewModel(@NonNull Application application) {
        super(application);

        repository = MovieRepository.getInstance(application);
    }

    public LiveData<Movie> getMovieById(String imdbId) {
        return repository.getMovieById(imdbId);
    }
}
