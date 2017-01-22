package com.tiagoespinha.popmovee;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.consumers.AddToMovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.MovieListConsumerMainActivity;
import com.tiagoespinha.popmovee.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.listeners.MovieEndlessRecyclerViewScrollListener;
import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;
import com.tiagoespinha.popmovee.services.TMDBService;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tb_main_activity) Toolbar mToolbar;
    @BindView(R.id.rv_movie_grid) RecyclerView mMovieRecyclerView;
    @BindView(R.id.spinner_nav) Spinner mSpinnerNav;

    @Inject MovieThumbnailAdapter mMovieThumbnailAdapter;
    @Inject TMDBService mTMDBService;
    @Inject GridLayoutManager mLayoutManager;
    @Inject MovieEndlessRecyclerViewScrollListener mMovieEndlessRecyclerViewScrollListener;
    @Inject AddToMovieListConsumerMainActivity mAddToMovieListConsumer;
    @Inject MovieListConsumerMainActivity mMovieListConsumer;
    @Inject ThrowableConsumerMainActivity mThrowableConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((PopMoveeApp)getApplication()).getMainActivityComponent().inject(this);

        mSpinnerNav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Observable<TMDBMovieResultSet> movieResultSet;
                if (position == 0) {
                    PopMoveeApp.setShowingPopularMovies(true);
                    movieResultSet = mTMDBService.listPopularMovies();
                } else {
                    PopMoveeApp.setShowingPopularMovies(false);
                    movieResultSet = mTMDBService.listTopRatedMovies();
                }
                movieResultSet
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mMovieListConsumer, mThrowableConsumer);
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

        int orientation = getResources().getConfiguration().orientation;
        mLayoutManager.setSpanCount(PopMoveeApp.getMovieListSpan(orientation));

        mMovieRecyclerView.setLayoutManager(mLayoutManager);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter(mMovieThumbnailAdapter);
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
