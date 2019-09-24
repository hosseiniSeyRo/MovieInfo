package com.rhosseini.movieinfo.model.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.rhosseini.movieinfo.model.database.MovieDatabase;
import com.rhosseini.movieinfo.model.database.dao.MovieDao;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.webServise.MovieApi;
import com.rhosseini.movieinfo.model.webServise.RetrofitClient;
import com.rhosseini.movieinfo.model.webServise.responseModel.MovieSearchResponse.MovieInSearch;
import com.rhosseini.movieinfo.model.webServise.responseModel.MovieSearchResponse;
import com.rhosseini.movieinfo.utils.Method;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private final String TAG = "rHosseini -> " + this.getClass().getSimpleName();
    private Context mContext;
    private static MovieRepository movieRepository;
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;
    private MovieApi movieApi;


    public static MovieRepository getInstance(Application app) {
        if (movieRepository == null) {
            movieRepository = new MovieRepository(app);
        }
        return movieRepository;
    }


    private MovieRepository(Application app) {
        this.mContext = app;
        this.movieApi = RetrofitClient.createService(MovieApi.class);
        this.movieDao = MovieDatabase.getINSTANCE(app).movieDao();
    }

    /* get all Movies */
    public LiveData<List<Movie>> getAllMovies(String searchText, Integer page) {
        allMovies = movieDao.getAllMovies();

        // if internet is connected fetch data from server
        if (Method.isInternetConnected(mContext)) {
            movieApi.getMoviesBySearch(searchText, page).enqueue(new Callback<MovieSearchResponse>() {
                @Override
                public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                    if (response.isSuccessful()) {
                        // save data in db
                        saveInDb(response);
                    } else {
                        Log.e(TAG, response.code() + " " + response.message());
                        Toast.makeText(mContext, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                    Log.e(TAG, t.getMessage() != null ? t.getMessage() : "Something went wrong");
                    Toast.makeText(mContext, t.getMessage() != null ? t.getMessage() : "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Internet not is connected");
            Toast.makeText(mContext, "Internet not is connected", Toast.LENGTH_SHORT).show();
        }

        return allMovies;
    }

    // save server response in database
    private void saveInDb(Response<MovieSearchResponse> response) {
        if (response.body().getMovieList() != null) {
            // convert server response to database entity and insert in db
            insertMovies(convertResponseMovieToDBMovie(response.body().getMovieList()));
        }
    }

    // convert server response to database entity
    private List<Movie> convertResponseMovieToDBMovie(List<MovieInSearch> movieListInSearch) {
        List<Movie> movieList = new ArrayList<>();
        for (MovieInSearch currentMovie : movieListInSearch) {
            movieList.add(new Movie(currentMovie.getImdbID(), currentMovie.getTitle(), currentMovie.getYear(), currentMovie.getPoster()));
        }

        return movieList;
    }

    // insert movies in db
    private void insertMovies(List<Movie> movieList) {
        // database tasks should be done asynchronously
        new InsertMoviesAsyncTask(movieDao).execute((ArrayList<Movie>) movieList);
    }

    // insert movie asyncTask
    private class InsertMoviesAsyncTask extends AsyncTask<ArrayList<Movie>, Void, Void> {

        private MovieDao dao;

        private InsertMoviesAsyncTask(MovieDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ArrayList<Movie>... arrayLists) {
            dao.insertMovies(arrayLists[0]);
            return null;
        }
    }
}
