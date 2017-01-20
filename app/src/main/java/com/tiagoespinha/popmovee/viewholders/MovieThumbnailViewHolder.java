package com.tiagoespinha.popmovee.viewholders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tiagoespinha.popmovee.MovieDetailsActivity;
import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.model.MovieMetadata;

import java.net.URL;
import java.util.Random;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView mMovieItemImageView;
    private MovieMetadata mMovieMetadata;

    public MovieThumbnailViewHolder(View itemView) {
        super(itemView);

        mMovieItemImageView = (ImageView) itemView.findViewById(R.id.iv_movie_grid_item);
        mMovieItemImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mMovieMetadata == null) {
            Log.e(MovieThumbnailViewHolder.class.getSimpleName(),
                    "Click on movie thumbnail but have no movie metadata...");
            return;
        }

        Intent openMovieDetailsIntent = new Intent(v.getContext(), MovieDetailsActivity.class);
        openMovieDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_METADATA_EXTRA_KEY, mMovieMetadata);

        itemView.getContext().startActivity(openMovieDetailsIntent);
    }

    public void setPosterMetadata(MovieMetadata movieMetadata) {
        mMovieMetadata = movieMetadata;
        Picasso.with(mMovieItemImageView.getContext())
                .load(movieMetadata.getPosterThumbnailURL().toString())
                .into(mMovieItemImageView);
    }
}
