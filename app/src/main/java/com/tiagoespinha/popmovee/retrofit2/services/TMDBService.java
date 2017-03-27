package com.tiagoespinha.popmovee.retrofit2.services;

import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieResultSet;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieReviewResultSet;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideoResultSet;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("/3/movie/{movieid}/videos")
    Observable<TMDBMovieVideoResultSet> listVideosForMovieId(@Path("movieid") int movieId);

    @GET("/3/movie/{movieid}/reviews")
    Observable<TMDBMovieReviewResultSet> listReviewsForMovieId(@Path("movieid") int movieId);
}
