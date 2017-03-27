package com.tiagoespinha.popmovee.model;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.tiagoespinha.popmovee.content.FavoriteMovieContract;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovie;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieResultSet;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieListDto {
    public List<MovieMetadata> getMovieMetadata() {
        return movieMetadata;
    }
    private List<MovieMetadata> movieMetadata = new ArrayList<>();
    private int page;
    private int totalMovieCount;
    private int totalPageCount;

    private static final String MOVIE_POSTER_IMAGE_BASE_ENDPOINT = "https://image.tmdb.org/t/p/w342/";
    private static final Uri MOVIE_POSTER_IMAGE_BASE_URI;

    static {
        MOVIE_POSTER_IMAGE_BASE_URI = Uri.parse(MOVIE_POSTER_IMAGE_BASE_ENDPOINT);
    }

    public void setMovieMetadata(List<MovieMetadata> movieMetadata) {
        this.movieMetadata = movieMetadata;
    }

    public int getPage() {
        return page;
    }
    private void setPage(int page) {
        this.page = page;
    }

    public int getTotalMovieCount() {
        return totalMovieCount;
    }
    private void setTotalMovieCount(int totalMovieCount) {
        this.totalMovieCount = totalMovieCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }
    private void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public static MovieListDto parseFromTMDBMovieResultSet(TMDBMovieResultSet tmdbMovieResultSet) {
        MovieListDto newMovieListDto = new MovieListDto();
        List<MovieMetadata> movieMetadatas = new ArrayList<>();

        newMovieListDto.setPage(tmdbMovieResultSet.getPage());
        newMovieListDto.setTotalPageCount(tmdbMovieResultSet.getTotalPages());
        newMovieListDto.setTotalMovieCount(tmdbMovieResultSet.getTotalResults());
        newMovieListDto.setMovieMetadata(movieMetadatas);

        for (TMDBMovie tmdbMovie : tmdbMovieResultSet.getResults()) {
            MovieMetadata movieMetadata = new MovieMetadata();
            movieMetadata.setId(tmdbMovie.getId());
            movieMetadata.setPosterThumbnailURL(buildPosterURLFromPath(tmdbMovie.getPosterPath()));
            movieMetadata.setVoteAverage(tmdbMovie.getVoteAverage());
            movieMetadata.setOverview(tmdbMovie.getOverview());
            movieMetadata.setOriginalTitle(tmdbMovie.getOriginalTitle());
            movieMetadata.setReleaseDate(buildCalendarFromDateString(tmdbMovie.getReleaseDate()));
            movieMetadatas.add(movieMetadata);
        }

        return newMovieListDto;
    }

    public static MovieListDto parseFromFavoriteMovieProviderCursor(Cursor c) {
        if (c == null || !c.moveToFirst()) {
            return new MovieListDto();
        }

        MovieListDto movieListDto = new MovieListDto();

        List<MovieMetadata> movieMetadataList = new ArrayList<>(c.getCount());
        do {
            MovieMetadata movieMetadata = new MovieMetadata();

            // ID
            movieMetadata.setId(c.getInt(c.getColumnIndex(
                    FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID)));

            // Poster thumbnail URL
            try {
                movieMetadata.setPosterThumbnailURL(new URL(c.getString(c.getColumnIndex(
                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_THUMBNAIL_URL))));
            } catch (MalformedURLException e) {
                Log.e(MovieListDto.class.getSimpleName(), "Failed to parse poster thumbnail URL!", e);
            }

            // Release date
            Calendar releaseDate = Calendar
                    .getInstance();
            releaseDate
                    .setTimeInMillis(c.getInt(c.getColumnIndex(
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)));

            movieMetadata.setReleaseDate(releaseDate);
            movieMetadata.setOriginalTitle(c.getString(c.getColumnIndex(
                    FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE)));
            movieMetadata.setOverview(c.getString(c.getColumnIndex(
                    FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW)));
            movieMetadata.setVoteAverage(c.getInt(c.getColumnIndex(
                    FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE)));

            movieMetadataList.add(movieMetadata);
        } while (c.moveToNext());

        movieListDto.setTotalMovieCount(movieMetadataList.size());
        movieListDto.setTotalPageCount(1);
        movieListDto.setPage(1);
        movieListDto.setMovieMetadata(movieMetadataList);

        return movieListDto;
    }

    private static Calendar buildCalendarFromDateString(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal  = Calendar.getInstance();

        try {
            cal.setTime(df.parse(date));
            return cal;
        } catch (ParseException e) {
            Log.w(MovieListDto.class.getSimpleName(),e);
        }
        return null;
    }

    private static URL buildPosterURLFromPath(String posterUrlPath) {
        try {
            return new URL(MOVIE_POSTER_IMAGE_BASE_URI
                    .buildUpon()
                    .appendEncodedPath(posterUrlPath)
                    .build()
                    .toString());
        } catch (MalformedURLException e) {
            Log.w(MovieListDto.class.getSimpleName(),e);
        }
        return null;
    }
}
