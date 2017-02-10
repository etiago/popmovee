package com.tiagoespinha.popmovee;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.listeners.MovieEndlessRecyclerViewScrollListener;
import com.tiagoespinha.popmovee.listeners.NavigationSpinnerOnItemSelectedListener;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tb_main_activity) Toolbar mToolbar;
    @BindView(R.id.rv_movie_grid) RecyclerView mMovieRecyclerView;
    @BindView(R.id.spinner_nav) Spinner mSpinnerNav;

    @Inject IMovieThumbnailAdapter<MovieMetadata> mMovieThumbnailAdapter;
    @Inject GridLayoutManager mLayoutManager;
    @Inject MovieEndlessRecyclerViewScrollListener mMovieEndlessRecyclerViewScrollListener;
    @Inject AddToMovieListConsumerMainActivity mAddToMovieListConsumer;
    @Inject NavigationSpinnerOnItemSelectedListener mNavigationSpinnerOnItemSelectedListener;
    @Inject ArrayAdapter<CharSequence> mNavigationSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ButterKnife.bind(this);
        ((PopMoveeApp)getApplication()).getMainActivityComponent().inject(this);

        mSpinnerNav.setOnItemSelectedListener(mNavigationSpinnerOnItemSelectedListener);

        // Apply the adapter to the spinner
        mSpinnerNav.setAdapter(mNavigationSpinnerAdapter);

        int orientation = getResources().getConfiguration().orientation;
        mLayoutManager.setSpanCount(PopMoveeApp.getMovieListSpan(orientation));

        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter((RecyclerView.Adapter)mMovieThumbnailAdapter);
        mMovieRecyclerView.addOnScrollListener(mMovieEndlessRecyclerViewScrollListener);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mLayoutManager.setSpanCount(PopMoveeApp.getMovieListSpan(newConfig.orientation));
    }
}
