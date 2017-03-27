package com.tiagoespinha.popmovee.retrofit2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBMovieReviewResultSet {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TMDBMovieReview> getMovieReviewList() {
        return movieReviewList;
    }

    public void setMovieReviewList(List<TMDBMovieReview> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }

    private int id;
    @SerializedName("results")
    private List<TMDBMovieReview> movieReviewList;
}
