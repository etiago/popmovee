package com.tiagoespinha.popmovee.services;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tiago on 20/01/2017.
 */
@Module
public class MainActivityModule {
    private final Context mContext;

    public MainActivityModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context context() {
        return mContext;
    }

    @Provides
    @Singleton
    static MovieThumbnailAdapter provideMovieThumbnailAdapter() {
        return new MovieThumbnailAdapter();
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager() {
        return new GridLayoutManager(mContext,2);
    }
}
