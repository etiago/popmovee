package com.tiagoespinha.popmovee.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieMetadata implements Parcelable {
    private String originalTitle;
    private URL posterThumbnailURL;
    private String overview;
    private double voteAverage;
    private Calendar releaseDate;

    public MovieMetadata() {
        /* Default constructor */
    }

    private MovieMetadata(Parcel in) {
        originalTitle = in.readString();
        try {
            posterThumbnailURL = new URL(in.readString());
        } catch (MalformedURLException e) {
            Log.e(MovieMetadata.class.getSimpleName(), "Error reading poster thumbnail URL", e);
        }
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = Calendar.getInstance();
        releaseDate.setTimeInMillis(in.readLong());
    }

    public static final Creator<MovieMetadata> CREATOR = new Creator<MovieMetadata>() {
        @Override
        public MovieMetadata createFromParcel(Parcel in) {
            return new MovieMetadata(in);
        }

        @Override
        public MovieMetadata[] newArray(int size) {
            return new MovieMetadata[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(posterThumbnailURL.toString());
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeLong(releaseDate.getTimeInMillis());
    }
}
