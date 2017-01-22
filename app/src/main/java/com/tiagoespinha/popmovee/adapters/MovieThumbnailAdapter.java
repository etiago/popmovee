package com.tiagoespinha.popmovee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiagoespinha.popmovee.R;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.viewholders.MovieThumbnailViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 15/01/2017.
 */

public class MovieThumbnailAdapter extends RecyclerView.Adapter<MovieThumbnailViewHolder> {
    private List<MovieMetadata> mMovieMetadatas;

    public MovieThumbnailAdapter() {
        mMovieMetadatas = new ArrayList<>();
    }

    public void addMovieMetadata(List<MovieMetadata> movieMetadatas) {
        mMovieMetadatas.addAll(movieMetadatas);
        notifyDataSetChanged();
    }
    public void setMovieMetadata(List<MovieMetadata> movieMetadatas) {
        mMovieMetadatas = movieMetadatas;
        notifyDataSetChanged();
    }

    @Override
    public MovieThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_grid_item, parent, false);

        return new MovieThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieThumbnailViewHolder holder, int position) {
        if (position < 0 || position > mMovieMetadatas.size() - 1) {
            return;
        }

        holder.setPosterMetadata(mMovieMetadatas.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieMetadatas == null) {
            return 0;
        }

        return mMovieMetadatas.size();
    }
}
