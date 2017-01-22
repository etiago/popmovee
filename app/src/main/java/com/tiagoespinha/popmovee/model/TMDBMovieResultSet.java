package com.tiagoespinha.popmovee.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tiago on 22/01/2017.
 */
public class TMDBMovieResultSet {
    private int page;
    private List<TMDBMovie> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TMDBMovie> getResults() {
        return results;
    }

    public void setResults(List<TMDBMovie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
