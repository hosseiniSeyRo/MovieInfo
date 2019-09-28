package com.rhosseini.movieinfo.model.webServise;

import com.rhosseini.movieinfo.model.webServise.responseModel.MovieResponse;
import com.rhosseini.movieinfo.model.webServise.responseModel.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("?apikey=40970e78")
    Call<MovieSearchResponse> getMoviesByTitle(
            @Query("s") String title,
            @Query("page") Integer page);

    @GET("?apikey=40970e78")
    Call<MovieResponse> getMoviesById(
            @Query("i") String imdbId);
}
