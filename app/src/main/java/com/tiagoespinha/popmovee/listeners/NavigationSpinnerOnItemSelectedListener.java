package com.tiagoespinha.popmovee.listeners;

import android.content.Context;

import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tiagoespinha.popmovee.MovieListType;
import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.rx.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.content.FavoriteMovieContract;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.retrofit2.services.TMDBService;

import java.util.function.Function;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tiago on 23/01/2017.
 */

public class NavigationSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private Observable<MovieListDto> mMovieResultSetObservable;
    private TMDBService mTMDBService;
    private MovieListConsumerMainActivity mMovieListConsumer;
    private Function<Spinner, Consumer<Throwable>> mHttpErrorHandler;
    private Context mContext;

    @Inject public NavigationSpinnerOnItemSelectedListener(Context context,
                                                           TMDBService tmdbService,
                                                           MovieListConsumerMainActivity movieListConsumer,
                                                           Function<Spinner, Consumer<Throwable>> httpErrorHandler) {
        mTMDBService = tmdbService;
        mMovieListConsumer = movieListConsumer;
        mHttpErrorHandler = httpErrorHandler;
        mContext = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = mContext.getString(R.string.most_popular_movies);
        if (position == 0) {
            PopMoveeApp.setMovieListType(MovieListType.MOST_POPULAR);

            mMovieResultSetObservable = mTMDBService
                    .listPopularMovies()
                    .map(MovieListDto::parseFromTMDBMovieResultSet);
        } else if (position == 1) {
            PopMoveeApp.setMovieListType(MovieListType.HIGHEST_RATED);
            label = mContext.getString(R.string.highest_rated_movies);
            mMovieResultSetObservable = mTMDBService
                    .listTopRatedMovies()
                    .map(MovieListDto::parseFromTMDBMovieResultSet);
        } else {
            Uri moviesUri = FavoriteMovieContract.FavoriteMovieEntry
                    .CONTENT_URI
                    .buildUpon()
                    .appendPath("page")
                    .appendPath("1")
                    .build();

            PopMoveeApp.setMovieListType(MovieListType.FAVORITES);
            label = mContext.getString(R.string.favorite_movies);
            mMovieResultSetObservable = Observable.fromCallable(() -> {
                Cursor c = mContext.getContentResolver()
                        .query(moviesUri,
                                null,
                                null,
                                null,
                                null);

                return MovieListDto.parseFromFavoriteMovieProviderCursor(c);
            });
        }


        TextView movieListLabel = ((TextView)parent.getRootView().findViewById(R.id.tv_movie_list_label));

        movieListLabel.setText(label);

        Spinner movieListSelector = ((Spinner)parent.getRootView().findViewById(R.id.spinner_nav));

        mMovieResultSetObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMovieListConsumer, mHttpErrorHandler.apply(movieListSelector));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /* This shouldn't happen... the user should always select an option */
    }

    public Observable<MovieListDto> getMovieResultSetObservable() {
        return mMovieResultSetObservable;
    }
}
