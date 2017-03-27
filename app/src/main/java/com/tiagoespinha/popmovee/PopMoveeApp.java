package com.tiagoespinha.popmovee;

import android.app.Application;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.tiagoespinha.popmovee.ioc.components.DaggerMainActivityComponent;
import com.tiagoespinha.popmovee.ioc.components.DaggerMovieDetailsActivityComponent;
import com.tiagoespinha.popmovee.ioc.components.MainActivityComponent;
import com.tiagoespinha.popmovee.ioc.components.MovieDetailsActivityComponent;
import com.tiagoespinha.popmovee.ioc.modules.MainActivityModule;

public class PopMoveeApp extends Application {
    private static int movieListSpanLandscape;
    private static int movieListSpanPortrait;
    private MainActivityComponent mMainActivityComponent;
    private MovieDetailsActivityComponent mMovieDetailsActivityComponent;
    private static MovieListType mMovieListType;

    public static MovieListType getMovieListType() {
        return mMovieListType;
    }

    public static void setMovieListType(MovieListType movieListType) {
        mMovieListType = movieListType;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMovieListType = MovieListType.MOST_POPULAR;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        movieListSpanLandscape = Math.min(6,Math.round(dpHeight /342)*3);
        movieListSpanPortrait = Math.min(6,Math.round(dpWidth /342)*3);
        mMainActivityComponent = DaggerMainActivityComponent
                .builder()
                .mainActivityModule(new MainActivityModule(this))
                .build();

        mMovieDetailsActivityComponent = DaggerMovieDetailsActivityComponent.create();
    }

    public static int getMovieListSpan(int orientation){
        // Checks the orientation of the screen
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return movieListSpanLandscape;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            return movieListSpanPortrait;
        }
        return -1;
    }

    public MainActivityComponent getMainActivityComponent() {
        return mMainActivityComponent;
    }
    public MovieDetailsActivityComponent getMovieDetailsActivityComponent() {
        return mMovieDetailsActivityComponent;
    }

    public void setMainActivityComponent(MainActivityComponent mainActivityComponent) {
        mMainActivityComponent = mainActivityComponent;
    }
}
