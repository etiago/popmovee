package com.tiagoespinha.popmovee.adapters;

import java.util.List;

/**
 * Created by tiago on 23/01/2017.
 */

public interface IMovieThumbnailAdapter<T>  {
    void addMovieMetadata(List<T> metadatas);
    void setMovieMetadata(List<T> metadatas);
}
