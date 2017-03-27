package com.tiagoespinha.popmovee.rx.consumers;

import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.MovieMetadata;

/**
 * Created by tiago on 22/01/2017.
 */

public class AddToMovieListConsumerMainActivity extends MovieListConsumerMainActivity {
    public AddToMovieListConsumerMainActivity(IMovieThumbnailAdapter<MovieMetadata> movieThumbnailAdapter) {
        super(movieThumbnailAdapter);
    }

    @Override
    public void accept(MovieListDto movieListDto) throws Exception {
        mMovieThumbnailAdapter.addMovieMetadata(movieListDto.getMovieMetadata());
    }
}
