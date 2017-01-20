package com.tiagoespinha.popmovee.model;

import java.io.Serializable;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieMetadata implements Serializable {
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public URL getPosterThumbnailURL() {
        return posterThumbnailURL;
    }

    public void setPosterThumbnailURL(URL posterThumbnailURL) {
        this.posterThumbnailURL = posterThumbnailURL;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    private String originalTitle;
    private URL posterThumbnailURL;
    private String overview;
    private double voteAverage;

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    private Calendar releaseDate;
}
