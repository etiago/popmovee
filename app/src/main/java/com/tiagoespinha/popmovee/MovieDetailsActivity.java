package com.tiagoespinha.popmovee;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.squareup.picasso.Picasso;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideo;
import com.tiagoespinha.popmovee.rx.consumers.ThrowableConsumerMainActivity;
import com.tiagoespinha.popmovee.content.FavoriteMovieContract;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideoResultSet;
import com.tiagoespinha.popmovee.retrofit2.services.TMDBService;

import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_METADATA_EXTRA_KEY = "MOVIE_METADATA_EXTRA_KEY";

    @Inject TMDBService mTMDBService;
    @Inject
    Function<Activity, Consumer<Throwable>> mHttpErrorHandler;

    @BindView(R.id.tv_movie_title) TextView mMovieTitleTextView;
    @BindView(R.id.tv_plot_synopsis) TextView mMoviePlotSynopsisTextView;
    @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImageView;
    @BindView(R.id.tv_release_date) TextView mMovieReleaseDateTextView;
    @BindView(R.id.tv_vote_average) TextView mMovieVoteAverageTextView;
    @BindView(R.id.tb_movie_details) Toolbar mToolbar;
    @BindView(R.id.tv_star_icon) TextView mStarIconTextView;
    ShareActionProvider mShareActionProvider;

    private MovieMetadata mMovieMetadata;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        mTMDBService
                .listVideosForMovieId(mMovieMetadata.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tmdbMovieVideoResultSet -> {
                    List<TMDBMovieVideo> tmdbMovieVideoList = tmdbMovieVideoResultSet.getMovieVideoList();
                    if (tmdbMovieVideoList.size() < 1) {
                        return;
                    }

                    TMDBMovieVideo tmdbMovieVideo = tmdbMovieVideoList.get(0);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            String.format(
                                    getResources().getString(R.string.share_text),
                                    String.format(
                                            getString(R.string.youtube_base_uri),
                                            tmdbMovieVideo.getKey())));
                    sendIntent.setType("text/plain");

                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(Intent.createChooser(sendIntent, getResources().getText(R.string.share_trailer)));
                    }
                }, mHttpErrorHandler.apply(this));

        // Return true to display menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);
        ((PopMoveeApp)getApplication()).getMovieDetailsActivityComponent().inject(this);


        mToolbar.setBackgroundResource(R.color.colorAccent);
        mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryTextOnAccent));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent originatorIntent = getIntent();
        if (!originatorIntent.getExtras().containsKey(MOVIE_METADATA_EXTRA_KEY)) {
            Log.wtf(MovieDetailsActivity.class.getSimpleName(),
                    "Movie details activity started without metadata... should never happen.");
            throw new AssertionError();
        }

        mMovieMetadata = originatorIntent.getParcelableExtra(MOVIE_METADATA_EXTRA_KEY);

        // Hydrating UI
        mMovieTitleTextView.setText(mMovieMetadata.getOriginalTitle());
        mMoviePlotSynopsisTextView.setText(mMovieMetadata.getOverview());
        Picasso.with(this)
                .load(mMovieMetadata.getPosterThumbnailURL().toString())
                .error(R.mipmap.transparent)
                .into(mMoviePosterImageView);

        mMovieReleaseDateTextView.setText(String.valueOf(mMovieMetadata.getReleaseDate().get(Calendar.YEAR)));
        mMovieVoteAverageTextView.setText(getResources().getString(R.string.vote_average_format, (int) mMovieMetadata.getVoteAverage()));

        if (movieIsFavorite(mMovieMetadata)){
            mStarIconTextView.setText(getString(R.string.favorite_icon_clicked));
            mStarIconTextView.setOnClickListener(v -> unsetMovieAsFavorite(v, mMovieMetadata));
        } else {
            mStarIconTextView.setText(getString(R.string.favorite_icon_unclicked));
            mStarIconTextView.setOnClickListener(v -> setMovieAsFavorite(v, mMovieMetadata));
        }


        // Setting up trailer fragment
        TrailerListFragment trailerListFragment =
                (TrailerListFragment) getSupportFragmentManager().findFragmentById(R.id.fg_movie_list);

        mTMDBService
                .listVideosForMovieId(mMovieMetadata.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailerListFragment, mHttpErrorHandler.apply(this));

        // Setting up review fragment
        ReviewListFragment reviewListFragment = (ReviewListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fg_review_list);

        mTMDBService
                .listReviewsForMovieId(mMovieMetadata.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviewListFragment, mHttpErrorHandler.apply(this));
    }

    private boolean movieIsFavorite(MovieMetadata movieMetadata) {
        Cursor c = getContentResolver()
                .query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                        null,
                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                        new String[]{String.valueOf(movieMetadata.getId())},
                        null);

        return MovieListDto.parseFromFavoriteMovieProviderCursor(c).getTotalMovieCount() != 0;
    }

    private void unsetMovieAsFavorite(View view, MovieMetadata movieMetadata) {
        Observable<Boolean> movieWasRemovedObservable = Observable
                .fromCallable(() -> movieIsFavorite(movieMetadata))
                .map(movieIsFavorite -> {
                    if (!movieIsFavorite) {
                        return false;
                    }

                    int affectedRows = getContentResolver().delete(
                            Uri.withAppendedPath(
                                    FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                                    String.valueOf(movieMetadata.getId())),
                            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                            new String[]{String.valueOf(movieMetadata.getId())});

                    return affectedRows == 1;
                });

        movieWasRemovedObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieWasRemoved -> {
                    if (movieWasRemoved) {
                        ((TextView) view).setText(getString(R.string.favorite_icon_unclicked));
                        view.setOnClickListener(v -> setMovieAsFavorite(v,movieMetadata));
                    }
                }, throwable -> Log.e(getClass().getSimpleName(), "Something went wrong unfavoriting the movie", throwable));
    }

    private void setMovieAsFavorite(View view, MovieMetadata movieMetadata) {
        Observable<Boolean> movieWasAddedObservable = Observable
                .fromCallable(() -> movieIsFavorite(movieMetadata))
                .map(movieIsFavorite -> {
                    if (movieIsFavorite) {
                        return false;
                    }

                    // Insert new task data via a ContentResolver
                    // Create new empty ContentValues object
                    ContentValues contentValues = new ContentValues();
                    // Put the task description and selected mPriority into the ContentValues
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                            movieMetadata.getId());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE,
                            movieMetadata.getOriginalTitle());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
                            movieMetadata.getOverview());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_THUMBNAIL_URL,
                            movieMetadata.getPosterThumbnailURL().toString());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                            movieMetadata.getReleaseDate().getTimeInMillis());
                    contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE,
                            movieMetadata.getVoteAverage());

                    // Insert the content values via a ContentResolver
                    Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);

                    return uri != null;
                });

        movieWasAddedObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieWasAdded -> {
                    if (movieWasAdded) {
                        ((TextView) view).setText(getString(R.string.favorite_icon_clicked));
                        view.setOnClickListener(v -> unsetMovieAsFavorite(v,movieMetadata));
                    }
                }, throwable -> Log.e(getClass().getSimpleName(), "Something went wrong favoriting the movie", throwable));

    }
//    private void addTrailerListFragment(int movieId) {
//        TrailerListFragment trailerListFragment = new TrailerListFragment();
//
//        Bundle b = new Bundle();
//        b.putInt(TrailerListFragment.MOVIE_ID_KEY, movieId);
//        trailerListFragment.setArguments(b);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.ll_vertical_layout, trailerListFragment)
//                .commit();
//    }
}
