package com.rhosseini.movieinfo.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.repository.MovieRepository;
import com.rhosseini.movieinfo.model.webServise.responseModel.ResponseWrapper;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);

        repository = MovieRepository.getInstance(application);
    }

    public LiveData<ResponseWrapper<List<Movie>>> getMoviesByTitle(String searchText, Integer page) {
        return repository.getMoviesByTitle(searchText, page);
    }
}
