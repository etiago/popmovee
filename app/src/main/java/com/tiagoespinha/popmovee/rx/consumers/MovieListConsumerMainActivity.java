package com.tiagoespinha.popmovee.rx.consumers;

import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.MovieMetadata;

import io.reactivex.functions.Consumer;

/**
 * Created by tiago on 22/01/2017.
 */

public class MovieListConsumerMainActivity implements Consumer<MovieListDto> {
    IMovieThumbnailAdapter<MovieMetadata> mMovieThumbnailAdapter;

    public MovieListConsumerMainActivity(IMovieThumbnailAdapter movieThumbnailAdapter) {
        mMovieThumbnailAdapter = movieThumbnailAdapter;
    }

    @Override
    public void accept(MovieListDto movieListDto) throws Exception {
        mMovieThumbnailAdapter.setMovieMetadata(movieListDto.getMovieMetadata());
    }
}
