package com.tiagoespinha.popmovee.services;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.listeners.MovieEndlessRecyclerViewScrollListener;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by tiago on 20/01/2017.
 */
@Module(includes = TMDBServiceModule.class)
public class MainActivityModule {
    private final Context mContext;
    private GridLayoutManager mGridLayoutManager;

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
    @Singleton
    static MovieListConsumerMainActivity provideMovieListConsumerMainActivity(MovieThumbnailAdapter movieThumbnailAdapter) {
        return new MovieListConsumerMainActivity(movieThumbnailAdapter);
    }

    @Provides
    @Singleton
    static AddToMovieListConsumerMainActivity provideAddToMovieListConsumerMainActivity(MovieThumbnailAdapter movieThumbnailAdapter) {
        return new AddToMovieListConsumerMainActivity(movieThumbnailAdapter);
    }

    @Provides
    @Singleton
    static ThrowableConsumerMainActivity provideThrowableConsumerMainActivity() {
        return new ThrowableConsumerMainActivity();
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager(Context context) {
        mGridLayoutManager = new GridLayoutManager(context,2);
        return mGridLayoutManager;
    }

    @Provides
    @Named("cached")
    public GridLayoutManager provideCachedGridLayoutManager(Context context) {
        if (mGridLayoutManager == null) {
            mGridLayoutManager = new GridLayoutManager(context, 2);
        }
        return mGridLayoutManager;
    }

    @Provides
    MovieEndlessRecyclerViewScrollListener provideEndlessRecyclerViewScrollListener(
            TMDBService tmdbService,
            @Named("cached") GridLayoutManager gridLayoutManager,
            AddToMovieListConsumerMainActivity addToMovieListConsumerMainActivity,
            ThrowableConsumerMainActivity throwableConsumerMainActivity) {
        return new MovieEndlessRecyclerViewScrollListener(gridLayoutManager,
                addToMovieListConsumerMainActivity,
                throwableConsumerMainActivity,
                tmdbService);
    }
}
