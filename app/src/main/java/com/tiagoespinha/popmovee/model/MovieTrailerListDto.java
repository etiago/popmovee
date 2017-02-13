package com.tiagoespinha.popmovee.model;

import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideo;
import com.tiagoespinha.popmovee.retrofit2.model.TMDBMovieVideoResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TiagoEspinha on 12/02/2017.
 */

public class MovieTrailerListDto {
    public MovieTrailerListDto(int id, List<MovieTrailerMetadata> movieTrailerMetadataList) {
        this.id = id;
        this.movieTrailerMetadataList = movieTrailerMetadataList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieTrailerMetadata> getMovieTrailerMetadataList() {
        return movieTrailerMetadataList;
    }

    public void setMovieTrailerMetadataList(List<MovieTrailerMetadata> movieTrailerMetadataList) {
        this.movieTrailerMetadataList = movieTrailerMetadataList;
    }

    private int id;
    private List<MovieTrailerMetadata> movieTrailerMetadataList;


    public static MovieTrailerListDto parseFromTMDBMovieVideoResultSet(TMDBMovieVideoResultSet tmdbMovieVideoResultSet) {
        List<MovieTrailerMetadata> movieTrailerMetadataList = new ArrayList<>();

        for (TMDBMovieVideo tmdbMovieVideo : tmdbMovieVideoResultSet.getMovieVideoList()) {
            movieTrailerMetadataList.add(MovieTrailerMetadata.parseFromTMDBMovieVideo(tmdbMovieVideo));
        }

        return new MovieTrailerListDto(tmdbMovieVideoResultSet.getId(), movieTrailerMetadataList);
    }
}
