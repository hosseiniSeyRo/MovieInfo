package com.rhosseini.movieinfo.model.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.rhosseini.movieinfo.model.database.MovieDatabase;
import com.rhosseini.movieinfo.model.database.dao.MovieDao;
import com.rhosseini.movieinfo.model.database.entity.Movie;
import com.rhosseini.movieinfo.model.webServise.MovieApi;
import com.rhosseini.movieinfo.model.webServise.RetrofitClient;
import com.rhosseini.movieinfo.model.webServise.responseModel.MovieSearchResponse.MovieInSearch;
import com.rhosseini.movieinfo.model.webServise.responseModel.MovieSearchResponse;
import com.rhosseini.movieinfo.model.webServise.responseModel.Resource;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private final String TAG = "rHosseini -> " + this.getClass().getSimpleName();
    private static MovieRepository movieRepository;
    private MovieDao movieDao;
    private MovieApi movieApi;
    public MediatorLiveData<Resource<List<Movie>>> responseWrapper = new MediatorLiveData<>();


    public static MovieRepository getInstance(Application app) {
        if (movieRepository == null) {
            movieRepository = new MovieRepository(app);
        }
        return movieRepository;
    }

    private MovieRepository(Application app) {
        this.movieApi = RetrofitClient.createService(MovieApi.class);
        this.movieDao = MovieDatabase.getINSTANCE(app).movieDao();
    }

    /* get all Movies */
    public void getMoviesByTitle(String title, Integer page) {

//        LiveData<List<Movie>> allMovies = movieDao.getAllMovies();
//        responseWrapper.addSource(allMovies, movies -> {
//            responseWrapper.removeSource(allMovies);
            responseWrapper.setValue(Resource.loading(null));


            // fetch data from server
            movieApi.getMoviesByTitle(title, page).enqueue(new Callback<MovieSearchResponse>() {
                @Override
                public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                    if (response.isSuccessful()) {
                        List<Movie> convertedList = convertResponseMovieToDBMovie(response.body().getMovieList());

                        //TODO Success status
                        responseWrapper.setValue(Resource.success(convertedList));

                        // save data in db
                        saveMoviesInDb(convertedList);
                    } else {
                        //TODO ERROR status
                        responseWrapper.setValue(Resource.error(response.message(), null));

                        Log.e(TAG, response.code() + " " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                    //TODO ERROR status
                    responseWrapper.setValue(Resource.error(t.getMessage(), null));
                    Log.e(TAG, t.getMessage() != null ? t.getMessage() : "Something went wrong");
                }
            });
//        });
    }

    // save server response in database
    private void saveMoviesInDb(List<Movie> movieList) {
        if (movieList != null) {
            insertMovies(movieList);
        }
    }

    // convert server response to database entity
    private List<Movie> convertResponseMovieToDBMovie(List<MovieInSearch> movieListInSearch) {
        if (movieListInSearch == null || movieListInSearch.size() == 0)
            return null;

        List<Movie> movieList = new ArrayList<>();
        for (MovieInSearch currentMovie : movieListInSearch) {
            movieList.add(new Movie(currentMovie.getImdbID(), currentMovie.getTitle(), currentMovie.getYear(), currentMovie.getPoster()));
        }

        return movieList;
    }

    // insert movies in db
    private void insertMovies(List<Movie> movieList) {
        if (movieList != null)
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
            dao.insert(arrayLists[0]);
            return null;
        }
    }

//    // save server response in database
//    private void saveMovieDetailsInDb(Response<MovieResponse> response) {
//        if (response.body() != null) {
//            // convert server response to database entity and insert in db
//            insert(convertResponseMovieByIdToDBMovie(response.body()));
//        }
//    }

//    // convert server response to database entity
//    private List<Movie> convertResponseMovieByIdToDBMovie(MovieResponse movie) {
//        List<Movie> movieList = new ArrayList<>();
//
//        movieList.add(new Movie(movie.getImdbID(), movie.getTitle(), movie.getYear(), movie.getGenre(), movie.getDirector(), movie.getWriter(), movie.getActors(), movie.getLanguage(), movie.getCountry(), movie.getPoster()));
//
//        return movieList;
//    }

//    public LiveData<Movie> getMovieById(String imdbId) {
//        //TODO if movie detail exist in db fetch from db else fetch from server and save in db
//        LiveData<Movie> currentMovie = movieDao.getMovieById(imdbId);
//
//        // check movie detail exist in db or not
//        if (currentMovie != null) {
//            if (currentMovie.getValue().getGenre() != null) {
//                // fetch movie details from db
//                return currentMovie;
//            } else {
//                // fetch movie details from server
//                movieApi.getMoviesById(imdbId).enqueue(new Callback<MovieResponse>() {
//                    @Override
//                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                        if (response.isSuccessful()) {
//                            // save data in db
//                            saveMovieDetailsInDb(response);
//                        } else {
//                            Log.e(TAG, response.code() + " " + response.message());
//                            Toast.makeText(mContext, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MovieResponse> call, Throwable t) {
//                        Log.e(TAG, t.getMessage() != null ? t.getMessage() : "Something went wrong");
//                        Toast.makeText(mContext, t.getMessage() != null ? t.getMessage() : "Something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }
//
//        return currentMovie;
//    }


}
