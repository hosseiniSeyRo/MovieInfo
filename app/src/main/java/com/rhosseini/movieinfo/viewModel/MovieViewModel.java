package com.rhosseini.movieinfo.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.database.entity.SearchHistory;
import com.rhosseini.movieinfo.model.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> allMovies;
    private LiveData<List<SearchHistory>> allSearchHistories;

    public MovieViewModel(@NonNull Application application) {
        super(application);

        repository = MovieRepository.getInstance(application);
    }

    public LiveData<List<SearchHistory>> getAllSearchHistories() {
        allSearchHistories = repository.getAllSearchHistories();

        return allSearchHistories;
    }

    public LiveData<List<Movie>> getAllMovies(String searchText, Integer page) {
        allMovies = repository.getAllMovies(searchText, page);

        return allMovies;
    }
}
