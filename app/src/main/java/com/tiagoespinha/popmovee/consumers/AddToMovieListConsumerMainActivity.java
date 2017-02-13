package com.tiagoespinha.popmovee.consumers;

import com.tiagoespinha.popmovee.adapters.IMovieThumbnailAdapter;
import com.tiagoespinha.popmovee.model.MovieListDto;
import com.tiagoespinha.popmovee.model.MovieMetadata;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieResultSet;

/**
 * Created by tiago on 22/01/2017.
 */

public class AddToMovieListConsumerMainActivity extends MovieListConsumerMainActivity {
    public AddToMovieListConsumerMainActivity(IMovieThumbnailAdapter<MovieMetadata> movieThumbnailAdapter) {
        super(movieThumbnailAdapter);
    }

    @Override
    public void accept(TMDBMovieResultSet tmdbMovieResultSet) throws Exception {
        MovieListDto movieListDto = MovieListDto.parseFromTMDBMovieResultSet(tmdbMovieResultSet);
        mMovieThumbnailAdapter.addMovieMetadata(movieListDto.getMovieMetadata());
    }
}
