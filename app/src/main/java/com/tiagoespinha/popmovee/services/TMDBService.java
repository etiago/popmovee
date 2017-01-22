package com.tiagoespinha.popmovee.services;

import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tiago on 19/01/2017.
 */

public interface TMDBService {
    @GET("/3/movie/popular")
    Observable<TMDBMovieResultSet> listPopularMovies();

    @GET("/3/movie/popular")
    Observable<TMDBMovieResultSet> listPopularMovies(@Query("page") int page);

    @GET("/3/movie/top_rated")
    Observable<TMDBMovieResultSet> listTopRatedMovies();

    @GET("/3/movie/top_rated")
    Observable<TMDBMovieResultSet> listTopRatedMovies(@Query("page") int page);
}
