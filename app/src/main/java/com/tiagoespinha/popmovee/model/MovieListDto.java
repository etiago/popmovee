package com.tiagoespinha.popmovee.model;

import android.net.Uri;
import android.util.Log;

import com.tiagoespinha.popmovee.services.TMDBService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
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
    public List<MovieMetadata> getMovieMetadatas() {
        return movieMetadatas;
    }

    public void setMovieMetadatas(List<MovieMetadata> movieMetadatas) {
        this.movieMetadatas = movieMetadatas;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalMovieCount() {
        return totalMovieCount;
    }

    public void setTotalMovieCount(int totalMovieCount) {
        this.totalMovieCount = totalMovieCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    private List<MovieMetadata> movieMetadatas;
    private int page;
    private int totalMovieCount;
    private int totalPageCount;

    private static final String MOVIE_POSTER_IMAGE_BASE_ENDPOINT = "https://image.tmdb.org/t/p/w342/";
    public static final Uri MOVIE_POSTER_IMAGE_BASE_URI;
    static {
        MOVIE_POSTER_IMAGE_BASE_URI = Uri.parse(MOVIE_POSTER_IMAGE_BASE_ENDPOINT);
    }

    public static MovieListDto parseFromTMDBMovieResultSet(TMDBService.TMDBMovieResultSet tmdbMovieResultSet) {
        MovieListDto newMovieListDto = new MovieListDto();
        List<MovieMetadata> movieMetadatas = new ArrayList<>();

        newMovieListDto.setPage(tmdbMovieResultSet.page);
        newMovieListDto.setTotalPageCount(tmdbMovieResultSet.totalPages);
        newMovieListDto.setTotalMovieCount(tmdbMovieResultSet.totalResults);
        newMovieListDto.setMovieMetadatas(movieMetadatas);

        for (TMDBService.TMDBMovie tmdbMovie : tmdbMovieResultSet.results) {
            MovieMetadata movieMetadata = new MovieMetadata();
            movieMetadata.setPosterThumbnailURL(buildPosterURLFromPath(tmdbMovie.posterPath));
            movieMetadata.setVoteAverage(tmdbMovie.voteAverage);
            movieMetadata.setOverview(tmdbMovie.overview);
            movieMetadata.setOriginalTitle(tmdbMovie.originalTitle);
            movieMetadata.setReleaseDate(buildCalendarFromDateString(tmdbMovie.releaseDate));
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

            newMovieListDto.setMovieMetadatas(metadatas);
        } catch (JSONException e) {
            e.printStackTrace();
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
