package com.tiagoespinha.popmovee.services;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tiago on 19/01/2017.
 */

public interface TMDBService {
    @GET("/3/movie/popular")
    Call<TMDBMovieResultSet> listPopularMovies();

    @GET("/3/movie/popular")
    Call<TMDBMovieResultSet> listPopularMovies(@Query("page") int page);

    @GET("/3/movie/top_rated")
    Call<TMDBMovieResultSet> listTopRatedMovies();

    @GET("/3/movie/top_rated")
    Call<TMDBMovieResultSet> listTopRatedMovies(@Query("page") int page);

    class TMDBMovieResultSet {
        public int page;
        public List<TMDBMovie> results;
        @SerializedName("total_results")
        public int totalResults;
        @SerializedName("total_pages")
        public int totalPages;
    }

    class TMDBMovie {
        @SerializedName("poster_path")
        public String posterPath;
        public boolean adult;
        public String overview;
        @SerializedName("release_date")
        public String releaseDate;
        @SerializedName("genre_ids")
        public List<Integer> genreIds;
        public int id;
        @SerializedName("original_title")
        public String originalTitle;
        @SerializedName("original_language")
        public String originalLanguage;
        public String title;
        @SerializedName("backdrop_path")
        public String backdropPath;
        public double popularity;
        @SerializedName("vote_count")
        public double voteCount;
        public boolean video;
        @SerializedName("vote_average")
        public float voteAverage;
    }
}
