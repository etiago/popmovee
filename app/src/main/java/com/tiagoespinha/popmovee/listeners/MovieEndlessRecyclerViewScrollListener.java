package com.tiagoespinha.popmovee.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieResultSet;
import com.tiagoespinha.popmovee.services.TMDBService;
import com.tiagoespinha.popmovee.utils.EndlessRecyclerViewScrollListener;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tiago on 22/01/2017.
 */

public class MovieEndlessRecyclerViewScrollListener extends EndlessRecyclerViewScrollListener {
    private TMDBService mTMDBService;
    private AddToMovieListConsumerMainActivity mAddToMovieListConsumer;
    private ThrowableConsumerMainActivity mThrowableConsumer;

    @Inject public MovieEndlessRecyclerViewScrollListener(GridLayoutManager gridLayoutManager,
                                                          AddToMovieListConsumerMainActivity addToMovieListConsumerMainActivity,
                                                          ThrowableConsumerMainActivity throwableConsumerMainActivity,
                                                          TMDBService tmdbService) {
        super(gridLayoutManager);
        mTMDBService = tmdbService;
        mAddToMovieListConsumer = addToMovieListConsumerMainActivity;
        mThrowableConsumer = throwableConsumerMainActivity;
    }

    @Override
    public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to the bottom of the list
        Observable<TMDBMovieResultSet> movieResultSet;
        if (PopMoveeApp.isShowingPopularMovies()) {
            movieResultSet = mTMDBService.listPopularMovies(page+1);
        } else {
            movieResultSet = mTMDBService.listTopRatedMovies(page+1);
        }
        movieResultSet
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAddToMovieListConsumer, mThrowableConsumer);
    }
}
