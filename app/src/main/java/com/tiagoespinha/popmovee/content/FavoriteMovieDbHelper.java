package com.tiagoespinha.popmovee.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tiagoespinha.popmovee.content.FavoriteMovieContract.FavoriteMovieEntry;

/**
 * Created by tiago on 17/02/2017.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE =
            "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                    FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                    FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_POSTER_THUMBNAIL_URL + " TEXT NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                    FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL);";

    public static final String DATABASE_NAME = "favoriteMovies.db";
    public static final int VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
