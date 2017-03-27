package com.tiagoespinha.popmovee.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.tiagoespinha.popmovee.content.FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;

public class FavoriteMovieProvider extends ContentProvider {

    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_ID = 101;
    public static final int FAVORITE_MOVIES_WITH_PAGE = 102;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,
                FavoriteMovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,
                FavoriteMovieContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_ID);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY,
                FavoriteMovieContract.PATH_FAVORITE_MOVIES + "/page/#", FAVORITE_MOVIES_WITH_PAGE);

        return uriMatcher;
    }

    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    @Override
    public boolean onCreate() {
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the movies directory and write a default case
        switch (match) {
            case FAVORITE_MOVIES:
                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            // Query for the movies directory
            case FAVORITE_MOVIES_WITH_PAGE:
                String page = uri.getLastPathSegment();

                if (page == null) {
                    throw new android.database.SQLException("Page number is mandatory for MovieProvider query " + uri);
                }

                // The idea here is to get paging working.
                // Page 1: 0,10
                // Page 2: 10, 10
                // etc...
                String offsetLimit = (Integer.valueOf(page) - 1) * 10 + ", 10";

                retCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        offsetLimit);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Get access to the movie database (to write new data to)
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the movies directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVORITE_MOVIES:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted movies
        int moviesDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITE_MOVIE_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                moviesDeleted = db.delete(TABLE_NAME, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (moviesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of movies deleted
        return moviesDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
