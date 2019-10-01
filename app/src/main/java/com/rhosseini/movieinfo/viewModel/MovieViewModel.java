package com.rhosseini.movieinfo.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.repository.MovieRepository;
import com.rhosseini.movieinfo.model.webServise.responseModel.Resource;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;
    public LiveData<Resource<List<Movie>>> movieList;

    public MovieViewModel(@NonNull Application application) {
        super(application);

        repository = MovieRepository.getInstance(application);
        movieList = repository.responseWrapper;
    }

    public void getMoviesByTitle(String searchText, Integer page) {
        repository.getMoviesByTitle(searchText, page);
    }
}
