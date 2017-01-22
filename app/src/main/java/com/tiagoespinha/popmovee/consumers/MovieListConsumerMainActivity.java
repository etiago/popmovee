package com.tiagoespinha.popmovee.consumers;

import android.content.Context;

import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;

import io.reactivex.functions.Consumer;

/**
 * Created by tiago on 22/01/2017.
 */

public class MovieListConsumerMainActivity implements Consumer<TMDBMovieResultSet> {
    MovieThumbnailAdapter mMovieThumbnailAdapter;

    public MovieListConsumerMainActivity(MovieThumbnailAdapter movieThumbnailAdapter) {
        mMovieThumbnailAdapter = movieThumbnailAdapter;
    }

    @Override
    public void accept(TMDBMovieResultSet tmdbMovieResultSet) throws Exception {
        MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(tmdbMovieResultSet);
        mMovieThumbnailAdapter.setMovieMetadata(movieListDto.getMovieMetadata());
    }
}
