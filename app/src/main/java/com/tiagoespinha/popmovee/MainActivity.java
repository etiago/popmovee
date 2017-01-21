package com.tiagoespinha.popmovee;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.services.MainActivityModule;
import com.tiagoespinha.popmovee.services.TMDBService;
import com.tiagoespinha.popmovee.utils.EndlessRecyclerViewScrollListener;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView mMovieRecyclerView;
    protected Toolbar mToolbar;
    protected boolean recyclerViewIsLoading;
    protected Spinner mSpinnerNav;
    protected Toast mToast;
    protected boolean mShowPopularMovies = true;

    @Inject MovieThumbnailAdapter mMovieThumbnailAdapter;
    @Inject TMDBService mTMDBService;
    @Inject GridLayoutManager mLayoutManager;

    protected EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;

    //protected static final int NUMBER_OF_ITEMS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((PopMoveeApp)getApplication()).getMainActivityComponent().inject(this);

        mSpinnerNav = (Spinner) findViewById(R.id.spinner_nav);
        mSpinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mShowPopularMovies = true;
                    mTMDBService.listPopularMovies().enqueue(new FillMovieListInitialCallback());
                } else {
                    mShowPopularMovies = false;
                    mTMDBService.listTopRatedMovies().enqueue(new FillMovieListInitialCallback());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_sort_array, android.R.layout.simple_spinner_item);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mSpinnerNav.setAdapter(adapter);

        if (mShowPopularMovies) {
            mTMDBService.listPopularMovies().enqueue(new FillMovieListInitialCallback());
        } else {
            mTMDBService.listTopRatedMovies().enqueue(new FillMovieListInitialCallback());
        }

        recyclerViewIsLoading = true;

        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_grid);

        int orientation = getResources().getConfiguration().orientation;
        mLayoutManager.setSpanCount(PopMoveeApp.getMovieListSpan(orientation));
        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter(mMovieThumbnailAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (mShowPopularMovies) {
                    view.post(() -> mTMDBService.listPopularMovies(page + 1).enqueue(new AddToMovieListCallback()));
                } else {
                    view.post(() -> mTMDBService.listTopRatedMovies(page + 1).enqueue(new AddToMovieListCallback()));
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        mMovieRecyclerView.addOnScrollListener(scrollListener);
        //MovieListDto movieListDto = new MovieListDto();

        mToolbar = (Toolbar) findViewById(R.id.tb_main_activity);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mLayoutManager.setSpanCount(PopMoveeApp.getMovieListSpan(newConfig.orientation));
    }

    private class FillMovieListInitialCallback implements Callback<TMDBService.TMDBMovieResultSet> {
        @Override
        public void onResponse(Call<TMDBService.TMDBMovieResultSet> call, retrofit2.Response<TMDBService.TMDBMovieResultSet> response) {
            MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(response.body());
            mMovieThumbnailAdapter.setMovieMetadata(movieListDto.getMovieMetadatas());
        }

        @Override
        public void onFailure(Call<TMDBService.TMDBMovieResultSet> call, Throwable t) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }

            mToast = Toast.makeText(getApplicationContext(), R.string.error_loading_movies, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    private class AddToMovieListCallback implements Callback<TMDBService.TMDBMovieResultSet> {
        @Override
        public void onResponse(Call<TMDBService.TMDBMovieResultSet> call, retrofit2.Response<TMDBService.TMDBMovieResultSet> response) {
            MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(response.body());
            mMovieThumbnailAdapter.addMovieMetadata(movieListDto.getMovieMetadatas());
        }

        @Override
        public void onFailure(Call<TMDBService.TMDBMovieResultSet> call, Throwable t) {
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }

            mToast = Toast.makeText(getApplicationContext(), R.string.error_loading_movies, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
}
