package com.tiagoespinha.popmovee.content;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {

    public static final String AUTHORITY = "com.tiagoespinha.popmovee";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";


    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_THUMBNAIL_URL = "poster_thumbnail_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
