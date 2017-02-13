package com.tiagoespinha.popmovee.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieResultSet;
import com.tiagoespinha.popmovee.services.TMDBService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tiago on 23/01/2017.
 */

public class NavigationSpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private Observable<TMDBMovieResultSet> mMovieResultSetObservable;
    private TMDBService mTMDBService;
    private MovieListConsumerMainActivity mMovieListConsumer;
    private ThrowableConsumerMainActivity mThrowableConsumer;

    @Inject public NavigationSpinnerOnItemSelectedListener(TMDBService tmdbService,
                                                           MovieListConsumerMainActivity movieListConsumer,
                                                           ThrowableConsumerMainActivity throwableConsumer) {
        mTMDBService = tmdbService;
        mMovieListConsumer = movieListConsumer;
        mThrowableConsumer = throwableConsumer;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            PopMoveeApp.setShowingPopularMovies(true);
            mMovieResultSetObservable = mTMDBService.listPopularMovies();
        } else {
            PopMoveeApp.setShowingPopularMovies(false);
            mMovieResultSetObservable = mTMDBService.listTopRatedMovies();
        }
        mMovieResultSetObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMovieListConsumer, mThrowableConsumer);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /* This shouldn't happen... the user should always select an option */
    }

    public Observable<TMDBMovieResultSet> getMovieResultSetObservable() {
        return mMovieResultSetObservable;
    }
}
