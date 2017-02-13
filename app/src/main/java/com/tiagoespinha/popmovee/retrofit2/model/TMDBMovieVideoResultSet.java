package com.tiagoespinha.popmovee.retrofit2.model;

import java.util.List;

/**
 * Created by TiagoEspinha on 12/02/2017.
 */
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
    private List<TMDBMovieVideo> movieVideoList;
}
