package com.tiagoespinha.popmovee.consumers;

import android.content.Context;

import com.tiagoespinha.popmovee.adapters.MovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.TMDBMovieResultSet;

/**
 * Created by tiago on 22/01/2017.
 */

public class AddToMovieListConsumerMainActivity extends MovieListConsumerMainActivity {
    public AddToMovieListConsumerMainActivity(MovieThumbnailAdapter movieThumbnailAdapter) {
        super(movieThumbnailAdapter);
    }

    @Override
    public void accept(TMDBMovieResultSet tmdbMovieResultSet) throws Exception {
        MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(tmdbMovieResultSet);
        mMovieThumbnailAdapter.addMovieMetadata(movieListDto.getMovieMetadata());
    }
}
