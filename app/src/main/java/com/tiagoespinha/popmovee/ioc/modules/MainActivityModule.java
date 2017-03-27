package com.tiagoespinha.popmovee.ioc.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tiagoespinha.popmovee.MovieListType;
import com.tiagoespinha.popmovee.PopMoveeApp;
import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.rx.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.rx.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.listeners.MovieEndlessRecyclerViewScrollListener;
import com.tiagoespinha.popmovee.listeners.NavigationSpinnerOnItemSelectedListener;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.retrofit2.services.TMDBService;

import java.util.function.Function;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.reactivex.functions.Consumer;

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
    public Function<Spinner, Consumer<Throwable>> provideTMDBFetchErrorHandler(
            Context context,
            ArrayAdapter<CharSequence> navigatonMenuArrayAdapter) {
        return (spinner) -> new Consumer<Throwable>() {
            private Toast toast;

            @Override
            public void accept(Throwable throwable) throws Exception {
                int favoritePosition =
                        navigatonMenuArrayAdapter
                                .getPosition(context.getString(R.string.favorite_movies));

                spinner.setSelection(favoritePosition);
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(mContext, mContext.getString(R.string.error_loading_movies_showing_favorites), Toast.LENGTH_LONG);
                toast.show();
            }
        };
    }

    @Provides
    @Singleton
    public NavigationSpinnerOnItemSelectedListener
        provideNavigationSpinnerOnItemSelected(Context context,
                                               TMDBService tmdbService,
                                               MovieListConsumerMainActivity movieListConsumerMainActivity,
                                               Function<Spinner, Consumer<Throwable>> httpErrorHandler) {
        return new NavigationSpinnerOnItemSelectedListener(context, tmdbService, movieListConsumerMainActivity, httpErrorHandler);
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
            Context context,
            TMDBService tmdbService,
            @Named("cached") GridLayoutManager gridLayoutManager,
            AddToMovieListConsumerMainActivity addToMovieListConsumerMainActivity,
            Function<Spinner, Consumer<Throwable>> httpErrorHandler) {
        return new MovieEndlessRecyclerViewScrollListener(context,
                gridLayoutManager,
                addToMovieListConsumerMainActivity,
                httpErrorHandler,
                tmdbService);
    }


}
