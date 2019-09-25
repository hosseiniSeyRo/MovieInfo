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

    public MovieViewModel(@NonNull Application application) {
        super(application);

        repository = MovieRepository.getInstance(application);
    }

    public LiveData<List<SearchHistory>> getAllSearchHistories() {
        return repository.getAllSearchHistories();
    }

    public void insertSearchHistory(SearchHistory searchHistory) {
        repository.insertSearchHistory(searchHistory);
    }

    public LiveData<List<Movie>> getMoviesByTitle(String searchText, Integer page) {
        return repository.getMoviesByTitle(searchText, page);
    }
}
