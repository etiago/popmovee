package com.tiagoespinha.popmovee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tiagoespinha.popmovee.model.MovieMetadata;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tiago on 18/01/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_METADATA_EXTRA_KEY = "MOVIE_METADATA_EXTRA_KEY";

    @BindView(R.id.tv_movie_title) TextView mMovieTitleTextView;
    @BindView(R.id.tv_plot_synopsis) TextView mMoviePlotSynopsisTextView;
    @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImageView;
    @BindView(R.id.tv_release_date) TextView mMovieReleaseDateTextView;
    @BindView(R.id.tv_vote_average) TextView mMovieVoteAverageTextView;
    @BindView(R.id.tb_movie_details) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent originatorIntent = getIntent();
        if (!originatorIntent.getExtras().containsKey(MOVIE_METADATA_EXTRA_KEY)) {
            Log.wtf(MovieDetailsActivity.class.getSimpleName(),
                    "Movie details activity started without metadata... should never happen.");
            throw new AssertionError();
        }

        MovieMetadata movieMetadata = (MovieMetadata) originatorIntent.getParcelableExtra(MOVIE_METADATA_EXTRA_KEY);

        mMovieTitleTextView.setText(movieMetadata.getOriginalTitle());
        mMoviePlotSynopsisTextView.setText(movieMetadata.getOverview());
        Picasso.with(this)
                .load(movieMetadata.getPosterThumbnailURL().toString())
                .into(mMoviePosterImageView);

        mMovieReleaseDateTextView.setText(String.valueOf(movieMetadata.getReleaseDate().get(Calendar.YEAR)));
        mMovieVoteAverageTextView.setText(getResources().getString(R.string.vote_average_format, (int) movieMetadata.getVoteAverage()));
    }
}
