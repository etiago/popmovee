package com.tiagoespinha.popmovee.listeners;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.rx.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.content.FavoriteMovieContract;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.retrofit2.services.TMDBService;
import com.tiagoespinha.popmovee.utils.EndlessRecyclerViewScrollListener;

import java.util.function.Function;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tiago on 22/01/2017.
 */

public class MovieEndlessRecyclerViewScrollListener extends EndlessRecyclerViewScrollListener {
    private TMDBService mTMDBService;
    private AddToMovieListConsumerMainActivity mAddToMovieListConsumer;
    private Function<Spinner, Consumer<Throwable>> mHttpErrorHandler;
    private Context mContext;

    @Inject public MovieEndlessRecyclerViewScrollListener(Context context,
                                                          GridLayoutManager gridLayoutManager,
                                                          AddToMovieListConsumerMainActivity addToMovieListConsumerMainActivity,
                                                          Function<Spinner, Consumer<Throwable>> httpErrorHandler,
                                                          TMDBService tmdbService) {
        super(gridLayoutManager);
        mContext = context;
        mTMDBService = tmdbService;
        mAddToMovieListConsumer = addToMovieListConsumerMainActivity;
        mHttpErrorHandler = httpErrorHandler;
    }

    @Override
    public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to the bottom of the list
        Observable<MovieListDto> movieResultSet;
        switch (PopMoveeApp.getMovieListType()) {
            case MOST_POPULAR:
                movieResultSet = mTMDBService
                        .listPopularMovies(page+1)
                        .map(MovieListDto::parseFromTMDBMovieResultSet);
                break;
            case HIGHEST_RATED:
                movieResultSet = mTMDBService
                        .listTopRatedMovies(page+1)
                        .map(MovieListDto::parseFromTMDBMovieResultSet);
                break;
            default:
                Uri moviesUri = FavoriteMovieContract.FavoriteMovieEntry
                        .CONTENT_URI
                        .buildUpon()
                        .appendPath("page")
                        .appendPath(String.valueOf(page+1))
                        .build();
                movieResultSet = Observable.fromCallable(() -> {
                    Cursor c = mContext.getContentResolver()
                            .query(moviesUri,
                                    null,
                                    null,
                                    null,
                                    null);

                    return MovieListDto.parseFromFavoriteMovieProviderCursor(c);
                });
        }

        movieResultSet
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAddToMovieListConsumer,
                        mHttpErrorHandler.apply(((Spinner) view.getRootView().findViewById(R.id.spinner_nav))));
    }
}
