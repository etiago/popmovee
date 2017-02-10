package com.tiagoespinha.popmovee.services;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ArrayAdapter;

import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.listeners.MovieEndlessRecyclerViewScrollListener;
import com.tiagoespinha.popmovee.listeners.NavigationSpinnerOnItemSelectedListener;
import com.tiagoespinha.popmovee.model.MovieMetadata;

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
    public IMovieThumbnailAdapter<MovieMetadata> provideMovieThumbnailAdapter() {
        return new MovieThumbnailAdapter();
    }

    @Provides
    @Singleton
    public MovieListConsumerMainActivity provideMovieListConsumerMainActivity(IMovieThumbnailAdapter<MovieMetadata> movieThumbnailAdapter) {
        return new MovieListConsumerMainActivity(movieThumbnailAdapter);
    }

    @Provides
    @Singleton
    public AddToMovieListConsumerMainActivity provideAddToMovieListConsumerMainActivity(IMovieThumbnailAdapter<MovieMetadata> movieThumbnailAdapter) {
        return new AddToMovieListConsumerMainActivity(movieThumbnailAdapter);
    }

    @Provides
    @Singleton
    public ThrowableConsumerMainActivity provideThrowableConsumerMainActivity() {
        return new ThrowableConsumerMainActivity();
    }

    @Provides
    @Singleton
    public NavigationSpinnerOnItemSelectedListener
        provideNavigationSpinnerOnItemSelected(TMDBService tmdbService,
                                               MovieListConsumerMainActivity movieListConsumerMainActivity,
                                               ThrowableConsumerMainActivity throwableConsumerMainActivity) {
        return new NavigationSpinnerOnItemSelectedListener(tmdbService, movieListConsumerMainActivity, throwableConsumerMainActivity);
    }

    @Provides
    @Singleton
    public ArrayAdapter<CharSequence> provideNavigationSpinnerArrayAdapter(Context context) {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(context,
                R.array.movie_sort_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return arrayAdapter;
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
    public MovieEndlessRecyclerViewScrollListener provideEndlessRecyclerViewScrollListener(
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
