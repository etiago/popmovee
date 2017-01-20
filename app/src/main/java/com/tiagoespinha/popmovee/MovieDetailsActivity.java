package com.tiagoespinha.popmovee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tiagoespinha.popmovee.model.MovieMetadata;

import java.util.Calendar;

/**
 * Created by tiago on 18/01/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String MOVIE_METADATA_EXTRA_KEY = "MOVIE_METADATA_EXTRA_KEY";

    TextView mMovieTitleTextView;
    TextView mMoviePlotSynopsisTextView;
    ImageView mMoviePosterImageView;
    TextView mMovieReleaseDateTextView;
    TextView mMovieVoteAverageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent originatorIntent = getIntent();
        if (!originatorIntent.getExtras().containsKey(MOVIE_METADATA_EXTRA_KEY)) {
            Log.wtf(MovieDetailsActivity.class.getSimpleName(),
                    "Movie details activity started without metadata... should never happen.");
            throw new AssertionError();
        }

        MovieMetadata movieMetadata = (MovieMetadata) originatorIntent.getSerializableExtra(MOVIE_METADATA_EXTRA_KEY);

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mMovieTitleTextView.setText(movieMetadata.getOriginalTitle());

        mMoviePlotSynopsisTextView = (TextView) findViewById(R.id.tv_plot_synopsis);
        mMoviePlotSynopsisTextView.setText(movieMetadata.getOverview());

        mMoviePosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        Picasso.with(this)
                .load(movieMetadata.getPosterThumbnailURL().toString())
                .into(mMoviePosterImageView);

        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mMovieReleaseDateTextView.setText(String.valueOf(movieMetadata.getReleaseDate().get(Calendar.YEAR)));

        mMovieVoteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        mMovieVoteAverageTextView.setText(String.valueOf(movieMetadata.getVoteAverage()) + "/10");


    }
}
