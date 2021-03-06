package com.tiagoespinha.popmovee.retrofit2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBMovieVideoResultSet {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TMDBMovieVideo> getMovieVideoList() {
        return movieVideoList;
    }

    public void setMovieVideoList(List<TMDBMovieVideo> movieVideoList) {
        this.movieVideoList = movieVideoList;
    }

    private int id;
    @SerializedName("results")
    private List<TMDBMovieVideo> movieVideoList;
}
