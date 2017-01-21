package com.tiagoespinha.popmovee;

import android.app.Application;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.tiagoespinha.popmovee.services.DaggerMainActivityComponent;
import com.tiagoespinha.popmovee.services.MainActivityComponent;
import com.tiagoespinha.popmovee.services.MainActivityModule;

/**
 * Created by tiago on 19/01/2017.
 */

public class PopMoveeApp extends Application {
    private float dpHeight;
    private float dpWidth;
    private static int movieListSpanLandscape;
    private static int movieListSpanPortrait;
    private MainActivityComponent mMainActivityComponent;

    public PopMoveeApp() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        movieListSpanLandscape = Math.min(6,Math.round(dpHeight/342)*3);
        movieListSpanPortrait = Math.min(6,Math.round(dpWidth/342)*3);
        mMainActivityComponent = DaggerMainActivityComponent
                .builder()
                .mainActivityModule(new MainActivityModule(this))
                .build();
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
}
