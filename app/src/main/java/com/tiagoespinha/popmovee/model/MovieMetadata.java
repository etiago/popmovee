package com.tiagoespinha.popmovee.model;

import java.io.Serializable;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieMetadata implements Serializable {
    private String originalTitle;
    private URL posterThumbnailURL;
    private String overview;
    private double voteAverage;
    private Calendar releaseDate;

    public String getOriginalTitle() {
        return originalTitle;
    }
    void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public URL getPosterThumbnailURL() {
        return posterThumbnailURL;
    }
    void setPosterThumbnailURL(URL posterThumbnailURL) {
        this.posterThumbnailURL = posterThumbnailURL;
    }

    public String getOverview() {
        return overview;
    }
    void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
    void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }
    void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }
}
