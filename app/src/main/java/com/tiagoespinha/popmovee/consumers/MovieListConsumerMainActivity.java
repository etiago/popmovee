package com.tiagoespinha.popmovee.consumers;

import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;

import io.reactivex.functions.Consumer;

/**
 * Created by tiago on 22/01/2017.
 */

public class MovieListConsumerMainActivity implements Consumer<TMDBMovieResultSet> {
    IMovieThumbnailAdapter<MovieMetadata> mMovieThumbnailAdapter;

    public MovieListConsumerMainActivity(IMovieThumbnailAdapter movieThumbnailAdapter) {
        mMovieThumbnailAdapter = movieThumbnailAdapter;
    }

    @Override
    public void accept(TMDBMovieResultSet tmdbMovieResultSet) throws Exception {
        MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(tmdbMovieResultSet);
        mMovieThumbnailAdapter.setMovieMetadata(movieListDto.getMovieMetadata());
    }
}
