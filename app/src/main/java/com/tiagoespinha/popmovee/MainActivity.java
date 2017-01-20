package com.tiagoespinha.popmovee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
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
    protected MovieThumbnailAdapter mMovieThumbnailAdapter;
    protected GridLayoutManager mLayoutManager;
    protected boolean recyclerViewIsLoading;

    @Inject TMDBService mTMDBService;

    //protected static final int NUMBER_OF_ITEMS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((PopMoveeApp) getApplication()).getNetIOComponent().inject(this);

        mTMDBService.listPopularMovies().enqueue(new Callback<TMDBService.TMDBMovieResultSet>() {
            @Override
            public void onResponse(Call<TMDBService.TMDBMovieResultSet> call, retrofit2.Response<TMDBService.TMDBMovieResultSet> response) {
                MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(response.body());
                mMovieThumbnailAdapter.setMovieMetadata(movieListDto.getMovieMetadatas());
            }

            @Override
            public void onFailure(Call<TMDBService.TMDBMovieResultSet> call, Throwable t) {
                // TODO - Handle HTTP failures
            }
        });

        recyclerViewIsLoading = true;

        mMovieThumbnailAdapter = new MovieThumbnailAdapter();

        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_grid);

        mLayoutManager = new GridLayoutManager(this, 3);
        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter(mMovieThumbnailAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mMovieRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTMDBService.listPopularMovies(page + 1).enqueue(new Callback<TMDBService.TMDBMovieResultSet>() {
                            @Override
                            public void onResponse(Call<TMDBService.TMDBMovieResultSet> call, retrofit2.Response<TMDBService.TMDBMovieResultSet> response) {
                                MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(response.body());
                                mMovieThumbnailAdapter.addMovieMetadata(movieListDto.getMovieMetadatas());
                            }

                            @Override
                            public void onFailure(Call<TMDBService.TMDBMovieResultSet> call, Throwable t) {
                                // TODO - Handle HTTP failures
                            }
                        });
                    }
                });

            }
        };
        // Adds the scroll listener to RecyclerView
        mMovieRecyclerView.addOnScrollListener(scrollListener);
        //MovieListDto movieListDto = new MovieListDto();


    }
}
