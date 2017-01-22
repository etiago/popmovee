package com.tiagoespinha.popmovee.model;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List<MovieMetadata> movieMetadata;
    private int page;
    private int totalMovieCount;
    private int totalPageCount;

    private static final String MOVIE_POSTER_IMAGE_BASE_ENDPOINT = "https://image.tmdb.org/t/p/w342/";
    private static final Uri MOVIE_POSTER_IMAGE_BASE_URI;

    static {
        MOVIE_POSTER_IMAGE_BASE_URI = Uri.parse(MOVIE_POSTER_IMAGE_BASE_ENDPOINT);
    }

    private void setMovieMetadata(List<MovieMetadata> movieMetadata) {
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
            movieMetadata.setPosterThumbnailURL(buildPosterURLFromPath(tmdbMovie.getPosterPath()));
            movieMetadata.setVoteAverage(tmdbMovie.getVoteAverage());
            movieMetadata.setOverview(tmdbMovie.getOverview());
            movieMetadata.setOriginalTitle(tmdbMovie.getOriginalTitle());
            movieMetadata.setReleaseDate(buildCalendarFromDateString(tmdbMovie.getReleaseDate()));
            movieMetadatas.add(movieMetadata);
        }

        return newMovieListDto;
    }

    public static MovieListDto parseFromJSON(String jsonMovieListDto) {
        MovieListDto newMovieListDto = new MovieListDto();
        List<MovieMetadata> metadatas = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonMovieListDto);
            newMovieListDto.setPage(jsonObject.getInt("page"));
            newMovieListDto.setTotalMovieCount(jsonObject.getInt("total_results"));
            newMovieListDto.setTotalPageCount(jsonObject.getInt("total_pages"));

            JSONArray movieResults = jsonObject.getJSONArray("results");

            for(int i = 0; i<movieResults.length(); i++) {
                JSONObject singleMovie = movieResults.getJSONObject(i);
                MovieMetadata movieMetadata = new MovieMetadata();
                movieMetadata.setPosterThumbnailURL(buildPosterURLFromPath(singleMovie.getString("poster_path")));
                movieMetadata.setOriginalTitle(singleMovie.getString("original_title"));
                movieMetadata.setOverview(singleMovie.getString("overview"));
                movieMetadata.setVoteAverage(singleMovie.getDouble("vote_average"));
                movieMetadata.setReleaseDate(buildCalendarFromDateString(singleMovie.getString("release_date")));
                metadatas.add(movieMetadata);
            }

            newMovieListDto.setMovieMetadata(metadatas);
        } catch (JSONException e) {
            Log.w(MovieListDto.class.getSimpleName(),e);
        }

        return newMovieListDto;
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
